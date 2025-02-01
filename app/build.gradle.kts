plugins {

    // "alias(libs.plugins." e mapeado para libs.versions.toml
    alias(libs.plugins.android.application)         // -> android.application
    alias(libs.plugins.jetbrains.kotlin.android)    // -> jetbrains.kotlin.android

    // FIREBASE
    id("com.google.gms.google-services")  // Add the Google services Gradle plugin
}

android {
    namespace = "a26052.pdmshoppinglist"
    compileSdk = 34

    defaultConfig {
        applicationId = "a26052.pdmshoppinglist"
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    // FIREBASE
    // https://firebase.google.com/docs/android/setup#available-libraries
    // Import the Firebase BoM
    implementation(platform("com.google.firebase:firebase-bom:33.8.0"))
    // TODO: Add the dependencies for Firebase products you want to use
    // When using the BoM, don't specify versions in Firebase dependencies
    // By using the Firebase Android BoM, your app will always use compatible Firebase library versions.
    implementation("com.google.firebase:firebase-analytics")    // Google Analytics
    implementation("com.google.firebase:firebase-firestore-ktx") // Firestore (Database)
    implementation("com.google.firebase:firebase-auth-ktx") // (Opcional) Authentication


    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}