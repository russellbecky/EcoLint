package com.russellsolutions.ecolint_android.registry

import com.android.tools.lint.client.api.IssueRegistry
import com.android.tools.lint.client.api.Vendor
import com.android.tools.lint.detector.api.CURRENT_API
import com.russellsolutions.ecolint_android.detectors.environment.batch.JobCoalesceDetector
import com.russellsolutions.ecolint_android.detectors.environment.batch.SensorCoalesceDetector
import com.russellsolutions.ecolint_android.detectors.environment.bottleneck.InternetInLoopDetector
import com.russellsolutions.ecolint_android.detectors.environment.bottleneck.UncompressedOutputStreamDetector
import com.russellsolutions.ecolint_android.detectors.environment.bottleneck.WifiMulticastLockDetector
import com.russellsolutions.ecolint_android.detectors.environment.idleness.ContinuousRenderingDetector
import com.russellsolutions.ecolint_android.detectors.environment.idleness.DurableWakeLockDetector
import com.russellsolutions.ecolint_android.detectors.environment.idleness.KeepCpuOnDetector
import com.russellsolutions.ecolint_android.detectors.environment.idleness.KeepScreenOnFlagsDetector
import com.russellsolutions.ecolint_android.detectors.environment.idleness.KeepVoiceAwakeDetector
import com.russellsolutions.ecolint_android.detectors.environment.idleness.RigidAlarmDetector
import com.russellsolutions.ecolint_android.detectors.environment.leakage.CameraLeakDetector
import com.russellsolutions.ecolint_android.detectors.environment.leakage.LocationLeakDetector
import com.russellsolutions.ecolint_android.detectors.environment.leakage.MediaPlayerLeakDetector
import com.russellsolutions.ecolint_android.detectors.environment.leakage.MediaRecorderLeakDetector
import com.russellsolutions.ecolint_android.detectors.environment.leakage.ResourceLeakDetector
import com.russellsolutions.ecolint_android.detectors.environment.leakage.SensorManagerLeakDetector
import com.russellsolutions.ecolint_android.detectors.environment.optimized_api.BluetoothLowEnergyDetector
import com.russellsolutions.ecolint_android.detectors.environment.optimized_api.FusedLocationDetector
import com.russellsolutions.ecolint_android.detectors.environment.sobriety.BrightnessOverrideDetector
import com.russellsolutions.ecolint_android.detectors.environment.sobriety.HighFrameRateDetector
import com.russellsolutions.ecolint_android.detectors.environment.sobriety.ThriftyBluetoothAdvertiseModeDetector
import com.russellsolutions.ecolint_android.detectors.xml.power.ChargeAwarenessXmlDetector
import com.russellsolutions.ecolint_android.detectors.xml.power.SaveModeAwarenessXmlDetector
import com.russellsolutions.ecolint_android.detectors.environment.sobriety.ThriftyBluetoothConnectionPriorityDetector
import com.russellsolutions.ecolint_android.detectors.environment.sobriety.ThriftyGeolocationCriteriaDetector
import com.russellsolutions.ecolint_android.detectors.environment.sobriety.ThriftyGeolocationMinDistanceDetector
import com.russellsolutions.ecolint_android.detectors.environment.sobriety.ThriftyGeolocationMinTimeDetector
import com.russellsolutions.ecolint_android.detectors.environment.sobriety.ThriftyMotionSensorDetector
import com.russellsolutions.ecolint_android.detectors.environment.sobriety.ThriftyNotificationDetector
import com.russellsolutions.ecolint_android.detectors.environment.sobriety.TorchFreeDetector
import com.russellsolutions.ecolint_android.detectors.environment.sobriety.VibrationFreeDetector
import com.russellsolutions.ecolint_android.detectors.social.privacy.GoogleTrackerDetector
import com.russellsolutions.ecolint_android.detectors.xml.batch.ServiceBootTimeXmlDetector
import com.russellsolutions.ecolint_android.detectors.xml.idleness.KeepCpuOnXmlDetector
import com.russellsolutions.ecolint_android.detectors.xml.idleness.KeepScreenOnXmlDetector
import com.russellsolutions.ecolint_android.detectors.xml.power.CompanionInBackgroundXmlDetector
import com.russellsolutions.ecolint_android.detectors.xml.power.IgnoreBatteryOptimizationsXmlDetector
import com.russellsolutions.ecolint_android.detectors.xml.sobriety.DarkUIBrightColorsXmlDetector
import com.russellsolutions.ecolint_android.detectors.xml.sobriety.DarkUIThemeManifestXmlDetector
import com.russellsolutions.ecolint_android.detectors.xml.sobriety.DarkUIThemeStyleXmlDetector

