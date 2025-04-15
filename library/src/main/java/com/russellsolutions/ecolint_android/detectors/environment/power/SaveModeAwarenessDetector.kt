package com.russellsolutions.ecolint_android.detectors.environment.power

import com.android.tools.lint.detector.api.*
import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiVariable
import org.jetbrains.uast.*
import org.jetbrains.uast.kotlin.KotlinStringTemplateUPolyadicExpression
import org.jetbrains.uast.kotlin.KotlinUFunctionCallExpression

@Suppress("UnstableApiUsage")
class SaveModeAwarenessDetector : Detector(), SourceCodeScanner {
    companion object {
        val ISSUE: Issue = Issue.create(
            id = "\uD83C\uDF31 EcoLint: SaveModeAwareness",
            briefDescription = "Power save mode or battery awareness",
            explanation = "Taking into account when the device is entering or exiting the power save mode is a good practice.",
            category = Category.CORRECTNESS,
            priority = 3,
            severity = Severity.WARNING,
            implementation = Implementation(
                SaveModeAwarenessDetector::class.java,
                Scope.JAVA_FILE_SCOPE
            )
        )
        private const val action = "android.intent.action.BATTERY_CHANGED"
    }

    override fun getApplicableMethodNames(): List<String> = listOf("addAction", "create", "isPowerSaveMode")
    override fun getApplicableConstructorTypes(): List<String> = listOf("android.content.IntentFilter")

    override fun visitConstructor(context: JavaContext, node: UCallExpression, constructor: PsiMethod) {
        val arg = node.valueArguments.firstOrNull() ?: return
        if ((arg is ULiteralExpression && arg.value == action) ||
            (arg is KotlinStringTemplateUPolyadicExpression && arg.asRenderString().replace("\"","") == action) ||
            (arg is KotlinUFunctionCallExpression && arg.asRenderString().replace("\"","") == action)) {
            context.report(ISSUE, arg, context.getLocation(arg), ISSUE.getExplanation(TextFormat.TEXT))
        }
    }

    override fun visitMethodCall(context: JavaContext, node: UCallExpression, method: PsiMethod) {
        val methodName = node.methodName ?: return
        if (methodName == "isPowerSaveMode" && node.receiverType?.canonicalText == "android.os.PowerManager") {
            context.report(ISSUE, node, context.getLocation(node), ISSUE.getExplanation(TextFormat.TEXT))
            return
        }

        if (node.receiverType?.canonicalText == "android.content.IntentFilter") {
            val arg = node.valueArguments.firstOrNull() ?: return
            val value = when (arg) {
                is ULiteralExpression -> arg.value as? String
                is USimpleNameReferenceExpression -> {
                    val resolved = arg.resolve()
                    if (resolved is PsiVariable && resolved.hasModifierProperty("final")) {
                        resolved.computeConstantValue() as? String
                    } else null
                }
                is KotlinUFunctionCallExpression -> arg.asRenderString().replace("\"","")
                is KotlinStringTemplateUPolyadicExpression -> arg.asRenderString().replace("\"","")
                else -> null

            }

            if (value == action) {
                context.report(ISSUE, node, context.getLocation(arg), ISSUE.getExplanation(TextFormat.TEXT))
            }
        }
    }
}
