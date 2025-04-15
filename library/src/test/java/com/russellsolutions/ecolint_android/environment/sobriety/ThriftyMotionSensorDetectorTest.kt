package com.russellsolutions.ecolint_android.detectors.environment.sobriety

import com.android.tools.lint.checks.infrastructure.LintDetectorTest
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Issue
import com.russellsolutions.ecolint_android.detectors.utils.ecolint

class ThriftyMotionSensorDetectorTest : LintDetectorTest() {
    override fun getDetector(): Detector = ThriftyMotionSensorDetector()
    override fun getIssues(): List<Issue> = listOf(ThriftyMotionSensorDetector.ISSUE)

    fun testFastestSensorRate() {
        lint().ecolint().files(
            kotlin("""
                import android.hardware.SensorManager
                fun monitor(sm: SensorManager) {
                    sm.registerListener(null, null, SensorManager.SENSOR_DELAY_FASTEST)
                }
            """)
        ).run().expectWarningCount(1).expect("""
            src/test.kt:4: Warning: Avoid using SENSOR_DELAY_FASTEST or SENSOR_DELAY_GAME as they are more battery-intensive. [ThriftyMotionSensor]
                                sm.registerListener(null, null, SensorManager.SENSOR_DELAY_FASTEST)
                                ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            0 errors, 1 warnings
        """.trimIndent())
    }
}
