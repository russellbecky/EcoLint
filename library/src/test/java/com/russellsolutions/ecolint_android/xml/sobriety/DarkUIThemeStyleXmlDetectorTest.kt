package com.russellsolutions.ecolint_android.detectors.xml.sobriety

import com.android.tools.lint.checks.infrastructure.*
import com.russellsolutions.ecolint_android.detectors.utils.ecolint

class DarkUIThemeStyleXmlDetectorTest : LintDetectorTest() {
    override fun getDetector() = DarkUIThemeStyleXmlDetector()
    override fun getIssues() = listOf(DarkUIThemeStyleXmlDetector.ISSUE)

    fun testLightThemeInStyles() {
        lint().ecolint().files(
            xml(
                "res/values/styles.xml",
                """
                <resources>
                    <style name="AppTheme" parent="@android:style/Theme.Material.Light" />
                </resources>
                """
            )
        ).run().expectWarningCount(1).expect("""
            res/values/styles.xml:3: Warning: Using a light theme may have a significant impact on energy consumption on (AM)OLED screens. [LightThemeUsage]
                                <style name="AppTheme" parent="@android:style/Theme.Material.Light" />
                                                       ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            0 errors, 1 warnings
        """.trimIndent())
    }

    fun testAllowsDarkThemes() {
        lint().ecolint().files(
            xml(
                "res/values/styles.xml",
                """
                <resources>
                    <style name="AppTheme" parent="Theme.Material3.Dark" />
                </resources>
                """
            )
        ).run().expectClean()
    }
}
