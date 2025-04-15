package com.russellsolutions.ecolint_android.detectors.xml.idleness

import com.android.SdkConstants.TAG_PERMISSION
import com.android.SdkConstants.TAG_USES_PERMISSION
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

class KeepCpuOnXmlDetector : Detector(), XmlScanner {
    companion object {
        val ISSUE = Issue.create(
            id = "\uD83C\uDF31 EcoLint: KeepCpuOnPermission",
            briefDescription = "Avoid using WAKE_LOCK permission",
            explanation = "Keeping the screen on should be avoided to avoid draining battery.",
            category = Category.PERFORMANCE,
            priority = 6,
            severity = Severity.WARNING,
            implementation = Implementation(KeepCpuOnXmlDetector::class.java, Scope.MANIFEST_SCOPE)
        )

        private val restrictedActions = listOf(
            "android.permission.WAKE_LOCK"
        )
    }

    override fun getApplicableElements(): Collection<String> = listOf(TAG_PERMISSION, TAG_USES_PERMISSION)

    override fun visitElement(context: XmlContext, element: Element) {
        val attributes = element.attributes
        attributes.forEach { node ->
            if (node.nodeName == "android:name"){
                if (restrictedActions.any { node.nodeValue.contains(it) }) {
                    context.report(ISSUE, element, context.getLocation(element), ISSUE.getExplanation(TextFormat.TEXT))
                }
            }
        }
    }

    override fun getApplicableAttributes(): List<String> = listOf("android:name")

    override fun visitAttribute(context: XmlContext, attribute: Attr) {
        if (attribute.value == "android.permission.WAKE_LOCK") {
            context.report(
                ISSUE, attribute, context.getLocation(attribute), ISSUE.getExplanation(
                TextFormat.TEXT))
        }
    }
}