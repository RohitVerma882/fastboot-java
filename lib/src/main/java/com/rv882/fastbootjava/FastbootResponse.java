package com.rv882.fastbootjava;

import java.nio.charset.StandardCharsets;

import androidx.annotation.NonNull;

public class FastbootResponse {
	@NonNull
    private ResponseStatus status;
	@NonNull
    private String data;

	public FastbootResponse(@NonNull ResponseStatus status, @NonNull String data) {
        this.status = status;
        this.data = data;
    }

	@NonNull
    public ResponseStatus getStatus() {
        return status;
    }

	@NonNull
    public String getData() {
        return data;
    }

	@NonNull
    public static FastbootResponse fromBytes(@NonNull byte[] arr) {
        return fromString(new String(arr, StandardCharsets.UTF_8));
    }

	@NonNull
    public static FastbootResponse fromString(@NonNull String str) {
		String substring = str.substring(0, 4);
		ResponseStatus value = ResponseStatus.valueOf(substring);
		String substring2 = str.substring(4);
		return new FastbootResponse(value, substring2);
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
