package com.seo.todayweather.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.seo.todayweather.data.StylePost
import com.seo.todayweather.databinding.ItemStyleBinding

class StyleRecyclerAdapter(private var items: List<StylePost>) :
    RecyclerView.Adapter<StyleRecyclerAdapter.PostInfo>() {
    lateinit var context: Context

    inner class PostInfo(val itemBinding: ItemStyleBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PostInfo {
        this.context = parent.context
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
            postInfo.uri.let {
                Glide.with(context)
                    .load(it)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .apply(RequestOptions().format(DecodeFormat.PREFER_ARGB_8888))
                    .into(ivStyle)
            }
            postInfo.userUri.let {
                Glide.with(context)
                    .load(it)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .apply(RequestOptions().format(DecodeFormat.PREFER_ARGB_8888))
                    .into(ivUserImage)
            }
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    fun setItems(newItems: List<StylePost>) {
        items = newItems
        notifyDataSetChanged()
    }

    fun filterByCategory(category: Int) {
        val filteredList = if (category == 0) {
            items // 전체 카테고리를 선택한 경우, 필터링 없이 전체 아이템을 보여줌
        } else {
            items.filter { it.userStyle == category }
        }
        setItems(filteredList)
    }
}