plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
    id("androidx.navigation.safeargs.kotlin")
}

android {
    namespace = "com.adrammedia.storenet"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.adrammedia.storenet"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
        dataBinding =true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    //Coroutines
    implementation (libs.kotlinx.coroutines.android)
    implementation (libs.kotlinx.coroutines.core)
    //Lifecycle
    implementation (libs.androidx.lifecycle.viewmodel.ktx)
    implementation (libs.androidx.lifecycle.runtime.ktx)
    implementation (libs.androidx.lifecycle.livedata.ktx)
    implementation (libs.androidx.activity.ktx)
    //Dagger Hilt
    implementation (libs.hilt.android)
    kapt (libs.hilt.compiler)
    kapt (libs.kotlinx.metadata.jvm)

    //OkHttp
    implementation (libs.okhttp)
    implementation (libs.logging.interceptor)
    //Retrofit
    implementation (libs.retrofit)
    implementation (libs.converter.gson)
    //Gson
    implementation (libs.gson)
    //Coil
    implementation (libs.coil)

    implementation (libs.williamchart)

    implementation (libs.jsoup)
    //No internet
    implementation (libs.material.v1110)

    implementation (libs.oopsnointernet)

    //Glide
    implementation (libs.glide)

    //Circle image

    implementation (libs.circleimageview)

    //Location
    implementation (libs.play.services.location)

}
kapt {
    correctErrorTypes = true
}