package com.russellsolutions.ecolint_android.detectors.xml.sobriety

import com.android.tools.lint.checks.infrastructure.*
import com.russellsolutions.ecolint_android.detectors.utils.ecolint

class DarkUIBrightColorsXmlDetectorTest : LintDetectorTest() {
    override fun getDetector() = DarkUIBrightColorsXmlDetector()
    override fun getIssues() = listOf(DarkUIBrightColorsXmlDetector.ISSUE)

    fun testBrightHexColorsInLayout() {
        lint().ecolint().files(
            xml(
                "res/layout/test.xml",
                """
                <TextView xmlns:android="http://schemas.android.com/apk/res/android"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:textColor="#FFFFFF"
                    android:background="#eeeeee" />
                """
            )
        ).run().expectWarningCount(2).expect("""
            res/layout/test.xml:5: Warning: Avoid using too bright colors for (AM)OLED screens. [BrightColorUsage]
                                android:textColor="#FFFFFF"
                                ~~~~~~~~~~~~~~~~~~~~~~~~~~~
            res/layout/test.xml:6: Warning: Avoid using too bright colors for (AM)OLED screens. [BrightColorUsage]
                                android:background="#eeeeee" />
                                ~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            0 errors, 2 warnings
        """.trimIndent())
    }

    fun testAllowsDarkColors() {
        lint().ecolint().files(
            xml(
                "res/layout/test.xml",
                """
                <TextView xmlns:android="http://schemas.android.com/apk/res/android"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:textColor="#121212"
                    android:background="#222222" />
                """
            )
        ).run().expectClean()
    }
}
