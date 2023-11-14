package com.seo.todayweather.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.seo.todayweather.data.Post
import com.seo.todayweather.databinding.ItemStyleBinding

class StyleRecyclerAdapter(private var items: List<Post>) :
    RecyclerView.Adapter<StyleRecyclerAdapter.PostInfo>() {

    inner class PostInfo(val itemBinding: ItemStyleBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PostInfo {
        val binding =
            ItemStyleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostInfo(binding)
    }

    override fun getItemCount(): Int = items.size


    override fun onBindViewHolder(holder: PostInfo, position: Int) {
        val postInfo = items[position]

        with(holder.itemBinding) {
            postInfo.title.let {
                tvTitle.text = it
            }
            postInfo.content.let {
                tvAuthor.text = it
            }
        }
    }
}