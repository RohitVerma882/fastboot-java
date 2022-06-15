package com.rv882.fastbootjava;

import android.content.ContentProvider;
import android.net.Uri;
import android.database.Cursor;
import android.content.ContentValues;

public class FastbootInitProvider extends ContentProvider {
	
	@Override
	public boolean onCreate() {
		try {
            if (getContext() == null) throw new Exception("Failed to get context.");
            FastbootJava.initialize(getContext());
        } catch (Exception ex) {
            
        }
		return false;
	}

	@Override
	public Cursor query(Uri p1, String[] p2, String p3, String[] p4, String p5) {
		return null;
	}

	@Override
	public String getType(Uri p1) {
		return null;
	}

	@Override
	public Uri insert(Uri p1, ContentValues p2) {
		return null;
	}

	@Override
	public int delete(Uri p1, String p2, String[] p3) {
		return 0;
	}

	@Override
	public int update(Uri p1, ContentValues p2, String p3, String[] p4) {
		return 0;
	}
}
