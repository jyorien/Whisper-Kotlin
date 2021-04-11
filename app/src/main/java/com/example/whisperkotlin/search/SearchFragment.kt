package com.example.whisperkotlin.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import com.example.whisperkotlin.R
import com.example.whisperkotlin.databinding.FragmentSearchBinding
import com.example.whisperkotlin.music.Song
import com.example.whisperkotlin.music.SongList


class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    private lateinit var adapter: SearchAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false)
        adapter = SearchAdapter(SongList.bts)
        binding.searchList.adapter = adapter
        binding.searchInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                filter(s.toString())
            }

        })
        return binding.root
    }

    fun filter(text: String) {
        val newList = mutableListOf<Song>()
        SongList.bts.forEach {
            if (it.songName.toLowerCase().contains(text.toLowerCase())) {
                newList.add(it)
            }

        }
        adapter.updateList(newList)

    }

}