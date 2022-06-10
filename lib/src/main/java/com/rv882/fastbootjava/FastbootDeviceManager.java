package com.rv882.fastbootjava;

import java.util.HashMap;
import java.util.ArrayList;
import java.lang.ref.WeakReference;
import android.content.Context;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbDevice;
import com.rv882.fastbootjava.transport.UsbTransport;
import android.hardware.usb.UsbInterface;
import com.rv882.fastbootjava.transport.Transport;
import java.util.List;
import java.util.Set;
import java.util.Collections;
import java.util.Arrays;
import android.util.Pair;
import java.util.Map;

public class FastbootDeviceManager {
    private static final int USB_CLASS = 0xff;
    private static final int USB_SUBCLASS = 0x42;
    private static final int USB_PROTOCOL = 0x03;

    private static final HashMap<String, FastbootDeviceContext> connectedDevices = new HashMap<String, FastbootDeviceContext>();
    private static final UsbDeviceManager usbDeviceManager = new UsbDeviceManager(new WeakReference<Context>(FastbootMobile.getApplicationContext()));
    private static final ArrayList<FastbootDeviceManagerListener> listeners = new ArrayList<FastbootDeviceManagerListener>();

	private static UsbDeviceManagerListener usbDeviceManagerListener = new UsbDeviceManagerListener() {
		@Override
		public boolean filterDevice(UsbDevice device) {
			return filterDevice(device);
		}

		@Override
		public synchronized void onUsbDeviceAttached(final UsbDevice device) {
			for (FastbootDeviceManagerListener listener : listeners) {
				listener.onFastbootDeviceAttached(device.getSerialNumber());
			}
		}

		@Override
		public synchronized void onUsbDeviceDetached(final UsbDevice device) {
			if (connectedDevices.containsKey(device.getSerialNumber())) {
                connectedDevices.get(device.getSerialNumber()).close();
            }
            connectedDevices.remove(device.getSerialNumber());

			for (FastbootDeviceManagerListener listener : listeners) {
				listener.onFastbootDeviceDetached(device.getSerialNumber());
			}
		}

		@Override
		public synchronized void onUsbDeviceConnected(UsbDevice device, UsbDeviceConnection connection) {
			Transport transport = new UsbTransport(findFastbootInterface(device), connection);
			FastbootDeviceContext deviceContext = new FastbootDeviceContext(
				transport);

            connectedDevices.get(device.getSerialNumber()).close();
            connectedDevices.put(device.getSerialNumber(), deviceContext);

			for (FastbootDeviceManagerListener listener : listeners) {
				listener.onFastbootDeviceConnected(device.getSerialNumber(), deviceContext);
			}
		}
    };

    private static final boolean filterDevice(UsbDevice device) {
        if (device.getDeviceClass() == USB_CLASS &&
			device.getDeviceSubclass() == USB_SUBCLASS &&
			device.getDeviceProtocol() == USB_PROTOCOL) {
            return true;
        }
        return findFastbootInterface(device) != null;
    }

	private static final UsbInterface findFastbootInterface(UsbDevice device) {
        for (int i = 0; i < device.getInterfaceCount(); i++) {
            UsbInterface deviceInterface = device.getInterface(i);
            if (deviceInterface.getInterfaceClass() == USB_CLASS &&
				deviceInterface.getInterfaceSubclass() == USB_SUBCLASS &&
				deviceInterface.getInterfaceProtocol() == USB_PROTOCOL) {
                return deviceInterface;
            }
        }
        return null;
    }

    public final synchronized void addFastbootDeviceManagerListener(final FastbootDeviceManagerListener listener) {
        listeners.add(listener);
        if (listeners.size() == 1) {
            usbDeviceManager.addUsbDeviceManagerListener(usbDeviceManagerListener);
        }
    }

    public final synchronized void removeFastbootDeviceManagerListener(final FastbootDeviceManagerListener listener) {
        listeners.remove(listener);
        if (listeners.size() == 0) {
            usbDeviceManager.removeUsbDeviceManagerListener(usbDeviceManagerListener);
        }
    }

	public synchronized void connectToDevice(String deviceId) {
		
	}
	
	public List<String> getAttachedDeviceIds() {
		return null;
	}
	
	public List<String> getConnectedDeviceIds() {
		return null;
	}
	
    public final synchronized Pair<String, FastbootDeviceContext> getDeviceContext(final String deviceId) {
     return null;
	}
}
