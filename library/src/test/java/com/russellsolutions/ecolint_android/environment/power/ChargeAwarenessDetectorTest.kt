package com.russellsolutions.ecolint_android.detectors.environment.power

import com.android.tools.lint.checks.infrastructure.*
import com.russellsolutions.ecolint_android.detectors.utils.ecolint

class ChargeAwarenessDetectorTest : LintDetectorTest() {
    override fun getDetector() = ChargeAwarenessDetector()
    override fun getIssues() = listOf(ChargeAwarenessDetector.ISSUE)

    fun testPowerBroadcastActions() {
        lint().ecolint().files(
            kotlin(
                """
                package com.example
                import android.content.IntentFilter
                class PowerListener {
                    fun register() {
                        val filter = IntentFilter()
                        filter.addAction("android.intent.action.ACTION_POWER_CONNECTED")
                        filter.addAction("android.intent.action.BATTERY_LOW")
                    }
                }
                """
            )
        ).run().expectWarningCount(2).expect("""
            src/com/example/PowerListener.kt:7: Warning: Monitoring power changes and customizing behavior depending on battery level is a good practice. [ChargeAwarenessIntentUsage]
                                    filter.addAction("android.intent.action.ACTION_POWER_CONNECTED")
                                                     ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            src/com/example/PowerListener.kt:8: Warning: Monitoring power changes and customizing behavior depending on battery level is a good practice. [ChargeAwarenessIntentUsage]
                                    filter.addAction("android.intent.action.BATTERY_LOW")
                                                     ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            0 errors, 2 warnings
        """.trimIndent())
    }

    fun testConstructorUsage() {
        lint().ecolint().files(
            kotlin(
                """
                package com.example
                import android.content.IntentFilter
                class BatteryReceiver {
                    fun create() {
                        val filter = IntentFilter("android.intent.action.BATTERY_OKAY")
                    }
                }
                """
            )
        ).run().expectWarningCount(1).expect("""
            src/com/example/BatteryReceiver.kt:6: Warning: Monitoring power changes and customizing behavior depending on battery level is a good practice. [ChargeAwarenessIntentUsage]
                                    val filter = IntentFilter("android.intent.action.BATTERY_OKAY")
                                                              ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            0 errors, 1 warnings
        """.trimIndent())
    }

    fun testAllowsOtherIntentFilters() {
        lint().ecolint().files(
            kotlin(
                """
                package com.example
                import android.content.IntentFilter
                class Example {
                    fun ignore() {
                        val filter = IntentFilter("android.intent.action.ACTION_AIRPLANE_MODE_CHANGED")
                        filter.addAction("android.intent.action.DATE_CHANGED")
                    }
                }
                """
            )
        ).run().expectClean()
    }
}
