package com.rv882.fastbootjava;

import java.nio.charset.StandardCharsets;

import com.rv882.fastbootjava.transport.Transport;

class FastbootDeviceContext {
	private static final int DEFAULT_TIMEOUT = 1000;

    private final Transport transport;
	
	public FastbootDeviceContext newDeviceContext(Transport transport) {
		return new FastbootDeviceContext(transport);
	}

    public FastbootDeviceContext(final Transport transport) {
        this.transport = transport;
    }
	
	public final FastbootResponse sendCommand(final FastbootCommand command, final boolean force) {
		return sendCommand(command, DEFAULT_TIMEOUT, force);
	}

    public final FastbootResponse sendCommand(final FastbootCommand command, final int timeout, final boolean force) {
        if (!transport.isConnected()) {
            transport.connect(force);
        }
        final String commandStr = command.toString();
        final Transport transport = this.transport;
        final byte[] bytes = commandStr.getBytes(StandardCharsets.UTF_8);
        transport.send(bytes, timeout);
        final byte[] responseBytes = new byte[64];
        this.transport.receive(responseBytes, timeout);
        return FastbootResponse.fromBytes(responseBytes);
    }

    public final void close() {
        transport.disconnect();
        transport.close();
    }
}

