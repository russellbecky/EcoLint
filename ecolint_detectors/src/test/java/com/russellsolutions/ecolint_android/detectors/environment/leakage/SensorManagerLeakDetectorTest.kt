package com.russellsolutions.ecolint_android.detectors.environment.leakage

import com.android.tools.lint.checks.infrastructure.LintDetectorTest
import com.android.tools.lint.checks.infrastructure.TestMode
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Issue
import com.russellsolutions.ecolint_android.detectors.utils.ecolint

class SensorManagerLeakDetectorTest : LintDetectorTest() {
    override fun getDetector(): Detector = SensorManagerLeakDetector()
    override fun getIssues(): List<Issue> = listOf((getDetector() as ResourceLeakDetector).ISSUE)

    fun testSensorRegistration() {
        lint().ecolint().files(
            kotlin("""
                import android.hardware.SensorManager
                fun listen(sensors: SensorManager) {
                    sensors.registerListener(null, null, 0)
                }
            """)
        ).skipTestModes(TestMode.SUPPRESSIBLE).run().expectWarningCount(1).expect("""
            src/test.kt:3: Warning: Failing to call android.hardware.SensorManager#unregisterListener() can drain the battery in just a few hours. [🌱 EcoLint: SensorManagerLeak]
                            fun listen(sensors: SensorManager) {
                                ~~~~~~
            0 errors, 1 warnings
        """.trimIndent())
    }

    fun testSensorRegistrationAndUnregisterListener() {
        lint().ecolint().files(
            kotlin("""
                import android.hardware.SensorManager
                fun listen(sensors: SensorManager) {
                    sensors.registerListener(null, null, 0)
                    sensors.unregisterListener(null)
                }
            """), *arrayOf(sensorManagerStub)
        ).run().expectClean()
    }

    private val sensorManagerStub = java(
        """
            package android.hardware;
            public class SensorManager {
                public boolean registerListener(SensorEventListener listener, Sensor sensor, int samplingPeriodUs) { }
                public void unregisterListener(SensorListener listener) { }
            }
    """
    ).indented()
}
