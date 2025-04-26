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

class ThriftyGeolocationMinTimeDetector : Detector(), SourceCodeScanner {
    companion object {
        val ISSUE = Issue.create(
            // ID string has spaces, a colon and emoji - want to preserve this
            // to make the lint rule stand out in reporting
            //noinspection LintImplIdFormat
            "\uD83C\uDF31 EcoLint: ThriftyMinTime",
            "Location update minTime should be > 0",
            "Location updates should be done with a time interval greater than 0 to conserve battery.",
            Category.PERFORMANCE, 6, Severity.WARNING,
            Implementation(ThriftyGeolocationMinTimeDetector::class.java, Scope.JAVA_FILE_SCOPE)
        )
    }

    override fun getApplicableMethodNames(): List<String> = listOf("requestLocationUpdates")

    override fun visitMethodCall(context: JavaContext, node: UCallExpression, method: PsiMethod) {
        if (node.receiverType?.canonicalText?.contains("LocationManager") == true && node.valueArgumentCount >= 2) {
            val timeArg = node.valueArguments[1]
            val constant = timeArg.evaluate() as? Number
            if (constant != null && constant.toLong() == 0L) {
                context.report(ISSUE, node, context.getLocation(timeArg), ISSUE.getExplanation(TextFormat.TEXT))
            }
        }
    }
}
