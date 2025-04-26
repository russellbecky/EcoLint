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
import com.intellij.psi.PsiVariable
import org.jetbrains.uast.UCallExpression
import org.jetbrains.uast.ULiteralExpression
import org.jetbrains.uast.USimpleNameReferenceExpression

@Suppress("UnstableApiUsage")
class ThriftyBluetoothAdvertiseModeDetector : Detector(), SourceCodeScanner {
    companion object {
        val ISSUE: Issue = Issue.create(
            // ID string has spaces, a colon and emoji - want to preserve this
            // to make the lint rule stand out in reporting
            //noinspection LintImplIdFormat
            id = "\uD83C\uDF31 EcoLint: ThriftyBLEAdvertiseMode",
            briefDescription = "Use ADVERTISE_MODE_LOW_POWER for energy savings",
            explanation = "You should call `AdvertiseSettings.Builder.setAdvertiseMode(ADVERTISE_MODE_LOW_POWER)` or `AdvertiseSettings.Builder.setAdvertiseMode(ADVERTISE_TX_POWER_LOW)` or `AdvertiseSettings.Builder.setAdvertiseMode(ADVERTISE_TX_POWER_ULTRA_LOW)` to optimize battery usage.",
            category = Category.PERFORMANCE,
            priority = 6,
            severity = Severity.WARNING,
            implementation = Implementation(
                ThriftyBluetoothAdvertiseModeDetector::class.java,
                Scope.JAVA_FILE_SCOPE
            )
        )

        private val restrictedModes = listOf(
            "ADVERTISE_MODE_LOW_LATENCY",
            "ADVERTISE_MODE_BALANCED",
            "ADVERTISE_MODE_LOW_LATENCY",
            "ADVERTISE_TX_POWER_HIGH",
            "ADVERTISE_TX_POWER_LOW",
            "ADVERTISE_TX_POWER_MEDIUM",
        )
    }

    override fun getApplicableMethodNames(): List<String> = listOf("setAdvertiseMode")

    override fun visitMethodCall(context: JavaContext, node: UCallExpression, method: PsiMethod) {
        val receiverType = node.receiverType?.canonicalText ?: return
        if (!receiverType.contains("android.bluetooth.le.AdvertiseSettings.Builder")) return
        val argument = node.valueArguments.firstOrNull() ?: return
        if (argument.getExpressionType()?.canonicalText == "int" &&
            restrictedModes.any { argument.asRenderString().contains(it) }
        ) {
            context.report(ISSUE, node, context.getLocation(node), ISSUE.getExplanation(TextFormat.TEXT))
        }
    }
}
