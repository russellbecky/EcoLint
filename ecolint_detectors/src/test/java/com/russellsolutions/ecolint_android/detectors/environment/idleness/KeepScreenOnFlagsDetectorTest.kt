package com.russellsolutions.ecolint_android.detectors.environment.idleness

import com.android.tools.lint.checks.infrastructure.LintDetectorTest
import com.android.tools.lint.checks.infrastructure.TestMode
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Issue
import com.russellsolutions.ecolint_android.detectors.utils.ecolint

class KeepScreenOnFlagsDetectorTest : LintDetectorTest() {
    override fun getDetector(): Detector = KeepScreenOnFlagsDetector()
    override fun getIssues(): List<Issue> = listOf(KeepScreenOnFlagsDetector.ISSUE)

    fun testSetFlagKeepScreenOn() {
        lint().ecolint().files(
            kotlin("""
                import android.view.Window
                import android.view.WindowManager
                fun test(window: Window) {
                    window.setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, 0)
                }
            """)
        ).skipTestModes(TestMode.SUPPRESSIBLE).run().expectWarningCount(1).expect("""
            src/test.kt:4: Warning: Setting FLAG_KEEP_SCREEN_ON may unnecessarily keep screen and CPU active. Using FLAG_KEEP_SCREEN_ON can prevent the device from sleeping, which drains battery. [🌱 EcoLint: KeepScreenOnSetFlag]
                            fun test(window: Window) {
                            ^
            0 errors, 1 warnings
        """.trimIndent())
    }

    fun testAddFlagKeepScreenOn() {
        lint().ecolint().files(
            kotlin("""
                import android.view.WindowManager
                fun test(flags: Int, params: WindowManager.LayoutParams) {
                    params.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
                    params.flags = params.flags or WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                }
            """)
        ).skipTestModes(TestMode.SUPPRESSIBLE).run().expectWarningCount(1).expect("""
            src/test.kt:3: Warning: Setting FLAG_KEEP_SCREEN_ON may unnecessarily keep screen and CPU active. Using FLAG_KEEP_SCREEN_ON can prevent the device from sleeping, which drains battery. [🌱 EcoLint: KeepScreenOnSetFlag]
                            fun test(flags: Int, params: WindowManager.LayoutParams) {
                            ^
            0 errors, 1 warnings
        """.trimIndent())
    }
}
