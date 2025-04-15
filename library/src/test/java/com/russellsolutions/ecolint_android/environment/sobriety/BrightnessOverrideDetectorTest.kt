package com.russellsolutions.ecolint_android.detectors.environment.sobriety

import com.android.tools.lint.checks.infrastructure.LintDetectorTest
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Issue
import com.russellsolutions.ecolint_android.detectors.utils.ecolint

class BrightnessOverrideDetectorTest : LintDetectorTest() {
    override fun getDetector(): Detector = BrightnessOverrideDetector()
    override fun getIssues(): List<Issue> = listOf(BrightnessOverrideDetector.ISSUE)

    fun testBrightnessOverride() {
        lint().ecolint().files(
            kotlin("""
                import android.view.WindowManager
                fun adjust(params: WindowManager.LayoutParams) {
                    params.screenBrightness = 1f
                }
            """)
        ).run().expectWarningCount(1).expect("""
            src/test.kt:4: Warning: Forcing brightness to max value may cause useless energy consumption. [MaxBrightnessAssignment]
                                params.screenBrightness = 1f
                                ~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            0 errors, 1 warnings
        """.trimIndent())
    }

    fun testIgnoresBrightnessSetToLower() {
        lint().ecolint().files(
            kotlin(
                """
                import android.view.WindowManager
                class MainActivity {
                    fun adjustBrightness(params: WindowManager.LayoutParams) {
                        params.screenBrightness = 0.8f
                    }
                }
                """
            )
        ).run().expectClean()
    }
}