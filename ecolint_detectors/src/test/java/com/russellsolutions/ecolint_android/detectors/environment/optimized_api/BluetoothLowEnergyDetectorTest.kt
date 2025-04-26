package com.russellsolutions.ecolint_android.detectors.environment.optimized_api

import com.android.tools.lint.checks.infrastructure.LintDetectorTest
import com.android.tools.lint.checks.infrastructure.TestMode
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Issue
import com.russellsolutions.ecolint_android.detectors.utils.ecolint

class BluetoothLowEnergyDetectorTest : LintDetectorTest() {
    override fun getDetector(): Detector = BluetoothLowEnergyDetector()
    override fun getIssues(): List<Issue> = listOf(BluetoothLowEnergyDetector.ISSUE)

    fun testGoogleAnalyticsImport() {
        lint().ecolint().files(
            kotlin("""
                import android.bluetooth.BluetoothAdapter
                class Example() {
                    fun send() {
                        val t = BluetoothAdapter.getDefaultAdapter()
                    }
                }
            """).indented(), *arrayOf(bluetoothStub)
        ).skipTestModes(TestMode.SUPPRESSIBLE).testModes(TestMode.IMPORT_ALIAS).run().expectWarningCount(2).expect("""
            src/Example.kt:1: Warning: You are using Bluetooth. Did you take a look at the Bluetooth Low Energy API? [🌱 EcoLint: BluetoothLowEnergy]
            import android.bluetooth.BluetoothAdapter
            ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            src/Example.kt:2: Warning: You are using Bluetooth. Did you take a look at the Bluetooth Low Energy API? [🌱 EcoLint: BluetoothLowEnergy]
            import android.bluetooth.BluetoothAdapter as IMPORT_ALIAS_1_BLUETOOTHADAPTER
            ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            0 errors, 2 warnings
        """.trimIndent())
    }

    private val bluetoothStub = java(
        """
            package android.bluetooth;
            class BluetoothAdapter {
                BluetoothAdapter() { }
                public BluetoothAdapter getDefaultAdapter() { return BluetoothAdapter(); }
            }
    """
    ).indented()
}
