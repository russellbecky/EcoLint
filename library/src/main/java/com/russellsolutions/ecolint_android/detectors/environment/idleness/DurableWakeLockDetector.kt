package com.russellsolutions.ecolint_android.detectors.environment.idleness

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

class DurableWakeLockDetector : Detector(), SourceCodeScanner {
    companion object {
        val ISSUE = Issue.create(
            "\uD83C\uDF31 EcoLint: DurableWakeLock",
            "Use timed wake locks",
            "Prefer setting a timeout when acquiring a wake lock to avoid running down the device's battery excessively.",
            Category.PERFORMANCE, 6, Severity.WARNING,
            Implementation(DurableWakeLockDetector::class.java, Scope.JAVA_FILE_SCOPE)
        )
    }

    override fun getApplicableMethodNames(): List<String> = listOf("acquire")

    override fun visitMethodCall(context: JavaContext, node: UCallExpression, method: PsiMethod) {
        val receiver = method.containingClass?.qualifiedName
        if (receiver?.contains("PowerManager.WakeLock") == true && node.valueArguments.isEmpty()) {
            context.report(ISSUE, node, context.getLocation(node), ISSUE.getExplanation(TextFormat.TEXT))
        }
    }
}
