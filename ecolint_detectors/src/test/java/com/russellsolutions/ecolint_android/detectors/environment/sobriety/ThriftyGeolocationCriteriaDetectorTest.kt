package com.russellsolutions.ecolint_android.detectors.environment.sobriety

import com.android.tools.lint.checks.infrastructure.LintDetectorTest
import com.android.tools.lint.checks.infrastructure.TestMode
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Issue
import com.russellsolutions.ecolint_android.detectors.utils.ecolint

class ThriftyGeolocationCriteriaDetectorTest : LintDetectorTest() {
    override fun getDetector(): Detector = ThriftyGeolocationCriteriaDetector()
    override fun getIssues(): List<Issue> = listOf(ThriftyGeolocationCriteriaDetector.ISSUE)

    fun testMissingBestProvider() {
        lint().ecolint().files(
            kotlin(
                """
                import android.location.LocationManager
                class LocationExample {
                    fun start(manager: LocationManager) {
                        manager.requestLocationUpdates("gps", 0L, 0f, {}) 
                    }
                }
                """
            )
        ).skipTestModes(TestMode.SUPPRESSIBLE).run().expectWarningCount(1).expect("""
            src/LocationExample.kt:5: Warning: You should configure a location provider (LocationManager.getBestProvider(...)) to optimize battery usage. [🌱 EcoLint: ThriftyGeolocationCriteria]
                                    manager.requestLocationUpdates("gps", 0L, 0f, {}) 
                                    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            0 errors, 1 warnings
        """.trimIndent())
    }

    fun testMissingPowerRequirement() {
        lint().ecolint().files(
            kotlin(
                """
                import android.location.LocationManager
                class LocationExample {
                    fun start(manager: LocationManager) {
                        manager.getBestProvider(null, true)
                        manager.requestLocationUpdates("gps", 0L, 0f, {})
                    }
                }
                """
            )
        ).skipTestModes(TestMode.SUPPRESSIBLE).run().expectWarningCount(1).expect("""
            src/LocationExample.kt:5: Warning: You should call Criteria.setPowerRequirement(POWER_LOW) to optimize battery usage. [🌱 EcoLint: ThriftyGeolocationCriteria]
                                    manager.getBestProvider(null, true)
                                    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            0 errors, 1 warnings
        """.trimIndent())
    }

    fun testWrongPowerRequirement() {
        lint().ecolint().files(
            kotlin(
                """ 
                import android.location.Criteria
                class LocationExample {
                    fun config(criteria: Criteria) {
                        criteria.setPowerRequirement(3)
                    }
                }
                """
            )
        ).skipTestModes(TestMode.SUPPRESSIBLE).run().expectWarningCount(1).expect("""
            src/LocationExample.kt:5: Warning: You should set the power requirement to POWER_LOW to optimize battery usage [🌱 EcoLint: ThriftyGeolocationCriteria]
                                    criteria.setPowerRequirement(3)
                                    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            0 errors, 1 warnings
        """.trimIndent())
    }

    fun testAllowsProperPowerRequirement() {
        lint().ecolint().files(
            kotlin(
                """
                import android.location.Criteria
                class LocationExample {
                    fun config(criteria: Criteria) {
                        criteria.setPowerRequirement(1)
                    }
                }
                """
            )
        ).run().expectClean()
    }
}
