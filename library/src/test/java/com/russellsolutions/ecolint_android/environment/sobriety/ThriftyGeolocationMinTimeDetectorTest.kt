package com.russellsolutions.ecolint_android.detectors.environment.sobriety

import com.android.tools.lint.checks.infrastructure.LintDetectorTest
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Issue
import com.russellsolutions.ecolint_android.detectors.utils.ecolint

class ThriftyGeolocationMinTimeDetectorTest : LintDetectorTest() {
    override fun getDetector(): Detector = ThriftyGeolocationMinTimeDetector()
    override fun getIssues(): List<Issue> = listOf(ThriftyGeolocationMinTimeDetector.ISSUE)

    fun testMinTimeZero() {
        lint().ecolint().files(
            kotlin("""
                import android.location.LocationManager
                fun track(lm: LocationManager) {
                    lm.requestLocationUpdates("gps", 0L, 5f, {})
                }
            """)
        ).run().expectWarningCount(1).expect("""
            src/test.kt:4: Warning: Location updates should be done with a time interval greater than 0 to conserve battery. [ThriftyMinTime]
                                lm.requestLocationUpdates("gps", 0L, 5f, {})
                                                                 ~~
            0 errors, 1 warnings
        """.trimIndent())
    }
}
