package com.amar3ndar.musicly

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.amar3ndar.musicly.adapter.SongListAdapter
import com.amar3ndar.musicly.models.CategoryModel
import com.amar3ndar.musicstream.databinding.ActivitySongsListBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions

class SongsListActivity : AppCompatActivity() {

    companion object{
        lateinit var category: CategoryModel
    }

    lateinit var binding: ActivitySongsListBinding
    lateinit var songListAdapter : SongListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongsListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.nameTextView.text = category.name
        Glide.with(binding.coverImageView).load(category.coverUrl) //for cover image
            .apply(
                RequestOptions().transform(RoundedCorners(32))
            ).into(binding.coverImageView)

        setupSongListRecyclerView()
    }

    fun setupSongListRecyclerView(){
        songListAdapter = SongListAdapter(category.songs)
        binding.songsListRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.songsListRecyclerView.adapter = songListAdapter
    }
}