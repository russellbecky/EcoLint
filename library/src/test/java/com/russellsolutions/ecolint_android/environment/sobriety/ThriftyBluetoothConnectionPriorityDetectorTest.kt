package com.russellsolutions.ecolint_android.detectors.environment.sobriety

import com.android.tools.lint.checks.infrastructure.LintDetectorTest
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Issue
import com.russellsolutions.ecolint_android.detectors.utils.ecolint

class ThriftyBluetoothConnectionPriorityDetectorTest : LintDetectorTest() {
    override fun getDetector(): Detector = ThriftyBluetoothConnectionPriorityDetector()
    override fun getIssues(): List<Issue> = listOf(ThriftyBluetoothConnectionPriorityDetector.ISSUE)

    fun testHighPriorityRequest() {
        lint().ecolint().files(
            kotlin("""
                import android.bluetooth.BluetoothGatt
                fun connect(gatt: BluetoothGatt) {
                    gatt.requestConnectionPriority(BluetoothGatt.CONNECTION_PRIORITY_HIGH)
                }
            """)
        ).run().expectWarningCount(1).expect("""
            src/test.kt:4: Warning: High BLE connection priority increases power consumption. Use only when needed. [ThriftyBlePriority]
                                gatt.requestConnectionPriority(BluetoothGatt.CONNECTION_PRIORITY_HIGH)
                                ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            0 errors, 1 warnings
        """.trimIndent())
    }
}
