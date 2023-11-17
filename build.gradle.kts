

buildscript {

    repositories {
        mavenCentral()
    }
    dependencies {
        classpath ("com.android.tools.build:gradle:7.0.0") // Use the latest version available
        classpath ("com.google.dagger:hilt-android-gradle-plugin:2.40.5")
        classpath ("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.0")
    }
}
plugins {
    id("com.android.application") version "8.1.1" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
    id ("org.jetbrains.kotlin.jvm") version "1.9.0"
    id ("com.google.devtools.ksp") version "1.9.0-1.0.12"
    id("com.google.gms.google-services") version "4.4.0" apply false
    id("com.google.dagger.hilt.android") version "2.44" apply false

}