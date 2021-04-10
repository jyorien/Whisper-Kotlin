package com.example.whisperkotlin.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.whisperkotlin.R
import com.example.whisperkotlin.databinding.FragmentHomeBinding
import com.example.whisperkotlin.music.MusicService
import com.example.whisperkotlin.music.SongList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        binding.horizontalRecyclerview.adapter = HomeListAdapter(SongList.bts) { song ->
            val intent = Intent(requireContext(), MusicService::class.java)
            intent.putExtra("url", song.songUrl)
            requireActivity().startService(intent)
        }
        return binding.root
    }


}