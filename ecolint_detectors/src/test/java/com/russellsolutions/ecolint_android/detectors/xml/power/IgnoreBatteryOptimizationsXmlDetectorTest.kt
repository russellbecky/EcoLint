package com.russellsolutions.ecolint_android.detectors.xml.power

import com.android.tools.lint.checks.infrastructure.*
import com.russellsolutions.ecolint_android.detectors.utils.ecolint

class IgnoreBatteryOptimizationsXmlDetectorTest : LintDetectorTest() {
    override fun getDetector() = IgnoreBatteryOptimizationsXmlDetector()
    override fun getIssues() = listOf(IgnoreBatteryOptimizationsXmlDetector.ISSUE)

    fun testIgnoreBatteryOptimizationPermission() {
        lint().ecolint().files(
            xml(
                "AndroidManifest.xml",
                """
                <manifest xmlns:android="http://schemas.android.com/apk/res/android"
                    package="com.example">

                    <uses-permission android:name="android.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS" />

                </manifest>
                """
            )
        ).skipTestModes(TestMode.SUPPRESSIBLE).run().expectWarningCount(1).expect("""
            AndroidManifest.xml:5: Warning: Battery optimization should not be ignored. [🌱 EcoLint: IgnoreBatteryOptimizationsPermission]
                                <uses-permission android:name="android.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS" />
                                                 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            0 errors, 1 warnings
        """.trimIndent())
    }

    fun testAllowsOtherPermissions() {
        lint().ecolint().files(
            xml(
                "AndroidManifest.xml",
                """
                <manifest xmlns:android="http://schemas.android.com/apk/res/android"
                    package="com.example">

                    <uses-permission android:name="android.permission.FOREGROUND_SERVICE" />

                </manifest>
                """
            )
        ).run().expectClean()
    }
}
