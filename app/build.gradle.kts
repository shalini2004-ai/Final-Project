plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {

    namespace = "com.raitha.bharosa"
    compileSdk = 35

    defaultConfig {

        applicationId = "com.raitha.bharosa"

        minSdk = 26
        targetSdk = 35

        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner =
            "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField(
            "String",
            "GEMINI_API_KEY",
            "\"${project.findProperty("GEMINI_API_KEY")}\""
        )
    }

    buildTypes {

        release {

            isMinifyEnabled = false

            proguardFiles(
                getDefaultProguardFile(
                    "proguard-android-optimize.txt"
                ),
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
        buildConfig = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)

    implementation(libs.androidx.lifecycle.runtime.ktx)

    implementation(libs.androidx.activity.compose)

    implementation(
        platform(libs.androidx.compose.bom)
    )

    implementation(libs.androidx.ui)

    implementation(libs.androidx.ui.graphics)

    implementation(
        libs.androidx.ui.tooling.preview
    )

    implementation(libs.androidx.material3)

    implementation(libs.androidx.material.icons)

    implementation(
        libs.androidx.navigation.compose
    )

    implementation(
        libs.androidx.lifecycle.viewmodel.compose
    )

    implementation(libs.retrofit)

    implementation(libs.retrofit.gson)

    implementation(libs.okhttp.logging)

    implementation(
        libs.kotlinx.coroutines.android
    )

    implementation(libs.coil.compose)

    implementation(libs.androidx.datastore)

    implementation(libs.generativeai)

    debugImplementation(
        libs.androidx.ui.tooling
    )
}