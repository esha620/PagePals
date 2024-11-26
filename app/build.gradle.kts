plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.google.gms.google.services)
    id("org.jetbrains.kotlin.kapt")
    id("kotlin-parcelize")
    id("androidx.navigation.safeargs.kotlin")
}

android {
    namespace = "com.example.pagepals1"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.pagepals1"
        minSdk = 26
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        viewBinding = true
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
}

dependencies {

    implementation("androidx.room:room-runtime:2.6.1") // Replace with the latest version
    implementation("androidx.room:room-ktx:2.6.1")
    implementation(libs.firebase.database.ktx)
    implementation (libs.androidx.security.crypto)
    implementation(libs.firebase.common.ktx)
    implementation(libs.androidx.espresso.core)
    implementation(libs.androidx.junit.ktx) // Kotlin extensions for Room
    implementation(libs.firebase.common.ktx) // Kotlin extensions for Room
    implementation(libs.firebase.common.ktx)
    implementation(libs.androidx.junit.ktx)
    implementation(libs.androidx.fragment.testing)
    kapt("androidx.room:room-compiler:2.6.1") // Annotation processor for Room

    //Android Navigation
    implementation("androidx.navigation:navigation-fragment-ktx:2.1.0-alpha05")
    implementation ("androidx.navigation:navigation-ui-ktx:2.1.0-alpha05")

    // lifecycle
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation("androidx.lifecycle:lifecycle-common-java8:2.2.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.2.0")

    // kotlin
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.3.72")
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.5")
    api("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.5")

    // books explore dependencies
    implementation("com.android.volley:volley:1.2.1")
    implementation("com.squareup.picasso:picasso:2.8")
    implementation("com.github.bumptech.glide:glide:4.15.1")

    // location service
    implementation("com.google.android.gms:play-services-location:17.0.0")



    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.auth.ktx)
    implementation(libs.androidx.navigation.runtime.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)


}