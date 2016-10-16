package com.blue.sky.mobile.manager.file;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.*;
import com.blue.sky.common.activity.BaseActivity;
import com.blue.sky.common.component.util.FileUtils;
import com.blue.sky.common.component.util.PackageUtils;
import com.blue.sky.common.utils.Constants;
import com.blue.sky.common.utils.EnumUtil;
import com.blue.sky.common.utils.UIHelp;
import com.blue.sky.mobile.manager.R;
import com.blue.sky.mobile.manager.apk.model.AppInfo;
import com.blue.sky.mobile.manager.apk.utils.AppUtil;
import com.blue.sky.mobile.manager.apk.utils.FileUtil;
import com.blue.sky.mobile.manager.file.common.FileDBHelper;
import com.blue.sky.mobile.manager.file.common.FileInfo;
import com.blue.sky.mobile.manager.file.common.FileType;
import com.blue.sky.mobile.manager.picture.activity.GalleryActivity;
import com.blue.sky.mobile.manager.search.SearchAsyncTask;
import com.blue.sky.mobile.manager.search.SearchResultPop;
import com.blue.sky.mobile.manager.video.common.VideoPlayerActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件管理
 */
public class FileManagerActivity extends BaseActivity implements View.OnClickListener {


    private GridView searchGridView;
    private EditText query;
    private ImageButton clearSearch;

    private SearchResultPop searchResultPop;
    private FileListAdapter fileListAdapter;
    private List<FileInfo> searchFileList = new ArrayList<FileInfo>();


    private GridView gridView;
    private FileListAdapter adapter;
    private List<FileInfo> list = new ArrayList<FileInfo>();
    private LinearLayout fileNav;
    private File root;
    private List<String> navList = new ArrayList<String>();
    private ImageView imageBack;

    private TextView txtAppOpen, txtUnInstall, txtDetail, txtAppInstall, txtAppUpgrade, txtDelete;
    private View popView;
    private WindowManager windowManager;
    private AppInfo selectedAppInfo;

