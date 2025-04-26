package com.russellsolutions.ecolint_android.detectors.environment.sobriety

import com.android.tools.lint.detector.api.*
import com.intellij.psi.PsiMethod
import org.jetbrains.uast.*

@Suppress("UnstableApiUsage")
class ThriftyNotificationDetector : Detector(), SourceCodeScanner {
    companion object {
        val ISSUE: Issue = Issue.create(
            // ID string has spaces, a colon and emoji - want to preserve this
            // to make the lint rule stand out in reporting
            //noinspection LintImplIdFormat
            id = "\uD83C\uDF31 EcoLint: ThriftyNotification",
            //noinspection LintImplTextFormat
            briefDescription = "Avoid sound/vibration in notifications for better energy efficiency",
            explanation = "Avoid using vibration or sound when notifying the users to use less energy.",
            category = Category.PERFORMANCE,
            priority = 6,
            severity = Severity.WARNING,
            implementation = Implementation(
                ThriftyNotificationDetector::class.java,
                Scope.JAVA_FILE_SCOPE
            )
        )
    }

    private val methodTargets = mapOf(
        "setSound" to listOf(
            "android.app.NotificationChannel",
            "android.app.Notification.Builder"
        ),
        "setVibrationPattern" to listOf(
            "android.app.NotificationChannel"
        ),
        "setVibrate" to listOf(
            "android.app.Notification.Builder"
        )
    )

    override fun getApplicableMethodNames(): List<String> = methodTargets.keys.toList()

    override fun visitMethodCall(context: JavaContext, node: UCallExpression, method: PsiMethod) {
        val methodName = node.methodName ?: return
        val expectedTypes = methodTargets[methodName] ?: return
        val receiverType = node.receiverType?.canonicalText ?: return

        if (expectedTypes.none { receiverType.contains(it) }) return

        val argument = node.valueArguments.firstOrNull() ?: return
        if (argument !is ULiteralExpression || argument.value != null) {
            context.report(ISSUE, node, context.getLocation(node), ISSUE.getExplanation(TextFormat.TEXT))
        }
    }
}
