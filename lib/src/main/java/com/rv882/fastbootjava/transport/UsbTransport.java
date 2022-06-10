package com.rv882.fastbootjava.transport;

import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbConstants;

import androidx.annotation.NonNull;

public class UsbTransport implements Transport {
	@NonNull
	private UsbInterface fastbootInterface;
	@NonNull
    private UsbDeviceConnection connection;
    private boolean isConnected = false;
    private UsbEndpoint inEndpoint;
    private UsbEndpoint outEndpoint;

	public UsbTransport(@NonNull UsbInterface fastbootInterface, @NonNull UsbDeviceConnection connection) {
        this.fastbootInterface = fastbootInterface;
        this.connection = connection;

        for (int i = 0; i < fastbootInterface.getEndpointCount(); ++i) {
            UsbEndpoint e1 = fastbootInterface.getEndpoint(i);
            if (e1.getDirection() == UsbConstants.USB_DIR_IN) {
                inEndpoint = e1;
            } else {
                outEndpoint = e1;
            }
        }
		
        if (inEndpoint == null) {
            throw new RuntimeException("No endpoint found for input.");
        }
        if (outEndpoint == null) {
            throw new RuntimeException("No endpoint found for output.");
        }
    }

	@Override
	public boolean isConnected() {
		return isConnected;
	}

	@Override
	public void setConnected(boolean isConnected) {
		this.isConnected = isConnected;
	}

	@Override
	public void connect(boolean force) {
		connection.claimInterface(fastbootInterface, force);
        setConnected(true);
	}

	@Override
	public void disconnect() {
		if (!isConnected()) {
            return;
        }
        connection.releaseInterface(fastbootInterface);
        setConnected(false);
	}

	@Override
	public void close() {
		disconnect();
        connection.close();
	}

	@Override
	public void send(@NonNull byte[] buffer, int timeout) {
		connection.bulkTransfer(outEndpoint, buffer, buffer.length, timeout);
	}

	@Override
	public void receive(@NonNull byte[] buffer, int timeout) {
		connection.bulkTransfer(inEndpoint, buffer, buffer.length, timeout);
	}
}
