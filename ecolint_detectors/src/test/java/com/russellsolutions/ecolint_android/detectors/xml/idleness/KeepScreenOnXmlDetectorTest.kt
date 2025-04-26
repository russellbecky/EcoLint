package com.russellsolutions.ecolint_android.detectors.xml.idleness

import com.android.tools.lint.checks.infrastructure.LintDetectorTest
import com.android.tools.lint.checks.infrastructure.TestMode
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Issue
import com.russellsolutions.ecolint_android.detectors.utils.ecolint

class KeepScreenOnXmlDetectorTest : LintDetectorTest() {

    override fun getDetector(): Detector = KeepScreenOnXmlDetector()

    override fun getIssues(): List<Issue> = listOf(KeepScreenOnXmlDetector.ISSUE)

    fun testKeepScreenOnTrue() {
        lint().ecolint().files(
            xml("res/layout/layout.xml", """
                <LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
                    android:layout_width="match_parent"
                    android:layout_height="match_parent"
                    android:keepScreenOn="true">
                </LinearLayout>
            """)
        ).skipTestModes(TestMode.SUPPRESSIBLE).run().expectWarningCount(1).expect("""
            res/layout/layout.xml:2: Warning: Keeping the screen on should be avoided to avoid draining the battery. [🌱 EcoLint: KeepScreenOnAttribute]
                <LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
                ^
0 errors, 1 warnings
        """)
    }
}
