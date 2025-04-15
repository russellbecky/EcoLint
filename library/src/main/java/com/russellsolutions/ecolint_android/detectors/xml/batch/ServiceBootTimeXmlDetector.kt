package com.russellsolutions.ecolint_android.detectors.xml.batch

import com.android.tools.lint.detector.api.*
import org.w3c.dom.Attr

@Suppress("UnstableApiUsage")
class ServiceBootTimeXmlDetector : ResourceXmlDetector() {
    companion object {
        val ISSUE: Issue = Issue.create(
            id = "\uD83C\uDF31 EcoLint: BootCompletedReceiver",
            briefDescription = "Avoid launching service at boot",
            explanation = "Avoid using a receiver to launch a service with BOOT_COMPLETED to drain less battery",
            category = Category.PERFORMANCE,
            priority = 6,
            severity = Severity.WARNING,
            implementation = Implementation(
                ServiceBootTimeXmlDetector::class.java,
                Scope.MANIFEST_SCOPE
            )
        )
        private const val BOOT_COMPLETED = "android.intent.action.BOOT_COMPLETED"
    }

    override fun getApplicableAttributes(): Collection<String> = listOf("name")

    override fun visitAttribute(context: XmlContext, attr: Attr) {
        if (attr.value == BOOT_COMPLETED) {
            context.report(ISSUE, attr, context.getLocation(attr), ISSUE.getExplanation(TextFormat.TEXT))
        }
    }
}
