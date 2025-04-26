package com.russellsolutions.ecolint_android.detectors.environment.sobriety

import com.android.tools.lint.checks.infrastructure.LintDetectorTest
import com.android.tools.lint.checks.infrastructure.TestMode
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Issue
import com.russellsolutions.ecolint_android.detectors.utils.ecolint

class ThriftyBluetoothAdvertiseModeDetectorTest : LintDetectorTest() {
    override fun getDetector(): Detector = ThriftyBluetoothAdvertiseModeDetector()
    override fun getIssues(): List<Issue> = listOf(ThriftyBluetoothAdvertiseModeDetector.ISSUE)

    fun testHighAdvertiseMode() {
        lint().ecolint().files(
            kotlin("""
                import android.bluetooth.le.AdvertiseSettings
                class Example {
                    fun configure(settings: AdvertiseSettings.Builder) {
                        settings.setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_LOW_LATENCY)
                    }
                }
            """), *arrayOf(advertiseSettingsStub)
        ).skipTestModes(TestMode.SUPPRESSIBLE).run().expectWarningCount(1).expect("""
            src/Example.kt:5: Warning: You should call AdvertiseSettings.Builder.setAdvertiseMode(ADVERTISE_MODE_LOW_POWER) or AdvertiseSettings.Builder.setAdvertiseMode(ADVERTISE_TX_POWER_LOW) or AdvertiseSettings.Builder.setAdvertiseMode(ADVERTISE_TX_POWER_ULTRA_LOW) to optimize battery usage. [🌱 EcoLint: ThriftyBLEAdvertiseMode]
                                    settings.setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_LOW_LATENCY)
                                    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            0 errors, 1 warnings
        """.trimIndent())
    }

    fun testLowAdvertiseMode() {
        lint().ecolint().files(
            kotlin("""
                import android.bluetooth.le.AdvertiseSettings
                class Example {
                    fun configure(settings: AdvertiseSettings.Builder) {
                        settings.setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_LOW_POWER)
                    }
                }
            """), *arrayOf(advertiseSettingsStub)
        ).run().expectClean()
    }

    private val advertiseSettingsStub = java(
        """
            package android.bluetooth.le;
            public class AdvertiseSettings {
                public static final int ADVERTISE_MODE_BALANCED = 1;
                public static final int ADVERTISE_MODE_LOW_LATENCY = 2;
                public static final int ADVERTISE_MODE_LOW_POWER = 0;
                public static final int ADVERTISE_TX_POWER_HIGH = 3;
                public static final int ADVERTISE_TX_POWER_LOW = 1;
                public static final int ADVERTISE_TX_POWER_MEDIUM = 2;
                public static final int ADVERTISE_TX_POWER_ULTRA_LOW = 0;
                public static final class Builder { 
                    public Builder setAdvertiseMode(int advertiseMode) {}
                }
            }
    """
    ).indented()
}
