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

class UncompressedOutputStreamDetector : Detector(), SourceCodeScanner {
    companion object {
        val ISSUE = Issue.create(
            // ID string has spaces, a colon and emoji - want to preserve this
            // to make the lint rule stand out in reporting
            //noinspection LintImplIdFormat
            "\uD83C\uDF31 EcoLint: UncompressedData",
            "Use compression for data transmission",
            "Prefer wrapping `OutputStream` with `GZIPOutputStream` for better energy efficiency.",
            Category.PERFORMANCE, 5, Severity.WARNING,
            Implementation(UncompressedOutputStreamDetector::class.java, Scope.JAVA_FILE_SCOPE)
        )
    }

    override fun getApplicableConstructorTypes(): List<String> = listOf("java.io.OutputStream")

    override fun visitConstructor(context: JavaContext, node: UCallExpression, constructor: PsiMethod) {
        val arg = node.valueArguments.firstOrNull()?.asRenderString() ?: return
        if (arg.contains("getOutputStream") && !node.classReference?.resolvedName?.contains("GZIPOutputStream")!!) {
            context.report(ISSUE, node, context.getLocation(node), ISSUE.getExplanation(TextFormat.TEXT))
        }
    }
}
