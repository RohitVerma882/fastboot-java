package com.rv882.fastbootjava;

import java.nio.charset.StandardCharsets;

import androidx.annotation.NonNull;

public class FastbootResponse {
	@NonNull
    private static ResponseStatus status;
	@NonNull
    private static String data;
	
	public FastbootResponse(@NonNull ResponseStatus status, @NonNull String data) {
        this.status = status;
        this.data = data;
    }

	@NonNull
    public static ResponseStatus getStatus() {
        return status;
    }

	@NonNull
    public static String getData() {
        return data;
    }

	@NonNull
    public static FastbootResponse fromBytes(@NonNull byte[] bytes) {
        return fromString(new String(bytes, StandardCharsets.UTF_8));
    }

	@NonNull
    public static FastbootResponse fromString(@NonNull String str) {
		return new FastbootResponse(ResponseStatus.fromString(str.substring(0, 4)), str.substring(4));
	}

	public static enum ResponseStatus {
        INFO("INFO"),
        FAIL("FAIL"),
        OKAY("OKAY"),
        DATA("DATA"),
		UNKNOWN("UNKNOWN");

		@NonNull
		private String text;
		
        private ResponseStatus(String text) {
            this.text = text;
        }
		
		@NonNull
		private static ResponseStatus fromString(String text) {
            if (text == null) {
                return UNKNOWN;
            }
            for (ResponseStatus s : ResponseStatus.values()) {
                if (text.equals(s.toString())) {
                    return s;
                }
            }
            return UNKNOWN;
        }

		@NonNull
        public String getText() {
            return text;
        }
    }
}
