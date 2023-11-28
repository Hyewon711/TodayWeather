package com.seo.todayweather.ui.home.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.seo.todayweather.databinding.InitBottomSheetBinding

class InitBottomSheet : BottomSheetDialogFragment() {
    private lateinit var binding: InitBottomSheetBinding
    private lateinit var selectChip: ChipGroup
    private lateinit var getChipOrder: Button
    var chipSelectedListener: ChipSelectedListener? = null

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
        val selectedChipsOrder = mutableListOf<String>()
        with(binding) {
            selectChip = chipGroup
            getChipOrder = getOrderButton

            for (i in 0 until chipGroup.childCount) {
                val chip = chipGroup.getChildAt(i) as Chip

                chip.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) {
                        selectedChipsOrder.add(chip.text.toString())
                    } else {
                        selectedChipsOrder.remove(chip.text.toString())
                    }
                }
            }
        }

        getChipOrder.setOnClickListener {
            // Filter out unchecked chips before notifying the listener
            val selectedChips = selectedChipsOrder.toList()
            onChipSelected(selectedChips)
            Toast.makeText(activity, "$selectedChips", Toast.LENGTH_SHORT).show()
            dismiss()
        }
    }

    private fun onChipSelected(chips: List<String>) {
        chipSelectedListener?.onChipSelected(chips)
    }
}