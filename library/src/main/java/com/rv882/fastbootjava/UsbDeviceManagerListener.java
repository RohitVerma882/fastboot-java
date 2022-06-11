package com.rv882.fastbootjava;

import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;

import androidx.annotation.NonNull;

public interface UsbDeviceManagerListener {
    boolean filterDevice(@NonNull UsbDevice device);
    void onUsbDeviceAttached(@NonNull UsbDevice device);
    void onUsbDeviceDetached(@NonNull UsbDevice device);
	void onUsbDeviceConnected(@NonNull UsbDevice device, @NonNull UsbDeviceConnection connection);
}
