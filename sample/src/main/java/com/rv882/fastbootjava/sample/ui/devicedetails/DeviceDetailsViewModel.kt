
package com.rv882.fastbootjava.sample.ui.devicedetails

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import com.google.android.fastbootmobile.DeviceId
import com.google.android.fastbootmobile.FastbootDeviceContext
import com.google.android.fastbootmobile.FastbootDeviceManager
import com.google.android.fastbootmobile.FastbootDeviceManagerListener
import com.rv882.fastbootjava.sample.data.FastbootDevice

class DeviceDetailsViewModel : ViewModel(), FastbootDeviceManagerListener {
    var fastbootDevice: MutableLiveData<FastbootDevice?> = MutableLiveData()
    private var deviceContext: FastbootDeviceContext? = null

    init {
        FastbootDeviceManager.addFastbootDeviceManagerListener(this)
    }

    fun connectToDevice(deviceId: DeviceId) {
        fastbootDevice.value = null
        deviceContext?.close()
        FastbootDeviceManager.connectToDevice(deviceId)
    }

    override fun onFastbootDeviceAttached(deviceId: DeviceId) {}

    override fun onFastbootDeviceDetached(deviceId: DeviceId) {
        if (fastbootDevice.value?.deviceId != deviceId) return
        deviceContext?.close()
        deviceContext = null
    }

    override fun onFastbootDeviceConnected(deviceId: DeviceId, deviceContext: FastbootDeviceContext) {
        this.deviceContext = deviceContext
        fastbootDevice.value = FastbootDevice.fromFastbootDeviceContext(deviceId, deviceContext)
    }

    override fun onCleared() {
        super.onCleared()
        FastbootDeviceManager.removeFastbootDeviceManagerListener(this)
        deviceContext?.close()
        deviceContext = null
    }
}
