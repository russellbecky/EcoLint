package com.russellsolutions.ecolint_android.detectors.xml.power

import com.android.SdkConstants.TAG_ACTION
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
import org.w3c.dom.Element

class ChargeAwarenessXmlDetector : Detector(), XmlScanner {
    companion object {
        val ISSUE = Issue.create(
            // ID string has spaces, a colon and emoji - want to preserve this
            // to make the lint rule stand out in reporting
            //noinspection LintImplIdFormat
            "\uD83C\uDF31 EcoLint: ChargeAwareness",
            "Use battery state receivers for eco-awareness",
            "Monitoring power changes and customizing behavior depending on battery level is a good practice.",
            Category.USABILITY, 3, Severity.WARNING,
            Implementation(ChargeAwarenessXmlDetector::class.java, Scope.MANIFEST_SCOPE)
        )

        private val restrictedActions = listOf(
            "android.intent.action.ACTION_POWER_CONNECTED",
            "android.intent.action.ACTION_POWER_DISCONNECTED",
            "android.intent.action.BATTERY_LOW",
            "android.intent.action.BATTERY_OKAY"
        )
    }

    override fun getApplicableElements(): Collection<String> = listOf(TAG_ACTION)

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
}
