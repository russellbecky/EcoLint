package com.russellsolutions.ecolint_android.detectors.environment.sobriety

import com.android.tools.lint.checks.infrastructure.LintDetectorTest
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Issue
import com.russellsolutions.ecolint_android.detectors.utils.ecolint

class TorchFreeDetectorTest : LintDetectorTest() {
    override fun getDetector(): Detector = TorchFreeDetector()
    override fun getIssues(): List<Issue> = listOf(TorchFreeDetector.ISSUE)

    fun testTorchEnabledTrue() {
        lint().ecolint().files(
            kotlin(
                """
                import android.hardware.camera2.CameraManager
                class TorchExample {
                    fun turnOn(manager: CameraManager) {
                        manager.setTorchMode("0", true)
                    }
                }
                """
            ), *arrayOf(cameraManagerStub)
        ).run().expectWarningCount(1).expect("""
            src/TorchExample.kt:5: Warning: Flashlight is one of the most energy-intensive components. Don't programmatically turn it on. [TorchModeEnabled]
                                    manager.setTorchMode("0", true)
                                                              ~~~~
            0 errors, 1 warnings
        """.trimIndent())
    }

    fun testAllowsTorchDisabledFalse() {
        lint().ecolint().files(
            kotlin(
                """
                import android.hardware.camera2.CameraManager
                class TorchExample {
                    fun turnOff(manager: CameraManager) {
                        manager.setTorchMode("0", false)
                    }
                }
                """
            ), *arrayOf(cameraManagerStub)
        ).run().expectClean()
    }

    private val cameraManagerStub = java(
        """
            package android.hardware.camera2;
            public class CameraManager {
                public void setTorchMode(@NonNull String cameraId, boolean enabled) { }
            }
    """
    ).indented()
}
