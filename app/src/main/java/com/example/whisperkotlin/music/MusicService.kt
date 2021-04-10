package com.example.whisperkotlin.music

import android.app.Service
import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import android.os.IBinder
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import java.io.IOException

class MusicService : Service() {
    private lateinit var mediaPlayer: MediaPlayer
    var url = ""
    var playlist = emptyList<Song>()
    override fun onCreate() {
        super.onCreate()
        mediaPlayer = MediaPlayer()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent != null) {
            url = intent.getStringExtra("url").toString()
            playlist = intent.getBundleExtra("playlist_bundle")?.getSerializable("playlist") as List<Song>
            Toast.makeText(applicationContext, playlist[0].songName, Toast.LENGTH_SHORT).show()
            preparePlayer(url)
            mediaPlayer.setOnPreparedListener { player ->
                player.start()
            }
            val controllerIntent = Intent("SONGNAME")
            controllerIntent.putExtra("songName",intent.getStringExtra("songName"))
            LocalBroadcastManager.getInstance(this).sendBroadcast(controllerIntent)

        }

        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    private fun preparePlayer(url_link: String) {
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
}