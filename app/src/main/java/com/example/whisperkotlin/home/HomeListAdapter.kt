package com.example.whisperkotlin.home

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.whisperkotlin.R
import com.example.whisperkotlin.music.MusicService
import com.example.whisperkotlin.music.Song

class HomeListAdapter(val songList: List<Song>, val onItemClicked: (Song) -> Unit) :
    RecyclerView.Adapter<HomeListAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.albumImage)
        val artiste: TextView = itemView.findViewById(R.id.artisteName)
        val songName: TextView = itemView.findViewById(R.id.songName)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.horizontal_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.artiste.text = songList[position].artiste
        holder.songName.text = songList[position].songName
        Glide.with(holder.image.context).load(songList[position].imageUrl)
            .centerCrop().into(holder.image)
        holder.itemView.setOnClickListener {
            onItemClicked(songList[position])
        }

    }

    override fun getItemCount(): Int = songList.size

}