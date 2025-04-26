plugins {
    alias(libs.plugins.android.lint)
    alias(libs.plugins.kotlin.jvm)
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
    compileOnly(libs.bundles.lint.api)
    testImplementation(libs.bundles.lint.tests)
    testImplementation(libs.lint.api)
}

tasks.jar {
    manifest {
        attributes("Lint-Registry-v2" to "com.russellsolutions.ecolint_android.registry.EcoLintDetectorRegistry")
    }
}
