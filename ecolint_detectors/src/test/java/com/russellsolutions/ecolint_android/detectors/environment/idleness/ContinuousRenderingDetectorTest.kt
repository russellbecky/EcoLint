package com.russellsolutions.ecolint_android.detectors.environment.idleness

import com.android.tools.lint.checks.infrastructure.LintDetectorTest
import com.android.tools.lint.checks.infrastructure.TestMode
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Issue
import com.russellsolutions.ecolint_android.detectors.utils.ecolint

class ContinuousRenderingDetectorTest : LintDetectorTest() {
    override fun getDetector(): Detector = ContinuousRenderingDetector()
    override fun getIssues(): List<Issue> = listOf(ContinuousRenderingDetector.ISSUE)

    fun testContinuousRenderMode() {
        lint().ecolint().files(
            kotlin("""
                import android.opengl.GLSurfaceView
                class TestView(val view: GLSurfaceView) {
                    fun bad() {
                        view.setRenderMode(1)
                    }
                }
            """)
        ).skipTestModes(TestMode.SUPPRESSIBLE).run().expectWarningCount(1).expect("""
            src/TestView.kt:5: Warning: Using RENDERMODE_WHEN_DIRTY instead of RENDERMODE_CONTINUOUSLY can improve battery life. [🌱 EcoLint: ContinuousRendering]
                                    view.setRenderMode(1)
                                    ~~~~~~~~~~~~~~~~~~~~~
            0 errors, 1 warnings
        """.trimIndent())
    }
}
