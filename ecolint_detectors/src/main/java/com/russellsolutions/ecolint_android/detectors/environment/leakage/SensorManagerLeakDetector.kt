package com.russellsolutions.ecolint_android.detectors.environment.leakage

class SensorManagerLeakDetector : ResourceLeakDetector(
    initMethod = "registerListener",
    releaseMethod = "unregisterListener",
    typeFqn = "android.hardware.SensorManager",
    issueId = "SensorManagerLeak",
    message = "Failing to call android.hardware.SensorManager#unregisterListener() can drain the battery in just a few hours."
)
