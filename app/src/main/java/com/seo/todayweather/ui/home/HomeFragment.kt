package com.seo.todayweather.ui.home

import android.annotation.SuppressLint
import android.graphics.drawable.BitmapDrawable
import android.location.Location
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.card.MaterialCardView
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.seo.todayweather.R
import com.seo.todayweather.base.BaseFragment
import com.seo.todayweather.data.DailyItem
import com.seo.todayweather.data.HourlyItem
import com.seo.todayweather.data.SelectChip
import com.seo.todayweather.data.chooseOutfit
import com.seo.todayweather.databinding.FragmentHomeBinding
import com.seo.todayweather.remote.helper.DatabaseHelper
import com.seo.todayweather.remote.helper.OpenWeatherHelper
import com.seo.todayweather.remote.model.Daily
import com.seo.todayweather.remote.model.HourlyAndCurrent
import com.seo.todayweather.ui.adapter.DailyWeatherAdapter
import com.seo.todayweather.ui.adapter.HourlyWeatherAdapter
import com.seo.todayweather.ui.home.bottomsheet.InitBottomSheet
import com.seo.todayweather.util.common.CurrentLocation
import com.seo.todayweather.util.common.CurrentTemp
import com.seo.todayweather.util.common.HOME
import com.seo.todayweather.util.common.PrefManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale


