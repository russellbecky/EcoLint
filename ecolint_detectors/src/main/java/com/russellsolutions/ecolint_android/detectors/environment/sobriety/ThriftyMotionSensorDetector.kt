package com.russellsolutions.ecolint_android.detectors.environment.sobriety

import com.android.tools.lint.detector.api.Category
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.JavaContext
import com.android.tools.lint.detector.api.Scope
import com.android.tools.lint.detector.api.Severity
import com.android.tools.lint.detector.api.SourceCodeScanner
import com.android.tools.lint.detector.api.TextFormat
import com.intellij.psi.PsiMethod
import org.jetbrains.uast.UCallExpression

class ThriftyMotionSensorDetector : Detector(), SourceCodeScanner {
    companion object {
        val ISSUE = Issue.create(
            // ID string has spaces, a colon and emoji - want to preserve this
            // to make the lint rule stand out in reporting
            //noinspection LintImplIdFormat
            "\uD83C\uDF31 EcoLint: ThriftyMotionSensor",
            //noinspection LintImplTextFormat
            "Use SENSOR_DELAY_NORMAL or SENSOR_DELAY_UI instead of FASTEST",
            "Avoid using SENSOR_DELAY_FASTEST or SENSOR_DELAY_GAME as they are more battery-intensive.",
            Category.PERFORMANCE, 6, Severity.WARNING,
            Implementation(ThriftyMotionSensorDetector::class.java, Scope.JAVA_FILE_SCOPE)
        )
    }

    override fun getApplicableMethodNames(): List<String> = listOf("registerListener")

    override fun visitMethodCall(context: JavaContext, node: UCallExpression, method: PsiMethod) {
        if (node.valueArguments.size >= 3) {
            val rateArg = node.valueArguments[2].asRenderString()
            if (rateArg.contains("FASTEST") || rateArg.contains("GAME")) {
                context.report(ISSUE, node, context.getLocation(node), ISSUE.getExplanation(TextFormat.TEXT))
            }
        }
    }
}
