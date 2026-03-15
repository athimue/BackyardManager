plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    id("com.google.dagger.hilt.android") version "2.52"
    id("kotlin-kapt")
}

android {
    namespace = "com.athimue.backyard.feature.countdown.impl.domain"
    compileSdk = 36

    defaultConfig {
        minSdk = 23
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    implementation(project(":feature:countdown:api"))
    implementation(project(":feature:race:api"))
    implementation(project(":feature:race:impl:domain"))

    implementation("com.google.dagger:hilt-android:2.52")
    kapt("com.google.dagger:hilt-android-compiler:2.52")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
}
