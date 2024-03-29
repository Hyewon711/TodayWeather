package com.seo.todayweather.ui.commend

import android.graphics.drawable.BitmapDrawable
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.ImageView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.seo.todayweather.R
import com.seo.todayweather.base.BaseFragment
import com.seo.todayweather.data.StylePost
import com.seo.todayweather.data.chooseOutfit
import com.seo.todayweather.databinding.FragmentCommendBinding
import com.seo.todayweather.ui.adapter.StyleRecyclerAdapter
import com.seo.todayweather.util.common.CurrentTemp.temp
import com.seo.todayweather.util.common.PrefManager
import com.seo.todayweather.util.common.TAG
import com.seo.todayweather.ui.viewmodel.StyleViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CommendFragment : BaseFragment<FragmentCommendBinding>(FragmentCommendBinding::inflate) {
    private val viewModel: StyleViewModel by activityViewModels()
    private val storage: FirebaseStorage = Firebase.storage
    private lateinit var styleAdapter: StyleRecyclerAdapter

    override fun onCreateView() {
    }

    override fun onViewCreated() {
        changeStatusBarTextColor(true)
        getStylePost()
        setWearView()
        initView()
    }

    private fun initView() {
        with(binding) {
            lyCommend.setOnAvoidDuplicateClick {

            }
            lyCommendTomorrow.setOnAvoidDuplicateClick {

            }
            btnRefresh.setOnAvoidDuplicateClick {
                lifecycleScope.launch {
                    simulateLoadingProcess()
                }
            }
        }
    }

    /**
     * 현재 온도를 기준으로 랜덤 옷을 가져온다.
     *
     */
    private fun setWearView() {
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
     * svg 파일을 읽은 후 비트맵으로 변환하는 메서드
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

    private fun getStylePost() {
        var postList: List<StylePost>

        // StateFlow를 관찰하고 UI를 업데이트
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.stylePosts.collect {
                    postList = it
                    with(binding.rvStyle) {
                        layoutManager = GridLayoutManager(activity, 2)
                        styleAdapter = StyleRecyclerAdapter(viewModel.stylePosts.value)
                        adapter = StyleRecyclerAdapter(postList.take(4))
                        styleAdapter.filterByCategory(PrefManager.getInstance().selectStyle)
                    }
                }
            }
        }
    }

    private fun changeStatusBarTextColor(isLight: Boolean) {
        // Android 6.0 (API level 23) 이상에서만 동작
        val window: Window? = activity?.window
        if (isLight) {
            // StatusBar 글씨색을 검은색으로 변경
            window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } else {
            // StatusBar 글씨색을 흰색으로 변경
            window?.decorView?.systemUiVisibility = 0
        }
    }

    private suspend fun simulateLoadingProcess() {
        binding.progressBar.visibility = View.VISIBLE
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                setWearView()
            }
        }
        delay(1000)
        binding.progressBar.visibility = View.GONE
    }
}