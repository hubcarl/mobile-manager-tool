package com.blue.sky.mobile.manager.doc.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.*;
import com.blue.sky.common.component.util.FileUtils;
import com.blue.sky.common.fragment.BaseFragment;
import com.blue.sky.common.ui.DialogListener;
import com.blue.sky.common.utils.SharedPreferenceUtil;
import com.blue.sky.common.utils.UIHelp;
import com.blue.sky.mobile.manager.R;
import com.blue.sky.mobile.manager.apk.utils.FileUtil;
import com.blue.sky.mobile.manager.doc.adapter.FileListAdapter;
import com.blue.sky.mobile.manager.doc.model.FileInfo;
import com.blue.sky.mobile.message.ActionManager;

import java.io.File;
import java.util.*;

/**
 * Created by Administrator on 2014/10/14.
 */
public class DocumentFragment extends BaseFragment implements View.OnClickListener{


    private View rootView;
    private ListView listView;
    private List<FileInfo> fileInfoList = new ArrayList<FileInfo>();
    private FileListAdapter fileListAdapter;
    private FileInfo selectedFileInfo;

    private TextView txtMenuOpen, txtMenuBackup, txtMenuBlue, txtMenuShare, txtDelete;
    private View popView;
    private TextView btnOpt;
    private CheckBox btnCheckAll;


    private TextView tipMessage;
    private ProgressBar progressBar;
    private LinearLayout loading;

    private WindowManager windowManager;
    private String fileType;

    public DocumentFragment(String fileType){
        this.fileType = fileType;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.sky_activity_common_list_opt, container, false);

        initControl();
        initEvent();
        initApkList();
        initPopView();

        return rootView;
    }


    private void initControl(){
        loading = (LinearLayout) rootView.findViewById(R.id.loading);
        btnOpt = (TextView)rootView.findViewById(R.id.btnOpt);
        btnCheckAll = (CheckBox)rootView.findViewById(R.id.btnCheckAll);

        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        tipMessage = (TextView) rootView.findViewById(R.id.tip_message);

        listView = (ListView) rootView.findViewById(R.id.list_view);
        fileListAdapter = new FileListAdapter(getActivity(), fileInfoList, 1);
        listView.setAdapter(fileListAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedFileInfo = fileInfoList.get(position);
                showWindow(view);
            }
        });

    }

    private void initEvent(){
        btnOpt.setOnClickListener(this);
        btnCheckAll.setOnClickListener(this);
    }



    private void initPopView() {
        LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        popView = layoutInflater.inflate(R.layout.doc_list_menu_item, null);
        txtMenuOpen = (TextView) popView.findViewById(R.id.menu_open);
        txtMenuBackup = (TextView) popView.findViewById(R.id.menu_backup);
        txtMenuBlue = (TextView) popView.findViewById(R.id.menu_blue);
        txtMenuShare = (TextView) popView.findViewById(R.id.menu_share);
        txtDelete = (TextView) popView.findViewById(R.id.menu_delete);

        txtMenuOpen.setOnClickListener(this);
        txtMenuBackup.setOnClickListener(this);
        txtMenuBlue.setOnClickListener(this);
        txtMenuShare.setOnClickListener(this);
        txtDelete.setOnClickListener(this);

        windowManager = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_REFRESH:
                    fileInfoList.addAll((List<FileInfo>) msg.obj);
                    fileListAdapter.notifyDataSetChanged();
                    break;
                case MSG_FINISHED:
                    progressBar.setVisibility(View.GONE);
                    tipMessage.setText("扫描完成, 总共有" + fileInfoList.size() + "个文件");
                    fileListAdapter.notifyDataSetChanged();
                    SharedPreferenceUtil.getInstance(getActivity()).putInt(fileType, fileInfoList.size());
                    getActivity().sendBroadcast(new Intent(ActionManager.ACTION_FILE_CHANGED));
                    break;
                case HIDE_LOADING:
                    loading.setVisibility(View.GONE);
                    btnOpt.setText("一键清理(共"+ fileInfoList.size() +  "个文件)");
                    break;
                case MSG_EMPTY:
                    loading.setVisibility(View.GONE);
                    UIHelp.showToast(getActivity(), "没有找到SD卡");
                    break;
                case FILE_DELETE_FINISHED:
                    fileListAdapter.notifyDataSetChanged();
                    UIHelp.closeLoading();
                    break;
            }
        }
    };

    private void initApkList() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    scanFile(Environment.getExternalStorageDirectory(), fileType);
                    sendMessage(handler, MSG_FINISHED, null);
                    sendMessageDelayed(handler, HIDE_LOADING, null, 2000);
                }
            }).start();
        } else {
            handler.sendEmptyMessage(MSG_EMPTY);
        }
    }

