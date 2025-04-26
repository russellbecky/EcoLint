package com.russellsolutions.ecolint_android.detectors.xml.power

import com.android.tools.lint.checks.infrastructure.*
import com.russellsolutions.ecolint_android.detectors.utils.ecolint

class CompanionInBackgroundXmlDetectorTest : LintDetectorTest() {
    override fun getDetector() = CompanionInBackgroundXmlDetector()
    override fun getIssues() = listOf(CompanionInBackgroundXmlDetector.ISSUE)

    fun testCompanionPermission() {
        lint().ecolint().files(
            xml(
                "AndroidManifest.xml",
                """
                <manifest xmlns:android="http://schemas.android.com/apk/res/android"
                    package="com.example">

                    <uses-permission android:name="android.permission.REQUEST_COMPANION_RUN_IN_BACKGROUND" />

                </manifest>
                """
            )
        ).skipTestModes(TestMode.SUPPRESSIBLE).run().expectWarningCount(1).expect("""
            AndroidManifest.xml:5: Warning: Using the permission REQUEST_COMPANION_RUN_IN_BACKGROUND will have a negative effect on the device's battery life. [🌱 EcoLint: CompanionBackgroundPermission]
                                <uses-permission android:name="android.permission.REQUEST_COMPANION_RUN_IN_BACKGROUND" />
                                                 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
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

                    <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />

                </manifest>
                """
            )
        ).run().expectClean()
    }
}
