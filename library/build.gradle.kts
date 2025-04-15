plugins {
    id("java-library")
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.android.lint)
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

lint {
    htmlReport = true
    htmlOutput = file("lint-report.html")
    textReport = true
    absolutePaths = false
    ignoreTestSources = true
}

dependencies {
    // For a description of the below dependencies, see the main project README
    compileOnly(libs.bundles.lint.api)
    testImplementation(libs.bundles.lint.tests)
}


//plugins {
//    alias(libs.plugins.android.library)
//    alias(libs.plugins.kotlin.android)
//}
//
//android {
////    namespace = "com.russellsolutions.ecolint_android"
////    compileSdk = 35
////
////    defaultConfig {
////        applicationId = "com.russellsolutions.ecolint_android"
////        minSdk = 24
////        targetSdk = 35
////        versionCode = 1
////        versionName = "1.0"
////
////        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
////    }
////
////    buildTypes {
////        release {
////            isMinifyEnabled = false
////            proguardFiles(
////                getDefaultProguardFile("proguard-android-optimize.txt"),
////                "proguard-rules.pro"
////            )
////        }
////    }
//    compileOptions {
//        sourceCompatibility = JavaVersion.VERSION_17
//        targetCompatibility = JavaVersion.VERSION_17
//    }
//    kotlinOptions {
//        jvmTarget = "17"
//    }
//}
//
//dependencies {
//    compileOnly(libs.lint.api)
//    testImplementation(libs.junit.v4132)
//    testImplementation(libs.lint.api)
//    testImplementation(libs.lint.tests)
//    testImplementation(libs.kotlin.test)
//}





//jar {
//    manifest {
//        attributes('Lint-Registry-v2': 'com.russellsolutions.ecolint_android.registry.EcoLintDetectorRegistry')
//    }
//}



//dependencies {
//    implementation(libs.androidx.core.ktx)
//    implementation(libs.androidx.appcompat)
//    implementation(libs.material)
//    testImplementation(libs.junit)
//    androidTestImplementation(libs.androidx.junit)
//    androidTestImplementation(libs.androidx.espresso.core)
//}