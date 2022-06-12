package com.rv882.fastbootjava;

import androidx.annotation.NonNull;

public class FastbootCommand {
	@NonNull
	public static FastbootCommand oem(@NonNull String arg) {
		return command(String.format("oem %s", arg));
	}
	
	@NonNull
	public static FastbootCommand download(@NonNull String data) {
		return command(String.format("download:%s", data));
	}
	
	@NonNull
	public static FastbootCommand getVar(@NonNull String variable) {
		return command(String.format("getvar:%s", variable));
	}
	
	@NonNull
	public static FastbootCommand setActiveSlot(@NonNull String slot) {
		return command(String.format("set_active:%s", slot));
	}
	
	@NonNull
	public static FastbootCommand reboot() {
		return command("reboot");
	}
	
	@NonNull
	public static FastbootCommand rebootBootloader() {
		return command("reboot-bootloader");
	}
	
	@NonNull
	public static FastbootCommand powerDown() {
		return command("powerdown");
	}

	@NonNull
	public static FastbootCommand continueBooting() {
		return command("continue");
	}
	
	@NonNull
	public static FastbootCommand command(@NonNull String command) {
		return new FastbootCommand(command);
	}
	
	@NonNull
    private String command;
	
    private FastbootCommand(String command) {
        this.command = command;
    }

	@NonNull
    @Override
    public String toString() {
        return command;
    }
}

