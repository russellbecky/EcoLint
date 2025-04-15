package com.russellsolutions.ecolint_android.detectors.environment.sobriety

import com.android.tools.lint.detector.api.*
import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiVariable
import org.jetbrains.uast.*

@Suppress("UnstableApiUsage")
class ThriftyGeolocationCriteriaDetector : Detector(), SourceCodeScanner {
    companion object {
        val ISSUE: Issue = Issue.create(
            id = "\uD83C\uDF31 EcoLint: ThriftyGeolocationCriteria",
            briefDescription = "Ensure efficient geolocation configuration",
            explanation = """
                Always use LocationManager.getBestProvider(...) before requesting location updates,
                and configure Criteria with setPowerRequirement(POWER_LOW) for battery efficiency.
            """.trimIndent(),
            category = Category.PERFORMANCE,
            priority = 7,
            severity = Severity.WARNING,
            implementation = Implementation(
                ThriftyGeolocationCriteriaDetector::class.java,
                Scope.JAVA_FILE_SCOPE
            )
        )

        private const val MSG_MISSING_BEST_PROVIDER =
            "You should configure a location provider (LocationManager.getBestProvider(...)) to optimize battery usage."
        private const val MSG_MISSING_POWER_REQUIREMENT =
            "You should call Criteria.setPowerRequirement(POWER_LOW) to optimize battery usage."
        private const val MSG_WRONG_POWER_REQUIREMENT =
            "You should set the power requirement to POWER_LOW to optimize battery usage."
    }

    private val requestLocationNodes = mutableListOf<UCallExpression>()
    private val bestProviderNodes = mutableListOf<UCallExpression>()
    private var powerRequirementSetProperly = false

    override fun getApplicableMethodNames(): List<String> =
        listOf("requestLocationUpdates", "getBestProvider", "setPowerRequirement")

    override fun visitMethodCall(context: JavaContext, node: UCallExpression, method: PsiMethod) {
        val receiver = node.receiverType?.canonicalText ?: return
        when (node.methodName) {
            "requestLocationUpdates" -> {
                if (receiver == "android.location.LocationManager") {
                    requestLocationNodes.add(node)
                }
            }

            "getBestProvider" -> {
                if (receiver == "android.location.LocationManager") {
                    bestProviderNodes.add(node)
                }
            }

            "setPowerRequirement" -> {
                if (receiver == "android.location.Criteria") {
                    val arg = node.valueArguments.firstOrNull()
                    val value = when (arg) {
                        is ULiteralExpression -> arg.value as? Int
                        is USimpleNameReferenceExpression -> {
                            val resolved = arg.resolve()
                            if (resolved is PsiVariable && resolved.hasModifierProperty("final"))
                                resolved.computeConstantValue() as? Int
                            else null
                        }
                        else -> null
                    }

                    powerRequirementSetProperly = (value == 1)
                    if (!powerRequirementSetProperly) {
                        context.report(ISSUE, node, context.getLocation(node), MSG_WRONG_POWER_REQUIREMENT)
                    }
                }
            }
        }
    }

    override fun afterCheckRootProject(context: Context) {
        if (requestLocationNodes.isNotEmpty()) {
            if (bestProviderNodes.isEmpty()) {
                requestLocationNodes.forEach {
                    context.report(ISSUE, context.getLocation(it), MSG_MISSING_BEST_PROVIDER)
                }
            } else if (!powerRequirementSetProperly) {
                bestProviderNodes.forEach {
                    context.report(ISSUE, context.getLocation(it), MSG_MISSING_POWER_REQUIREMENT)
                }
            }
        }
        // Reset state (important for multi-file runs)
        requestLocationNodes.clear()
        bestProviderNodes.clear()
        powerRequirementSetProperly = false
    }
}
