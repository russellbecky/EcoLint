package com.russellsolutions.ecolint_android.detectors.environment.bottleneck

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
import org.jetbrains.uast.UElement
import org.jetbrains.uast.ULoopExpression

class InternetInLoopDetector : Detector(), SourceCodeScanner {
    companion object {
        val ISSUE = Issue.create(
            "\uD83C\uDF31 EcoLint: InternetInLoop",
            "Avoid opening internet connections inside loops",
            "Opening connections in loops is battery-intensive. Open them outside the loop when possible.",
            Category.PERFORMANCE, 6, Severity.WARNING,
            Implementation(InternetInLoopDetector::class.java, Scope.JAVA_FILE_SCOPE)
        )
    }

    override fun getApplicableMethodNames(): List<String> = listOf("openConnection")

    override fun visitMethodCall(context: JavaContext, node: UCallExpression, method: PsiMethod) {
        val isUrl = node.receiverType?.canonicalText == "java.net.URL"
        if (isUrl && isInsideLoop(node)) {
            context.report(ISSUE, node, context.getLocation(node), ISSUE.getExplanation(TextFormat.TEXT))
        }
    }

    private fun isInsideLoop(node: UElement?): Boolean {
        var current: UElement? = node?.uastParent
        while (current != null) {
            if (current is ULoopExpression) return true
            current = current.uastParent
        }
        return false
    }
}
