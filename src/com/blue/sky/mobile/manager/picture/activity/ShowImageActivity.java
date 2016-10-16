package com.blue.sky.mobile.manager.picture.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import com.blue.sky.common.activity.BaseActivity;
import com.blue.sky.common.component.util.FileUtils;
import com.blue.sky.common.ui.DialogListener;
import com.blue.sky.common.utils.UIHelp;
import com.blue.sky.mobile.manager.R;
import com.blue.sky.mobile.manager.apk.utils.FileUtil;
import com.blue.sky.mobile.manager.picture.adapter.ChildAdapter;
import com.blue.sky.mobile.message.ActionManager;

import java.io.File;
import java.util.ArrayList;

public class ShowImageActivity extends BaseActivity {
    private GridView mGridView;
    private ArrayList<String> list;
    private ChildAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.picture_show_image_activity);

        UIHelp.setHeaderMenuView(this, getIntent().getStringExtra("folderName"));

        mGridView = (GridView) findViewById(R.id.child_grid);
        list = getIntent().getStringArrayListExtra("fileList");
        adapter = new ChildAdapter(this, list, mGridView);
        mGridView.setAdapter(adapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(ShowImageActivity.this, GalleryActivity.class);
                intent.putStringArrayListExtra("image_list", list);
                intent.putExtra("image_position", position);
                startActivity(intent);
            }
        });

        mGridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                final String path = list.get(position);
                final File file = new  File(path);
                String size = FileUtil.format(file.length());
                String desc = "<p><span>文件名:</span><span style=\"word-break:break-all\">" + file.getName() + "</span></p>" +
                        "<p><span>图片大小:</span><span style=\"word-break:break-all;\">" + size + "</span></p>" +
                        "<p><span>图片路径:</span><span style=\"word-break:break-all\">" + path + "</span></p>";

                UIHelp.confirmDialog(ShowImageActivity.this, "信息提示", desc, "删  除", "关  闭", true,  new DialogListener() {
                    @Override
                    public void onConfirm(Dialog dialog, View v) {
                        file.delete();
                        list.remove(position);
                        adapter.notifyDataSetChanged();
                        showToast("[" + file.getName() + "]删除成功");
                        sendBroadcast(new Intent(ActionManager.ACTION_FILE_CHANGED));
                        super.onConfirm(dialog, v);
                    }

                    @Override
                    public void onCancel(Dialog dialog, View v) {
                        super.onCancel(dialog, v);
                    }
                });
                return true;
            }
        });
    }

}
