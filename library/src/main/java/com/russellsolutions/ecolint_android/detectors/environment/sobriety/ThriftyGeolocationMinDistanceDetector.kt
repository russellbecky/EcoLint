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

class ThriftyGeolocationMinDistanceDetector : Detector(), SourceCodeScanner {
    companion object {
        val ISSUE = Issue.create(
            "\uD83C\uDF31 EcoLint: ThriftyMinDistance",
            "Location update minDistance should be > 0",
            "Location updates should be done with a distance interval greater than 0.",
            Category.PERFORMANCE, 6, Severity.WARNING,
            Implementation(ThriftyGeolocationMinDistanceDetector::class.java, Scope.JAVA_FILE_SCOPE)
        )
    }

    override fun getApplicableMethodNames(): List<String> = listOf("requestLocationUpdates")

    override fun visitMethodCall(context: JavaContext, node: UCallExpression, method: PsiMethod) {
        if (node.receiverType?.canonicalText?.contains("LocationManager") == true && node.valueArgumentCount >= 3) {
            val distanceArg = node.valueArguments[2]
            val constant = distanceArg.evaluate() as? Number
            if (constant != null && constant.toFloat() == 0f) {
                context.report(ISSUE, node, context.getLocation(distanceArg), ISSUE.getExplanation(TextFormat.TEXT))
            }
        }
    }
}
