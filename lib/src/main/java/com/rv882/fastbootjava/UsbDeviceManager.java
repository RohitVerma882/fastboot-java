package com.rv882.fastbootjava;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.BroadcastReceiver;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Map;

import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;

public class UsbDeviceManager {
	private static final String ACTION_USB_PERMISSION = "com.rv882.fastbootjava.USB_PERMISSION";

    private final WeakReference<Context> context;
    private final ArrayList<UsbDeviceManagerListener> listeners;
    private UsbManager usbManager;

	private BroadcastReceiver usbActionReceiver = new BroadcastReceiver() {
		@Override
        public void onReceive(Context context, Intent intent) {
            if (intent == null) return;
			switch (intent.getAction()) {
				case UsbManager.ACTION_USB_DEVICE_ATTACHED:
					for (UsbDeviceManagerListener listener : listeners) {
						UsbDevice device = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
						if (listener.filterDevice(device)) listener.onUsbDeviceAttached(device);
					}
					break;
				case UsbManager.ACTION_USB_DEVICE_DETACHED:
					for (UsbDeviceManagerListener listener : listeners) {
						UsbDevice device = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
						if (listener.filterDevice(device)) listener.onUsbDeviceDetached(device);
					}
					break;
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

    public UsbDeviceManager(final WeakReference<Context> context) {
        this.context = context;
        listeners = new ArrayList<UsbDeviceManagerListener>();

        if (context.get() == null) {
			throw new RuntimeException("null cannot be cast to non-null type android.hardware.usb.UsbManager");
		} else {
			usbManager = (UsbManager)context.get().getSystemService(Context.USB_SERVICE);

			IntentFilter permissionFilter = new IntentFilter(ACTION_USB_PERMISSION);
            context.get().registerReceiver(usbPermissionReceiver, permissionFilter);
        }
    }

    public final void addUsbDeviceManagerListener(final UsbDeviceManagerListener listener) {
        listeners.add(listener);
        if (listeners.size() == 1) {
            final IntentFilter usbActionFilter = new IntentFilter(UsbManager.ACTION_USB_DEVICE_ATTACHED);
            usbActionFilter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
            if (context.get() != null) {
                context.get().registerReceiver(usbActionReceiver, usbActionFilter);
            }
        }
    }

    public final void removeUsbDeviceManagerListener(final UsbDeviceManagerListener listener) {
        listeners.remove(listener);
        if (listeners.size() == 0) {
            if (context.get() != null) {
                context.get().unregisterReceiver(usbActionReceiver);
            }
        }
    }

    public final Map<String, UsbDevice> getDevices() {
        return usbManager.getDeviceList();
    }

    public final void connectToDevice(final UsbDevice device) {
        final PendingIntent permissionIntent = PendingIntent.getBroadcast(context.get(), 0, new Intent(ACTION_USB_PERMISSION), 0);
        if (usbManager.hasPermission(device)) {
            connectToDeviceInternal(device);
        } else {
            usbManager.requestPermission(device, permissionIntent);
        }
    }

    private final void connectToDeviceInternal(final UsbDevice device) {
        final UsbDeviceConnection connection = usbManager.openDevice(device);
        for (final UsbDeviceManagerListener listener : listeners) {
            if (listener.filterDevice(device)) {
                listener.onUsbDeviceConnected(device, connection);
            }
        }
    }
}

