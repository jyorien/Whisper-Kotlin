package com.example.whisperkotlin.minicontroller

import android.content.*
import android.os.Bundle
import android.os.IBinder
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.whisperkotlin.R
import com.example.whisperkotlin.databinding.FragmentMediaBinding
import com.example.whisperkotlin.music.MusicPlayerActivity
import com.example.whisperkotlin.music.MusicService


class MediaFragment : Fragment() {
    private lateinit var mService: MusicService
    private var mBound = false
    private lateinit var binding: FragmentMediaBinding

    private val mReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent != null) {
                binding.controllerSong.text = intent.getStringExtra("songName")
                binding.controllerArtiste.text = intent.getStringExtra("artisteName")
                Glide.with(requireContext()).load(intent.getStringExtra("imageUrl"))
                    .into(binding.controllerImage)
                binding.root.visibility = View.VISIBLE
            }
        }
    }

    private val mConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as MusicService.LocalBinder
            mService = binder.getService()
            mBound = true
            if (mService.isPlaying())
                binding.controllerPlayPause.setBackgroundResource(R.drawable.ic_baseline_play_arrow_24)
            else
                binding.controllerPlayPause.setBackgroundResource(R.drawable.ic_baseline_pause_24)
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            mBound = false
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LocalBroadcastManager.getInstance(requireContext())
            .registerReceiver(mReceiver, IntentFilter("SONG"))
        Intent(requireContext(), MusicService::class.java).also { intent ->
            // bind activity to service
            requireActivity().bindService(intent, mConnection, Context.BIND_AUTO_CREATE)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_media, container, false)
        binding.root.visibility = View.GONE

        binding.controllerPlayPause.setOnClickListener {
            if (mService.isPlaying()) {
                mService.pausePlayer()
                binding.controllerPlayPause.setBackgroundResource(R.drawable.ic_baseline_play_arrow_24)
            } else {
                mService.resumePlayer()
                binding.controllerPlayPause.setBackgroundResource(R.drawable.ic_baseline_pause_24)
            }
        }

        binding.root.setOnClickListener {
            Intent(requireActivity(), MusicPlayerActivity::class.java).also { intent->
                startActivity(intent)
            }
        }


        return binding.root
    }

    override fun onResume() {
        super.onResume()
        if (::mService.isInitialized) {
            // only check if activity resumed from back stack
            if (mService.isPlaying()) {
                binding.controllerPlayPause.setBackgroundResource(R.drawable.ic_baseline_pause_24)
            } else {
                binding.controllerPlayPause.setBackgroundResource(R.drawable.ic_baseline_play_arrow_24)
            }
        }


    }

}