package com.russellsolutions.ecolint_android.detectors.social.privacy

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

class GoogleTrackerDetector : Detector(), Detector.UastScanner {
    companion object {
        val ISSUE = Issue.create(
            "\uD83C\uDF31 EcoLint: GoogleTrackerImport",
            "Avoid importing Google Analytics or Firebase Analytics",
            "Using com.google.android.gms.analytics.* or com.google.firebase.analytics.* may introduce privacy concerns.",
            Category.CUSTOM_LINT_CHECKS, 6, Severity.WARNING,
            Implementation(GoogleTrackerDetector::class.java, Scope.JAVA_FILE_SCOPE)
        )

        private val restrictedImports = listOf("com.google.android.gms.analytics", "com.google.firebase.analytics")
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
