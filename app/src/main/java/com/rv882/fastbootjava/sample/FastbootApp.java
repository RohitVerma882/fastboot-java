package com.rv882.fastbootjava.sample;

import android.app.Application;

import java.io.File;
import java.io.IOException;

import com.google.common.io.Files;
import com.google.common.base.Charsets;
import com.google.common.base.Throwables;

public class FastbootApp extends Application {
    private final Thread.UncaughtExceptionHandler oldHandler = Thread.getDefaultUncaughtExceptionHandler();

	@Override
	public void onCreate() {
		super.onCreate();
		
		// Crash handler
		setupCrashHandler();
	}
	
	private void setupCrashHandler() {
		Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
				@Override
				public void uncaughtException(Thread p1, Throwable p2) {
					try {
						String stackTraceString = Throwables.getStackTraceAsString(p2);
						File file = new File(getExternalFilesDir(null), "last_crash_log.txt");
						Files.createParentDirs(file);
						Files.write(stackTraceString, file, Charsets.UTF_8);
					} catch (IOException e) {
						// ignore
					}
					if (oldHandler != null) {
						oldHandler.uncaughtException(p1, p2);
					}
					killCurrentProcess();
				}
			});
	}
	
	private static void killCurrentProcess() {
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(10);
    }
}
