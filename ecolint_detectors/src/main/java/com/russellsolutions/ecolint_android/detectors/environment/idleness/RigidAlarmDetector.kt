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

class RigidAlarmDetector : Detector(), SourceCodeScanner {
    companion object {
        val ISSUE = Issue.create(
            // ID string has spaces, a colon and emoji - want to preserve this
            // to make the lint rule stand out in reporting
            //noinspection LintImplIdFormat
            "\uD83C\uDF31 EcoLint: RigidAlarm",
            "Avoid using exact alarms unnecessarily",
            "Using exact alarms reduces the OS's ability to optimize battery life via Doze Mode.",
            Category.PERFORMANCE, 6, Severity.WARNING,
            Implementation(RigidAlarmDetector::class.java, Scope.JAVA_FILE_SCOPE)
        )

        private val methods = listOf("setExact", "setExactAndAllowWhileIdle", "setRepeating")
    }

    override fun getApplicableMethodNames(): List<String> = methods

    override fun visitMethodCall(context: JavaContext, node: UCallExpression, method: PsiMethod) {
        val receiver = node.receiverType?.canonicalText
        if (receiver?.contains("AlarmManager") == true) {
            context.report(ISSUE, node, context.getLocation(node), ISSUE.getExplanation(TextFormat.TEXT))
        }
    }
}