    private ScanSdFilesReceiver scanReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.file_activity_main);
        UIHelp.setHeaderMenuView(this, "文件管理");

        adapter = new FileListAdapter(this, list, new FileListAdapter.ItemListener() {
            @Override
            public void onClick(View v,FileInfo fileInfo) {
                File file = new File(fileInfo.getPath());
                if (file.isDirectory()) {
                    addNav(file, false);
                    listFile(file);
                }else{
                    openFile(v,fileInfo);
                }
            }
        });

        gridView = (GridView) findViewById(R.id.gridView);
        gridView.setAdapter(adapter);

        fileNav = (LinearLayout) findViewById(R.id.fileNav);
        root = Environment.getExternalStorageDirectory();

        addNav(root, true);
        listFile(root);

        imageBack = (ImageView) findViewById(R.id.back);
        imageBack.setOnClickListener(this);

        initPopView();

        //initSearch();
    }

    private void initSearch(){
        // 搜索框
        query = (EditText) findViewById(R.id.txtKeyword);
        query.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        // 搜索框中清除button
        clearSearch = (ImageButton)findViewById(R.id.search_clear);
        query.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //adapter.getFilter().filter(s);
                if (s.length() > 0) {
                    clearSearch.setVisibility(View.VISIBLE);
                } else {
                    clearSearch.setVisibility(View.INVISIBLE);
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void afterTextChanged(Editable s) {
                if (query.getText().toString().length() < 2) {
                    return;
                }
                Log.d(">>>afterTextChanged", query.getText().toString());
                new SearchAsyncTask(new SearchAsyncTask.CallbackResult() {

                    @Override
                    public List<FileInfo> request(String... params) {
                        return FileDBHelper.getFileList(getApplicationContext(), query.getText().toString());
                    }

                    @Override
                    public void response(List<FileInfo> list) {
                        Log.d(">>>list", list.size() + "");
                        if (searchResultPop == null) {
                            searchFileList.addAll(list);
                            fileListAdapter = new FileListAdapter(FileManagerActivity.this, searchFileList, new FileListAdapter.ItemListener() {
                                @Override
                                public void onClick(View view,FileInfo fileInfo) {
                                    fileOptDialog(fileInfo);
                                }
                            });
                            searchResultPop = new SearchResultPop(FileManagerActivity.this, fileListAdapter);
                        } else {
                            searchFileList.clear();
                            searchFileList.addAll(list);
                            fileListAdapter.notifyDataSetChanged();
                        }
                        searchResultPop.showSearchPopWindow(query);
                    }
                }).execute();
            }
        });
        query.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    hideSoftInput();
                    return true;
                }
                return false;
            }
        });

        clearSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                query.getText().clear();
            }
        });
    }


    public void fileOptDialog(final FileInfo fileInfo) {
        final Dialog dialog = new Dialog(this, R.style.exit_dialog);
        dialog.setContentView(R.layout.sky_file_opt_dialog);

        final File file = new File(fileInfo.getPath());
        String size = FileUtil.format(file.length());
        String description = "<p><span>文件名称:</span><span style=\"word-break:break-all\">" + file.getName() + "</span></p>" +
                "<p><span>文件大小:</span><span style=\"word-break:break-all;\">" + size + "</span></p>" +
                "<p><span>文件路径:</span><span style=\"word-break:break-all\">" + fileInfo.getPath() + "</span></p>";

        TextView txtDescription = (TextView) dialog.findViewById(R.id.description);
        //滚动
        txtDescription.setMovementMethod(ScrollingMovementMethod.getInstance());
        txtDescription.setText(Html.fromHtml(description));

        TextView btnOpen = (TextView) dialog.findViewById(R.id.btn_open);

        btnOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        TextView btnRename = (TextView) dialog.findViewById(R.id.btn_rename);

        btnRename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        TextView btnDel = (TextView) dialog.findViewById(R.id.btn_delete);

        btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                file.delete();
                for(FileInfo file : searchFileList){
                    if(file.getPath().equals(file.getPath())){
                        searchFileList.remove(file);
                        break;
                    }
                }
                fileListAdapter.notifyDataSetChanged();
                showToast("[" + file.getName() + "]删除成功");
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    /**
     * 点击返回键只返回桌面不关闭程序
     */
//    @Override
//    public boolean dispatchKeyEvent(KeyEvent event) {
//        if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
//            if(searchResultPop!=null){
//                searchResultPop.close();
//            }
//            Intent intent = new Intent(Intent.ACTION_MAIN);
//            intent.addCategory(Intent.CATEGORY_HOME);
//            this.startActivity(intent);
//            return true;
//        }
//        /** 按下其它键，调用父类方法，进行默认操作 */
//        return super.dispatchKeyEvent(event);
//    }

    private void openFile(View view, FileInfo fileInfo){
        int type = fileInfo.getType();

        if(type == FileType.APP.getIndex()){
            selectedAppInfo = AppUtil.getApkInfo(this,fileInfo.getPath());
            showWindow(view);
        }else if(type == FileType.IMAGE.getIndex()){
            String dirPath = fileInfo.getPath().substring(0, fileInfo.getPath().lastIndexOf("/"));
            File[] fileList = new File(dirPath).listFiles();
            ArrayList<String> files = new ArrayList<String>();
            int position = 0;
            for(int i=0;i<fileList.length;i++){
                if(FileType.IMAGE.equals(FileType.getFileType(fileList[i]))){
                    files.add(fileList[i].getAbsolutePath());
                    if(fileList[i].getAbsolutePath().equals(fileInfo.getPath())){
                        position = files.size()-1;
                    }
                }
            }
            Intent intent = new Intent(this,GalleryActivity.class);
            intent.putStringArrayListExtra("image_list", files);
            intent.putExtra("image_position", position);
            startActivity(intent);
        }else if(type == FileType.MUSIC.getIndex()){
            safeStartActivity(FileUtil.getAudioFileIntent(fileInfo.getPath()));
        }else if(type == FileType.VIDEO.getIndex()){
            Intent intent = new Intent(this, VideoPlayerActivity.class);
            intent.putExtra("title", fileInfo.getName());
            intent.putExtra("path", fileInfo.getPath());
            startActivity(intent);
        }else if(type == FileType.PDF.getIndex()){
            safeStartActivity(FileUtil.getPdfFileIntent(fileInfo.getPath()));
        }else if(type == FileType.TXT.getIndex()){
            safeStartActivity(FileUtil.getTextFileIntent(fileInfo.getPath(), false));
        }else if(type == FileType.CHM.getIndex()){
            safeStartActivity(FileUtil.getChmFileIntent(fileInfo.getPath()));
        }else if(type == FileType.WORD.getIndex()){
            safeStartActivity(FileUtil.getChmFileIntent(fileInfo.getPath()));
        }else if(type == FileType.XLS.getIndex()){
            safeStartActivity(FileUtil.getChmFileIntent(fileInfo.getPath()));
        }else if(type == FileType.PPT.getIndex()){
            safeStartActivity(FileUtil.getPptFileIntent(fileInfo.getPath()));
        }else if(type == FileType.HTML.getIndex()){
            safeStartActivity(FileUtil.getHtmlFileIntent(fileInfo.getPath()));
        }else{
            safeStartActivity(FileUtil.getAllIntent(fileInfo.getPath()));
        }
    }

    private void safeStartActivity(Intent intent){

        try{
            startActivity(intent);
        }catch (Exception e){

        }
    }


    private void addNav(final File file, boolean isRoot) {
        TextView txt = new TextView(this);

        txt.setTextColor(getResources().getColor(R.color.sky_light_blank));
        txt.setTextSize(20);
        if (isRoot) {
            txt.setText("Root/");
        } else {
            txt.setText(file.getName() + "/");
        }
        txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = 0;
                String path = root.getAbsolutePath() + "/";
                Log.d(">>>count1", fileNav.getChildCount() + "");
                int count = fileNav.getChildCount();
                for (int i = 1; i < count; i++) {
                    TextView txtView = (TextView) fileNav.getChildAt(i);
                    if(txtView!=null){
                        if (v.equals(txtView)) {
                            index = i;
                            path += txtView.getText().toString();
                        } else if (i > 1 && index == 0) {
                            path += txtView.getText().toString();
                        } else {
                            fileNav.removeView(fileNav.getChildAt(index + 1));
                        }
                    }
                }
                listFile(new File(path));
            }
        });
        fileNav.addView(txt);
    }

    private void listFile(File file) {
        List<FileInfo> files = FileInfo.convert(file.listFiles());
        FileUtil.sort(files);

        list.clear();
        list.addAll(files);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                int count = fileNav.getChildCount();
                if (count > 1) {
                    fileNav.removeView(fileNav.getChildAt(count - 1));
                }
                String path = root.getAbsolutePath() + "/";
                for (int i = 1; i < count - 1; i++) {
                    TextView txtView = (TextView) fileNav.getChildAt(i);
                    path += txtView.getText().toString();
                }
                listFile(new File(path));
                break;
            case R.id.app_open:
                PackageUtils.startApp(this, selectedAppInfo.getPackageName());
                popupWindow.dismiss();
                break;
            case R.id.app_unInstall:
                UIHelp.showToast(this, "[" + selectedAppInfo.getAppName() + "]正在卸载,请稍后...");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        int flag  = PackageUtils.uninstall(getApplicationContext(), selectedAppInfo.getPackageName());
                        if(flag != PackageUtils.DELETE_SUCCEEDED){
                            PackageUtils.uninstallNormal(getApplicationContext(), selectedAppInfo.getPackageName());
                        }
                    }
                }).start();
                popupWindow.dismiss();
                break;
            case R.id.app_detail:
                PackageUtils.startInstalledAppDetails(this, selectedAppInfo.getPackageName());
                popupWindow.dismiss();
                break;
            case R.id.app_install:
                UIHelp.showToast(this, "[" + selectedAppInfo.getAppName() + "]正在安装,请稍后...");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        int flag  = PackageUtils.install(getApplicationContext(), selectedAppInfo.getFilePath());
                        if(flag != PackageUtils.INSTALL_SUCCEEDED){
                            PackageUtils.installNormal(getApplicationContext(), selectedAppInfo.getFilePath());
                        }
                    }
                }).start();
                popupWindow.dismiss();
                break;
            case R.id.app_delete:
                popupWindow.dismiss();
                FileUtils.deleteFile(selectedAppInfo.getFilePath());
                for(FileInfo f : list){
                    if(f.getPath().equals(selectedAppInfo.getFilePath())){
                        list.remove(f);
                        adapter.notifyDataSetChanged();
                        break;
                    }
                }
                showToast("[" + selectedAppInfo.getAppName() + "]刪除成功");
                break;
            case R.id.app_upgrade:
                popupWindow.dismiss();
                PackageUtils.install(this, selectedAppInfo.getPackageName());
                break;
        }
    }

    private PopupWindow popupWindow;

    private void showWindow(View parent) {

        int[] location = new int[2];
        //location [0]--->x坐标,location [1]--->y坐标
        parent.getLocationInWindow(location);

        // 创建一个PopuWidow对象
        popupWindow = new PopupWindow(popView,  UIHelp.dip2px(this, 100), WindowManager.LayoutParams.WRAP_CONTENT);
        // 焦点
        popupWindow.setFocusable(true);
        // 设置允许在外点击消失
        popupWindow.setOutsideTouchable(true);
        // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
        popupWindow.setBackgroundDrawable(new BitmapDrawable());

        boolean isInstalled = AppUtil.isAppInstalled(this, selectedAppInfo);
        boolean isUpgrade = AppUtil.isAppUpgrade(this, selectedAppInfo);

        if (isInstalled) {
            txtAppOpen.setVisibility(View.VISIBLE);
            txtUnInstall.setVisibility(View.VISIBLE);
            txtDetail.setVisibility(View.VISIBLE);
            txtAppInstall.setVisibility(View.GONE);
            if (isUpgrade) {
                txtAppUpgrade.setVisibility(View.VISIBLE);
            } else {
                txtAppUpgrade.setVisibility(View.GONE);
            }
        } else {
            txtAppOpen.setVisibility(View.GONE);
            txtUnInstall.setVisibility(View.GONE);
            txtDetail.setVisibility(View.GONE);
            txtAppUpgrade.setVisibility(View.GONE);
            txtAppInstall.setVisibility(View.VISIBLE);
        }
        popupWindow.showAsDropDown(parent, location[0], 0);
    }

    private void initPopView() {
        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        popView = layoutInflater.inflate(R.layout.apk_pop_menu_item, null);
        txtAppOpen = (TextView) popView.findViewById(R.id.app_open);
        txtUnInstall = (TextView) popView.findViewById(R.id.app_unInstall);
        txtDetail = (TextView) popView.findViewById(R.id.app_detail);
        txtAppInstall = (TextView) popView.findViewById(R.id.app_install);
        txtDelete = (TextView) popView.findViewById(R.id.app_delete);
        txtAppUpgrade = (TextView) popView.findViewById(R.id.app_upgrade);

        txtAppOpen.setOnClickListener(this);
        txtUnInstall.setOnClickListener(this);
        txtDetail.setOnClickListener(this);
        txtAppInstall.setOnClickListener(this);
        txtDelete.setOnClickListener(this);
        txtAppUpgrade.setOnClickListener(this);

        windowManager = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
    }


    private void refreshMediaResource(){
        // Android 4.4
        if(Build.VERSION.SDK_INT>= 19){
            MediaScannerConnection.scanFile(this, new String[]{Environment.getExternalStorageDirectory().getAbsolutePath()},
                    null, new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String path, Uri uri) {


                        }
                    }
            );
        }else{
            IntentFilter intentFilter = new IntentFilter(Intent.ACTION_MEDIA_SCANNER_STARTED);
            intentFilter.addAction(Intent.ACTION_MEDIA_SCANNER_FINISHED);
            intentFilter.addDataScheme("file");
            scanReceiver = new ScanSdFilesReceiver();
            registerReceiver(scanReceiver, intentFilter);
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStorageDirectory().getAbsolutePath())));
        }
    }



    // 在Handler中获取消息，重写handleMessage()方法
    private android.os.Handler scanHandler = new android.os.Handler() {

        @Override
        public void handleMessage(Message msg) {
            // 判断消息码是否为1
            if (msg.what == Constants.FINISHED) {

            }
        }
    };

    private class ScanSdFilesReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (Intent.ACTION_MEDIA_SCANNER_STARTED.equals(action)) {
                scanHandler.sendEmptyMessage(0);
            }
            if (Intent.ACTION_MEDIA_SCANNER_FINISHED.equals(action)) {
                scanHandler.sendEmptyMessage(1);
            }
        }
    }

    @Override
    protected void onDestroy(){
        if(scanReceiver!=null){
            unregisterReceiver(scanReceiver);
        }
        super.onDestroy();
    }

}
