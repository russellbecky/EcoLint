package com.russellsolutions.ecolint_android.detectors.xml.batch
import com.android.tools.lint.checks.infrastructure.*
import com.russellsolutions.ecolint_android.detectors.utils.ecolint

class ServiceBootTimeXmlDetectorTest : LintDetectorTest() {
    override fun getDetector() = ServiceBootTimeXmlDetector()
    override fun getIssues() = listOf(ServiceBootTimeXmlDetector.ISSUE)

    fun testBootCompletedReceiver() {
        lint().ecolint().files(
            xml(
                "AndroidManifest.xml",
                """
                <manifest xmlns:android="http://schemas.android.com/apk/res/android"
                    package="com.example">

                    <application>
                        <receiver android:name=".MyBootReceiver">
                            <intent-filter>
                                <action android:name="android.intent.action.BOOT_COMPLETED"/>
                            </intent-filter>
                        </receiver>
                    </application>

                </manifest>
                """
            )
        ).skipTestModes(TestMode.SUPPRESSIBLE).run().expectWarningCount(1).expect("""
            AndroidManifest.xml:8: Warning: Avoid using a receiver to launch a service with BOOT_COMPLETED to drain less battery [🌱 EcoLint: BootCompletedReceiver]
                                            <action android:name="android.intent.action.BOOT_COMPLETED"/>
                                                    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            0 errors, 1 warnings
        """.trimIndent())
    }

    fun testAllowsOtherReceivers() {
        lint().ecolint().files(
            xml(
                "AndroidManifest.xml",
                """
                <manifest xmlns:android="http://schemas.android.com/apk/res/android"
                    package="com.example">

                    <application>
                        <receiver android:name=".MyReceiver">
                            <intent-filter>
                                <action android:name="android.intent.action.ACTION_POWER_CONNECTED"/>
                            </intent-filter>
                        </receiver>
                    </application>

                </manifest>
                """
            )
        ).run().expectClean()
    }
}
