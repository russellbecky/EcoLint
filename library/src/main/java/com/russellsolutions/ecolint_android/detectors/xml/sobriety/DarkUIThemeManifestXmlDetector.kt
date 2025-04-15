package com.russellsolutions.ecolint_android.detectors.xml.sobriety

import com.android.tools.lint.detector.api.*
import org.w3c.dom.Attr

@Suppress("UnstableApiUsage")
class DarkUIThemeManifestXmlDetector : ResourceXmlDetector() {
    companion object {
        val ISSUE: Issue = Issue.create(
            id = "\uD83C\uDF31 EcoLint: LightThemeUsage",
            briefDescription = "Light theme on AMOLED impacts battery life",
            explanation = "Using a light theme may have a significant impact on energy consumption on (AM)OLED screens.",
            category = Category.PERFORMANCE,
            priority = 6,
            severity = Severity.WARNING,
            implementation = Implementation(
                DarkUIThemeManifestXmlDetector::class.java,
                Scope.MANIFEST_SCOPE
            )
        )
        private val restrictedThemes = listOf(
            "Theme.Holo.Light",
            "Theme.Material.Light",
            "Theme.Material3.Light",
            "Theme.AppCompat.Light",
            "Theme.MaterialComponents.Light"
        )
    }

    override fun getApplicableAttributes(): Collection<String> = listOf("theme")

    override fun visitAttribute(context: XmlContext, attr: Attr) {
        val value = attr.value.removePrefix("@style/")
        if (restrictedThemes.any { value.startsWith(it) }) {
            context.report(ISSUE, attr, context.getLocation(attr), ISSUE.getExplanation(TextFormat.TEXT))
        }
    }
}
