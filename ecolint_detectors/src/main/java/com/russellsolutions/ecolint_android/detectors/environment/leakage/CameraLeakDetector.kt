package com.russellsolutions.ecolint_android.detectors.environment.leakage

class CameraLeakDetector : ResourceLeakDetector(
    initMethod = "open",
    releaseMethod = "release",
    typeFqn = "android.hardware.Camera",
    issueId = "CameraLeak",
    message = "Failing to call android.hardware.Camera#release() can drain the battery in just a few hours."
)
