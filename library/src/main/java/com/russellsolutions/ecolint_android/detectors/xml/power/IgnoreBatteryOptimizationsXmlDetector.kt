package com.russellsolutions.ecolint_android.detectors.xml.power

import com.android.tools.lint.detector.api.*
import org.w3c.dom.Attr

@Suppress("UnstableApiUsage")
class IgnoreBatteryOptimizationsXmlDetector : ResourceXmlDetector() {
    companion object {
        val ISSUE: Issue = Issue.create(
            id = "\uD83C\uDF31 EcoLint: IgnoreBatteryOptimizationsPermission",
            briefDescription = "Avoid disabling battery optimization",
            explanation = "Battery optimization should not be ignored.",
            category = Category.PERFORMANCE,
            priority = 6,
            severity = Severity.WARNING,
            implementation = Implementation(
                IgnoreBatteryOptimizationsXmlDetector::class.java,
                Scope.MANIFEST_SCOPE
            )
        )
        private const val TARGET_PERMISSION =
            "android.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS"
    }

    override fun getApplicableAttributes(): Collection<String> = listOf("name")

    override fun visitAttribute(context: XmlContext, attr: Attr) {
        if (attr.ownerElement.nodeName == "uses-permission" && attr.value == TARGET_PERMISSION) {
            context.report(ISSUE, attr, context.getLocation(attr), ISSUE.getExplanation(TextFormat.TEXT))
        }
    }
}
