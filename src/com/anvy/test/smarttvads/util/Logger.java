package com.anvy.test.smarttvads.util;

import android.util.Log;

import com.anvy.test.smarttvads.BuildConfig;

public class Logger {

	public static final void log(String tag, String msg) {
		if (BuildConfig.DEBUG) {
			Log.d(tag, msg);
		}
	}
}
