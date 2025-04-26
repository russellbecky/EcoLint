package com.russellsolutions.ecolint_android.detectors.environment.leakage

import com.android.tools.lint.checks.infrastructure.LintDetectorTest
import com.android.tools.lint.checks.infrastructure.TestMode
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Issue
import com.russellsolutions.ecolint_android.detectors.utils.ecolint

class MediaPlayerLeakDetectorTest : LintDetectorTest() {
    override fun getDetector(): Detector = MediaPlayerLeakDetector()
    override fun getIssues(): List<Issue> = listOf((getDetector() as ResourceLeakDetector).ISSUE)

    fun testMediaPlayerCreation() {
        lint().ecolint().files(
            kotlin("""
                import android.media.MediaPlayer
                fun play() {
                    val mp = MediaPlayer()
                }
            """)
        ).skipTestModes(TestMode.SUPPRESSIBLE).run().expectWarningCount(1).expect("""
            src/test.kt:3: Warning: Failing to call release() on a Media Player may lead to continuous battery consumption. [🌱 EcoLint: MediaPlayerLeak]
                            fun play() {
                                ~~~~
            0 errors, 1 warnings
        """.trimIndent())
    }

    fun testMediaPlayerCreationAndRelease() {
        lint().ecolint().files(
            kotlin("""
                import android.media.MediaPlayer
                fun play() {
                    val mp = MediaPlayer()
                    mp.release()
                }
            """)
        ).run().expectWarningCount(0)
    }
}
