package com.russellsolutions.ecolint_android.detectors.environment.idleness

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
import org.jetbrains.uast.UMethod


class KeepVoiceAwakeDetector : Detector(), Detector.UastScanner {
    companion object {
        val ISSUE = Issue.create(
            // ID string has spaces, a colon and emoji - want to preserve this
            // to make the lint rule stand out in reporting
            //noinspection LintImplIdFormat
            "\uD83C\uDF31 EcoLint: KeepVoiceAwake",
            "Avoid using FLAG_KEEP_SCREEN_ON with voice interaction",
            "Keeping the screen on for voice interaction prevents battery-saving modes from engaging.",
            Category.PERFORMANCE, 6, Severity.WARNING,
            Implementation(KeepVoiceAwakeDetector::class.java, Scope.JAVA_FILE_SCOPE)
        )

        val restrictedFlags = listOf("FLAG_KEEP_SCREEN_ON")
    }

    override fun getApplicableUastTypes(): List<Class<out UElement>> {
        return listOf(UMethod::class.java)
    }

    override fun createUastHandler(context: JavaContext): UElementHandler? {
        return object : UElementHandler() {
            override fun visitMethod(node: UMethod) {
                val body = node.uastBody?.asRenderString()
                if (restrictedFlags.any { body?.contains(it) == true }) {
                        context.report(ISSUE, node, context.getLocation(node), ISSUE.getExplanation(TextFormat.TEXT))
                }
            }
        }
    }
}
