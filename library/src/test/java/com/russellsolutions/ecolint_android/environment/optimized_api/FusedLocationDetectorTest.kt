package com.russellsolutions.ecolint_android.detectors.environment.optimized_api

import com.android.tools.lint.checks.infrastructure.LintDetectorTest
import com.android.tools.lint.checks.infrastructure.TestMode
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Issue
import com.russellsolutions.ecolint_android.detectors.utils.ecolint

class FusedLocationDetectorTest : LintDetectorTest() {
    override fun getDetector(): Detector = FusedLocationDetector()
    override fun getIssues(): List<Issue> = listOf(FusedLocationDetector.ISSUE)

    fun testAndroidLocationOnly() {
        lint().ecolint().files(
            kotlin("""
                import android.location.LocationManager
                class Example {
                    fun track() {
                        val manager = LocationManager::class.java
                    }
                }
            """).indented(), *arrayOf(trackerStub)
        ).testModes(TestMode.IMPORT_ALIAS).run().expectWarningCount(2).expect("""
            src/Example.kt:1: Warning: Use com.google.android.gms.location instead of android.location to maximize battery life. [FusedLocation]
            import android.location.LocationManager
            ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            src/Example.kt:2: Warning: Use com.google.android.gms.location instead of android.location to maximize battery life. [FusedLocation]
            import android.location.LocationManager as IMPORT_ALIAS_1_LOCATIONMANAGER
            ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            0 errors, 2 warnings
        """.trimIndent())
    }

    private val trackerStub = java(
        """
            package android.location;
            class LocationManager {
                LocationManager() { }
            }
    """
    ).indented()
}
