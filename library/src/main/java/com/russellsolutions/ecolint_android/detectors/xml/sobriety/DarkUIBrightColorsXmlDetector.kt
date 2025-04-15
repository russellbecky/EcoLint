package com.russellsolutions.ecolint_android.detectors.xml.sobriety

import com.android.tools.lint.detector.api.*
import org.w3c.dom.Attr

@Suppress("UnstableApiUsage")
class DarkUIBrightColorsXmlDetector : ResourceXmlDetector() {
    companion object {
        val ISSUE: Issue = Issue.create(
            id = "\uD83C\uDF31 EcoLint: BrightColorUsage",
            briefDescription = "Bright colors waste energy on (AM)OLED screens",
            explanation = "Avoid using too bright colors for (AM)OLED screens.",
            category = Category.PERFORMANCE,
            priority = 5,
            severity = Severity.WARNING,
            implementation = Implementation(
                DarkUIBrightColorsXmlDetector::class.java,
                Scope.RESOURCE_FILE_SCOPE
            )
        )
        private const val DARKNESS_CAP = 0.6
        private val ATTRIBUTES_TO_CHECK = listOf(
            "background", "foreground", "foregroundTint", "tint", "src", "textColor",
            "color", "textColorHighlight", "textColorHint", "textColorLink",
            "shadowColor", "srcCompat"
        )
    }

    override fun getApplicableAttributes(): Collection<String> = ATTRIBUTES_TO_CHECK

    override fun visitAttribute(context: XmlContext, attr: Attr) {
        val hex = attr.value
        if (!hex.startsWith("#")) return
        val color = try {
            parseHexColor(hex)
        } catch (e: Exception) {
            return
        }

        val darkness = 1 - (0.299 * color[0] + 0.587 * color[1] + 0.114 * color[2]) / 255
        if (darkness < DARKNESS_CAP) {
            context.report(ISSUE, attr, context.getLocation(attr), ISSUE.getExplanation(TextFormat.TEXT))
        }
    }

    private fun parseHexColor(hex: String): IntArray {
        val clean = hex.removePrefix("#")
        val colorInt = when (clean.length) {
            3 -> Integer.parseInt(clean.map { "$it$it" }.joinToString(""), 16)
            6, 8 -> Integer.parseInt(clean.takeLast(6), 16)
            else -> throw IllegalArgumentException("Unknown hex format")
        }

        val r = (colorInt shr 16) and 0xFF
        val g = (colorInt shr 8) and 0xFF
        val b = colorInt and 0xFF

        return intArrayOf(r, g, b)
    }
}
