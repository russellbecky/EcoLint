package com.russellsolutions.ecolint_android.detectors.xml.power

import com.android.tools.lint.checks.infrastructure.LintDetectorTest
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Issue
import com.russellsolutions.ecolint_android.detectors.utils.ecolint

class SaveModeAwarenessXmlDetectorTest : LintDetectorTest() {
    override fun getDetector(): Detector = SaveModeAwarenessXmlDetector()
    override fun getIssues(): List<Issue> = listOf(SaveModeAwarenessXmlDetector.ISSUE)

    fun testBatteryChangedIntent() {
        lint().ecolint().files(
            manifest("""
                <manifest xmlns:android="http://schemas.android.com/apk/res/android">
                    <application>
                        <receiver android:name=".PowerSaveReceiver">
                            <intent-filter>
                                <action android:name="android.intent.action.BATTERY_CHANGED" />
                            </intent-filter>
                        </receiver>
                    </application>
                </manifest>
            """)
        ).run().expectWarningCount(1).expect("""
            AndroidManifest.xml:6: Warning: Taking into account when the device is entering or exiting the power save mode is a good practice. [SaveModeAwareness]
                                            <action android:name="android.intent.action.BATTERY_CHANGED" />
                                            ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            0 errors, 1 warnings
        """.trimIndent())
    }
}
