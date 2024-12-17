package com.amar3ndar.musicly.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amar3ndar.musicly.MyExoplayer
import com.amar3ndar.musicly.PlayerActivity
import com.amar3ndar.musicly.models.SongModel
import com.amar3ndar.musicstream.databinding.SongListItemRecyclerRowBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.firestore.FirebaseFirestore

class SongListAdapter(private val songListId:List<String>): RecyclerView.Adapter<SongListAdapter.MyViewHolder>() {

    class MyViewHolder(private val binding:    SongListItemRecyclerRowBinding) : RecyclerView.ViewHolder(binding.root){
     // bind data with view
        fun bindData(songId:String){
            FirebaseFirestore.getInstance().collection("songs")
                .document(songId).get()
                .addOnSuccessListener {
                    val song = it.toObject(SongModel::class.java)
                    song?.apply {
                        binding.songTitleTextView.text = title
                        binding.songSubtitleTextView.text = subtitle
                        Glide.with(binding.songCoverImageView).load(coverUrl) //for cover image
                            .apply(
                                RequestOptions().transform(RoundedCorners(32))
                            ).into(binding.songCoverImageView)
                        binding.root.setOnClickListener{
                            MyExoplayer.startPlaying(binding.root.context,song)
                            it.context.startActivity(Intent(it.context,PlayerActivity::class.java))
                        }
                    }
                }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = SongListItemRecyclerRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return songListId.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindData(songListId[position])
    }
}