package com.russellsolutions.ecolint_android.detectors.environment.leakage

import com.android.tools.lint.client.api.UElementHandler
import com.android.tools.lint.detector.api.Category
import com.android.tools.lint.detector.api.Context
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.JavaContext
import com.android.tools.lint.detector.api.Scope
import com.android.tools.lint.detector.api.Severity
import org.jetbrains.uast.UElement
import org.jetbrains.uast.UMethod

// Helper base class for resource leak rules
abstract class ResourceLeakDetector(
    private val initMethod: String,
    private val releaseMethod: String,
    private val typeFqn: String,
    private val issueId: String,
    private val message: String
) : Detector(), Detector.UastScanner {
    private var initCalled = false
    private var releaseCalled = false
    private var node: UMethod? = null

    val ISSUE: Issue = Issue.create(
        // ID string has spaces, a colon and emoji - want to preserve this
        // to make the lint rule stand out in reporting
        //noinspection LintImplIdFormat
        "\uD83C\uDF31 EcoLint: $issueId",
        "Resource not released",
        message,
        Category.PERFORMANCE, 6, Severity.WARNING,
        Implementation(this::class.java, Scope.JAVA_FILE_SCOPE)
    )

    override fun getApplicableMethodNames(): List<String> = listOf(initMethod, releaseMethod)

    override fun beforeCheckRootProject(context: Context) {
        initCalled = false
        releaseCalled = false
    }

    override fun getApplicableUastTypes(): List<Class<out UElement>> {
        return listOf(UMethod::class.java)
    }

    override fun createUastHandler(context: JavaContext): UElementHandler? {
        return object : UElementHandler() {
            override fun visitMethod(node: UMethod) {
                val body = node.uastBody?.asRenderString()
                if (node.uastParameters.any { it.asRenderString().contains(typeFqn) } || body?.contains(typeFqn) == true) {
                    if (body?.contains(initMethod) == true) {
                        initCalled = true
                        this@ResourceLeakDetector.node = node
                    }
                    if (body?.contains(releaseMethod) == true) {
                        releaseCalled = true
                        this@ResourceLeakDetector.node = node
                    }
                }
            }
        }
    }

    override fun afterCheckRootProject(context: Context) {
        if (!initCalled || !releaseCalled) {
            node?.let {
                context.report(ISSUE, context.getLocation(node), message)
            }
        }
    }
}
