plugins {
    alias(libs.plugins.android.application)
  //  id("com.android.application")  //
    id("com.google.gms.google-services")
 //   alias(libs.plugins.google.firebase.crashlytics)
    id("com.google.firebase.crashlytics")



}

android {
    namespace = "com.example.myapplication"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.myapplication"
        minSdk = 23
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
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation (platform("com.google.firebase:firebase-bom:32.7.0") )// Usa la versión más actual
    implementation ("com.google.firebase:firebase-crashlytics")
    implementation ("com.google.firebase:firebase-analytics")
    implementation ("com.google.firebase:firebase-messaging:23.4.1")
  //  implementation platform('com.google.firebase:firebase-bom:32.3.1')
    implementation ("com.google.firebase:firebase-database")
    implementation ("com.google.firebase:firebase-firestore:24.4.4")

  // implementation(platform("com.google.firebase:firebase-bom:33.10.0"))
    //implementation("com.google.firebase:firebase-crashlytics")
    //implementation("com.google.firebase:firebase-analytics")
    implementation ("com.google.firebase:firebase-storage")
    implementation ("com.google.firebase:firebase-auth:22.1.0")
    implementation ("com.google.firebase:firebase-auth:22.1.0")
    implementation ("androidx.core:core:1.12.0")
// Para manejo de imágenes (opcional)
    implementation ("com.github.bumptech.glide:glide:4.15.1")

// Permisos
    implementation ("androidx.activity:activity:1.7.2")
    implementation ("androidx.fragment:fragment:1.6.1")

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.mediarouter)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.crashlytics)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}

