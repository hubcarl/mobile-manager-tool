package com.blue.sky.mobile.manager.video.common;

import android.app.Activity;
import android.content.Intent;
import com.blue.sky.mobile.manager.R;
import io.vov.vitamio.Vitamio;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public final class LibsChecker {
	public static final String FROM_ME = "fromVitamioInitActivity";

	//
	//	public static final boolean checkVitamioLibs(Activity ctx, int msgID, int rawID) {
	//		new Vitamio().
	//		if (!Vitamio.isInitialized(ctx) && !ctx.getIntent().getBooleanExtra(FROM_ME, false)) {
	//			Intent i = new Intent();
	//			i.setClassName(Vitamio.getCompatiblePackage(), "com.nmbb.oplayer.ui.vitamio.InitActivity");
	//			i.putExtras(ctx.getIntent());
	//			i.setData(ctx.getIntent().getData());
	//			i.putExtra("package", ctx.getApplicationInfo().packageName);
	//			i.putExtra("className", ctx.getClass().getName());
	//			i.putExtra("EXTRA_MSG", msgID);
	//			i.putExtra("EXTRA_FILE", rawID);
	//			ctx.startActivity(i);
	//			ctx.finish();
	//			return false;
	//		}
	//		return true;
	//	}



	public static final boolean checkVitamioLibs(Activity ctx, int msgID) {
		if ((!Vitamio.isInitialized(ctx)) && (!ctx.getIntent().getBooleanExtra("fromVitamioInitActivity", false))) {
			Intent i = new Intent();
			i.setClassName(ctx.getPackageName(), "com.blue.sky.mobile.manager.video.common.InitActivity");//io.vov.vitamio.activity.InitActivity
			i.putExtras(ctx.getIntent());
			i.setData(ctx.getIntent().getData());
			i.putExtra("package", ctx.getPackageName());
			i.putExtra("className", ctx.getClass().getName());
			i.putExtra("EXTRA_MSG", msgID);
			ctx.startActivity(i);
			ctx.finish();
			return false;
		}
		return true;
	}
}
