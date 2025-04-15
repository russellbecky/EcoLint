package com.russellsolutions.ecolint_android.detectors.environment.batch

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

class JobCoalesceDetector : Detector(), SourceCodeScanner {
    companion object {
        val ISSUE = Issue.create(
            "🌱 EcoLint: JobCoalescing",
            "Use JobScheduler instead of direct alarms or sync adapters",
            "Avoid using AlarmManager or SyncAdapter for alarms. Use JobScheduler to allow the system to optimize scheduling.",
            Category.PERFORMANCE, 6, Severity.WARNING,
            Implementation(JobCoalesceDetector::class.java, Scope.JAVA_FILE_SCOPE)
        )

        private val methodNames = listOf(
            "set", "setAlarmClock", "setAndAllowWhileIdle", "setExact", "setExactAndAllowWhileIdle",
            "setInexactRepeating", "setRepeating", "setWindow", "onPerformSync", "getSyncAdapterBinder"
        )
    }

    override fun getApplicableMethodNames(): List<String> = methodNames

    override fun visitMethodCall(context: JavaContext, node: UCallExpression, method: PsiMethod) {
        val qualified = method.containingClass?.qualifiedName ?: return
        if (qualified.contains("AlarmManager") || qualified.contains("SyncAdapter") || node.methodName in methodNames) {
            context.report(ISSUE, node, context.getLocation(node), ISSUE.getExplanation(TextFormat.TEXT))
        }
    }
}
