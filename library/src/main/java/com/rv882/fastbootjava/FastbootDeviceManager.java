package com.rv882.fastbootjava;

import android.content.Context;
import android.util.Pair;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Comparator;
import java.util.function.Predicate;
import java.util.function.Function;
import java.util.Map;
import java.util.Set;
import java.util.Collection;
import java.util.function.Consumer;
import java.util.Iterator;

import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbInterface;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.rv882.fastbootjava.transport.Transport;
import com.rv882.fastbootjava.transport.UsbTransport;

public class FastbootDeviceManager {
    private static final int USB_CLASS = 0xff;
    private static final int USB_SUBCLASS = 0x42;
    private static final int USB_PROTOCOL = 0x03;

	@NonNull
    private static HashMap<String, FastbootDeviceContext> connectedDevices = new HashMap<>();
    @NonNull
	private static UsbDeviceManager usbDeviceManager = new UsbDeviceManager(new WeakReference<Context>(FastbootMobile.getApplicationContext()));
    @NonNull
	private static ArrayList<FastbootDeviceManagerListener> listeners = new ArrayList<>();
	@NonNull
    public static FastbootDeviceManager Instance = new FastbootDeviceManager();

	private FastbootDeviceManager() {
	}

	private UsbDeviceManagerListener usbDeviceManagerListener = new UsbDeviceManagerListener() {
		@Override
		public boolean filterDevice(@NonNull UsbDevice device) {
			return Instance.filterDevice(device);
		}

		@Override
		public synchronized void onUsbDeviceAttached(@NonNull UsbDevice device) {
			for (FastbootDeviceManagerListener listener : listeners) {
				listener.onFastbootDeviceAttached(device.getDeviceName());
			}
		}

		@Override
		public synchronized void onUsbDeviceDetached(@NonNull UsbDevice device) {
			FastbootDeviceContext deviceContext;
			if (connectedDevices.containsKey(device.getDeviceName()) && (deviceContext = connectedDevices.get(device.getDeviceName())) != null) {
				deviceContext.close();
			}
			connectedDevices.remove(device.getDeviceName());
			for (FastbootDeviceManagerListener listener : listeners) {
				listener.onFastbootDeviceDetached(device.getDeviceName());
			}
		}

		@Override
		public synchronized void onUsbDeviceConnected(@NonNull UsbDevice device, UsbDeviceConnection connection) {
			FastbootDeviceContext deviceContext0 = connectedDevices.get(device.getDeviceName());
			if (deviceContext0 != null) {
				deviceContext0.close();
			}
			FastbootDeviceContext deviceContext = new FastbootDeviceContext(new UsbTransport(Instance.findFastbootInterface(device), connection));
            connectedDevices.put(device.getDeviceName(), deviceContext);
			for (FastbootDeviceManagerListener listener : listeners) {
				listener.onFastbootDeviceConnected(device.getDeviceName(), deviceContext);
			}
		}
    };

    private boolean filterDevice(UsbDevice device) {
        if (device.getDeviceClass() == USB_CLASS &&
			device.getDeviceSubclass() == USB_SUBCLASS &&
			device.getDeviceProtocol() == USB_PROTOCOL) {
            return true;
        }
        return findFastbootInterface(device) != null;
    }

    public synchronized void addFastbootDeviceManagerListener(@NonNull FastbootDeviceManagerListener listener) {
        listeners.add(listener);
        if (listeners.size() == 1) {
            usbDeviceManager.addUsbDeviceManagerListener(usbDeviceManagerListener);
        }
    }

    public synchronized void removeFastbootDeviceManagerListener(@NonNull FastbootDeviceManagerListener listener) {
        listeners.remove(listener);
        if (listeners.size() == 0) {
            usbDeviceManager.removeUsbDeviceManagerListener(usbDeviceManagerListener);
        }
    }

	public synchronized void connectToDevice(@NonNull String deviceId) {
		List<UsbDevice> devices = usbDeviceManager.getDevices().values().stream()
			.filter(new Predicate<UsbDevice>() {
				@Override
				public boolean test(UsbDevice p1) {
					return filterDevice(p1);
				}
			})
			.collect(Collectors.toList());

		for (UsbDevice device : devices) {
			if (!device.getDeviceName().equals(deviceId)) {
				continue;
			}
			usbDeviceManager.connectToDevice(device);
		}
	}

	@NonNull
	public synchronized List<String> getAttachedDeviceIds() {
		return usbDeviceManager.getDevices().values().stream()
		    .filter(new Predicate<UsbDevice>() {
				@Override
				public boolean test(UsbDevice p1) {
					return filterDevice(p1);
				}
			}).map(new Function<UsbDevice, String>() {
				@Override
				public String apply(UsbDevice p1) {
					return p1.getDeviceName();
				}
			}).collect(Collectors.toList());
	}

	@NonNull
	public synchronized List<String> getConnectedDeviceIds() {
		return connectedDevices.keySet().stream().collect(Collectors.toList());
	}

	@Nullable
    public synchronized Pair<String, FastbootDeviceContext> getDeviceContext(@NonNull String deviceId) {
		Pair<String, FastbootDeviceContext> pair = null;
		for (Map.Entry<String, FastbootDeviceContext> entry : connectedDevices.entrySet()) {
			if (!entry.getKey().equals(deviceId)) {
				continue;
			}
			pair = new Pair<String, FastbootDeviceContext>(entry.getKey(), entry.getValue());
		}
		return pair;
	}

	private UsbInterface findFastbootInterface(UsbDevice device) {
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
}
