package com.russellsolutions.ecolint_android.detectors.environment.batch

import com.android.tools.lint.checks.infrastructure.LintDetectorTest
import com.android.tools.lint.checks.infrastructure.TestMode
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Issue
import com.russellsolutions.ecolint_android.detectors.utils.ecolint

class SensorCoalesceDetectorTest : LintDetectorTest() {
    override fun getDetector(): Detector = SensorCoalesceDetector()
    override fun getIssues(): List<Issue> = listOf(SensorCoalesceDetector.ISSUE)

    fun testMissingSensorLatency() {
        lint().ecolint().files(
            kotlin("""
                import android.hardware.SensorManager
                class Example(val manager: SensorManager) {
                    fun register(sensor: Any, listener: Any, delay: Int) {
                        manager.registerListener(listener, sensor, delay)
                    }
                }
            """), *arrayOf(sensorManagerStub)
        ).skipTestModes(TestMode.SUPPRESSIBLE).run().expectWarningCount(1).expect("""
            src/Example.kt:5: Warning: Prefer using a latency parameter in SensorManager.registerListener to reduce power consumption. [🌱 EcoLint: SensorLatencyCoalescing]
                                    manager.registerListener(listener, sensor, delay)
                                    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            0 errors, 1 warnings
        """.trimIndent())
    }

    private val sensorManagerStub = java(
        """
            package android.hardware;

            import android.annotation.Nullable;
            import android.os.Handler;
            import android.os.MemoryFile;
            import java.util.List;

            public abstract class SensorManager {

                SensorManager() {
                    throw new RuntimeException("Stub!");
                }

                /** @deprecated */
                @Deprecated
                public boolean registerListener(SensorListener listener, int sensors, int rate) {
                    throw new RuntimeException("Stub!");
                }
            }
    """
    ).indented()
}
