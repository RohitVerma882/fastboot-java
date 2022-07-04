package com.rv882.fastbootjava.sample.ui.devicedetails;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.MutableLiveData;

import com.rv882.fastbootjava.FastbootDeviceManagerListener;
import com.rv882.fastbootjava.FastbootDeviceContext;
import com.rv882.fastbootjava.FastbootDeviceManager;

import com.rv882.fastbootjava.sample.data.FastbootDevice;

public class DeviceDetailsViewModel extends ViewModel implements FastbootDeviceManagerListener {

    public MutableLiveData<FastbootDevice> fastbootDevice = new MutableLiveData<>();
    private FastbootDeviceContext deviceContext = null;
    
    public DeviceDetailsViewModel() {
        FastbootDeviceManager.Instance.addFastbootDeviceManagerListener(this);
    }
    
    public void connectToDevice(String deviceId) {
        fastbootDevice.setValue(null);
        if (deviceContext != null) {
            deviceContext.close();
        }
        FastbootDeviceManager.Instance.connectToDevice(deviceId);
    }
    
    @Override
    public void onFastbootDeviceAttached(String deviceId) {
    }

    @Override
    public void onFastbootDeviceDetached(String deviceId) {
        FastbootDevice device = fastbootDevice.getValue();
        if (device != null && device.getDeviceId() != deviceId) {
            return;
        }
        if (deviceContext != null) {
            deviceContext.close();
            deviceContext = null;
        }
    }

    @Override
    public void onFastbootDeviceConnected(String deviceId, FastbootDeviceContext deviceContext) {
        this.deviceContext = deviceContext;
        fastbootDevice.setValue(FastbootDevice.fromDeviceContext(deviceId, deviceContext));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        FastbootDeviceManager.Instance.removeFastbootDeviceManagerListener(this);
        if (deviceContext != null) {
            deviceContext.close();
            deviceContext = null;
        }
    }
}
