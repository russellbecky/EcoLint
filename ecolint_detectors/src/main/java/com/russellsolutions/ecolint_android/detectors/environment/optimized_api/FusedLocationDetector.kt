package com.russellsolutions.ecolint_android.detectors.environment.optimized_api

import com.android.tools.lint.client.api.UElementHandler
import com.android.tools.lint.detector.api.Category
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.JavaContext
import com.android.tools.lint.detector.api.Scope
import com.android.tools.lint.detector.api.Severity
import com.android.tools.lint.detector.api.TextFormat
import org.jetbrains.uast.UElement
import org.jetbrains.uast.UImportStatement

class FusedLocationDetector : Detector(), Detector.UastScanner {
    companion object {
        val ISSUE = Issue.create(
            // ID string has spaces, a colon and emoji - want to preserve this
            // to make the lint rule stand out in reporting
            //noinspection LintImplIdFormat
            "\uD83C\uDF31 EcoLint: FusedLocation",
            "Prefer FusedLocationProviderClient over android.location",
            "Use com.google.android.gms.location instead of android.location to maximize battery life.",
            Category.PERFORMANCE, 5, Severity.WARNING,
            Implementation(FusedLocationDetector::class.java, Scope.JAVA_FILE_SCOPE)
        )

        private val restrictedImports = listOf("android.location", "com.google.android.gms.location")
    }

    override fun getApplicableUastTypes(): List<Class<out UElement>> {
        return listOf(UImportStatement::class.java)
    }

    override fun createUastHandler(context: JavaContext): UElementHandler? {
        return object : UElementHandler() {
            override fun visitImportStatement(node: UImportStatement) {
                val import = node.importReference?.asSourceString()
                if (restrictedImports.any { import?.contains(it) == true }) {
                    context.report(ISSUE, node.sourcePsi, context.getLocation(node), ISSUE.getExplanation(TextFormat.TEXT))
                }
            }
        }
    }
}
