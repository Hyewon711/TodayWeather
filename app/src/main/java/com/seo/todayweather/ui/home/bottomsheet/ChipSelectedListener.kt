package com.seo.todayweather.ui.home.bottomsheet

import com.seo.todayweather.data.SelectChip

interface ChipSelectedListener {
    fun onChipSelected(chip: List<SelectChip>)
}