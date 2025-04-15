package com.russellsolutions.ecolint_android.detectors.environment.leakage

import com.android.tools.lint.checks.infrastructure.LintDetectorTest
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Issue
import com.russellsolutions.ecolint_android.detectors.utils.ecolint

class CameraLeakDetectorTest : LintDetectorTest() {
    override fun getDetector(): Detector = CameraLeakDetector()
    override fun getIssues(): List<Issue> = listOf((getDetector() as ResourceLeakDetector).ISSUE)

    fun testCameraOpen() {
        lint().ecolint().files(
            kotlin("""
                import android.hardware.Camera
                fun useCamera() {
                    val cam = Camera.open()
                }
            """)
        ).run().expectWarningCount(1).expect("""
            src/test.kt:3: Warning: Failing to call android.hardware.Camera#release() can drain the battery in just a few hours. [CameraLeak]
                            fun useCamera() {
                                ~~~~~~~~~
            0 errors, 1 warnings
        """.trimIndent())
    }

    fun testCameraOpenAndRelease() {
        lint().ecolint().files(
            kotlin(
                """
                import android.hardware.Camera
                fun useCamera() {
                    val cam = Camera.open()
                    cam.release()
                }
            """
            )
        ).run().expectWarningCount(0)
    }
}
