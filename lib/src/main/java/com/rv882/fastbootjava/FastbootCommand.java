package com.rv882.fastbootjava;

public class FastbootCommand {
	public final FastbootCommand oem(final String arg) {
		return command(String.format("oem %s", arg));
	}
	
	public final FastbootCommand download(final String data) {
		return command(String.format("download:%s", data));
	}
	
	public final FastbootCommand getVar(final String variable) {
		return command(String.format("getvar:%s", variable));
	}
	
	public final FastbootCommand setActiveSlot(final String slot) {
		return command(String.format("set_active:%s", slot));
	}
	
	public final FastbootCommand reboot() {
		return command("reboot");
	}
	
	public final FastbootCommand rebootBootloader() {
		return command("reboot-bootloader");
	}
	
	public final FastbootCommand powerDown() {
		return command("powerdown");
	}

	public final FastbootCommand continueBooting() {
		return command("continue");
	}
	
	public FastbootCommand command(String command) {
		return new FastbootCommand(command);
	}
	
    private final String command;
    private FastbootCommand(final String command) {
        this.command = command;
    }

    @Override
    public String toString() {
        return command;
    }
}

