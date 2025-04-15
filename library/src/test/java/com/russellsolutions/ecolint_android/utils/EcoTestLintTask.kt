package com.russellsolutions.ecolint_android.detectors.utils

import com.android.tools.lint.checks.infrastructure.TestLintTask
import java.io.File

internal fun TestLintTask.ecolint() =
    this.sdkHome(File("/Users/becky.russell/Library/Android/sdk"))