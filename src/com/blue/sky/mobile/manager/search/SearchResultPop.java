package com.blue.sky.mobile.manager.search;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.PopupWindow;
import com.blue.sky.mobile.manager.R;
import com.blue.sky.mobile.manager.file.FileListAdapter;

/**
 * Created by Administrator on 2010/12/20.
 */
public class SearchResultPop {

    private PopupWindow popupWindow;
    private FileListAdapter adapter;

    public SearchResultPop(Context context, final FileListAdapter adapter){

        this.adapter = adapter;

        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.search_result_pop, null);

        GridView gridView = (GridView) view.findViewById(R.id.gridView);
        gridView.setAdapter(adapter);
        gridView.requestFocus();
        // EditText 与 popwindow焦点冲突
//        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
//                popupWindow.setFocusable(true);
//                listener.onClick((FileInfo)adapter.getItem(arg2), arg2);
//            }
//        });

        popupWindow = new PopupWindow(view,
                windowManager.getDefaultDisplay().getWidth(),
                windowManager.getDefaultDisplay().getHeight()*4/5);

        // 使其聚集
        //popupWindow.setFocusable(true);
        // 解决popupWindow挡住虚拟键盘
        popupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        // 设置允许在外点击消失
        popupWindow.setOutsideTouchable(false);
        // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
    }

    public void showSearchPopWindow( View parent) {

        int[] location = new int[2];
        //location [0]--->x坐标,location [1]--->y坐标
        parent.getLocationInWindow(location);

        popupWindow.showAsDropDown(parent, location[0], 0);
    }

    public void setFocus(boolean isFocus) {
        if(popupWindow.isShowing()){
            popupWindow.setFocusable(isFocus);
        }
    }

    public void close() {
        if(popupWindow.isShowing()){
            popupWindow.dismiss();
        }
    }
}
