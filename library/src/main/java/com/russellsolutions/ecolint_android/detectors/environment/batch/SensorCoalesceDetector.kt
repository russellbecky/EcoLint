package com.russellsolutions.ecolint_android.detectors.environment.batch

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

class SensorCoalesceDetector : Detector(), SourceCodeScanner {
    companion object {
        val ISSUE = Issue.create(
            "🌱 EcoLint: SensorLatencyCoalescing",
            "Specify sensor latency in SensorManager.registerListener",
            "Prefer using a latency parameter in SensorManager.registerListener to reduce power consumption.",
            Category.PERFORMANCE, 5, Severity.WARNING,
            Implementation(SensorCoalesceDetector::class.java, Scope.JAVA_FILE_SCOPE)
        )
    }

    override fun getApplicableMethodNames(): List<String> = listOf("registerListener")

    override fun visitMethodCall(context: JavaContext, node: UCallExpression, method: PsiMethod) {
        if (node.receiverType?.canonicalText?.contains("SensorManager") == true && node.valueArgumentCount < 4) {
            context.report(ISSUE, node, context.getLocation(node), ISSUE.getExplanation(TextFormat.TEXT))
        }
    }
}
