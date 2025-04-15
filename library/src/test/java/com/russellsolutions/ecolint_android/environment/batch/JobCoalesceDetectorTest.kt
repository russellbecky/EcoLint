package com.russellsolutions.ecolint_android.detectors.environment.batch

import com.android.tools.lint.checks.infrastructure.LintDetectorTest
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Issue
import com.russellsolutions.ecolint_android.detectors.environment.batch.JobCoalesceDetector
import com.russellsolutions.ecolint_android.detectors.utils.ecolint

class JobCoalesceDetectorTest : LintDetectorTest() {
    override fun getDetector(): Detector = JobCoalesceDetector()
    override fun getIssues(): List<Issue> = listOf(JobCoalesceDetector.ISSUE)

    fun testAlarmManagerUsage() {
        lint().ecolint().files(
            kotlin("""
                import android.app.AlarmManager
                class Example(val alarmManager: AlarmManager) {
                    fun schedule() {
                        alarmManager.set(AlarmManager.RTC_WAKEUP, 1000, null)
                    }
                }
            """)
        ).run().expectWarningCount(1)
            .expect(
                """
                    src/Example.kt:5: Warning: Avoid using AlarmManager or SyncAdapter for alarms. Use JobScheduler to allow the system to optimize scheduling. [JobCoalescing]
                                            alarmManager.set(AlarmManager.RTC_WAKEUP, 1000, null)
                                            ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                    0 errors, 1 warnings
                """.trimIndent()
            )
    }
}
