// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    id("com.android.library") version "8.5.1" apply false
    id("com.google.gms.google-services") version "4.4.2" apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
}