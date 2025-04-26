package com.russellsolutions.ecolint_android.detectors.xml.idleness

import com.android.tools.lint.checks.infrastructure.LintDetectorTest
import com.android.tools.lint.checks.infrastructure.TestMode
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Issue
import com.russellsolutions.ecolint_android.detectors.utils.ecolint

class KeepCpuOnXmlDetectorTest : LintDetectorTest() {
    override fun getDetector(): Detector = KeepCpuOnXmlDetector()
    override fun getIssues(): List<Issue> = listOf(KeepCpuOnXmlDetector.ISSUE)

    fun testKeepCpuOnPermission() {
        lint().ecolint().files(
            manifest("""
                <manifest xmlns:android="http://schemas.android.com/apk/res/android">
                    <uses-permission android:name="android.permission.WAKE_LOCK" />
                </manifest>
            """)
        ).skipTestModes(TestMode.SUPPRESSIBLE).run().expectWarningCount(1).expect("""
            AndroidManifest.xml:3: Warning: Keeping the screen on should be avoided to avoid draining battery. [🌱 EcoLint: KeepCpuOnPermission]
                    <uses-permission android:name="android.permission.WAKE_LOCK" />
                    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
0 errors, 1 warnings
        """)
    }
}
