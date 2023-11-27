package com.seo.todayweather.ui.home

import android.annotation.SuppressLint
import android.graphics.Color
import android.location.Location
import android.util.Log
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.card.MaterialCardView
import com.seo.todayweather.R
import com.seo.todayweather.base.BaseFragment
import com.seo.todayweather.data.DailyItem
import com.seo.todayweather.data.HourlyItem
import com.seo.todayweather.databinding.FragmentHomeBinding
import com.seo.todayweather.remote.helper.OpenWeatherHelper
import com.seo.todayweather.remote.model.Daily
import com.seo.todayweather.remote.model.HourlyAndCurrent
import com.seo.todayweather.ui.adapter.DailyWeatherAdapter
import com.seo.todayweather.ui.adapter.HourlyWeatherAdapter
import com.seo.todayweather.ui.commend.CommendFragment
import com.seo.todayweather.ui.home.bottomsheet.ChipSelectedListener
import com.seo.todayweather.ui.home.bottomsheet.InitBottomSheet
import com.seo.todayweather.util.common.CurrentLocation
import com.seo.todayweather.util.common.HOME
import com.seo.todayweather.util.extension.changeFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate),
    ChipSelectedListener {
    private lateinit var initBottomSheet: InitBottomSheet

    val TAG: String = "로그"
    private var location: Location? = null
    lateinit var setHourlyData: (ArrayList<HourlyAndCurrent>) -> Unit
    lateinit var setCurrentData: (HourlyAndCurrent) -> Unit
    lateinit var setDailyData: (ArrayList<Daily>) -> Unit
    var hourlyWeatherAdapter = HourlyWeatherAdapter()
    var dailyWeatherAdapter = DailyWeatherAdapter()


    override fun onViewCreated() {
        getLocation()
        initView()
        getWeatherApi()
        initViewFunctionOfCurrentData()
        initViewFunctionOfHourlyData()
        initViewFunctionOfDailyData()
    }

    private fun getLocation() {
        CurrentLocation.addLocationChangeListener {
            location = CurrentLocation.currentLocation
            Log.d(TAG, "Location updated: $location")
        }
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
            /* adapter 연결 */
            hourlyWeatherRecyclerView.apply {
                adapter = hourlyWeatherAdapter
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                addItemDecoration(DividerItemDecoration(context, 0))
            }

            dailyWeatherRecyclerView.apply {
                adapter = dailyWeatherAdapter
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            }
        }
    }

    /* OpenWeatherRepository API 호출 */
    private fun getWeatherApi() {
        CoroutineScope(Dispatchers.Main).launch {
            location?.let {
                launch(Dispatchers.IO) { // current
                    OpenWeatherHelper(requireContext()).requestCurrentWeatherAPI(
                        it.latitude.toString(),
                        it.longitude.toString(),
                        setCurrentData
                    )
                }
                launch(Dispatchers.IO) { // hourly
                    OpenWeatherHelper(requireContext()).requestHourlyWeatherAPI(
                        it.latitude.toString(),
                        it.longitude.toString(),
                        setHourlyData
                    )
                }
                launch(Dispatchers.IO) { // daily
                    OpenWeatherHelper(requireContext()).requestDailyWeatherAPI(
                        it.latitude.toString(),
                        it.longitude.toString(),
                        setDailyData
                    )
                }
            } ?: launch(Dispatchers.Main) {
                Log.d(
                    com.seo.todayweather.util.common.TAG,
                    "OpenWeatherRepository - getWeatherApi() called null 값? "
                )
            }
        }
    }

    // @SuppressLint("NewApi")는 해당 프로젝트의 설정 된 minSdkVersion 이후에 나온 API를 사용할때  warning을 없애고 개발자가 해당 APi를 사용할 수 있게 합니다.
    @SuppressLint("SimpleDateFormat")
    fun initViewFunctionOfHourlyData() {
        setHourlyData = { model ->
            println("hourly 실행")
            while (hourlyWeatherAdapter.itemCount > 0) hourlyWeatherAdapter.removeItem(0)
            // 시간별 날씨 recycler view 적용
            for (i in 0..23) {
                model[i].let { item ->
                    hourlyWeatherAdapter.addItem(
                        HourlyItem(
                            SimpleDateFormat("HH시").format(item.dt * 1000L),
                            "https://openweathermap.org/img/wn/${item.weather[0].icon}@2x.png",
                            "${(item.temp).toInt()}${getString(R.string.celsius)}"
                        )
                    )
                }
            }
        }
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    fun initViewFunctionOfDailyData() {
        setDailyData = { model ->
            println("daily 실행")
            model[0].let { item ->
                with(binding) {
                    tvTemp.text =
                        "${(item.temp.max).toInt()}${getString(R.string.celsius)} / ${(item.temp.min).toInt()}${
                            getString(R.string.celsius)
                        }"
                }
            }
            while (dailyWeatherAdapter.itemCount > 0) dailyWeatherAdapter.removeItem(0)
            for (i in 1..7) {
                model[i].let { item ->
                    dailyWeatherAdapter.addItem(
                        DailyItem(
                            SimpleDateFormat("MM/dd").format(item.dt * 1000L),
                            "https://openweathermap.org/img/wn/${item.weather[0].icon}@2x.png",
                            "${(item.temp.max).toInt()}",
                            "${(item.temp.min).toInt()}"
                        )
                    )
                }
            }
        }
    }

    private fun initViewFunctionOfCurrentData() {
        val DARK_COLOR = Color.parseColor("#505050")
        val LIGHT_COLOR = Color.parseColor("#eeeeee")

        setCurrentData = { model ->
            println("current 실행")
            model.weather[0].id.let {
//                if (it>800) {
//                    mainConstraintLayout.background = getDrawable(R.drawable.bg_clouds)
//                    changeColor(LIGHT_COLOR)
//                } else if (it==800) {
//                    mainConstraintLayout.background = getDrawable(R.drawable.bg_clear)
//                    changeColor(DARK_COLOR)
//                } else if (it>=700) {
//                    mainConstraintLayout.background = getDrawable(R.drawable.bg_atmosphere)
//                    changeColor(LIGHT_COLOR)
//                } else if (it>=600) {
//                    mainConstraintLayout.background = getDrawable(R.drawable.bg_snow)
//                    changeColor(DARK_COLOR)
//                } else if (it>=300) {
//                    mainConstraintLayout.background = getDrawable(R.drawable.bg_rain)
//                    changeColor(DARK_COLOR)
//                } else {
//                    mainConstraintLayout.background = getDrawable(R.drawable.bg_storm)
//                    changeColor(LIGHT_COLOR)
//                }
            }

//            Glide.with(this)
//                .load("https://openweathermap.org/img/wn/${model.weather[0].icon}@2x.png")
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .apply(RequestOptions().format(DecodeFormat.PREFER_ARGB_8888))
//                .into(mainWeatherImageView)
//            currentTempTextView.text = "${model.temp.toInt()}"
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
        (itemContainer.layoutParams as LinearLayout.LayoutParams).setMargins(
            marginInPixels,
            marginInPixels,
            marginInPixels,
            marginInPixels
        )

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
        (itemContainer.layoutParams as LinearLayout.LayoutParams).setMargins(
            marginInPixels,
            marginInPixels,
            marginInPixels,
            marginInPixels
        )

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
        (itemContainer.layoutParams as LinearLayout.LayoutParams).setMargins(
            marginInPixels,
            marginInPixels,
            marginInPixels,
            marginInPixels
        )

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
        (itemContainer.layoutParams as LinearLayout.LayoutParams).setMargins(
            marginInPixels,
            marginInPixels,
            marginInPixels,
            marginInPixels
        )

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