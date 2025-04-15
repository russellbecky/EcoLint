package com.russellsolutions.ecolint_android.detectors.environment.leakage

import com.android.tools.lint.checks.infrastructure.LintDetectorTest
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Issue
import com.russellsolutions.ecolint_android.detectors.utils.ecolint

class MediaRecorderLeakDetectorTest : LintDetectorTest() {
    override fun getDetector(): Detector = MediaRecorderLeakDetector()
    override fun getIssues(): List<Issue> = listOf((getDetector() as ResourceLeakDetector).ISSUE)

    fun testMediaRecorderCreation() {
        lint().ecolint().files(
            kotlin("""
                import android.media.MediaRecorder
                fun record() {
                    val rec = MediaRecorder()
                }
            """)
        ).run().expectWarningCount(1).expect("""
            src/test.kt:3: Warning: Failing to call release() on a Media Recorder may lead to continuous battery consumption. [MediaRecorderLeak]
                            fun record() {
                                ~~~~~~
            0 errors, 1 warnings
        """.trimIndent())
    }

    fun testMediaRecorderCreationAndRelease() {
        lint().ecolint().files(
            kotlin("""
                import android.media.MediaRecorder
                fun record() {
                    val rec = MediaRecorder()
                    rec.release()
                }
            """)
        ).run().expectWarningCount(0)
    }
}