class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate),
    TextToSpeech.OnInitListener {
    private lateinit var initBottomSheet: InitBottomSheet
    lateinit var openWeatherHelper: OpenWeatherHelper
    lateinit var databaseHelper: DatabaseHelper
    private val storage: FirebaseStorage = Firebase.storage
    private lateinit var textToSpeech: TextToSpeech

    val TAG: String = "로그"
    private var location: Location? = null
    private var addressText: String? = ""
    lateinit var setHourlyData: (ArrayList<HourlyAndCurrent>) -> Unit
    lateinit var setCurrentData: (HourlyAndCurrent) -> Unit
    lateinit var setDailyData: (ArrayList<Daily>) -> Unit
    var hourlyWeatherAdapter = HourlyWeatherAdapter()
    var dailyWeatherAdapter = DailyWeatherAdapter()

    override fun onViewCreated() {
        getLocation()
        getWeatherItem()
        initView()
        initViewFunctionOfCurrentData()
        initViewFunctionOfHourlyData()
        initViewFunctionOfDailyData()
        getDatabaseAndSetView()
    }

    private fun getLocation() {
        CurrentLocation.addLocationChangeListener { location, addressText ->
            this.location = location
            this.addressText = addressText
            Log.d(TAG, "Location updated: $location, Address updated: $addressText")
        }
        if (location != null && addressText != "") {
            with(binding) {
                tvMyLocation.text = addressText.toString()
            }
        }
    }

    private fun getWeatherItem() {
        PrefManager.getInstance().selectChipListLiveData.observe(viewLifecycleOwner, Observer { chipList ->
            if (chipList.isEmpty()) {
                initBottomSheet.show(childFragmentManager, HOME)
            } else {
                onChipSelect(chipList)
            }
        })
    }

    private fun initView() {
        initBottomSheet = InitBottomSheet()
        textToSpeech = TextToSpeech(requireContext(), this@HomeFragment)

        with(binding) {
            /* 상세 날씨 아이템 관리 */
            lyToolbar.ivSettings.setOnAvoidDuplicateClick {
                clearAllViews()
                initBottomSheet.show(childFragmentManager, HOME)
            }

            lyCommend.setOnAvoidDuplicateClick {

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
        openWeatherHelper = OpenWeatherHelper(requireContext()) // 미리 초기화 작업을 실행
    }

    /* 데이터 베이스 초기화작업 */
    private fun getDatabaseAndSetView() {
        CoroutineScope(Dispatchers.IO).launch {
            databaseHelper = DatabaseHelper.getInstance(requireContext())
            // TODO 네트워크가 끊겼을 때 Room DB 사용
            Log.d(TAG, "사이즈 : ${databaseHelper.currentDao().getCurrent().size}")
            if (databaseHelper.currentDao().getCurrent().size > 0) {
                Log.d(TAG, "현재 : ${databaseHelper.currentDao().getCurrent()[0]}") // dt = 1597656499
                Log.d(TAG, "시간별 데이터 개수 : ${databaseHelper.hourlyDao().getHourly().size}개")
                Log.d(TAG, "날짜별 데이터 개수 : ${databaseHelper.dailyDao().getDaily().size}개")
            }
            Log.d(TAG, "데이터베이스 작업 끝")
            getWeatherApi()
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

    /**
     * 시간별 날씨 데이터 view
     */

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
                            "http://openweathermap.org/img/wn/${item.weather[0].icon}@2x.png",
                            "${(item.temp).toInt()}${getString(R.string.celsius)}"
                        )
                    )
                }
            }
        }
    }

    /**
     *  일별 날씨 데이터 view
     *
     */
    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    fun initViewFunctionOfDailyData() {
        setDailyData = { model ->
            model[0].let { item ->
                with(binding) {
                    tvMinMaxTemp.text =
                        "${(item.temp.min).toInt()}${getString(R.string.celsius)} / ${(item.temp.max).toInt()}${
                            getString(
                                R.string.celsius
                            )
                        }"
                }
            }
            while (dailyWeatherAdapter.itemCount > 0) dailyWeatherAdapter.removeItem(0)
            for (i in 1..7) {
                model[i].let { item ->
                    dailyWeatherAdapter.addItem(
                        DailyItem(
                            SimpleDateFormat("MM/dd").format(item.dt * 1000L),
                            "http://openweathermap.org/img/wn/${item.weather[0].icon}@2x.png",
                            "${(item.temp.min).toInt()}",
                            "${(item.temp.max).toInt()}"
                        )
                    )
                }
            }
        }
    }

    /**
     * 현재 날씨 데이터를 기준으로 배경 색상과 현재 기온 변경
     *
     */
    @SuppressLint("SetTextI18n")
    private fun initViewFunctionOfCurrentData() {
        setCurrentData = { model ->
            CurrentTemp.temp = model.temp.toInt()
            model.weather[0].id.let {
                with(binding) {
                    if (it > 800) {
                        lyWeather.background =
                            getDrawable(requireContext(), R.drawable.home_weather_clouds) // 흐림
                        tvWeatherMessage.text = getString(R.string.home_weather_message80x)
                    } else if (it == 800) {
                        lyWeather.background =
                            getDrawable(requireContext(), R.drawable.home_weather_sunny) // 쾌청
                        tvWeatherMessage.text = getString(R.string.home_weather_message800)
                    } else if (it >= 700) {
                        lyWeather.background =
                            getDrawable(requireContext(), R.drawable.home_weather_storm) // 안개
                        tvWeatherMessage.text = getString(R.string.home_weather_message70x)
                    } else if (it >= 600) {
                        lyWeather.background =
                            getDrawable(requireContext(), R.drawable.home_weather_snow) // 눈
                        tvWeatherMessage.text = getString(R.string.home_weather_message60x)
                    } else if (it >= 300) {
                        lyWeather.background =
                            getDrawable(requireContext(), R.drawable.home_weather_rain) // 비
                        tvWeatherMessage.text = getString(R.string.home_weather_message30x)
                    } else {
                        lyWeather.background =
                            getDrawable(requireContext(), R.drawable.home_weather_storm) // 뇌우
                        tvWeatherMessage.text = getString(R.string.home_weather_message0)
                    }
                    // 알림 설정 확인
                    if (PrefManager.getInstance().getTTS) {
                        speakText(tvWeatherMessage.text.toString())
                    }
                }
            }
            Glide.with(this)
                .load("http://openweathermap.org/img/wn/${model.weather[0].icon}@2x.png")
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .apply(RequestOptions().format(DecodeFormat.PREFER_ARGB_8888))
                .into(binding.ivWeather)
            with(binding) {
                tvCurrentTemp.text = "${model.temp.toInt()}${getString(R.string.celsius)}"
            }
            getStorageImageSetView(model.temp.toInt())
        }
    }

    /**
     * firebase storage에서 image svg 파일을 url로 가져오는 메서드
     *
     * @param temp
     */
    private fun getStorageImageSetView(temp: Int) {
        val storageRef: StorageReference =
            storage.getReferenceFromUrl(getString(R.string.storage_url))

        val commendTop = chooseOutfit(temp).first
        val commendBottom = chooseOutfit(temp).second
        val commendOuter = chooseOutfit(temp).third

        with(binding) {
            if (commendTop != null) {
                tvCommendItem1.text = commendTop.outfit
            }
            if (commendBottom != null) {
                tvCommendItem2.text = commendBottom.outfit
            }
            if (commendOuter != null) {
                tvCommendItem3.text = commendOuter.outfit
            }
        }

        Log.d(TAG, "$commendTop $commendBottom $commendOuter")

        val topPath = "icons/" + commendTop.toString() + ".svg"
        val bottomPath = "icons/" + commendBottom.toString() + ".svg"
        val outerPath = "icons/" + commendOuter.toString() + ".svg"
        // 상의 Top
        storageRef.child(topPath).downloadUrl.addOnSuccessListener {
            val uri = it.toString()
            with(binding) {
                commendItem1.loadImageFromUrl(uri)
            }
        }.addOnFailureListener {
            Log.d(TAG, "storage 이미지 가져오기 실패")
        }

        // 하의 Bottom
        storageRef.child(bottomPath).downloadUrl.addOnSuccessListener {
            val uri = it.toString()
            with(binding) {
                commendItem2.loadImageFromUrl(uri)
            }
        }.addOnFailureListener {
            Log.d(TAG, "storage 이미지 가져오기 실패")
        }

        // 겉옷 Outer
        storageRef.child(outerPath).downloadUrl.addOnSuccessListener {
            val uri = it.toString()
            with(binding) {
                commendItem3.loadImageFromUrl(uri)
            }
        }.addOnFailureListener {
            Log.d(TAG, "storage 이미지 가져오기 실패")
        }
    }

    /**
     * Load image from url
     *
     * @param imageUrl
     */
    fun ImageView.loadImageFromUrl(imageUrl: String) {
        val imageLoader = ImageLoader.Builder(requireContext())
            .componentRegistry {
                add(SvgDecoder(context))
            }
            .build()

        val imageRequest = ImageRequest.Builder(requireContext())
            .crossfade(true)
            .crossfade(300)
            .data(imageUrl)
            .target(
                onSuccess = { result ->
                    val bitmap = (result as BitmapDrawable).bitmap
                    this.setImageBitmap(bitmap)
                },
            )
            .build()
        imageLoader.enqueue(imageRequest)
    }


    /**
     * 동적으로 추가한 View에 선택한 항목의 데이터를 set하는 메서드
     * hourly 시간별 일기예보 데이터 API 응답의 response 데이터로 설정한다.
     */

    fun onChipSelect(chip: List<SelectChip>) {
        // fragment에서는 viewLifecycleOwner 를 사용한다.
        // chip 선택으로 추가한 view는 roomDB 데이터를 불러온다.
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                if (databaseHelper.hourlyDao().getHourly().isNotEmpty()) {
                    for (element in chip) {
                        when (element.name) {
                            "미세먼지" -> element.additionalData =
                                databaseHelper.hourlyDao().getHourly()[0].feelstemp

                            "자외선 지수" -> element.additionalData =
                                databaseHelper.hourlyDao().getHourly()[0].uvi

                            "풍속" -> element.additionalData =
                                databaseHelper.hourlyDao().getHourly()[0].windspeed

                            "습도" -> element.additionalData =
                                databaseHelper.hourlyDao().getHourly()[0].humidity

                            "체감온도" -> element.additionalData =
                                databaseHelper.hourlyDao().getHourly()[0].feelstemp
                        }
                        Log.d(
                            TAG,
                            "풍속 넣을 값 : ${databaseHelper.hourlyDao().getHourly()[0].windspeed}"
                        )
                        Log.d(TAG, "additionalData 값 : ${element.additionalData}")
                    }
                }
                launch(Dispatchers.Main) {

                    Log.d(TAG, "onChipSelected: $chip")
                    when (chip.size) {
                        1 -> addView1(chip[0].name, chip[0].additionalData)
                        2 -> {
                            addView1(chip[0].name, chip[0].additionalData)
                            addView2(chip[1].name, chip[1].additionalData)
                        }

                        3 -> {
                            addView1(chip[0].name, chip[0].additionalData)
                            addView3(
                                chip[1].name,
                                chip[1].additionalData,
                                chip[2].name,
                                chip[2].additionalData
                            )
                        }

                        4 -> {
                            addView1(chip[0].name, chip[0].additionalData)
                            addView3(
                                chip[1].name,
                                chip[1].additionalData,
                                chip[2].name,
                                chip[2].additionalData
                            )
                            addView4(chip[3].name, chip[3].additionalData)
                        }

                        else -> {
                            addView1("비어있음", 0)
                        }
                    }
                }
            }
        }
    }

    private fun clearAllViews() {
        val parentLayout1 = requireActivity().findViewById<LinearLayout>(R.id.ly_add_view1)
        parentLayout1.removeAllViews()
        val parentLayout2 = requireActivity().findViewById<LinearLayout>(R.id.ly_add_view2)
        parentLayout2.removeAllViews()
        val parentLayout3 = requireActivity().findViewById<LinearLayout>(R.id.ly_add_view3)
        parentLayout3.removeAllViews()
    }

    private fun addView1(chipText: String, chipData: Number): LinearLayout {
        Log.d(TAG, "addView1: $chipText $chipData")
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
        cardView1.findViewById<TextView>(R.id.tv_title).text = chipText
        cardView1.findViewById<TextView>(R.id.tv_data).text = chipData.toString()

        setLayoutParamsLong(cardView1)
        itemContainer.addView(cardView1)

        // LinearLayout에 추가
        parentLayout1.addView(itemContainer)

        // Fragment에서 UI를 반환
        return parentLayout1
    }

    private fun addView2(chipText: String, chipData: Number): LinearLayout {
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
        cardView2.findViewById<TextView>(R.id.tv_title).text = chipText
        cardView2.findViewById<TextView>(R.id.tv_data).text = chipData.toString()
        setLayoutParamsShort(cardView2)
        itemContainer.addView(cardView2)

        // LinearLayout에 추가
        parentLayout2.addView(itemContainer)

        return parentLayout2
    }

    private fun addView3(
        chipText1: String,
        chipData1: Number,
        chipText2: String,
        chipData2: Number
    ): LinearLayout {
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
        cardView2.findViewById<TextView>(R.id.tv_title).text = chipText1
        cardView2.findViewById<TextView>(R.id.tv_data).text = chipData1.toString()
        setLayoutParamsShort(cardView2)
        itemContainer.addView(cardView2)

        // 아이템 3
        val cardView3 =
            layoutInflater.inflate(R.layout.item_home, itemContainer, false) as MaterialCardView
        cardView3.findViewById<TextView>(R.id.tv_title).text = chipText2
        cardView3.findViewById<TextView>(R.id.tv_data).text = chipData2.toString()
        setLayoutParamsShort(cardView3)
        itemContainer.addView(cardView3)

        // LinearLayout에 추가
        parentLayout3.addView(itemContainer)

        return parentLayout3
    }

    private fun addView4(chipText: String, chipData: Number): LinearLayout {
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
        cardView4.findViewById<TextView>(R.id.tv_title).text = chipText
        cardView4.findViewById<TextView>(R.id.tv_data).text = chipData.toString()
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

    /**
     * On init
     * TTS 엔진 초기화 시간 확보
     * @param status
     */
    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = textToSpeech.setLanguage(Locale.KOREA)

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                // Language data is missing or not supported
                // Handle error accordingly
            }
        } else {
            // Initialization failed
            // Handle error accordingly
        }
    }

    private fun speakText(text: String) {
        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
    }

    override fun onDestroy() {
        if (textToSpeech.isSpeaking) {
            textToSpeech.stop()
        }
        textToSpeech.shutdown()
        super.onDestroy()
    }
}