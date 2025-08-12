plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.st10361554.prog7314_ice_task_4"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.st10361554.prog7314_ice_task_4"
        minSdk = 31
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            // Add BuildConfig field for debug builds
            buildConfigField("String", "NEWS_API_KEY", "\"${findProperty("NEWS_API_KEY") ?: ""}\"")
        }
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

            // Add BuildConfig field for release builds
            buildConfigField("String", "NEWS_API_KEY", "\"${findProperty("NEWS_API_KEY") ?: ""}\"")
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
        viewBinding = true
        buildConfig = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Retrofit and Gson dependencies
    implementation("com.squareup.retrofit2:converter-gson:3.0.0")

    // glide dependencies
    implementation("com.github.bumptech.glide:glide:4.14.2")
}