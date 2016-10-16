package com.blue.sky.common.ui;


import android.app.Dialog;
import android.view.View;

public class DialogListener{

    public void onConfirm(Dialog dialog, View v){
        dialog.dismiss();
    }

    public void onCancel(Dialog dialog, View v){
        dialog.dismiss();
    }
}

