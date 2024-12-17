package com.amar3ndar.musicly.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amar3ndar.musicly.SongsListActivity
import com.amar3ndar.musicly.models.CategoryModel
import com.amar3ndar.musicstream.databinding.CategoryItemRecyclerRowBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions

class CategoryAdapter(private val categoryList: List<CategoryModel>) :
    RecyclerView.Adapter<CategoryAdapter.MyViewHolder>() {

    class MyViewHolder(private val binding: CategoryItemRecyclerRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        //bind the data with views
        fun bindData(category: CategoryModel) {
            binding.nameTextView.text = category.name  //for name

            Glide.with(binding.coverImageView).load(category.coverUrl) //for cover image
                .apply(RequestOptions().transform(RoundedCorners(32))
                ).into(binding.coverImageView)

            //Start Song List Activity
            val context = binding.root.context
            binding.root.setOnClickListener{
                SongsListActivity.category = category
                context.startActivity(Intent(context,SongsListActivity::class.java))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = CategoryItemRecyclerRowBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindData(categoryList[position])
    }
}