package com.russellsolutions.ecolint_android.detectors.environment.idleness

import com.android.tools.lint.checks.infrastructure.LintDetectorTest
import com.android.tools.lint.checks.infrastructure.TestMode
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Issue
import com.russellsolutions.ecolint_android.detectors.utils.ecolint

class KeepCpuOnDetectorTest : LintDetectorTest() {
    override fun getDetector(): Detector = KeepCpuOnDetector()
    override fun getIssues(): List<Issue> = listOf(KeepCpuOnDetector.ISSUE)

    fun testKeepScreenOnTrue() {
        lint().ecolint().files(
            kotlin("""
                import android.view.View
                fun test(view: View) {
                    view.setKeepScreenOn(true)
                }
            """)
        ).skipTestModes(TestMode.SUPPRESSIBLE).run().expectWarningCount(1).expect("""
            src/test.kt:4: Warning: Calling setKeepScreenOn(true) can prevent CPU sleep and drains battery. [🌱 EcoLint: KeepCpuOn]
                                view.setKeepScreenOn(true)
                                ~~~~~~~~~~~~~~~~~~~~~~~~~~
            0 errors, 1 warnings
        """.trimIndent())
    }
}
