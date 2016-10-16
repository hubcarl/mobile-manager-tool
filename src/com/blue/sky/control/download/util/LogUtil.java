package com.blue.sky.control.download.util;

import android.util.Log;

/**
 * 日志管理器
 * @author fengbingbing
 *
 */
public class LogUtil {
	/**
	 * 日志级别
	 */
	public static final boolean DEBUG = true;	//开发模式
//	public static final boolean DEBUG = false;	//发布模式

	public static void v(String tag, String msg) {
		if (DEBUG) {
			Log.v(tag, msg);
		}
	}

	public static void d(String tag, String msg) {
		if (DEBUG) {
			Log.d(tag, msg);
		}
	}

	public static void i(String tag, String msg) {
		if (DEBUG) {
			Log.i(tag, msg);
		}
	}

	public static void w(String tag, String msg) {
		if (DEBUG) {
			Log.w(tag, msg);
		}
	}

	public static void e(String tag, String msg) {
		if (DEBUG) {
			Log.e(tag, msg);
		}
	}
}
