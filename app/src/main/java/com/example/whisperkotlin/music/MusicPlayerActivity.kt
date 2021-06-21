package com.example.whisperkotlin.music

import android.content.*
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.bumptech.glide.Glide
import com.example.whisperkotlin.R
import com.example.whisperkotlin.databinding.ActivityMusicPlayerBinding
import kotlin.math.sqrt

class MusicPlayerActivity : AppCompatActivity() {
    private var mBound = false
    private lateinit var mService: MusicService
    private lateinit var binding: ActivityMusicPlayerBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMusicPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        startMusicService()
        binding.apply {

            btnPlayPause.setOnClickListener {
                if (mService.isPlaying()) {
                    mService.pausePlayer()
                    btnPlayPause.setImageResource(R.drawable.ic_baseline_play_arrow_24)
                }
                else {
                    mService.resumePlayer()
                    btnPlayPause.setImageResource(R.drawable.ic_baseline_pause_24)
                }
            }

            btnNext.setOnClickListener {
                mService.getNextSong()
                assignData()
            }

            btnPrevious.setOnClickListener {
                mService.getPrevSong()
                assignData()
            }
        }
    }

    private fun startMusicService() {
        Intent(applicationContext, MusicService::class.java).also { intent ->
            bindService(intent, mConnection, Context.BIND_AUTO_CREATE)
        }
    }

    private val mConnection = object: ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as MusicService.LocalBinder
            mService = binder.getService()
            mBound = true
            assignData()
            if (mService.isPlaying())
                binding.btnPlayPause.setImageResource(R.drawable.ic_baseline_pause_24)
            else
                binding.btnPlayPause.setImageResource(R.drawable.ic_baseline_play_arrow_24)
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            mBound = false
        }

    }
    private fun assignData() {
        binding.apply {
            displaySongName.text = mService.currentSongName
            displayArtisteName.text= mService.currentArtisteName
            Glide.with(applicationContext).load(mService.currentImageUrl).into(displayImage)

        }
    }


    override fun onDestroy() {
        super.onDestroy()
        mBound = false
        unbindService(mConnection)

    }


}