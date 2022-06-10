package com.rv882.fastbootjava;

import android.content.Context;

import java.lang.ref.WeakReference;

class FastbootMobile {
	private static WeakReference<Context> applicationContext;

    public static final synchronized void initialize(final Context context) {
        applicationContext = new WeakReference<Context>(context);
    }

    public static final Context getApplicationContext() {
		if (applicationContext.get() == null) {
            throw new RuntimeException("FastbootMobile not initialized.");
        }
        return applicationContext.get();
    }
}

