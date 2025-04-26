package com.russellsolutions.ecolint_android.detectors.environment.sobriety


import com.android.tools.lint.client.api.UElementHandler
import com.android.tools.lint.detector.api.*
import com.intellij.psi.PsiField
import org.jetbrains.uast.*

@Suppress("UnstableApiUsage")
class BrightnessOverrideDetector : Detector(), SourceCodeScanner {
    companion object {
        val ISSUE: Issue = Issue.create(
            // ID string has spaces, a colon and emoji - want to preserve this
            // to make the lint rule stand out in reporting
            //noinspection LintImplIdFormat
            id = "\uD83C\uDF31 EcoLint: MaxBrightnessAssignment",
            briefDescription = "Avoid setting brightness to max",
            explanation = "Forcing brightness to max value may cause useless energy consumption.",
            category = Category.PERFORMANCE,
            priority = 5,
            severity = Severity.WARNING,
            implementation = Implementation(
                BrightnessOverrideDetector::class.java,
                Scope.JAVA_FILE_SCOPE
            )
        )
    }

    override fun getApplicableUastTypes(): List<Class<out UElement>> = listOf(UBinaryExpression::class.java)

    override fun createUastHandler(context: JavaContext): UElementHandler {
        return object : UElementHandler() {
            override fun visitBinaryExpression(node: UBinaryExpression) {
                if (node.operator != UastBinaryOperator.ASSIGN) return

                val lhs = node.leftOperand
                val rhs = node.rightOperand

                if (lhs is UQualifiedReferenceExpression &&
                    lhs.resolvedName == "screenBrightness" &&
                    lhs.receiver.getExpressionType()?.canonicalText == "android.view.WindowManager.LayoutParams"
                ) {
                    val value = when (rhs) {
                        is ULiteralExpression -> rhs.value
                        is USimpleNameReferenceExpression -> {
                            val resolved = rhs.resolve()
                            if (resolved is PsiField && resolved.hasModifierProperty("final")) {
                                resolved.computeConstantValue()
                            } else null
                        }
                        else -> null
                    }

                    if (value == 1 || value == 1.0f || value == 1.0) {
                        context.report(
                            ISSUE,
                            node,
                            context.getLocation(node),
                            ISSUE.getExplanation(TextFormat.TEXT)
                        )
                    }
                }
            }
        }
    }
}
