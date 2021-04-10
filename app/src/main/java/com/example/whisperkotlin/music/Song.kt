package com.example.whisperkotlin.music

import android.os.Parcelable
import java.io.Serializable

data class Song(
    val songName: String,
    val artiste: String,
    val songUrl: String,
    val imageUrl: String,
) : Serializable


object SongList: Serializable {
    val bts = listOf(
        Song(
            "Boy With Luv",
            "BTS",
            "https://p.scdn.co/mp3-preview/d16797fb391fb909f3c46454d7cf89a2718f8171?cid=2afe87a64b0042dabf51f37318616965",
            "https://i.scdn.co/image/ab67616d0000b27329d00196831bec20ebbff5c7",
        ),
        Song(
            "Make It Right",
            "BTS",
            "https://p.scdn.co/mp3-preview/27d07e11fef56c0f8cb8a7e77a445e7371971ea5?cid=2afe87a64b0042dabf51f37318616965",
            "https://i.scdn.co/image/ab67616d0000b27329d00196831bec20ebbff5c7"
        ),
        Song(
            "FAKE LOVE",
            "BTS",
            "https://p.scdn.co/mp3-preview/bc24526c6a5e4d2a63bdbf364705a6012ff2d649?cid=2afe87a64b0042dabf51f37318616965",
            "https://i.scdn.co/image/ab67616d0000b27321108e396b83d171cc5c2692"
        ),
        Song(
            "The Truth Untold",
            "BTS",
            "https://p.scdn.co/mp3-preview/9962cf7e18a736f3123861973130908accc43769?cid=2afe87a64b0042dabf51f37318616965",
            "https://i.scdn.co/image/ab67616d0000b27321108e396b83d171cc5c2692"
        )
    )
}
