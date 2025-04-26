package com.russellsolutions.ecolint_android.detectors.environment.sobriety

import com.android.tools.lint.checks.infrastructure.LintDetectorTest
import com.android.tools.lint.checks.infrastructure.TestMode
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Issue
import com.russellsolutions.ecolint_android.detectors.utils.ecolint

class VibrationFreeDetectorTest : LintDetectorTest() {
    override fun getDetector(): Detector = VibrationFreeDetector()
    override fun getIssues(): List<Issue> = listOf(VibrationFreeDetector.ISSUE)

    fun testVibratorServiceUsed() {
        lint().ecolint().files(
            kotlin("""
                import android.content.Context
                fun vibrate(ctx: Context) {
                    ctx.getSystemService("vibrator")
                }
            """)
        ).skipTestModes(TestMode.SUPPRESSIBLE).run().expectWarningCount(1).expect("""
            src/test.kt:4: Warning: Avoid using the device vibrator to use less energy. [🌱 EcoLint: VibrationFree]
                                ctx.getSystemService("vibrator")
                                ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            0 errors, 1 warnings
        """.trimIndent())
    }
}
