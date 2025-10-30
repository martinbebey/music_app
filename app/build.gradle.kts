plugins {
    alias(libs.plugins.androidApplication) // First defined in the project gradle file
    alias(libs.plugins.jetbrainsKotlinAndroid)
}

apply<HelloWorldPlugin>() // Entry point for plugin

// Could define your own custon plugin that does something once you apply it in gradle
class HelloWorldPlugin: Plugin<Project>{
    override fun apply(target: Project) {
        println("Hello World!")
    }
}

android {
    namespace = "com.developer.musicapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.developer.musicapp" // updating this will result in google play store treating the new release as a new app
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        multiDexEnabled = false // Safe if total methods < 64K
    }

    // Define the type of product flavour dimensions
    flavorDimensions += listOf("licenseMode", "registrationMode")

    // Define the product flavours for each flavour dimension (such as paid and unpaid)
    productFlavors{
        create("paid"){
            dimension = "licenseMode"
            applicationIdSuffix = ".paid" // Added to the package name
            versionNameSuffix = "-paid" // Added to the version
        }

        create("unpaid"){
            dimension = "licenseMode"
            applicationIdSuffix = ".unpaid"
            versionNameSuffix = "-unpaid"
        }

        create("all"){
            dimension = "registrationMode"
        }
    }

    // Define build specific configs
    buildTypes {
        release {
            // Set to true for just release? so that gradle will use R8 tool to obfuscate code
            // Obfuscate = transform names of all functions/classes/files into short unreadable names
            // To prevent reverse engineering from an apk
            isMinifyEnabled = true
            isDebuggable = false // Controls whether the APK can be debugged using Android Studio / adb.
            isJniDebuggable = false // Controls whether native code (C/C++ via JNI) can be debugged.
            isRenderscriptDebuggable = false // Controls whether RenderScript code (used for high-performance GPU/CPU operations) is debuggable.
            isShrinkResources = true       // Remove unused resources (like drawables not referenced in code). Only works if minify is true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")
        }

        debug {
            // Stack traces are also obfuscated so we don't want that in a debug build
            isMinifyEnabled = false
        }

        // Beta build
        create("beta"){
            initWith(getByName("debug")) // Init with debug settings
            applicationIdSuffix = ".beta"
            versionNameSuffix = "-beta"
            isDebuggable = true
            isMinifyEnabled = false
            matchingFallbacks += listOf("debug") // Use debug resources if none are defined for staging
            buildConfigField(type = "String", name = "API_URL", value = "\"https://staging.api.example.com\"")
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
        buildConfig = true
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

//kotlin {
//    jvmToolchain(11) // Optional: specifies the JDK for Kotlin compilation
//}

//tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
//    kotlinOptions {
//        jvmTarget = "1.8" // keeps bytecode compatible
//    }
//    incremental = true // Enables incremental compilation
//}

dependencies {

    val nav_version = "2.7.5"
    val compose_version = "1.6.0-alpha08"
    implementation("androidx.navigation:navigation-compose:$nav_version")
    implementation("androidx.compose.ui:ui:$compose_version")
    implementation("androidx.compose.material:material:$compose_version")
    implementation("androidx.compose.ui:ui-tooling-preview:$compose_version")

    implementation(project(":streaming")) // To add a library from our own project

    // Define build/flavour specific dependencies here if needed
//    debugImplementation("debug-build-only-dependcy-name-and-version")

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