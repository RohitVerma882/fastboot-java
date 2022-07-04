package com.rv882.fastbootjava.sample.data;

import androidx.annotation.NonNull;

import com.rv882.fastbootjava.FastbootDeviceContext;
import com.rv882.fastbootjava.FastbootCommand;

public class FastbootDevice {
    @NonNull
    private String deviceId;
	@NonNull
	private String serialNumber;

	private FastbootDevice(@NonNull String deviceId, @NonNull String serialNumber) {
		this.deviceId = deviceId;
		this.serialNumber = serialNumber;
	}

	@NonNull
	public String getDeviceId() {
		return deviceId;
	}

	@NonNull
	public String getSerialNumber() {
		return serialNumber;
	}
	
	@NonNull
	public static FastbootDevice fromDeviceId(@NonNull String deviceId) {
		return new FastbootDevice(deviceId, "");
	}
	
	@NonNull
	public static FastbootDevice fromDeviceContext(@NonNull String deviceId, @NonNull FastbootDeviceContext deviceContext) {
		return new FastbootDevice(deviceId, deviceContext.sendCommand(FastbootCommand.getVar("serialno")).getData());
	}

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof FastbootDevice) {
            if (((FastbootDevice)obj).getDeviceId().equals(deviceId)) {
                return true;
            }
            if (((FastbootDevice)obj).getSerialNumber().equals(serialNumber)) {
                return true;
            }
        }
        return super.equals(obj);
    }
}
