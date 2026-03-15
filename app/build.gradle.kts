plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.dagger.hilt.android") version "2.52"
    id("kotlin-kapt")
}

android {
    namespace = "com.athimue.backyard"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.athimue.backyard"
        minSdk = 23
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

    }

    buildFeatures {
        compose = true
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

    hilt {
        enableAggregatingTask = false
    }
}

dependencies {
    // Compose BOM
    implementation(platform("androidx.compose:compose-bom:2024.02.00"))

    // Activity (ComponentActivity + setContent)

    // Compose core
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.runtime:runtime")
    implementation("androidx.compose.ui:ui-tooling-preview")

    // Android TV Material
    implementation(libs.androidx.tv.material)
    implementation(libs.androidx.activity.ktx)
    implementation(libs.activity.compose)

    // Hilt
    implementation("com.google.dagger:hilt-android:2.52")
    kapt("com.google.dagger:hilt-android-compiler:2.52")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

    // Navigation Compose
    implementation("androidx.navigation:navigation-compose:2.9.7")

    debugImplementation("androidx.compose.ui:ui-tooling")

    implementation(project(":core:database"))
    implementation(project(":core:theme"))
    implementation(project(":feature:countdown:api"))
    implementation(project(":feature:countdown:impl:ui"))
    implementation(project(":feature:countdown:impl:domain"))
    implementation(project(":feature:race:api"))
    implementation(project(":feature:race:impl:ui"))
    implementation(project(":feature:race:impl:data"))
    implementation(project(":feature:race:impl:domain"))
}
