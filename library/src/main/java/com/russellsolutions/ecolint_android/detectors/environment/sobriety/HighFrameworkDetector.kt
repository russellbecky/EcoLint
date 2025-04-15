package com.russellsolutions.ecolint_android.detectors.environment.sobriety

import com.android.tools.lint.detector.api.*
import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiVariable
import org.jetbrains.uast.*
import java.util.*

@Suppress("UnstableApiUsage")
class HighFrameRateDetector : Detector(), SourceCodeScanner {
    companion object {
        val ISSUE: Issue = Issue.create(
            id = "\uD83C\uDF31 EcoLint: HighFrameRate",
            briefDescription = "Avoid frame rates higher than 60Hz",
            explanation = "To optimize content refresh and save energy, frame rate should be set at maximum 60Hz.",
            category = Category.PERFORMANCE,
            priority = 5,
            severity = Severity.WARNING,
            implementation = Implementation(
                HighFrameRateDetector::class.java,
                Scope.JAVA_FILE_SCOPE
            )
        )

        private val restrictedClasses = listOf("android.view.Surface", "android.view.SurfaceView")
    }

    override fun getApplicableMethodNames(): List<String> = listOf("setFrameRate")

    override fun visitMethodCall(context: JavaContext, node: UCallExpression, method: PsiMethod) {
        val receiverType = node.receiverType?.canonicalText ?: return
        if (restrictedClasses.none { receiverType.contains(it) }) return
        val firstArg = node.valueArguments.getOrNull(0) ?: return
        val frameRateValue: Float? = when (firstArg) {
            is ULiteralExpression -> (firstArg.value as? Number)?.toFloat()
            is USimpleNameReferenceExpression -> {
                val resolved = firstArg.resolve()
                if (resolved is PsiVariable && resolved.hasModifierProperty("final")) {
                    (resolved.computeConstantValue() as? Number)?.toFloat()
                } else null
            }
            else -> null
        }
        if (frameRateValue != null && frameRateValue > 60.0f) {
            context.report(
                ISSUE,
                node,
                context.getLocation(node),
                ISSUE.getExplanation(TextFormat.TEXT)
            )
        }
    }
}
