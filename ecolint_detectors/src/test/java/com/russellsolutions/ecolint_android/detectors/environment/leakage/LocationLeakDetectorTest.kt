package com.russellsolutions.ecolint_android.detectors.environment.leakage

import com.android.tools.lint.checks.infrastructure.LintDetectorTest
import com.android.tools.lint.checks.infrastructure.TestMode
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Issue
import com.russellsolutions.ecolint_android.detectors.utils.ecolint

class LocationLeakDetectorTest : LintDetectorTest() {
    override fun getDetector(): Detector = LocationLeakDetector()
    override fun getIssues(): List<Issue> = listOf((getDetector() as ResourceLeakDetector).ISSUE)

    fun testRequestLocationUpdates() {
        lint().ecolint().files(
            kotlin("""
                import android.location.LocationManager
                fun trackLocation(manager: LocationManager) {
                    manager.requestLocationUpdates("gps", 1000L, 1f, {})
                }
            """)
        ).skipTestModes(TestMode.SUPPRESSIBLE).run().expectWarningCount(1).expect("""
            src/test.kt:3: Warning: Failing to call android.location.LocationManager#removeUpdates() can drain the battery in just a few hours. [🌱 EcoLint: LocationLeak]
                            fun trackLocation(manager: LocationManager) {
                                ~~~~~~~~~~~~~
            0 errors, 1 warnings
        """.trimIndent())
    }

    fun testRequestLocationUpdatesAndRemoveUpdates() {
        lint().ecolint().files(
            kotlin(
                """
                import android.location.LocationManager
                fun trackLocation(manager: LocationManager) {
                    manager.requestLocationUpdates("gps", 1000L, 1f, {})
                    manager.removeUpdates({})
                }
            """
            )
        ).run().expectWarningCount(0)
    }
}
