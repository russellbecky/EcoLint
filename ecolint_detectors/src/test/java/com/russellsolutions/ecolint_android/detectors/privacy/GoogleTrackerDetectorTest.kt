package com.russellsolutions.ecolint_android.detectors.social.privacy

import com.android.tools.lint.checks.infrastructure.LintDetectorTest
import com.android.tools.lint.checks.infrastructure.TestMode
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Issue
import com.russellsolutions.ecolint_android.detectors.utils.ecolint

class GoogleTrackerDetectorTest : LintDetectorTest() {
    override fun getDetector(): Detector = GoogleTrackerDetector()
    override fun getIssues(): List<Issue> = listOf(GoogleTrackerDetector.ISSUE)

    fun testGoogleAnalyticsImport() {
        lint().ecolint().files(
            kotlin("""
                import com.google.android.gms.analytics.Tracker
                class Example() {
                    fun send() {
                        val t = Tracker()
                    }
                }
            """).indented(), *arrayOf(trackerStub)
        ).skipTestModes(TestMode.SUPPRESSIBLE).testModes(TestMode.IMPORT_ALIAS).run().expectWarningCount(2).expect("""
            src/Example.kt:1: Warning: Using com.google.android.gms.analytics.* or com.google.firebase.analytics.* may introduce privacy concerns. [🌱 EcoLint: GoogleTrackerImport]
            import com.google.android.gms.analytics.Tracker
            ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            src/Example.kt:2: Warning: Using com.google.android.gms.analytics.* or com.google.firebase.analytics.* may introduce privacy concerns. [🌱 EcoLint: GoogleTrackerImport]
            import com.google.android.gms.analytics.Tracker as IMPORT_ALIAS_1_TRACKER
            ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            0 errors, 2 warnings
        """.trimIndent())
    }

    fun testFirebaseAnalyticsImport() {
        lint().ecolint().files(
            kotlin("""
                import com.google.firebase.analytics.FirebaseAnalytics
                class Example() {
                    fun track() {
                        val a = FirebaseAnalytics.getInstance(null)
                    }
                }
            """), *arrayOf(firebaseAnalyticsStub)
        ).skipTestModes(TestMode.SUPPRESSIBLE).testModes(TestMode.IMPORT_ALIAS).run().expectWarningCount(2).expect("""
            src/Example.kt:2: Warning: Using com.google.android.gms.analytics.* or com.google.firebase.analytics.* may introduce privacy concerns. [🌱 EcoLint: GoogleTrackerImport]
                            import com.google.firebase.analytics.FirebaseAnalytics
                            ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            src/Example.kt:3: Warning: Using com.google.android.gms.analytics.* or com.google.firebase.analytics.* may introduce privacy concerns. [🌱 EcoLint: GoogleTrackerImport]
            import com.google.firebase.analytics.FirebaseAnalytics as IMPORT_ALIAS_1_FIREBASEANALYTICS
            ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            0 errors, 2 warnings
        """.trimIndent())
    }

    private val trackerStub = java(
        """
            package com.google.android.gms.analytics;
            class Tracker {
                Tracker() { }
            }
    """
    ).indented()

    private val firebaseAnalyticsStub = java(
        """
            package com.google.firebase.analytics;
            public final class FirebaseAnalytics {
               @NonNull
                public static FirebaseAnalytics getInstance(@NonNull Context param0) { }
            }
    """
    ).indented()
}
