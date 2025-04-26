package com.russellsolutions.ecolint_android.detectors.environment.idleness

import com.android.tools.lint.checks.infrastructure.LintDetectorTest
import com.android.tools.lint.checks.infrastructure.TestMode
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Issue
import com.russellsolutions.ecolint_android.detectors.utils.ecolint

class KeepVoiceAwakeDetectorTest : LintDetectorTest() {
    override fun getDetector(): Detector = KeepVoiceAwakeDetector()
    override fun getIssues(): List<Issue> = listOf(KeepVoiceAwakeDetector.ISSUE)

    fun testVoiceInteractionKeepsScreenOn() {
        lint().ecolint().files(
            kotlin("""
                import android.view.WindowManager
                fun setupVoiceUI(params: WindowManager.LayoutParams) {
                    params.flags = params.flags or WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                }
            """)
        ).skipTestModes(TestMode.SUPPRESSIBLE).run().expectWarningCount(1).expect("""
            src/test.kt:3: Warning: Keeping the screen on for voice interaction prevents battery-saving modes from engaging. [🌱 EcoLint: KeepVoiceAwake]
                            fun setupVoiceUI(params: WindowManager.LayoutParams) {
                            ^
            0 errors, 1 warnings
        """.trimIndent())
    }
}
