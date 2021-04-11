package com.example.whisperkotlin.music

import android.app.Service
import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import android.os.Binder
import android.os.IBinder
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import java.io.IOException

class MusicService : Service() {
    private lateinit var mediaPlayer: MediaPlayer
    private var _currentSongName = ""
    val currentSongName
        get() = _currentSongName

    private var _currentArtisteName = ""
    val currentArtisteName
        get() = _currentArtisteName
    private var _currentImageUrl = ""
    val currentImageUrl
        get() = _currentImageUrl

    private var _currentSongUrl = ""
    val currentSongUrl
        get() = _currentSongUrl

    private var _currentSongList = emptyList<Song>()
    val currentSongList
        get() = _currentSongList

    private var pausePosition = 0
    override fun onCreate() {
        super.onCreate()
        mediaPlayer = MediaPlayer()
    }

    private val binder = LocalBinder()


    inner class LocalBinder : Binder() {
        fun getService(): MusicService = this@MusicService
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent != null) {
            _currentSongUrl = intent.getStringExtra("url").toString()
            _currentSongName = intent.getStringExtra("songName").toString()
            _currentArtisteName = intent.getStringExtra("artisteName").toString()
            _currentImageUrl = intent.getStringExtra("imageUrl").toString()
            _currentSongList =
                intent.getBundleExtra("playlist_bundle")?.getSerializable("playlist") as List<Song>
            preparePlayer(_currentSongUrl)
            startPlayer()

            // send broadcast to mini controller to display data
            broadcastSong(intent)

        }

        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent): IBinder {
        Toast.makeText(this, "BOUND TO SERVICE", Toast.LENGTH_SHORT).show()
        return binder
    }

    private fun preparePlayer(url_link: String) {
        // bring mediaplayer to prepared state
        mediaPlayer.reset()
        try {
            mediaPlayer.setAudioAttributes(
                AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build()
            )
            mediaPlayer.setDataSource(this, Uri.parse(url_link))
            mediaPlayer.prepareAsync()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun startPlayer() {
        mediaPlayer.setOnPreparedListener { player ->
            player.start()
            Toast.makeText(this, "START", Toast.LENGTH_SHORT).show()
        }
    }

    fun pausePlayer() {
        mediaPlayer.pause()
        pausePosition = mediaPlayer.currentPosition
    }

    fun isPlaying() = mediaPlayer.isPlaying

    fun resumePlayer() {
        mediaPlayer.start()
    }

    fun setCurrentDetails(
        songName: String,
        artisteName: String,
        songUrl: String,
        imageUrl: String
    ) {
        _currentSongName = songName
        _currentArtisteName = artisteName
        _currentSongUrl = songUrl
        _currentImageUrl = imageUrl
        preparePlayer(_currentSongUrl)
        startPlayer()

        val intent = Intent()
        intent.putExtra("songName", _currentSongName)
        intent.putExtra("artisteName", _currentArtisteName)
        intent.putExtra("imageUrl", _currentImageUrl)
        broadcastSong(intent)
    }


    fun broadcastSong(intent: Intent) {
        // send broadcast to mini controller to display data
        val controllerIntent = Intent("SONG")
        controllerIntent.putExtra("songName", intent.getStringExtra("songName"))
        controllerIntent.putExtra("artisteName", intent.getStringExtra("artisteName"))
        controllerIntent.putExtra("imageUrl", intent.getStringExtra("imageUrl"))

        LocalBroadcastManager.getInstance(this).sendBroadcast(controllerIntent)
    }


    fun getNextSong() {
        val song: Song = if (currentSongIndex() == _currentSongList.size - 1)
            _currentSongList[0]
        else
            _currentSongList[currentSongIndex() + 1]
        setCurrentDetails(song.songName, song.artiste, song.songUrl, song.imageUrl)
    }

    fun getPrevSong() {
        val song: Song
        if (currentSongIndex() == 0)
            song = currentSongList.last()
        else
            song = _currentSongList[currentSongIndex() - 1]
        setCurrentDetails(song.songName, song.artiste, song.songUrl, song.imageUrl)
    }

    private fun currentSongIndex(): Int {
        return currentSongList.indexOfFirst {
            it.songName == _currentSongName
        }
    }
}