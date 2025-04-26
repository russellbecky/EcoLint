package com.russellsolutions.ecolint_android.detectors.environment.power

import com.android.tools.lint.checks.infrastructure.*
import com.russellsolutions.ecolint_android.detectors.utils.ecolint

class SaveModeAwarenessDetectorTest : LintDetectorTest() {
    override fun getDetector() = SaveModeAwarenessDetector()
    override fun getIssues() = listOf(SaveModeAwarenessDetector.ISSUE)

    fun testBatteryChangedInConstructor() {
        lint().ecolint().files(
            kotlin(
                """
                package com.example
                import android.content.IntentFilter
                class Example {
                    fun setup() {
                        val filter = IntentFilter("android.intent.action.BATTERY_CHANGED")
                    }
                }
                """
            )
        ).skipTestModes(TestMode.SUPPRESSIBLE).run().expectWarningCount(1).expect("""
            src/com/example/Example.kt:6: Warning: Taking into account when the device is entering or exiting the power save mode is a good practice. [🌱 EcoLint: SaveModeAwareness]
                                    val filter = IntentFilter("android.intent.action.BATTERY_CHANGED")
                                                              ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            0 errors, 1 warnings
        """.trimIndent())
    }

    fun testBatteryChangedViaAddAction() {
        lint().ecolint().files(
            kotlin(
                """
                package com.example
                import android.content.IntentFilter
                class Example {
                    fun setup() {
                        val filter = IntentFilter()
                        filter.addAction("android.intent.action.BATTERY_CHANGED")
                    }
                }
                """
            )
        ).skipTestModes(TestMode.SUPPRESSIBLE).run().expectWarningCount(1).expect("""
            src/com/example/Example.kt:7: Warning: Taking into account when the device is entering or exiting the power save mode is a good practice. [🌱 EcoLint: SaveModeAwareness]
                                    filter.addAction("android.intent.action.BATTERY_CHANGED")
                                                     ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            0 errors, 1 warnings
        """.trimIndent())
    }

    fun testIsPowerSaveMode() {
        lint().ecolint().files(
            kotlin(
                """
                package com.example
                import android.os.PowerManager
                class PowerCheck {
                    fun check(pm: PowerManager) {
                        val isSaving = pm.isPowerSaveMode
                    }
                }
                """
            )
        ).skipTestModes(TestMode.SUPPRESSIBLE).run().expectWarningCount(1).expect("""
            src/com/example/PowerCheck.kt:6: Warning: Taking into account when the device is entering or exiting the power save mode is a good practice. [🌱 EcoLint: SaveModeAwareness]
                                    val isSaving = pm.isPowerSaveMode
                                                   ~~~~~~~~~~~~~~~~~~
            0 errors, 1 warnings
        """.trimIndent())
    }

    fun testAllowsOtherIntentFilters() {
        lint().ecolint().files(
            kotlin(
                """
                package com.example
                import android.content.IntentFilter
                class Other {
                    fun setup() {
                        val filter = IntentFilter("android.intent.action.AIRPLANE_MODE")
                    }
                }
                """
            )
        ).run().expectClean()
    }
}
