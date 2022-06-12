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
    public static FastbootResponse fromBytes(@NonNull byte[] arr) {
        return fromString(new String(arr, StandardCharsets.UTF_8));
    }

	@NonNull
    public static FastbootResponse fromString(@NonNull String str) {
		return new FastbootResponse(ResponseStatus.valueOf(str.substring(0, 4)), str.substring(4));
	}

	public static enum ResponseStatus {
        INFO("INFO"),
        FAIL("FAIL"),
        OKAY("OKAY"),
        DATA("DATA");

		@NonNull
		private String text;
		
        private ResponseStatus(String text) {
            this.text = text;
        }

		@NonNull
        public String getText() {
            return text;
        }
    }
}
