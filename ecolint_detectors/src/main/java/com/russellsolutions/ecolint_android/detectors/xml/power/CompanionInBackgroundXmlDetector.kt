package com.russellsolutions.ecolint_android.detectors.xml.power

import com.android.tools.lint.detector.api.*
import org.w3c.dom.Attr

@Suppress("UnstableApiUsage")
class CompanionInBackgroundXmlDetector : ResourceXmlDetector() {

    companion object {
        val ISSUE: Issue = Issue.create(
            // ID string has spaces, a colon and emoji - want to preserve this
            // to make the lint rule stand out in reporting
            //noinspection LintImplIdFormat
            id = "\uD83C\uDF31 EcoLint: CompanionBackgroundPermission",
            //noinspection LintImplTextFormat
            briefDescription = "Avoid companion background permission for better battery life",
            explanation = "Using the permission REQUEST_COMPANION_RUN_IN_BACKGROUND will have a negative effect on the device's battery life.",
            category = Category.PERFORMANCE,
            priority = 6,
            severity = Severity.WARNING,
            implementation = Implementation(
                CompanionInBackgroundXmlDetector::class.java,
                Scope.MANIFEST_SCOPE
            )
        )
        private const val TARGET_PERMISSION = "android.permission.REQUEST_COMPANION_RUN_IN_BACKGROUND"
    }

    override fun getApplicableAttributes(): Collection<String> = listOf("name")

    override fun visitAttribute(context: XmlContext, attr: Attr) {
        if (attr.ownerElement.nodeName == "uses-permission" && attr.value == TARGET_PERMISSION) {
            context.report(ISSUE, attr, context.getLocation(attr), ISSUE.getExplanation(TextFormat.TEXT))
        }
    }
}
