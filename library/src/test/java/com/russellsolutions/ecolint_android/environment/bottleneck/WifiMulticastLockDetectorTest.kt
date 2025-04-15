package com.russellsolutions.ecolint_android.detectors.environment.bottleneck

import com.android.tools.lint.checks.infrastructure.LintDetectorTest
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Issue
import com.russellsolutions.ecolint_android.detectors.utils.ecolint


class WifiMulticastLockDetectorTest : LintDetectorTest() {
    override fun getDetector(): Detector = WifiMulticastLockDetector()
    override fun getIssues(): List<Issue> = listOf(WifiMulticastLockDetector.ISSUE)

    fun testMulticastLockAcquire() {
        lint().ecolint().files(
            kotlin("""
                import android.net.wifi.WifiManager
                class Test(val lock: WifiManager.MulticastLock) {
                    fun enable() {
                        lock.acquire()
                    }
                }
            """)
        ).run().expectWarningCount(1).expect("""
            src/Test.kt:5: Warning: Failing to call release() on a MulticastLock can cause battery drain. [WifiMulticastLock]
                                    lock.acquire()
                                    ~~~~~~~~~~~~~~
            0 errors, 1 warnings
        """.trimIndent())
    }
}
