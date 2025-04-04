plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.room)
}

android {
    namespace = "com.senijoshua.pulitzer.core.database"
    compileSdk = 34

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

    room {
        schemaDirectory("$projectDir/schemas")
    }

    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    // Hilt
    implementation(libs.core.ktx)
    implementation(libs.hilt)
    ksp(libs.hilt.compiler)

    // Room
    implementation(libs.room)
    ksp(libs.room.compiler)
    // Kotlin Extensions and Coroutines support for Room
    implementation(libs.room.ktx)
    // Paging support for Room
    implementation(libs.room.paging)

    // Paging
    implementation(libs.paging.runtime)
    implementation(libs.paging.compose)
}