@Suppress("UnstableApiUsage")
class EcoLintDetectorRegistry : IssueRegistry() {
    override val issues =
        listOf(
            // Environment: Batch
            JobCoalesceDetector.ISSUE,
            SensorCoalesceDetector.ISSUE,
            // Environment: Bottleneck
            InternetInLoopDetector.ISSUE,
            UncompressedOutputStreamDetector.ISSUE,
            WifiMulticastLockDetector.ISSUE,
            // Environment: Idleness
            ContinuousRenderingDetector.ISSUE,
            DurableWakeLockDetector.ISSUE,
            KeepCpuOnDetector.ISSUE,
            KeepScreenOnFlagsDetector.ISSUE,
            KeepVoiceAwakeDetector.ISSUE,
            RigidAlarmDetector.ISSUE,
            // Environment: Leakage
            (CameraLeakDetector() as ResourceLeakDetector).ISSUE,
            (LocationLeakDetector() as ResourceLeakDetector).ISSUE,
            (MediaPlayerLeakDetector() as ResourceLeakDetector).ISSUE,
            (MediaRecorderLeakDetector() as ResourceLeakDetector).ISSUE,
            (SensorManagerLeakDetector() as ResourceLeakDetector).ISSUE,
            // Environment: Optimized api
            BluetoothLowEnergyDetector.ISSUE,
            FusedLocationDetector.ISSUE,
            // Environment: Power
            ChargeAwarenessXmlDetector.ISSUE,
            SaveModeAwarenessXmlDetector.ISSUE,
            // Environment: Sobriety
            BrightnessOverrideDetector.ISSUE,
            HighFrameRateDetector.ISSUE,
            ThriftyBluetoothAdvertiseModeDetector.ISSUE,
            ThriftyBluetoothConnectionPriorityDetector.ISSUE,
            ThriftyGeolocationCriteriaDetector.ISSUE,
            ThriftyGeolocationMinDistanceDetector.ISSUE,
            ThriftyGeolocationMinTimeDetector.ISSUE,
            ThriftyMotionSensorDetector.ISSUE,
            ThriftyNotificationDetector.ISSUE,
            TorchFreeDetector.ISSUE,
            VibrationFreeDetector.ISSUE,
            // Social: Privacy
            GoogleTrackerDetector.ISSUE,
            // Xml: Batch
            ServiceBootTimeXmlDetector.ISSUE,
            // Xml: Idleness
            KeepCpuOnXmlDetector.ISSUE,
            KeepScreenOnXmlDetector.ISSUE,
            // Xm: Power
            ChargeAwarenessXmlDetector.ISSUE,
            CompanionInBackgroundXmlDetector.ISSUE,
            IgnoreBatteryOptimizationsXmlDetector.ISSUE,
            SaveModeAwarenessXmlDetector.ISSUE,
            /// Xml: Sobriety
            DarkUIBrightColorsXmlDetector.ISSUE,
            DarkUIThemeManifestXmlDetector.ISSUE,
            DarkUIThemeStyleXmlDetector.ISSUE,
        )

    override val api: Int = CURRENT_API

    override val minApi: Int = 6

    override val vendor = Vendor(
        feedbackUrl = "https://github.com/russellbecky/EcoLint/issues",
        identifier = "com.russellsolutions.ecolint_android",
        vendorName = "Russell Solutions",
    )
}