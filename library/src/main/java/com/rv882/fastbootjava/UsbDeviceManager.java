package com.rv882.fastbootjava;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.BroadcastReceiver;
import android.annotation.SuppressLint;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Map;

import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;

import androidx.annotation.NonNull;

public class UsbDeviceManager {
	private static final String ACTION_USB_PERMISSION = "com.rv882.fastbootjava.USB_PERMISSION";

	@NonNull
    private WeakReference<Context> context;
	@NonNull
    private ArrayList<UsbDeviceManagerListener> listeners = new ArrayList<UsbDeviceManagerListener>();
	@NonNull
    private UsbManager usbManager;

	private BroadcastReceiver usbActionReceiver = new BroadcastReceiver() {
		@Override
        public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
            if (intent == null || action == null) return;
			if (action.equals(UsbManager.ACTION_USB_DEVICE_ATTACHED)) {
				for (UsbDeviceManagerListener listener : listeners) {
					UsbDevice device = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
					if (listener.filterDevice(device)) {
						listener.onUsbDeviceAttached(device);
					}
				}
			} else if (action.equals(UsbManager.ACTION_USB_DEVICE_DETACHED)) {
				for (UsbDeviceManagerListener listener : listeners) {
					UsbDevice device = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
					if (listener.filterDevice(device)) {
						listener.onUsbDeviceDetached(device);
					}
				}
            }
        }
    };

    private BroadcastReceiver usbPermissionReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent == null || ACTION_USB_PERMISSION != intent.getAction()) return;
            synchronized (this) {
                UsbDevice device = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                    connectToDeviceInternal(device);
                }
            }
        }
    };

    public UsbDeviceManager(@NonNull WeakReference<Context> context) {
        this.context = context;
        
        if (context.get() == null) {
			throw new RuntimeException("null cannot be cast to non-null type android.hardware.usb.UsbManager");
		} else {
			usbManager = (UsbManager)context.get().getSystemService(Context.USB_SERVICE);

			IntentFilter permissionFilter = new IntentFilter(ACTION_USB_PERMISSION);
            context.get().registerReceiver(usbPermissionReceiver, permissionFilter);
        }
    }

    public void addUsbDeviceManagerListener(@NonNull UsbDeviceManagerListener listener) {
        listeners.add(listener);
        if (listeners.size() == 1 && context.get() != null) {
			IntentFilter usbActionFilter = new IntentFilter(UsbManager.ACTION_USB_DEVICE_ATTACHED);
			usbActionFilter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
			context.get().registerReceiver(usbActionReceiver, usbActionFilter);
        }
    }

    public void removeUsbDeviceManagerListener(@NonNull UsbDeviceManagerListener listener) {
        listeners.remove(listener);
        if (listeners.size() == 0 && context.get() != null) {
			context.get().unregisterReceiver(usbActionReceiver);
        }
    }

	@NonNull
    public Map<String, UsbDevice> getDevices() {
        return usbManager.getDeviceList();
    }

	@SuppressLint("UnspecifiedImmutableFlag")
    public void connectToDevice(@NonNull UsbDevice device) {
        PendingIntent permissionIntent = PendingIntent.getBroadcast(context.get(), 0, new Intent(ACTION_USB_PERMISSION), 0);
        if (usbManager.hasPermission(device)) {
            connectToDeviceInternal(device);
        } else {
            usbManager.requestPermission(device, permissionIntent);
        }
    }

    public void connectToDeviceInternal(UsbDevice device) {
        UsbDeviceConnection connection = usbManager.openDevice(device);
        for (UsbDeviceManagerListener listener : listeners) {
            if (listener.filterDevice(device)) {
                listener.onUsbDeviceConnected(device, connection);
            }
        }
    }
}