//    private void initScanFileStatusTask() {
//        final Timer timer = new Timer();
//        TimerTask task = new TimerTask() {
//            @Override
//            public void run() {
//                if (scanFlag == tempScanFlag) {
//                    Log.i(">>>initScanFileStatusTask:", "finished");
//                    timer.cancel();
//                    sendMessage(handler, MSG_FINISHED, null);
//                    sendMessageDelayed(handler, HIDE_LOADING, null, 2000);
//                } else {
//                    tempScanFlag = scanFlag;
//                }
//            }
//        };
//        timer.schedule(task, 1000, 1000);
//    }

    // 遍历接收一个文件路径，然后把文件子目录中的所有文件遍历并输出来
    private void scanFile(File root, String fileType) {
        File files[] = root.listFiles();
        if (files != null) {
            List<FileInfo> tempFileList = new ArrayList<FileInfo>();
            for (File f : files) {
                if (f.isDirectory()) {
                    scanFile(f,fileType);
                } else {
                    int index = f.getName().lastIndexOf(".");
                    if(index>-1){
                        String mineType = f.getName().substring(index, f.getName().length()).toLowerCase() ;
                        Log.i("mineType", mineType);
                        if (mineType.length()==4 && fileType.contains(mineType)) {
                            tempFileList.add(FileUtil.make(f));
                        }
                    }
                }
            }
            if(tempFileList.size()>0){
                sendMessage(handler, MSG_REFRESH, tempFileList);
            }
        }
    }

    private PopupWindow popupWindow;

    private void showWindow(View parent) {

        int[] location = new int[2];
        //location [0]--->x坐标,location [1]--->y坐标
        parent.getLocationInWindow(location);

        // 创建一个PopuWidow对象
        popupWindow = new PopupWindow(popView, windowManager.getDefaultDisplay().getWidth(), UIHelp.dip2px(getActivity(), 56));
        // 焦点
        popupWindow.setFocusable(true);
        // 设置允许在外点击消失
        popupWindow.setOutsideTouchable(true);
        // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
        popupWindow.setBackgroundDrawable(new BitmapDrawable());

        popupWindow.showAsDropDown(parent, location[0], 0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnOpt:
                if(!fileListAdapter.getCheckItems().isEmpty()){
                    UIHelp.confirmDialog(getActivity(), "文件删除提示", "你确定删除选中的文件?<br/>", "确 认", "取 消", true, new DialogListener() {
                        @Override
                        public void onConfirm(Dialog dialog, View v) {
                            UIHelp.showLoading(getActivity(),"正在清理,请稍后....");
                            List<FileInfo> deleteList = new ArrayList<FileInfo>();
                            Set<Map.Entry<Integer,Boolean>> entrySet = fileListAdapter.getCheckItems().entrySet();
                            Iterator<Map.Entry<Integer,Boolean>> it = entrySet.iterator();
                            while (it.hasNext()) {
                                Map.Entry<Integer,Boolean> entry =  it.next();
                                if(entry.getValue()){

                                    FileInfo fileInfo = fileInfoList.get(entry.getKey());
                                    deleteList.add(fileInfo);

                                    new File(fileInfo.getFilePath()).delete();
                                }
                            }
                            if(deleteList.size()>0){
                                fileInfoList.removeAll(deleteList);
                            }
                            fileListAdapter.notifyDataSetChanged();
                            UIHelp.closeLoading();
                            UIHelp.showToast(getActivity(),"-^^-清理成功!");
                            SharedPreferenceUtil.getInstance(getActivity()).putInt(fileType, fileInfoList.size());
                            getActivity().sendBroadcast(new Intent(ActionManager.ACTION_FILE_CHANGED));
                            super.onConfirm(dialog, v);
                        }

                        @Override
                        public void onCancel(Dialog dialog, View v) {
                            super.onCancel(dialog, v);
                        }
                    });
                }
                break;
            case R.id.btnCheckAll:
                Log.i(">>>item btnCheckAll:", btnCheckAll.isChecked()+"");
                fileListAdapter.checkAll(btnCheckAll.isChecked());
                fileListAdapter.notifyDataSetChanged();
                break;
            case R.id.menu_open:
                Intent fileIntent = FileUtil.openFile(selectedFileInfo.getFilePath());
                startActivity(fileIntent);
                popupWindow.dismiss();
                break;
            case R.id.menu_backup:
                popupWindow.dismiss();
                break;
            case R.id.menu_blue:
                popupWindow.dismiss();
                break;
            case R.id.menu_share:
                popupWindow.dismiss();
                break;
            case R.id.menu_delete:
                FileUtils.deleteFile(selectedFileInfo.getFilePath());
                fileInfoList.remove(selectedFileInfo);
                fileListAdapter.notifyDataSetChanged();
                popupWindow.dismiss();
                break;

        }
    }
}