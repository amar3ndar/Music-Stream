plugins {
    alias(libs.plugins.kotlin.android)

    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.amar3ndar.musicstream"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.amar3ndar.musicstream"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    // Import the Firebase BoM

    implementation(platform("com.google.firebase:firebase-bom:33.6.0"))

    implementation("com.google.firebase:firebase-analytics")

    implementation("com.google.firebase:firebase-firestore:25.1.1")
    implementation("com.github.bumptech.glide:glide:4.16.0")
    implementation("com.google.firebase:firebase-database:21.0.0")
    implementation("com.google.firebase:firebase-auth:23.1.0")
    implementation("com.google.android.gms:play-services-base:18.0.1")

    implementation("androidx.media3:media3-exoplayer:1.4.1")
    implementation("androidx.media3:media3-exoplayer-dash:1.4.1")
    implementation("androidx.media3:media3-ui:1.4.1")

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.recyclerview)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
        
}
