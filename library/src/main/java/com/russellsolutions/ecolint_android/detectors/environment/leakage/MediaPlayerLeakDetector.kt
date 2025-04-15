package com.russellsolutions.ecolint_android.detectors.environment.leakage

class MediaPlayerLeakDetector : ResourceLeakDetector(
    initMethod = "MediaPlayer",
    releaseMethod = "release",
    typeFqn = "android.media.MediaPlayer",
    issueId = "MediaPlayerLeak",
    message = "Failing to call release() on a Media Player may lead to continuous battery consumption."
)
