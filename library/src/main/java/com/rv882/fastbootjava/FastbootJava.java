package com.rv882.fastbootjava;

import android.content.Context;

import java.lang.ref.WeakReference;

import androidx.annotation.NonNull;

public class FastbootJava {
	private static WeakReference<Context> applicationContext;

    public static synchronized void initialize(Context context) {
        applicationContext = new WeakReference<Context>(context);
    }

	@NonNull
    public static Context getApplicationContext() {
		if (applicationContext.get() == null) {
            throw new RuntimeException("FastbootJava not initialized.");
        }
        return applicationContext.get();
    }
}

