package com.seo.todayweather.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.seo.todayweather.data.StylePost
import com.seo.todayweather.databinding.ItemHomeBinding
import com.seo.todayweather.databinding.ItemStyleBinding

class StyleRecyclerAdapter(private var items: List<StylePost>) :
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
            postInfo.name.let {
                tvName.text = it
            }
            postInfo.title.let {
                tvTitle.text = it
            }
        }
    }
}