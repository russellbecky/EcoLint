package com.russellsolutions.ecolint_android.detectors.environment.sobriety

import com.android.tools.lint.detector.api.*
import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiVariable
import org.jetbrains.uast.*

@Suppress("UnstableApiUsage")
class TorchFreeDetector : Detector(), SourceCodeScanner {
    companion object {
        val ISSUE: Issue = Issue.create(
            id = "\uD83C\uDF31 EcoLint: TorchModeEnabled",
            briefDescription = "Avoid programmatically enabling flashlight",
            explanation = "Flashlight is one of the most energy-intensive components. Don't programmatically turn it on.",
            category = Category.PERFORMANCE,
            priority = 6,
            severity = Severity.WARNING,
            implementation = Implementation(
                TorchFreeDetector::class.java,
                Scope.JAVA_FILE_SCOPE
            )
        )
    }

    override fun getApplicableMethodNames(): List<String> = listOf("setTorchMode")

    override fun visitMethodCall(context: JavaContext, node: UCallExpression, method: PsiMethod) {
        val receiverType = node.receiverType?.canonicalText ?: return
        if (receiverType != "android.hardware.camera2.CameraManager") return

        val args = node.valueArguments
        if (args.size < 2) return

        val torchEnabledArg = args[1]
        val value = when (torchEnabledArg) {
            is ULiteralExpression -> torchEnabledArg.value as? Boolean
            is USimpleNameReferenceExpression -> {
                val resolved = torchEnabledArg.resolve()
                if (resolved is PsiVariable && resolved.hasModifierProperty("final")) {
                    resolved.computeConstantValue() as? Boolean
                } else null
            }
            else -> null
        }

        if (value == true) {
            context.report(ISSUE, node, context.getLocation(torchEnabledArg), ISSUE.getExplanation(TextFormat.TEXT))
        }
    }
}
