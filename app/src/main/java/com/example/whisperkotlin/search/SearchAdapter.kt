package com.example.whisperkotlin.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.whisperkotlin.R
import com.example.whisperkotlin.music.Song

class SearchAdapter(var dataList: List<Song>) :
    RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {

    inner class SearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val searchImage: ImageView = itemView.findViewById(R.id.searchImage)
        val searchSongName: TextView = itemView.findViewById(R.id.searchSongName)
        val searchArtisteName: TextView = itemView.findViewById(R.id.searchArtisteName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        // return a viewholder
        val view = LayoutInflater.from(parent.context).inflate(R.layout.search_item, parent, false)
        return SearchViewHolder(view)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.searchSongName.text = dataList[position].songName
        holder.searchArtisteName.text = dataList[position].artiste
        Glide.with(holder.searchImage.context).load(dataList[position].imageUrl).override(256,256)
            .centerCrop().into(holder.searchImage)
    }

    override fun getItemCount(): Int = dataList.size

    fun updateList(newDataList: List<Song>) {
        dataList = newDataList
        notifyDataSetChanged()
    }
}