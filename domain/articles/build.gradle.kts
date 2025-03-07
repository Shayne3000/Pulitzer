plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
}

android {
    namespace = "com.senijoshua.pulitzer.domain.article"
    compileSdk = 35

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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

    hilt {
        enableAggregatingTask = true
    }

    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation(project(":core:model"))
    // Kotlin Coroutines & Flow
    implementation(libs.kotlin.coroutines)
    // Paging
    implementation(libs.paging.runtime)
    implementation(libs.paging.compose)
    // Hilt
    implementation(libs.hilt)
    ksp(libs.hilt.compiler)
    // Test dependencies
    testImplementation(libs.junit)
    testImplementation(libs.paging.test)
    testImplementation(libs.kotlin.coroutines.test)
}
