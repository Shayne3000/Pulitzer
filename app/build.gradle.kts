plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
}

android {
    namespace = "com.senijoshua.pulitzer"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.senijoshua.pulitzer"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.11"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    //// Modules ////
    // Feature
    implementation(project(":feature:home"))
    implementation(project(":feature:bookmarks"))
    implementation(project(":feature:details"))

    // Domain
    implementation(project(":domain:articles"))

    // Data
    implementation(project(":data:articles"))

    // Core
    implementation(project(":core:database"))
    implementation(project(":core:network"))
    implementation(project(":core:test"))
    implementation(project(":core:ui"))

    // Kotlin extensions & core Android
    implementation(libs.activity.compose)

    // Hilt
    implementation(libs.hilt)
    ksp(libs.hilt.compiler)

    // Coil
    implementation(libs.coil)

    // Compose UI
    implementation(platform(libs.compose.bom))
    implementation(libs.navigation)

    //// Test dependencies ////
    // Local tests
    testImplementation(libs.hilt.android.testing)
    kspTest(libs.hilt.compiler)

    // Instrumented tests
    androidTestImplementation(libs.hilt.android.testing)
    kspAndroidTest(libs.hilt.compiler)
    androidTestImplementation(libs.navigation.testing)
    androidTestImplementation(libs.androidx.test.ext.junit)
}
