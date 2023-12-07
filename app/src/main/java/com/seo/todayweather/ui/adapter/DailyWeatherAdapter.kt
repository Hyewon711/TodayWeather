package com.seo.todayweather.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.seo.todayweather.data.DailyItem
import com.seo.todayweather.databinding.ItemDailyWeatherBinding
@GlideModule

class DailyWeatherAdapter : RecyclerView.Adapter<DailyWeatherAdapter.DailyInfo>() {
    var itemList = ArrayList<DailyItem>()
    lateinit var context: Context

    inner class DailyInfo(val itemBinding: ItemDailyWeatherBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DailyInfo {
        this.context = parent.context
        val binding =
            ItemDailyWeatherBinding.inflate(LayoutInflater.from(context), parent, false)
        return DailyInfo(binding)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: DailyInfo, position: Int) {
        with(holder.itemBinding) {
            dailyDateTextView.text = itemList[position].date
            Glide.with(context)
                .load(itemList[position].imageUri)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .apply(RequestOptions().format(DecodeFormat.PREFER_ARGB_8888))
                .into(dailyWeatherImageView)
            dailyMaxTempTextView.text = itemList[position].maxTemp
            dailyMinTempTextView.text = itemList[position].minTemp
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addItem(items: DailyItem) {
        itemList.add(items)
        notifyDataSetChanged()
    }

    fun removeItem(position: Int) {
        itemList.removeAt(position)
        notifyItemRemoved(position)
    }
}