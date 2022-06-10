package com.rv882.fastbootjava;

import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;

public interface UsbDeviceManagerListener {
    boolean filterDevice(UsbDevice device);
    void onUsbDeviceAttached(UsbDevice device);
    void onUsbDeviceDetached(UsbDevice device);
	void onUsbDeviceConnected(UsbDevice device, UsbDeviceConnection connection);
}
