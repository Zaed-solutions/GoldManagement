plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.firebase)
    id("com.google.firebase.crashlytics")
}

android {
    namespace = "com.zaed.accountant"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.zaed.accountant"
        minSdk = 24
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
}

dependencies {
    implementation(project(":common"))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation(libs.kotlin.compose.compiler.plugin)
    //Kotlinx-Serialization
    implementation(libs.kotlinx.serialization.json)
    //Kotlinx-DateTime
    implementation(libs.kotlinx.datetime)
    //Compose ViewModel Lifecycle
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    //Compose Navigation
    implementation(libs.androidx.navigation.compose)
    //Material3 Extended Icons
    implementation(libs.androidx.material.icons.extended)
    //Kotlinx-Coroutines
    implementation (libs.kotlinx.coroutines.core)
    //Coil
    implementation(libs.coil.compose)
    //Splash Screen
    implementation(libs.androidx.core.splashscreen)
    //Koin
    implementation(libs.koin.androidx.compose)
    implementation(libs.koin.androidx.compose.navigation)
    //Google Fonts
    implementation(libs.androidx.ui.text.google.fonts)
    //Lottie
    implementation(libs.lottie.compose)
}