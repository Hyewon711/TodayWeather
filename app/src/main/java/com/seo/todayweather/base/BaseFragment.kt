package com.seo.todayweather.base

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.google.android.material.navigation.NavigationView
import com.jakewharton.rxbinding4.view.clicks
import com.seo.todayweather.R
import com.seo.todayweather.databinding.LayoutToolbarBinding
import com.seo.todayweather.util.common.CLICK_INTERVAL_TIME
import com.seo.todayweather.util.common.FragmentInflate
import com.seo.todayweather.util.common.RXERROR
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

abstract class BaseFragment<VB : ViewBinding>(
    private val inflate: FragmentInflate<VB>,
) : Fragment() {
    var _binding: VB? = null
    val binding get() = _binding!!

    //    private lateinit var backPressCallback: OnBackPressedCallback // 뒤로 가기 이벤트 콜백
    protected open fun savedInstanceStateOnCreateView() {} // 필요하면 재정의
    protected open fun savedInstanceStateOnViewCreated() {} // 필요하면 재정의
    protected open fun onCreateView() {} // 필요하면 재정의
    protected abstract fun onViewCreated() // 반드시 재정의

    private val compositeDisposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = inflate.invoke(inflater, container, false)
        if (savedInstanceState == null) { // onSaveInstanceState로 데이터를 넘긴 것이 있다면 null이 아니므로 작동X -> onSaveInstanceState 전에 한번만 호출되었으면 하는 것
            savedInstanceStateOnCreateView()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onViewCreated()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        compositeDisposable.dispose() // compositeDisposable 해제
    }

    /**
     * On avoid duplicate click, view에 대한 중복 클릭 방지 이벤트 처리 메서드
     *
     * throttleFrist() 안에 sleep 타임은 0.3초로 설정되어 있음, 0.3초간 클릭 못함
     *
     * @param actionInMainThread : main 쓰레드에서 처리될 이벤트
     * @receiver 모든 view
     */
    fun View.setOnAvoidDuplicateClick(actionInMainThread: () -> Unit) {
        compositeDisposable
            .add(
                this.clicks()
                    .observeOn(Schedulers.io()) // 이후 chain의 메서드들은 쓰레드 io 영역에서 처리
                    .throttleFirst(CLICK_INTERVAL_TIME, TimeUnit.MILLISECONDS)
                    .observeOn(AndroidSchedulers.mainThread()) // 이후 chain의 메서드들은 쓰레드 main 영역에서 처리
                    .subscribe(
                        {
                            actionInMainThread()
                        }, {
                            Log.e(RXERROR, it.toString())
                        }
                    )
            )
    }
}