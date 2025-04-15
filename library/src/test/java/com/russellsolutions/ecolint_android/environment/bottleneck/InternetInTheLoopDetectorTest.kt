package com.russellsolutions.ecolint_android.detectors.environment.bottleneck

import com.android.tools.lint.checks.infrastructure.LintDetectorTest
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Issue
import com.russellsolutions.ecolint_android.detectors.utils.ecolint

class InternetInLoopDetectorTest : LintDetectorTest() {
    override fun getDetector(): Detector = InternetInLoopDetector()
    override fun getIssues(): List<Issue> = listOf(InternetInLoopDetector.ISSUE)

    fun testConnectionInLoop() {
        lint().ecolint().files(
            kotlin("""
                import java.net.URL
                fun download() {
                    for (i in 1..5) {
                        URL("https://example.com").openConnection()
                    }
                }
            """)
        ).run().expectWarningCount(1).expect("""
            src/test.kt:5: Warning: Opening connections in loops is battery-intensive. Open them outside the loop when possible. [InternetInLoop]
                                    URL("https://example.com").openConnection()
                                    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            0 errors, 1 warnings
        """.trimIndent())
    }
}
