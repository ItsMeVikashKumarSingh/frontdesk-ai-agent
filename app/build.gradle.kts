plugins {
    alias(libs.plugins.android.application)
    id("com.google.gms.google-services")
    alias(libs.plugins.jetbrains.kotlin.android)

}

android {
    namespace = "ai.frontdesk.agent"
    compileSdk = 35

    defaultConfig {
        applicationId = "ai.frontdesk.agent"
        minSdk = 24
        targetSdk = 35
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
       // sourceCompatibility = JavaVersion.VERSION_21 // Change to VERSION_21
       // targetCompatibility = JavaVersion.VERSION_21 // Change to VERSION_21
    }
    kotlin {
        jvmToolchain(21) // Specify JDK 21
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.firebase.common)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    implementation ("androidx.core:core:1.12.0") // Changed to 1.12.0
    implementation ("androidx.core:core-ktx:1.12.0")

    // Firebase
    implementation (libs.firebase.firestore)
    implementation (libs.firebase.analytics)
    implementation(platform(libs.firebase.bom))

// LiveKit
    var livekit_version = "2.14.2"

    implementation("io.livekit:livekit-android:$livekit_version")
    // CameraX support with pinch to zoom, torch control, etc.
    //implementation ("io.livekit:livekit-android-camerax:$livekit_version")

// Lifecycle ViewModel
    implementation (libs.lifecycle.viewmodel)
    implementation (libs.lifecycle.livedata)
    implementation (libs.kotlinx.coroutines.core)
// TextToSpeech support
    implementation(libs.webrtc)
}