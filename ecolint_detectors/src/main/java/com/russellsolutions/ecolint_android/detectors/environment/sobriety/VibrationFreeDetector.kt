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

class VibrationFreeDetector : Detector(), SourceCodeScanner {
    companion object {
        val ISSUE = Issue.create(
            // ID string has spaces, a colon and emoji - want to preserve this
            // to make the lint rule stand out in reporting
            //noinspection LintImplIdFormat
            "\uD83C\uDF31 EcoLint: VibrationFree",
            "Avoid using the device vibrator",
            "Avoid using the device vibrator to use less energy.",
            Category.PERFORMANCE, 5, Severity.WARNING,
            Implementation(VibrationFreeDetector::class.java, Scope.JAVA_FILE_SCOPE)
        )

        private val VIBRATOR_KEYS = listOf("vibrator", "vibrator_manager")
    }

    override fun getApplicableMethodNames(): List<String> = listOf("getSystemService")

    override fun visitMethodCall(context: JavaContext, node: UCallExpression, method: PsiMethod) {
        val args = node.valueArguments
        if (args.isNotEmpty()) {
            val serviceArg = args[0].asRenderString().trim('"')
            if (VIBRATOR_KEYS.contains(serviceArg)) {
                context.report(ISSUE, node, context.getLocation(node), ISSUE.getExplanation(TextFormat.TEXT))
            }
        }
    }
}
