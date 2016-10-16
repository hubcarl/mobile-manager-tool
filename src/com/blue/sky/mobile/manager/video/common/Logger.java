package com.blue.sky.mobile.manager.video.common;

import android.util.Log;

public class Logger {

	private static boolean isLog = true;
	private static final String TAG = "MobileManagerPlayer";

	public static void setLog(boolean isLog) {
		Logger.isLog = isLog;
	}

	public static boolean getIsLog() {
		return isLog;
	}

	public static void d(String tag, String msg) {
		if (isLog) {
			Log.d(tag, msg);
		}
	}

	public static void d(String msg) {
		Log.d(TAG, msg);
	}

	/**
	 * @param tag Used to identify the source of a log message. It usually
	 * identifies the class or activity where the log call occurs.
	 * @param msg The message you would like logged.
	 * @param tr An exception to log
	 */
	public static void d(String tag, String msg, Throwable tr) {
		if (isLog) {
			Log.d(tag, msg, tr);
		}
	}

	public static void e(Throwable tr) {
		if (isLog) {
			Log.e(TAG, "", tr);
		}
	}

	public static void i(String msg) {
		if (isLog) {
			Log.i(TAG, msg);
		}
	}
	
	public static void i(String tag, String msg) {
		if (isLog) {
			Log.i(tag, msg);
		}
	}

	/**
	 * @param tag Used to identify the source of a log message. It usually
	 * identifies the class or activity where the log call occurs.
	 * @param msg The message you would like logged.
	 * @param tr An exception to log
	 */
	public static void i(String tag, String msg, Throwable tr) {
		if (isLog) {
			Log.i(tag, msg, tr);
		}

	}

	/**
	 * @param tag Used to identify the source of a log message. It usually
	 * identifies the class or activity where the log call occurs.
	 * @param msg The message you would like logged.
	 */
	public static void e(String tag, String msg) {
		if (isLog) {
			Log.e(tag, msg);
		}
	}

	public static void e(String msg) {
		if (isLog) {
			Log.e(TAG, msg);
		}
	}

	/**
	 * @param tag Used to identify the source of a log message. It usually
	 * identifies the class or activity where the log call occurs.
	 * @param msg The message you would like logged.
	 * @param tr An exception to log
	 */
	public static void e(String tag, String msg, Throwable tr) {
		if (isLog) {
			Log.e(tag, msg, tr);
		}
	}

	public static void systemErr(String msg) {
		// if (true) {
		if (isLog) {
			if (msg != null) {
				Log.e(TAG, msg);
			}

		}
	}

}
