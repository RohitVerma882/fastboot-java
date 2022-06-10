package com.rv882.fastbootjava;

import java.nio.charset.StandardCharsets;

public class FastbootResponse {
    private final ResponseStatus status;
    private final String data;

	public FastbootResponse(final ResponseStatus status, final String data) {
        this.status = status;
        this.data = data;
    }

    public final ResponseStatus getStatus() {
        return this.status;
    }

    public final String getData() {
        return this.data;
    }

    public static final FastbootResponse fromBytes(final byte[] arr) {
        return fromString(new String(arr, StandardCharsets.UTF_8));
    }

    public static final FastbootResponse fromString(final String str) {
		final String substring = str.substring(0, 4);
		final ResponseStatus value = ResponseStatus.valueOf(substring);
		final String substring2 = str.substring(4);
		return new FastbootResponse(value, substring2);
	}

	public static enum ResponseStatus {
        INFO("INFO"),
        FAIL("FAIL"),
        OKAY("OKAY"),
        DATA("DATA");

		private final String text;
        private ResponseStatus(final String text) {
            this.text = text;
        }

        public final String getText() {
            return this.text;
        }
    }
}
