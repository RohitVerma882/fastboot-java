package com.rv882.fastbootjava;

import androidx.annotation.NonNull;

public interface FastbootDeviceManagerListener {
    void onFastbootDeviceAttached(@NonNull String deviceId);
    void onFastbootDeviceDetached(@NonNull String deviceId);
    void onFastbootDeviceConnected(@NonNull String deviceId, @NonNull FastbootDeviceContext deviceContext);
}
