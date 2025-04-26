package com.russellsolutions.ecolint_android.detectors.environment.leakage

class LocationLeakDetector : ResourceLeakDetector(
    initMethod = "requestLocationUpdates",
    releaseMethod = "removeUpdates",
    typeFqn = "android.location.LocationManager",
    issueId = "LocationLeak",
    message = "Failing to call android.location.LocationManager#removeUpdates() can drain the battery in just a few hours."
)
