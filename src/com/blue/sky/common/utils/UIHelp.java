package com.blue.sky.common.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.os.Handler;
import android.text.Html;
import android.text.format.DateUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.blue.sky.common.ui.DialogListener;
import com.blue.sky.control.dialog.MenuBarPopupWindow;
import com.blue.sky.mobile.manager.R;

/**
 * UI界面帮助类
 *
 */
public class UIHelp
{

    public final static String TAG = "UIHelp";

    /**
     * 默认时间LENGTH_LONG
     *
     * @param msg
     */
    public static void showToast(Context context,String msg) {

        showToast(context, msg, Toast.LENGTH_LONG);
    }

    /**
     * 不会一直重复重复重复重复的提醒了
     *
     * @param msg
     * @param length
     *            显示时间
     */
    protected static void showToast(Context context, String msg, int length) {
        Toast toast = Toast.makeText(context, msg, length);
        toast.show();
    }

    private static void setParams(LayoutParams lay, Activity context)
    {
        DisplayMetrics dm = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);
        Rect rect = new Rect();
        View view = context.getWindow().getDecorView();
        view.getWindowVisibleDisplayFrame(rect);
        lay.height = dm.heightPixels - rect.top;
        lay.width = dm.widthPixels;
    }

    /*
     * ChenRen/2013-07-06
     */
    private static ProgressDialog progressDialog;

    /** 默认显示"数据加载中..." */
    public static void showLoading(Context context)
    {
        showLoading(context, "数据加载中...");
    }

    /**
     *
     * @param context
     * @param message
     *            如果不传该参数, 默认显示"数据加载中...". 见: {@link #showLoading(android.content.Context)}
     */
    public static void showLoading(Context context, String message)
    {
        // 隐藏键盘
        ((Activity) context).getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        progressDialog = new ProgressDialog(context);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage(message);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(true);
        progressDialog.show();
    }

    public static void closeLoading()
    {
        if (progressDialog != null)
        {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    /**
     * 发送App异常崩溃报告
     *
     * @param cont
     * @param crashReport
     */
    public static void sendAppCrashReport(final Context cont, final String crashReport)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(cont);
        builder.setIcon(android.R.drawable.ic_dialog_info);
        builder.setTitle("应用程序错误");
        builder.setMessage("很抱歉，应用程序出现错误，即将退出。\n请提交错误报告，我们会尽快修复这个问题！");
        builder.setPositiveButton("提交报告", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(0);
            }
        });
        builder.setNegativeButton("确定", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
                // 退出
                // AppManager.getAppManager().AppExit(cont);
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(0);
            }
        });
        builder.show();
    }

    public static void hideSoftInputFromWindow(EditText editText)
    {
        InputMethodManager imm = (InputMethodManager) editText.getContext().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    public static void showSoftInputFromWindow(EditText editText)
    {
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        InputMethodManager inputManager = (InputMethodManager) editText.getContext().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(editText, 0);
    }

    /**
     * ListView滑动到底部
     *
     * @param listView
     *
     */
    public static void listViewScrollToBottom(final ListView listView)
    {
        if (listView != null && listView.getAdapter() != null)
        {
            listViewSetSelection(listView, listView.getAdapter().getCount() - 1);
        }
    }

    /**
     * ListView滑动到指定Position
     *
     * @param listView
     * @param position
     */
    public static void listViewSetSelection(final ListView listView, final int position)
    {
        if (listView != null)
        {
            new Handler().postDelayed(new Runnable()
            {
                public void run()
                {
                    listView.setSelection(position);
                }
            }, 300);
        }
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue)
    {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue)
    {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue
     * @return
     */
    public static int px2sp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (spValue * scale + 0.5f);
    }

    public static String getPullRefreshLabel(Context context){
        return "更新于" + DateUtils.formatDateTime(context, System.currentTimeMillis(),
                DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
    }

    public static void  setHeaderView(final Activity activity, String title, boolean isBack)
    {

        ((TextView)activity.findViewById(R.id.commonTitle)).setText(title);
        if(isBack){
            TextView leftButton = ((TextView)activity.findViewById(R.id.leftButton));
            leftButton.setVisibility(View.VISIBLE);
            leftButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    activity.finish();
                }
            });
        }
    }

    public static void  setHeaderView(final Activity activity, String title, boolean isBack, String rightText, int resId, View.OnClickListener listener){
        setHeaderView(activity,title,isBack);

        TextView rightButton = ((TextView)activity.findViewById(R.id.rightButton));
        if(Strings.isNotEmpty(rightText)){
            rightButton.setText(rightText);
            rightButton.setVisibility(View.VISIBLE);
        }
        if(resId>0){
            rightButton.setBackgroundResource(resId);
            rightButton.setVisibility(View.VISIBLE);
        }

        if(listener!=null){
            rightButton.setOnClickListener(listener);
        }
    }

    public static void  setHeaderMenuView(final Activity activity, String title){

        setHeaderView(activity,title, true);

        final TextView rightButton = ((TextView)activity.findViewById(R.id.rightButton));
        rightButton.setBackgroundResource(R.drawable.actionbar_icon_more_white);
        rightButton.setVisibility(View.VISIBLE);
        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MenuBarPopupWindow menuWindow = new MenuBarPopupWindow(activity, null);
                // 计算坐标的偏移量
                int xOffInPixels = menuWindow.getWidth() - rightButton.getWidth() + 10;
                menuWindow.showAsDropDown(rightButton, -xOffInPixels, 0);
            }
        });
    }

    public static void confirmDialog(Context context,String title, String description, String confirmText, String cancelText, boolean cancelShow, final DialogListener listener) {
        final Dialog dialog = new Dialog(context, R.style.exit_dialog);
        dialog.setContentView(R.layout.sky_confirm_dialog);

        TextView txtTitle = (TextView)dialog.findViewById(R.id.title);
        txtTitle.setText(title);

        TextView txtDescription = (TextView)dialog.findViewById(R.id.description);
        //滚动
        txtDescription.setMovementMethod(ScrollingMovementMethod.getInstance());
        txtDescription.setText(Html.fromHtml(description));

        TextView btnConfirm = (TextView) dialog.findViewById(R.id.btn_confirm);
        btnConfirm.setText(confirmText);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onConfirm(dialog, v);
            }
        });

        TextView btnCancel = (TextView) dialog.findViewById(R.id.btn_cancel);
        btnCancel.setText(cancelText);
        btnCancel.setVisibility(cancelShow ? View.VISIBLE : View.GONE);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onCancel(dialog, v);
            }
        });
        dialog.show();
    }
}
