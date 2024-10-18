plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("androidx.room")
    //id("kotlin-android")
    id("kotlin-kapt")
}

android {
    namespace = "com.example.pagepals1"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.pagepals1"
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }

    room {
        schemaDirectory("$projectDir/schemas")
    }
}

dependencies {

    //Android Navigation
    implementation("androidx.navigation:navigation-fragment-ktx:2.1.0-alpha05")
    implementation ("androidx.navigation:navigation-ui-ktx:2.1.0-alpha05")

    // for room
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    //kapt("androidx.room:room-compilier:2.6.1")
    annotationProcessor("androidx.room:room-compilier:2.6.1")


    // lifecycle
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation("androidx.lifecycle:lifecycle-common-java8:2.2.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.2.0")

    // kotlin
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.3.72")
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.5")
    api("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.5")


    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.runtime.ktx)
    implementation(libs.androidx.room.common)
    implementation(libs.androidx.room.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}