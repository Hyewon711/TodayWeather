package com.seo.todayweather.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.seo.todayweather.data.HourlyItem
import com.seo.todayweather.databinding.ItemHourlyWeatherBinding
import com.seo.todayweather.util.common.TAG

@GlideModule
class HourlyWeatherAdapter : RecyclerView.Adapter<HourlyWeatherAdapter.HourlyInfo>() {
    var itemList = ArrayList<HourlyItem>()
    lateinit var context: Context

    inner class HourlyInfo(val itemBinding: ItemHourlyWeatherBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HourlyInfo {
        this.context = parent.context
        val binding =
            ItemHourlyWeatherBinding.inflate(LayoutInflater.from(context), parent, false)
        return HourlyInfo(binding)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: HourlyInfo, position: Int) {
        with(holder.itemBinding) {
            hourlyItemTimeTextView.text = itemList[position].time
            Glide.with(context)
                .load(itemList[position].imageUri)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .apply(RequestOptions().format(DecodeFormat.PREFER_ARGB_8888))
                .into(hourlyItemImageView)
            hourlyItemTempTextView.text = itemList[position].temp
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addItem(items: HourlyItem) {
        itemList.add(items)
        notifyDataSetChanged()
    }

    fun removeItem(position: Int) {
        itemList.removeAt(position)
        notifyItemRemoved(position)
    }


}