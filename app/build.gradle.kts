plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("dagger.hilt.android.plugin")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
    kotlin("kapt")
}


android {
    namespace = "com.example.algartechchallenge"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.algartechchallenge"
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

            buildConfigField("String", "PLACES_API_KEY", "\"${System.getenv("PLACES_API_KEY") ?: ""}\"")
            buildConfigField("String", "MAPS_API_KEY", "\"${System.getenv("MAPS_API_KEY") ?: ""}\"")
            buildConfigField("String", "APP_ID_WEATHER", "\"${System.getenv("APP_ID_WEATHER") ?: ""}\"")
        }
        debug {
            buildConfigField("String", "PLACES_API_KEY", "\"${System.getenv("PLACES_API_KEY") ?: ""}\"")
            buildConfigField("String", "MAPS_API_KEY", "\"${System.getenv("MAPS_API_KEY") ?: ""}\"")
            buildConfigField("String", "APP_ID_WEATHER", "\"${System.getenv("APP_ID_WEATHER") ?: ""}\"")

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
        viewBinding = true
        buildConfig = true
    }
}

dependencies {
    customImplementation(Dependencies.app)
    implementation(project(":models"))
    implementation(project(":utilities"))
    implementation(project(":domain"))
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}

secrets {
    propertiesFileName = "secrets.properties"
    defaultPropertiesFileName = "local.properties"
}

