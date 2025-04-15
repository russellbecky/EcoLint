package com.russellsolutions.ecolint_android.detectors.xml.power

import com.android.tools.lint.checks.infrastructure.LintDetectorTest
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Issue
import com.russellsolutions.ecolint_android.detectors.utils.ecolint

class ChargeAwarenessXmlDetectorTest : LintDetectorTest() {
    override fun getDetector(): Detector = ChargeAwarenessXmlDetector()
    override fun getIssues(): List<Issue> = listOf(ChargeAwarenessXmlDetector.ISSUE)

    fun testActionPowerConnectedReceiver() {
        lint().ecolint().files(
            manifest("""
                <manifest xmlns:android="http://schemas.android.com/apk/res/android">
                    <application>
                        <receiver android:name=".BatteryReceiver">
                            <intent-filter>
                                <action android:name="android.intent.action.ACTION_POWER_CONNECTED" />
                            </intent-filter>
                        </receiver>
                    </application>
                </manifest>
            """)
        ).run().expectWarningCount(1).expect("""
            AndroidManifest.xml:6: Warning: Monitoring power changes and customizing behavior depending on battery level is a good practice. [ChargeAwareness]
                                            <action android:name="android.intent.action.ACTION_POWER_CONNECTED" />
                                            ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            0 errors, 1 warnings
        """.trimIndent())
    }

    fun testBatteryPowerDisconnectedReceiver() {
        lint().ecolint().files(
            manifest("""
                <manifest xmlns:android="http://schemas.android.com/apk/res/android">
                    <application>
                        <receiver android:name=".BatteryReceiver">
                            <intent-filter>
                                <action android:name="android.intent.action.ACTION_POWER_DISCONNECTED" />
                            </intent-filter>
                        </receiver>
                    </application>
                </manifest>
            """)
        ).run().expectWarningCount(1).expect("""
            AndroidManifest.xml:6: Warning: Monitoring power changes and customizing behavior depending on battery level is a good practice. [ChargeAwareness]
                                            <action android:name="android.intent.action.ACTION_POWER_DISCONNECTED" />
                                            ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            0 errors, 1 warnings
        """.trimIndent())
    }

    fun testBatteryLowReceiver() {
        lint().ecolint().files(
            manifest("""
                <manifest xmlns:android="http://schemas.android.com/apk/res/android">
                    <application>
                        <receiver android:name=".BatteryReceiver">
                            <intent-filter>
                                <action android:name="android.intent.action.BATTERY_LOW" />
                            </intent-filter>
                        </receiver>
                    </application>
                </manifest>
            """)
        ).run().expectWarningCount(1).expect("""
            AndroidManifest.xml:6: Warning: Monitoring power changes and customizing behavior depending on battery level is a good practice. [ChargeAwareness]
                                            <action android:name="android.intent.action.BATTERY_LOW" />
                                            ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            0 errors, 1 warnings
        """.trimIndent())
    }

    fun testBatteryOkReceiver() {
        lint().ecolint().files(
            manifest("""
                <manifest xmlns:android="http://schemas.android.com/apk/res/android">
                    <application>
                        <receiver android:name=".BatteryReceiver">
                            <intent-filter>
                                <action android:name="android.intent.action.BATTERY_OKAY" />
                            </intent-filter>
                        </receiver>
                    </application>
                </manifest>
            """)
        ).run().expectWarningCount(1).expect("""
            AndroidManifest.xml:6: Warning: Monitoring power changes and customizing behavior depending on battery level is a good practice. [ChargeAwareness]
                                            <action android:name="android.intent.action.BATTERY_OKAY" />
                                            ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            0 errors, 1 warnings
        """.trimIndent())
    }
}
