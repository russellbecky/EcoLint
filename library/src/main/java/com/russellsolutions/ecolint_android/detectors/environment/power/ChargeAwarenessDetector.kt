package com.russellsolutions.ecolint_android.detectors.environment.power

import com.android.tools.lint.detector.api.*
import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiVariable
import org.jetbrains.uast.*
import org.jetbrains.uast.kotlin.KotlinStringTemplateUPolyadicExpression
import org.jetbrains.uast.kotlin.KotlinUFunctionCallExpression

@Suppress("UnstableApiUsage")
class ChargeAwarenessDetector : Detector(), SourceCodeScanner {
    companion object {
        val ISSUE: Issue = Issue.create(
            id = "\uD83C\uDF31 EcoLint: ChargeAwarenessIntentUsage",
            briefDescription = "Encouraged use of power/battery broadcast actions",
            explanation = "Monitoring power changes and customizing behavior depending on battery level is a good practice.",
            category = Category.CORRECTNESS,
            priority = 3,
            severity = Severity.WARNING,
            implementation = Implementation(
                ChargeAwarenessDetector::class.java,
                Scope.JAVA_FILE_SCOPE
            )
        )
        private val actions = setOf(
            "android.intent.action.ACTION_POWER_CONNECTED",
            "android.intent.action.ACTION_POWER_DISCONNECTED",
            "android.intent.action.BATTERY_LOW",
            "android.intent.action.BATTERY_OKAY"
        )
    }

    override fun getApplicableMethodNames(): List<String> = listOf("addAction", "create")
    override fun getApplicableConstructorTypes(): List<String> =
        listOf("android.content.IntentFilter")

    override fun visitConstructor(context: JavaContext, node: UCallExpression, constructor: PsiMethod) {
        val arg = node.valueArguments.firstOrNull() ?: return
        checkAndReport(context, node, arg)
    }

    override fun visitMethodCall(context: JavaContext, node: UCallExpression, method: PsiMethod) {
        val receiverType = node.receiverType?.canonicalText ?: return
        if (receiverType != "android.content.IntentFilter") return

        val arg = node.valueArguments.firstOrNull() ?: return
        checkAndReport(context, node, arg)
    }

    private fun checkAndReport(context: JavaContext, node: UElement, arg: UExpression) {
        val action = when (arg) {
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

        if (action in actions) {
            context.report(ISSUE, node, context.getLocation(arg), ISSUE.getExplanation(TextFormat.TEXT))
        }
    }
}
