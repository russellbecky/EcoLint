package com.russellsolutions.ecolint_android.detectors.environment.idleness

import com.android.tools.lint.checks.infrastructure.LintDetectorTest
import com.android.tools.lint.checks.infrastructure.TestMode
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Issue
import com.russellsolutions.ecolint_android.detectors.utils.ecolint

class DurableWakeLockDetectorTest : LintDetectorTest() {
    override fun getDetector(): Detector = DurableWakeLockDetector()
    override fun getIssues(): List<Issue> = listOf(DurableWakeLockDetector.ISSUE)

    fun testAcquireNoArgs() {
        lint().ecolint().files(
            kotlin("""
                import android.os.PowerManager
                class MyClass(val wakeLock: PowerManager.WakeLock) {
                    fun acquireWakeLock() {
                        wakeLock.acquire()
                    }
                }
            """)
        ).skipTestModes(TestMode.SUPPRESSIBLE).run().expectWarningCount(1).expect("""
            src/MyClass.kt:5: Warning: Prefer setting a timeout when acquiring a wake lock to avoid running down the device's battery excessively. [🌱 EcoLint: DurableWakeLock]
                                    wakeLock.acquire()
                                    ~~~~~~~~~~~~~~~~~~
            0 errors, 1 warnings
        """.trimIndent())
    }
}
