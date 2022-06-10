package com.rv882.fastbootjava;

public final class FastbootCommand {
	public final FastbootCommand oem(final String arg) {
		return new FastbootCommand("oem " + arg);
	}
	
	public final FastbootCommand verify(final String data) {
		return new FastbootCommand("verify:" + data);
	}
	
	public final FastbootCommand flash(final String mode) {
		return new FastbootCommand("flash:" + mode);
	}
	
	public final FastbootCommand download(final String data) {
		return new FastbootCommand("download:" + data);
	}
	
	public final FastbootCommand getVar(final String variable) {
		return new FastbootCommand("getvar:" + variable);
	}
	
	public final FastbootCommand setActiveSlot(final String slot) {
		return new FastbootCommand("set_active:" + slot);
	}
	
	public final FastbootCommand reboot() {
		return new FastbootCommand("reboot");
	}
	
	public final FastbootCommand rebootBootloader() {
		return new FastbootCommand("reboot-bootloader");
	}
	
	public final FastbootCommand powerDown() {
		return new FastbootCommand("powerdown");
	}

	public final FastbootCommand continueBooting() {
		return new FastbootCommand("continue");
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

