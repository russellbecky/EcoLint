package com.russellsolutions.ecolint_android.detectors.environment.bottleneck

import com.android.tools.lint.checks.infrastructure.LintDetectorTest
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Issue
import com.russellsolutions.ecolint_android.detectors.utils.ecolint


class UncompressedOutputStreamDetectorTest : LintDetectorTest() {
    override fun getDetector(): Detector = UncompressedOutputStreamDetector()
    override fun getIssues(): List<Issue> = listOf(UncompressedOutputStreamDetector.ISSUE)

    fun testUncompressedOutputStream() {
        lint().ecolint().files(
            kotlin("""
                import java.io.OutputStream
                import java.net.URL
                class Example {
                    fun send() {
                        val url = URL("http://example.com")
                        val stream: OutputStream = OutputStream(url.openConnection().getOutputStream())
                    }
                }
            """)
        ).run().expectWarningCount(1).expect("""
            src/Example.kt:7: Warning: Prefer wrapping OutputStream with GZIPOutputStream for better energy efficiency. [UncompressedData]
                                    val stream: OutputStream = OutputStream(url.openConnection().getOutputStream())
                                                               ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            0 errors, 1 warnings
        """.trimIndent())
    }
}
