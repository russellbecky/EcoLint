package com.russellsolutions.ecolint_android.detectors.environment.sobriety

import com.android.tools.lint.checks.infrastructure.LintDetectorTest
import com.android.tools.lint.checks.infrastructure.TestMode
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Issue
import com.russellsolutions.ecolint_android.detectors.utils.ecolint

class HighFrameRateDetectorTest : LintDetectorTest() {
    override fun getDetector(): Detector = HighFrameRateDetector()
    override fun getIssues(): List<Issue> = listOf(HighFrameRateDetector.ISSUE)

    fun testHighFrameRate() {
        lint().ecolint().files(
            kotlin("""
                import android.view.SurfaceView
                fun setRate(view: SurfaceView) {
                    view.setFrameRate(120f, 0)
                }
            """), *arrayOf(surfaceStub, surfaceViewStub)
        ).skipTestModes(TestMode.SUPPRESSIBLE).run().expectWarningCount(1).expect("""
            src/test.kt:4: Warning: To optimize content refresh and save energy, frame rate should be set at maximum 60Hz. [🌱 EcoLint: HighFrameRate]
                                view.setFrameRate(120f, 0)
                                ~~~~~~~~~~~~~~~~~~~~~~~~~~
            0 errors, 1 warnings
        """.trimIndent())
    }

    fun testDetectsHighFrameRate() {
        lint().ecolint().files(
            kotlin(
                """
                package com.example.app
                import android.view.Surface
                class Example {
                    fun adjust(surface: Surface) {
                        surface.setFrameRate(90.0f, 0)
                    }
                }
                """
            ), *arrayOf(surfaceStub, surfaceViewStub)
        ).skipTestModes(TestMode.SUPPRESSIBLE).run().expectWarningCount(1).expect("""
            src/com/example/app/Example.kt:6: Warning: To optimize content refresh and save energy, frame rate should be set at maximum 60Hz. [🌱 EcoLint: HighFrameRate]
                                    surface.setFrameRate(90.0f, 0)
                                    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            0 errors, 1 warnings
        """.trimIndent())
    }

    fun testAllowsSixtyOrLess() {
        lint().ecolint().files(
            kotlin(
                """
                package com.example.app
                import android.view.Surface
                class Example {
                    fun adjust(surface: Surface) {
                        surface.setFrameRate(60.0f, 0)
                    }
                }
                """
            ), *arrayOf(surfaceStub, surfaceViewStub)
        ).run().expectClean()
    }

    fun testIgnoresNonSurfaceCalls() {
        lint().ecolint().files(
            kotlin(
                """
                package com.example.app
                class FakeSurface {
                    fun setFrameRate(rate: Float, mode: Int) {}
                }
                fun test() {
                    val fake = FakeSurface()
                    fake.setFrameRate(120f, 0)
                }
                """
            ), *arrayOf(surfaceStub, surfaceViewStub)
        ).run().expectClean()
    }

    private val surfaceStub = java(
        """
            package android.view;
            public class Surface {
                    public void setFrameRate(float frameRate, int compatibility) {}
            }
    """
    ).indented()

    private val surfaceViewStub = java(
        """
            package android.view;
            public class SurfaceView {
                    public void setFrameRate(float frameRate, int compatibility) {}
            }
    """
    ).indented()
}