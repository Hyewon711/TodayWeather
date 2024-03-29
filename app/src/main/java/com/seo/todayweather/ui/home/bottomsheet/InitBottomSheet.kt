package com.seo.todayweather.ui.home.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.seo.todayweather.data.SelectChip
import com.seo.todayweather.databinding.InitBottomSheetBinding
import com.seo.todayweather.util.common.PrefManager

class InitBottomSheet : BottomSheetDialogFragment() {
    private lateinit var binding: InitBottomSheetBinding
    private lateinit var selectChip: ChipGroup
    private lateinit var getChipOrder: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = InitBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val selectedChipsOrder = mutableListOf<SelectChip>()
        dialog?.setCanceledOnTouchOutside(false)

        with(binding) {
            selectChip = chipGroup
            getChipOrder = getOrderButton

            for (i in 0 until chipGroup.childCount) {
                val chip = chipGroup.getChildAt(i) as Chip

                chip.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) {
                        selectedChipsOrder.add(SelectChip(i, chip.text.toString(), 0))
                    } else {
                        selectedChipsOrder.remove(SelectChip(i, chip.text.toString(), 0))
                    }
                }
            }
        }

        getChipOrder.setOnClickListener {
            PrefManager.getInstance().setSelectChipList(selectedChipsOrder)
            dismiss()
        }
    }
}