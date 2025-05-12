// To publish: ./gradlew :ecolint_detectors:publishAllPublicationsToCentralPortal
// (https://medium.com/@iRYO400/how-to-upload-your-android-library-to-maven-central-central-portal-in-2024-af7348742247)
plugins {
    alias(libs.plugins.android.lint)
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.vanniktech.maven.publish)
    alias(libs.plugins.gradleup.maven.publish)
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17
    }
}

lint {
    htmlReport = true
    htmlOutput = file("lint-report.html")
    textReport = true
    absolutePaths = false
    ignoreTestSources = true
}

//nmcp {
//    centralPortal {
//        username = "MzzIc2vF"
//        password = "IQxj08ouH3bavh4x3Cz8Ut2rcaz5MFFGhvNGwMhoA+06"
//        publishingType = "USER_MANAGED"
//    }
//}

dependencies {
    compileOnly(libs.bundles.lint.api)
    testImplementation(libs.bundles.lint.tests)
    testImplementation(libs.lint.api)
}

tasks.jar {
    manifest {
        attributes("Lint-Registry-v2" to "com.russellsolutions.ecolint_android.registry.EcoLintDetectorRegistry")
    }
}
