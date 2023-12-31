import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
    id ("com.google.dagger.hilt.android")
}

fun getApiKey(propertyKey: String): String {
    return gradleLocalProperties(rootDir).getProperty(propertyKey)
}

android {
    namespace = "com.seo.todayweather"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.seo.todayweather"
        minSdk = 23
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        buildConfigField("String", "OPEN_WEATHER_KEY", getApiKey("open_weather_key"))
        buildConfigField("String", "KAM_KEY", getApiKey("kam_key"))


    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        viewBinding = true
    }
    buildFeatures {
        buildConfig = true
    }
    buildFeatures {
        dataBinding = true
    }
}

dependencies {
    // SplashScreen
    implementation("androidx.core:core-splashscreen:1.1.0-alpha02")
    // Jetpack Navigation
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.4")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.4")

    // WorkManager
    implementation ("androidx.work:work-runtime-ktx:2.8.1")

    implementation ("com.google.android.gms:play-services-location:21.0.1")
    implementation ("com.google.android.gms:play-services-maps:18.2.0")

    // Coroutine
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.1")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.1")

    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:32.5.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-firestore-ktx:24.8.1")

    // Retrofit2
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.squareup.okhttp3:logging-interceptor:4.9.0")

    //Gson
    implementation ("com.google.code.gson:gson:2.10")

    // DI
    implementation("com.google.dagger:hilt-android:2.48.1")
    implementation("com.google.firebase:firebase-storage-ktx:20.3.0")
    implementation("com.google.firebase:firebase-auth-ktx:22.3.0")
    annotationProcessor ("com.google.dagger:hilt-android-compiler:2.48.1")
    annotationProcessor ("com.google.dagger:dagger-compiler:2.48.1") // Dagger compiler
    kapt ("com.google.dagger:hilt-android-compiler:2.48.1")

    // Room DB
    val room_version = "2.5.0"
    implementation("androidx.room:room-runtime:$room_version")
    annotationProcessor("androidx.room:room-compiler:$room_version")
    kapt("androidx.room:room-compiler:$room_version")
    implementation("androidx.room:room-ktx:$room_version")

    // ViewModel, LiveData
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")
    implementation ("androidx.fragment:fragment-ktx:1.6.2")
    implementation ("androidx.activity:activity-ktx:1.8.1")
    debugImplementation ("androidx.fragment:fragment-testing:1.6.2")

    // Kakao
    implementation ("com.kakao.sdk:v2-user:2.18.0") // 카카오 로그인

    //implementation group: 'gun0912.ted', name: 'tedpermission', version: '2.2.3'
    implementation ("io.github.ParkSangGwon:tedpermission-normal:3.3.0")
    //implementation 'androidx.preference:preference-ktx:1.2.0'
    implementation("androidx.preference:preference-ktx:1.2.1") {
        exclude(group = "androidx.lifecycle", module = "lifecycle-viewmodel-ktx")
    }

    // Gilde
    implementation ("com.github.bumptech.glide:glide:4.16.0")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.14.2")
    // Coil (svg 이미지 처리)
    implementation ("io.coil-kt:coil:1.2.0")
    implementation ("io.coil-kt:coil-svg:1.2.0")


    // RxBinding
    implementation("com.jakewharton.rxbinding4:rxbinding:4.0.0")
    implementation("com.jakewharton.rxbinding4:rxbinding-material:4.0.0")
    implementation("com.jakewharton.rxbinding4:rxbinding-core:4.0.0")
    implementation("com.jakewharton.rxbinding4:rxbinding-appcompat:4.0.0")
    implementation("com.jakewharton.rxbinding4:rxbinding-leanback:4.0.0")
    implementation("com.jakewharton.rxbinding4:rxbinding-recyclerview:4.0.0")
    implementation("com.jakewharton.rxbinding4:rxbinding-slidingpanelayout:4.0.0")
    implementation("androidx.annotation:annotation:1.6.0")

    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")

    // FlowBinding
    val flowbinding_version = "1.2.0"
    implementation("io.github.reactivecircus.flowbinding:flowbinding-android:${flowbinding_version}")
    implementation("io.github.reactivecircus.flowbinding:flowbinding-material:${flowbinding_version}")
    implementation("io.github.reactivecircus.flowbinding:flowbinding-activity:${flowbinding_version}")
    implementation("io.github.reactivecircus.flowbinding:flowbinding-appcompat:${flowbinding_version}")
    implementation("io.github.reactivecircus.flowbinding:flowbinding-core:${flowbinding_version}")
    implementation("io.github.reactivecircus.flowbinding:flowbinding-lifecycle:${flowbinding_version}")
    implementation("io.github.reactivecircus.flowbinding:flowbinding-preference:${flowbinding_version}")
    implementation("io.github.reactivecircus.flowbinding:flowbinding-recyclerview:${flowbinding_version}")
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    implementation ("androidx.viewpager2:viewpager2:1.0.0")
}

