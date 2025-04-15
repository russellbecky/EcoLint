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

class ContinuousRenderingDetector : Detector(), SourceCodeScanner {
    companion object {
        val ISSUE = Issue.create(
            "\uD83C\uDF31 EcoLint: ContinuousRendering",
            "Use RENDERMODE_WHEN_DIRTY instead of RENDERMODE_CONTINUOUSLY",
            "Using RENDERMODE_WHEN_DIRTY instead of RENDERMODE_CONTINUOUSLY can improve battery life.",
            Category.PERFORMANCE, 6, Severity.WARNING,
            Implementation(ContinuousRenderingDetector::class.java, Scope.JAVA_FILE_SCOPE)
        )
    }

    override fun getApplicableMethodNames(): List<String> = listOf("setRenderMode")

    override fun visitMethodCall(context: JavaContext, node: UCallExpression, method: PsiMethod) {
        if (node.valueArguments.size == 1 && node.valueArguments[0].evaluate() == 1) {
            val receiverType = node.receiverType?.canonicalText
            if (receiverType?.contains("GLSurfaceView") == true) {
                context.report(ISSUE, node, context.getLocation(node), ISSUE.getExplanation(TextFormat.TEXT))
            }
        }
    }
}
