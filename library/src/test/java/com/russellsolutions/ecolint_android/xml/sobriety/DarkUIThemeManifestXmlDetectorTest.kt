package com.russellsolutions.ecolint_android.detectors.xml.sobriety

import com.android.tools.lint.checks.infrastructure.*
import com.russellsolutions.ecolint_android.detectors.utils.ecolint

class DarkUIThemeManifestXmlDetectorTest : LintDetectorTest() {
    override fun getDetector() = DarkUIThemeManifestXmlDetector()
    override fun getIssues() = listOf(DarkUIThemeManifestXmlDetector.ISSUE)

    fun testLightThemeInManifest() {
        lint().ecolint().files(
            xml(
                "AndroidManifest.xml",
                """
                <manifest xmlns:android="http://schemas.android.com/apk/res/android"
                    package="com.example">

                    <application android:theme="@style/Theme.Material3.Light" />
                </manifest>
                """
            )
        ).run().expectWarningCount(1).expect("""
            AndroidManifest.xml:5: Warning: Using a light theme may have a significant impact on energy consumption on (AM)OLED screens. [LightThemeUsage]
                                <application android:theme="@style/Theme.Material3.Light" />
                                             ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            0 errors, 1 warnings
        """.trimIndent())
    }

    fun testAllowsDarkThemes() {
        lint().ecolint().files(
            xml(
                "AndroidManifest.xml",
                """
                <manifest xmlns:android="http://schemas.android.com/apk/res/android"
                    package="com.example">

                    <application android:theme="@style/Theme.Material3.Dark" />
                </manifest>
                """
            )
        ).run().expectClean()
    }
}
