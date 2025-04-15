package com.russellsolutions.ecolint_android.detectors.xml.idleness

import com.android.SdkConstants.FRAME_LAYOUT
import com.android.SdkConstants.GRID_LAYOUT
import com.android.SdkConstants.LINEAR_LAYOUT
import com.android.SdkConstants.RELATIVE_LAYOUT
import com.android.SdkConstants.SCROLL_VIEW
import com.android.tools.lint.detector.api.Category
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.Scope
import com.android.tools.lint.detector.api.Severity
import com.android.tools.lint.detector.api.TextFormat
import com.android.tools.lint.detector.api.XmlContext
import com.android.tools.lint.detector.api.XmlScanner
import com.android.utils.forEach
import org.w3c.dom.Attr
import org.w3c.dom.Element

class KeepScreenOnXmlDetector : Detector(), XmlScanner {
    companion object {
        val ISSUE = Issue.create(
            id = "\uD83C\uDF31 EcoLint: KeepScreenOnAttribute",
            briefDescription = "Avoid setting keepScreenOn to true",
            explanation = "Keeping the screen on should be avoided to avoid draining the battery.",
            category = Category.PERFORMANCE,
            priority = 6,
            severity = Severity.WARNING,
            implementation = Implementation(KeepScreenOnXmlDetector::class.java, Scope.RESOURCE_FILE_SCOPE)
        )

        private val restrictedAttributes = listOf(
            "android:keepScreenOn"
        )
    }

    override fun getApplicableElements(): Collection<String> = listOf(FRAME_LAYOUT, LINEAR_LAYOUT, RELATIVE_LAYOUT, GRID_LAYOUT, SCROLL_VIEW)

    override fun visitElement(context: XmlContext, element: Element) {
        val attributes = element.attributes
        attributes.forEach { node ->
            if (restrictedAttributes.any { node.nodeName.contains(it) && node.nodeValue == "true" }) {
                context.report(ISSUE, element, context.getLocation(element), ISSUE.getExplanation(TextFormat.TEXT))
            }
        }
    }

    override fun getApplicableAttributes(): List<String> = listOf("android:keepScreenOn")

    override fun visitAttribute(context: XmlContext, attribute: Attr) {
        if (attribute.value == "true") {
            context.report(ISSUE, attribute, context.getLocation(attribute), ISSUE.getExplanation(TextFormat.TEXT))
        }
    }
}