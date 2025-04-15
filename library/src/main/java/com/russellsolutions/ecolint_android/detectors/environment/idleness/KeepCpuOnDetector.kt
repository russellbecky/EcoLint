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

class KeepCpuOnDetector : Detector(), SourceCodeScanner {
    companion object {
        val ISSUE = Issue.create(
            "\uD83C\uDF31 EcoLint: KeepCpuOn",
            "Avoid keepScreenOn(true) call",
            "Calling setKeepScreenOn(true) can prevent CPU sleep and drains battery.",
            Category.PERFORMANCE, 6, Severity.WARNING,
            Implementation(KeepCpuOnDetector::class.java, Scope.JAVA_FILE_SCOPE)
        )
    }

    override fun getApplicableMethodNames(): List<String> = listOf("setKeepScreenOn")

    override fun visitMethodCall(context: JavaContext, node: UCallExpression, method: PsiMethod) {
        val arg = node.valueArguments.firstOrNull()
        if (arg?.evaluate() == true) {
            context.report(ISSUE, node, context.getLocation(node), ISSUE.getExplanation(TextFormat.TEXT))
        }
    }
}
