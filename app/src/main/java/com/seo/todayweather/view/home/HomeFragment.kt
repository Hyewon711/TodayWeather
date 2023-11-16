package com.seo.todayweather.view.home

import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.card.MaterialCardView
import com.seo.todayweather.R
import com.seo.todayweather.base.BaseFragment
import com.seo.todayweather.databinding.FragmentHomeBinding
import com.seo.todayweather.util.common.HOME
import com.seo.todayweather.view.InitBottomSheet

class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {
    val TAG: String = "로그"
    override fun onViewCreated() {
        initView()
    }

    private fun initView() {
        InitBottomSheet().show(childFragmentManager, HOME)
        with(binding) {
            lyToolbar.ivMenu.setOnClickListener {
                activity?.findViewById<DrawerLayout>(R.id.ly_drawer)
                    ?.openDrawer(GravityCompat.START)
            }
        }
        addView1()
        addView2()
        addView3()
    }

    private fun addView1(): LinearLayout {
        // HomeFragment의 ly_add_view 레이아웃 찾기
        val parentLayout1 = requireActivity().findViewById<LinearLayout>(R.id.ly_add_view1)

        // 동적으로 추가될 아이템들을 담을 LinearLayout 생성
        val itemContainer = LinearLayout(requireContext())
        itemContainer.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        itemContainer.orientation = LinearLayout.VERTICAL

        // 아이템 1
        val cardView1 = layoutInflater.inflate(R.layout.item_home, itemContainer, false) as MaterialCardView
        setLayoutParamsLong(cardView1)
        itemContainer.addView(cardView1)

        // LinearLayout에 추가
        parentLayout1.addView(itemContainer)

        // Fragment에서 UI를 반환
        return parentLayout1
    }

    private fun addView2() : LinearLayout {
        val parentLayout2 = requireActivity().findViewById<LinearLayout>(R.id.ly_add_view2)

        // 동적으로 추가될 아이템들을 담을 LinearLayout 생성
        val itemContainer = LinearLayout(requireContext())
        itemContainer.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        itemContainer.orientation = LinearLayout.HORIZONTAL

        // 아이템 2
        val cardView2 = layoutInflater.inflate(R.layout.item_home, itemContainer, false) as MaterialCardView
        setLayoutParamsShort(cardView2)
        itemContainer.addView(cardView2)

        // 아이템 3
        val cardView3 = layoutInflater.inflate(R.layout.item_home, itemContainer, false) as MaterialCardView
        setLayoutParamsShort(cardView3)
        itemContainer.addView(cardView3)

        // LinearLayout에 추가
        parentLayout2.addView(itemContainer)

        return parentLayout2
    }

    private fun addView3(): LinearLayout {
        // HomeFragment의 ly_add_view 레이아웃 찾기
        val parentLayout3 = requireActivity().findViewById<LinearLayout>(R.id.ly_add_view3)

        // 동적으로 추가될 아이템들을 담을 LinearLayout 생성
        val itemContainer = LinearLayout(requireContext())
        itemContainer.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        itemContainer.orientation = LinearLayout.VERTICAL

        // 아이템 4
        val cardView4 = layoutInflater.inflate(R.layout.item_home, itemContainer, false) as MaterialCardView
        setLayoutParamsLong(cardView4)
        itemContainer.addView(cardView4)

        // LinearLayout에 추가
        parentLayout3.addView(itemContainer)

        // Fragment에서 UI를 반환
        return parentLayout3
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