plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
}

android {
    namespace = "com.developer.musicapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.developer.musicapp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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

        create("all"){
            dimension = "registrationMode"
        }
    }

    // Define build specific configs
    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

        debug {  }

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

    val nav_version = "2.7.5"
    val compose_version = "1.6.0-alpha08"
    implementation("androidx.navigation:navigation-compose:$nav_version")
    implementation("androidx.compose.ui:ui:$compose_version")
    implementation("androidx.compose.material:material:$compose_version")
    implementation("androidx.compose.ui:ui-tooling-preview:$compose_version")

    // Define build/flavour specific dependencies here if needed
    debugImplementation("debug-build-only-dependcy-name-and-version")

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