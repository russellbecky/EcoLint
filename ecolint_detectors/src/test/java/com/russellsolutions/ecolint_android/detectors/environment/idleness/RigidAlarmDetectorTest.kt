package com.russellsolutions.ecolint_android.detectors.environment.idleness

import com.android.tools.lint.checks.infrastructure.LintDetectorTest
import com.android.tools.lint.checks.infrastructure.TestMode
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Issue
import com.russellsolutions.ecolint_android.detectors.utils.ecolint

class RigidAlarmDetectorTest : LintDetectorTest() {
    override fun getDetector(): Detector = RigidAlarmDetector()
    override fun getIssues(): List<Issue> = listOf(RigidAlarmDetector.ISSUE)

    fun testExactAlarmUsage() {
        lint().ecolint().files(
            kotlin("""
                import android.app.AlarmManager
                class AlarmExample(val alarmManager: AlarmManager) {
                    fun schedule() {
                        alarmManager.setExact(AlarmManager.RTC_WAKEUP, 1000, null)
                    }
                }
            """)
        ).skipTestModes(TestMode.SUPPRESSIBLE).run().expectWarningCount(1).expect("""
            src/AlarmExample.kt:5: Warning: Using exact alarms reduces the OS's ability to optimize battery life via Doze Mode. [🌱 EcoLint: RigidAlarm]
                                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, 1000, null)
                                    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            0 errors, 1 warnings
        """.trimIndent())
    }
}
