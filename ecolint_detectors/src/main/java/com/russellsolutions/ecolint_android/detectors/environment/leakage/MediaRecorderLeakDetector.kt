package com.russellsolutions.ecolint_android.detectors.environment.leakage

class MediaRecorderLeakDetector : ResourceLeakDetector(
    initMethod = "MediaRecorder",
    releaseMethod = "release",
    typeFqn = "android.media.MediaRecorder",
    issueId = "MediaRecorderLeak",
    message = "Failing to call release() on a Media Recorder may lead to continuous battery consumption."
)
