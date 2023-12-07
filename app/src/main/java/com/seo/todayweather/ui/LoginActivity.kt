package com.seo.todayweather.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.AuthErrorCause
import com.kakao.sdk.user.UserApiClient
import com.seo.todayweather.databinding.ActivityLoginBinding
import com.seo.todayweather.ui.style.FirestoreManager
import com.seo.todayweather.util.common.CurrentLocationManager
import com.seo.todayweather.util.common.PermissionCheck
import com.seo.todayweather.util.common.PrefManager
import com.seo.todayweather.util.common.TAG
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var permissionCheck: PermissionCheck

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }
        installSplashScreen()
        initView()
        kakaoLogin()

        /*
         * 단말기가 6.0 이상일 경우 위치퍼미션에 대한 사용자 허락을 받는다
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            permissionCheck()
        }
        if (PrefManager.getInstance().isPermission) {
            lifecycleScope.launch {
                CurrentLocationManager(this@LoginActivity).checkLocationCurrentDevice()
            }
        }
    }

    private fun initView() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
    }

    override fun onResume() {
        super.onResume()
        if (!PrefManager.getInstance().isPermission) {
            permissionCheck()
        }
    }

    private fun permissionCheck() {
        permissionCheck = PermissionCheck(applicationContext, this)
        if (!permissionCheck.currentAppCheckPermission()) {
            permissionCheck.currentAppRequestPermissions()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (!permissionCheck.currentAppPermissionResult(requestCode, grantResults)) {
            permissionCheck.currentAppRequestPermissions()
        } else {
            PrefManager.getInstance().isPermission = true
        }
    }

    /**
     * Kakao login
     *
     */
    private fun kakaoLogin() {
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->

            if (error != null) {
                when {
                    error.toString() == AuthErrorCause.AccessDenied.toString() -> {
                        Toast.makeText(this, "접근이 거부 됨(동의 취소)", Toast.LENGTH_SHORT).show()
                    }

                    error.toString() == AuthErrorCause.InvalidClient.toString() -> {
                        Toast.makeText(this, "유효하지 않은 앱", Toast.LENGTH_SHORT).show()
                    }

                    error.toString() == AuthErrorCause.InvalidGrant.toString() -> {
                        Toast.makeText(this, "인증 수단이 유효하지 않아 인증할 수 없는 상태", Toast.LENGTH_SHORT)
                            .show()
                    }

                    error.toString() == AuthErrorCause.InvalidRequest.toString() -> {
                        Toast.makeText(this, "요청 파라미터 오류", Toast.LENGTH_SHORT).show()
                    }

                    error.toString() == AuthErrorCause.InvalidScope.toString() -> {
                        Toast.makeText(this, "유효하지 않은 scope ID", Toast.LENGTH_SHORT).show()
                    }

                    error.toString() == AuthErrorCause.Misconfigured.toString() -> {
                        Toast.makeText(this, "설정이 올바르지 않음(android key hash)", Toast.LENGTH_SHORT)
                            .show()
                    }

                    error.toString() == AuthErrorCause.ServerError.toString() -> {
                        Toast.makeText(this, "서버 내부 에러", Toast.LENGTH_SHORT).show()
                    }

                    error.toString() == AuthErrorCause.Unauthorized.toString() -> {
                        Toast.makeText(this, "앱이 요청 권한이 없음", Toast.LENGTH_SHORT).show()
                    }

                    else -> { // Unknown
                        Log.d("로그", "kakaoLogin: $error")
                        Toast.makeText(this, "기타 에러", Toast.LENGTH_SHORT).show()
                    }
                }
            } else if (token != null) {
                Toast.makeText(this, "로그인 성공", Toast.LENGTH_SHORT).show()
                UserApiClient.instance.me { user, error ->
                    if (error != null) {
                        Log.e(TAG, "Kakao login failed", error)
                    } else if (user != null) {
                        // Kakao 사용자 정보 가져오기 성공
                        val userData = hashMapOf(
                            "name" to user.kakaoAccount?.profile?.nickname,
                            "profilePictureUrl" to user.kakaoAccount?.profile?.thumbnailImageUrl
                        )
                        PrefManager.getInstance().getUserName = userData["name"]
                        PrefManager.getInstance().getUserImage = userData["profilePictureUrl"]

                        CoroutineScope(Dispatchers.IO).launch {
                            try {
                                user.id.let {
                                    FirestoreManager().db.collection("users")
                                        .document(it.toString())
                                        .set(userData)
                                        .await()
                                }
                            } catch (e: Exception) {
                                Log.e(TAG, "Error saving user data to Firestore", e)
                            }
                        }
                    }
                }
                // 이전에 스타일을 설정했다면 바로 MainActivity
                if(PrefManager.getInstance().selectStyle!=0) {
                    Intent(this, MainActivity::class.java).run {
                        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        startActivity(this)
                    }
                    finish()
                } else { // 설정하지 않은 경우 InitActivity
                    Intent(this, InitActivity::class.java).run {
                        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        startActivity(this)
                    }
                    finish()
                }
            }
        }
        with(binding) {
            btnLogin.setOnClickListener {
                if (UserApiClient.instance.isKakaoTalkLoginAvailable(this@LoginActivity)) {
                    UserApiClient.instance.loginWithKakaoTalk(
                        this@LoginActivity,
                        callback = callback
                    )
                } else {
                    UserApiClient.instance.loginWithKakaoAccount(
                        this@LoginActivity,
                        callback = callback
                    )
                }
            }
        }
    }
}