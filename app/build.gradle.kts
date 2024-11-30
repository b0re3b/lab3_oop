plugins {
    alias(libs.plugins.android.application)

}

android {
    namespace = "com.example.checkers"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.checkers"
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

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
}

dependencies {
    // JUnit 5 (Jupiter) залежності для юніт-тестів
    testImplementation(libs.junit.jupiter.api)
    testImplementation(libs.jupiter.junit.jupiter.engine)
    testImplementation (libs.mockito.core)
    testImplementation (libs.mockito.mockito.junit.jupiter)

    // Інші залежності для Android
    implementation(libs.appcompat)
    implementation(libs.material)

    // Залежності для стандартних JUnit (JUnit 4)
    testImplementation(libs.junit)

    // Залежності для Android UI тестів
    androidTestImplementation(libs.ext.junit)  // Для тестів UI, як JUnit 4
    androidTestImplementation(libs.espresso.core)  // Для тестів UI з Espresso
}
