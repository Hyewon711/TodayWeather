package com.seo.todayweather.ui.home

import android.util.Log
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.card.MaterialCardView
import com.seo.todayweather.R
import com.seo.todayweather.base.BaseFragment
import com.seo.todayweather.databinding.FragmentHomeBinding
import com.seo.todayweather.util.common.HOME
import com.seo.todayweather.util.extension.changeFragment
import com.seo.todayweather.ui.commend.CommendFragment
import com.seo.todayweather.ui.home.bottomsheet.ChipSelectedListener
import com.seo.todayweather.ui.home.bottomsheet.InitBottomSheet

class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate),
    ChipSelectedListener {
    private lateinit var initBottomSheet: InitBottomSheet
    val TAG: String = "로그"
    override fun onViewCreated() {
        initView()
    }

    private fun initView() {
        initBottomSheet = InitBottomSheet().apply {
            chipSelectedListener = this@HomeFragment
        }
        initBottomSheet.show(childFragmentManager, HOME)

        with(binding) {
            /* 위젯 관리 */
            lyToolbar.ivSettings.setOnAvoidDuplicateClick {
                initBottomSheet.show(childFragmentManager, HOME)
            }

            /* Commend Fragment 이동 */
            lyCommend.setOnAvoidDuplicateClick {
                homeLayout.changeFragment(this@HomeFragment, CommendFragment())
            }
        }

    }
    override fun onChipSelected(chip: List<String>) {
        Log.d(TAG, "onChipSelected: $chip")
        when (chip.size) {
            1 -> addView1(chip[0])
            2 -> {
                addView1(chip[0])
                addView2(chip[1])
            }

            3 -> {
                addView1(chip[0])
                addView3(chip[1], chip[2])
            }

            4 -> {
                addView1(chip[0])
                addView3(chip[1], chip[2])
                addView4(chip[3])
            }
            else -> {
                addView1("비어있음")
            }
        }
    }

    private fun addView1(chipText: String): LinearLayout {
        // HomeFragment의 ly_add_view 레이아웃 찾기
        val parentLayout1 = requireActivity().findViewById<LinearLayout>(R.id.ly_add_view1)

        // 동적으로 추가될 아이템들을 담을 LinearLayout 생성
        val itemContainer = LinearLayout(requireContext())
        itemContainer.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        val marginInPixels = resources.getDimensionPixelSize(R.dimen.home_horizontal_margin)
        (itemContainer.layoutParams as LinearLayout.LayoutParams).setMargins(marginInPixels, marginInPixels, marginInPixels, marginInPixels)

        itemContainer.orientation = LinearLayout.VERTICAL

        // 아이템 1
        val cardView1 =
            layoutInflater.inflate(R.layout.item_home, itemContainer, false) as MaterialCardView
        cardView1.findViewById<TextView>(R.id.tv_test).text = chipText
        setLayoutParamsLong(cardView1)
        itemContainer.addView(cardView1)

        // LinearLayout에 추가
        parentLayout1.addView(itemContainer)

        // Fragment에서 UI를 반환
        return parentLayout1
    }

    private fun addView2(chipText: String): LinearLayout {
        val parentLayout2 = requireActivity().findViewById<LinearLayout>(R.id.ly_add_view2)

        // 동적으로 추가될 아이템들을 담을 LinearLayout 생성
        val itemContainer = LinearLayout(requireContext())
        itemContainer.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        itemContainer.orientation = LinearLayout.HORIZONTAL
        val marginInPixels = resources.getDimensionPixelSize(R.dimen.home_horizontal_margin)
        (itemContainer.layoutParams as LinearLayout.LayoutParams).setMargins(marginInPixels, marginInPixels, marginInPixels, marginInPixels)

        // 아이템 2
        val cardView2 =
            layoutInflater.inflate(R.layout.item_home, itemContainer, false) as MaterialCardView
        cardView2.findViewById<TextView>(R.id.tv_test).text = chipText
        setLayoutParamsShort(cardView2)
        itemContainer.addView(cardView2)

        // LinearLayout에 추가
        parentLayout2.addView(itemContainer)

        return parentLayout2
    }

    private fun addView3(chipText1: String, chipText2: String): LinearLayout {
        val parentLayout3 = requireActivity().findViewById<LinearLayout>(R.id.ly_add_view2)

        // 동적으로 추가될 아이템들을 담을 LinearLayout 생성
        val itemContainer = LinearLayout(requireContext())
        itemContainer.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        itemContainer.orientation = LinearLayout.HORIZONTAL
        val marginInPixels = resources.getDimensionPixelSize(R.dimen.home_horizontal_margin)
        (itemContainer.layoutParams as LinearLayout.LayoutParams).setMargins(marginInPixels, marginInPixels, marginInPixels, marginInPixels)

        // 아이템 2
        val cardView2 =
            layoutInflater.inflate(R.layout.item_home, itemContainer, false) as MaterialCardView
        cardView2.findViewById<TextView>(R.id.tv_test).text = chipText1
        setLayoutParamsShort(cardView2)
        itemContainer.addView(cardView2)

        // 아이템 3
        val cardView3 =
            layoutInflater.inflate(R.layout.item_home, itemContainer, false) as MaterialCardView
        cardView3.findViewById<TextView>(R.id.tv_test).text = chipText2
        setLayoutParamsShort(cardView3)
        itemContainer.addView(cardView3)

        // LinearLayout에 추가
        parentLayout3.addView(itemContainer)

        return parentLayout3
    }

    private fun addView4(chipText: String): LinearLayout {
        // HomeFragment의 ly_add_view 레이아웃 찾기
        val parentLayout4 = requireActivity().findViewById<LinearLayout>(R.id.ly_add_view3)

        // 동적으로 추가될 아이템들을 담을 LinearLayout 생성
        val itemContainer = LinearLayout(requireContext())
        itemContainer.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        itemContainer.orientation = LinearLayout.VERTICAL
        val marginInPixels = resources.getDimensionPixelSize(R.dimen.home_horizontal_margin)
        (itemContainer.layoutParams as LinearLayout.LayoutParams).setMargins(marginInPixels, marginInPixels, marginInPixels, marginInPixels)

        // 아이템 4
        val cardView4 =
            layoutInflater.inflate(R.layout.item_home, itemContainer, false) as MaterialCardView
        cardView4.findViewById<TextView>(R.id.tv_test).text = chipText
        setLayoutParamsLong(cardView4)
        itemContainer.addView(cardView4)

        // LinearLayout에 추가
        parentLayout4.addView(itemContainer)

        // Fragment에서 UI를 반환
        return parentLayout4
    }

    private fun setLayoutParamsLong(cardView: MaterialCardView) {
        val layoutParams = ConstraintLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
        )
        layoutParams.topToTop = cardView.id
        layoutParams.endToEnd = cardView.id
        layoutParams.topMargin = resources.getDimensionPixelSize(R.dimen.home_vertical_margin)
        layoutParams.marginEnd = resources.getDimensionPixelSize(R.dimen.home_vertical_margin)

        // 아이템의 레이아웃 속성을 match_parent로 설정
        cardView.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT,
        )
    }

    private fun setLayoutParamsShort(cardView: MaterialCardView) {
        val layoutParams = ConstraintLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
        )
        layoutParams.topToTop = cardView.id
        layoutParams.endToEnd = cardView.id
        layoutParams.topMargin = resources.getDimensionPixelSize(R.dimen.home_vertical_margin)
        layoutParams.marginEnd = resources.getDimensionPixelSize(R.dimen.home_vertical_margin)

        // 아이템의 레이아웃 속성을 match_parent로 설정
        cardView.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT,
            1f
        )
    }
}