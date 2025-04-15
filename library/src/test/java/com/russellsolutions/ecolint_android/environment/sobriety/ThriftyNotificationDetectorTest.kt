package com.russellsolutions.ecolint_android.detectors.environment.sobriety

import com.android.tools.lint.checks.infrastructure.LintDetectorTest
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Issue
import com.russellsolutions.ecolint_android.detectors.utils.ecolint

class ThriftyNotificationDetectorTest : LintDetectorTest() {
    override fun getDetector(): Detector = ThriftyNotificationDetector()
    override fun getIssues(): List<Issue> = listOf(ThriftyNotificationDetector.ISSUE)

    fun testNonNullSoundOrVibration() {
        lint().ecolint().files(
            kotlin(
                """
                import android.app.Notification
                import android.app.NotificationChannel
                import android.net.Uri
                class Example {
                    fun notify(channel: NotificationChannel, builder: Notification.Builder) {
                        channel.setSound(Uri.parse("content://sound"))
                        channel.setVibrationPattern(longArrayOf(0, 100))
                        builder.setSound(Uri.parse("content://sound"))
                        builder.setVibrate(longArrayOf(100, 200))
                    }
                }
                """
            ), *arrayOf(notificationStub, notificationChannelStub)
        ).run().expectWarningCount(4).expect("""
            src/Example.kt:7: Warning: Avoid using vibration or sound when notifying the users to use less energy. [ThriftyNotification]
                                    channel.setSound(Uri.parse("content://sound"))
                                    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            src/Example.kt:8: Warning: Avoid using vibration or sound when notifying the users to use less energy. [ThriftyNotification]
                                    channel.setVibrationPattern(longArrayOf(0, 100))
                                    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            src/Example.kt:9: Warning: Avoid using vibration or sound when notifying the users to use less energy. [ThriftyNotification]
                                    builder.setSound(Uri.parse("content://sound"))
                                    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            src/Example.kt:10: Warning: Avoid using vibration or sound when notifying the users to use less energy. [ThriftyNotification]
                                    builder.setVibrate(longArrayOf(100, 200))
                                    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            0 errors, 4 warnings
        """.trimIndent())
    }

    fun testAllowsNullArguments() {
        lint().ecolint().files(
            kotlin(
                """
                package com.example.app
                import android.app.Notification
                import android.app.NotificationChannel
                class Example {
                    fun notify(channel: NotificationChannel, builder: Notification.Builder) {
                        channel.setSound(null)
                        channel.setVibrationPattern(null)
                        builder.setSound(null)
                        builder.setVibrate(null)
                    }
                }
                """
            ), *arrayOf(notificationStub, notificationChannelStub)
        ).run().expectClean()
    }

    private val notificationStub = java(
        """
            package android.app;
            public class Notification {
                public static final class Builder {
                    public Builder setSound(Uri sound) { }
                    public Builder setVibrate(long[] pattern) { }
                }
            }
    """
    ).indented()

    private val notificationChannelStub = java("""
        public final class NotificationChannel {
            public void setSound(Uri sound) { }
            public void setVibrationPattern(long[] vibrationPattern) { }
        }
    """.trimIndent())
}
