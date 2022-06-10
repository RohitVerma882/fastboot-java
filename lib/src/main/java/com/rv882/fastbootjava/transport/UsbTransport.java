package com.rv882.fastbootjava.transport;

import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbDeviceConnection;

class UsbTransport implements Transport {
	private final UsbInterface fastbootInterface;
    private final UsbDeviceConnection connection;
    private boolean isConnected;
    private UsbEndpoint inEndpoint;
    private UsbEndpoint outEndpoint;

	public UsbTransport(final UsbInterface fastbootInterface, final UsbDeviceConnection connection) {
        this.fastbootInterface = fastbootInterface;
        this.connection = connection;

        for (int i = 0; i < this.fastbootInterface.getEndpointCount(); ++i) {
            final UsbEndpoint e1 = this.fastbootInterface.getEndpoint(i);
            if (e1.getDirection() == 128) {
                this.inEndpoint = e1;
            } else {
                this.outEndpoint = e1;
            }
        }
        if (this.inEndpoint == null) {
            throw new RuntimeException("No endpoint found for input.");
        }
        if (this.outEndpoint == null) {
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
	public void send(byte[] buffer, int timeout) {
		connection.bulkTransfer(outEndpoint, buffer, buffer.length, timeout);
	}

	@Override
	public void receive(byte[] buffer, int timeout) {
		connection.bulkTransfer(inEndpoint, buffer, buffer.length, timeout);
	}
}
