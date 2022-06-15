package com.rv882.fastbootjava;

import java.nio.charset.StandardCharsets;

import androidx.annotation.NonNull;

import com.rv882.fastbootjava.transport.Transport;

public class FastbootDeviceContext {
	private static final int DEFAULT_TIMEOUT = 1000;

	@NonNull
    private Transport transport;

    public FastbootDeviceContext(@NonNull Transport transport) {
        this.transport = transport;
    }

	@NonNull
	public FastbootResponse sendCommand(@NonNull FastbootCommand command, boolean force) {
		return sendCommand(command, DEFAULT_TIMEOUT, force);
	}

	@NonNull
    public FastbootResponse sendCommand(@NonNull FastbootCommand command, int timeout, boolean force) {
        String commandStr = command.toString();
        byte[] bytes = commandStr.getBytes(StandardCharsets.UTF_8);
        return sendCommand(bytes, timeout, force);
    }

	@NonNull
	public FastbootResponse sendCommand(@NonNull byte[] bytes, boolean force) {
		return sendCommand(bytes, DEFAULT_TIMEOUT, force);
	}

	@NonNull
    public FastbootResponse sendCommand(@NonNull byte[] bytes, int timeout, boolean force) {
        if (!transport.isConnected()) {
            transport.connect(force);
        }
        transport.send(bytes, timeout);
        byte[] responseBytes = new byte[64];
        transport.receive(responseBytes, timeout);
        return FastbootResponse.fromBytes(responseBytes);
    }

    public void close() {
        transport.disconnect();
        transport.close();
    }
}


