package com.seo.todayweather.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.seo.todayweather.data.SelectWidget
import com.seo.todayweather.databinding.ItemHomeBinding

class HomeRecyclerAdapter(private var items: List<SelectWidget>) : RecyclerView.Adapter<HomeRecyclerAdapter.HomeInfo>() {

    inner class HomeInfo(val itemBinding: ItemHomeBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HomeInfo {
        val binding =
            ItemHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeInfo(binding)
    }

    override fun getItemCount(): Int = items.size


    override fun onBindViewHolder(holder: HomeInfo, position: Int) {
        val userSelectInfo = items[position]

        with(holder.itemBinding) {
            userSelectInfo.title.let{
                tvTest.text = it
            }
        }
    }
}