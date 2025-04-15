package com.russellsolutions.ecolint_android.detectors.environment.sobriety

import com.android.tools.lint.checks.infrastructure.LintDetectorTest
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Issue
import com.russellsolutions.ecolint_android.detectors.utils.ecolint

class ThriftyGeolocationMinDistanceDetectorTest : LintDetectorTest() {
    override fun getDetector(): Detector = ThriftyGeolocationMinDistanceDetector()
    override fun getIssues(): List<Issue> = listOf(ThriftyGeolocationMinDistanceDetector.ISSUE)

    fun testMinDistanceZero() {
        lint().ecolint().files(
            kotlin("""
                import android.location.LocationManager
                fun track(lm: LocationManager) {
                    lm.requestLocationUpdates("gps", 1000L, 0f, {})
                }
            """)
        ).run().expectWarningCount(1).expect("""
            src/test.kt:4: Warning: Location updates should be done with a distance interval greater than 0. [ThriftyMinDistance]
                                lm.requestLocationUpdates("gps", 1000L, 0f, {})
                                                                        ~~
            0 errors, 1 warnings
        """.trimIndent())
    }
}
