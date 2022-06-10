package com.rv882.fastbootjava;

public interface FastbootDeviceManagerListener {
    void onFastbootDeviceAttached(String deviceId);
    void onFastbootDeviceDetached(String deviceId);
    void onFastbootDeviceConnected(String deviceId, FastbootDeviceContext deviceContext);
}
