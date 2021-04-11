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
    private var url = ""
    private var playlist = emptyList<Song>()
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
            url = intent.getStringExtra("url").toString()
            playlist =
                intent.getBundleExtra("playlist_bundle")?.getSerializable("playlist") as List<Song>
            preparePlayer(url)
            startPlayer()

            // send broadcast to mini controller to display data
            val controllerIntent = Intent("SONG")
            controllerIntent.putExtra("songName", intent.getStringExtra("songName"))
            controllerIntent.putExtra("artisteName", intent.getStringExtra("artisteName"))
            controllerIntent.putExtra("imageUrl", intent.getStringExtra("imageUrl"))

            LocalBroadcastManager.getInstance(this).sendBroadcast(controllerIntent)

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

    fun startPlayer() {
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
}