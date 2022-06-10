
package com.rv882.fastbootjava.sample.data

import com.google.android.fastbootmobile.DeviceId
import com.google.android.fastbootmobile.FastbootCommand
import com.google.android.fastbootmobile.FastbootDeviceContext

data class FastbootDevice(val deviceId: DeviceId, val serialNumber: String, val currentSlot: String) {
    companion object {
        fun fromDeviceId(deviceId: DeviceId) = FastbootDevice(deviceId, "", "")

        fun fromFastbootDeviceContext(deviceId: DeviceId, deviceContext: FastbootDeviceContext): FastbootDevice =
                FastbootDevice(
                        deviceId,
                        deviceContext.sendCommand(FastbootCommand.getVar("serialno")).data,
                        deviceContext.sendCommand(FastbootCommand.getVar("current-slot")).data)
    }
}
