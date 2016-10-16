package com.blue.sky.mobile.manager.main;


import android.content.Context;
import com.blue.sky.common.utils.Constants;
import com.blue.sky.common.utils.EnumUtil;
import com.blue.sky.common.utils.SharedPreferenceUtil;
import com.blue.sky.common.utils.Strings;

public class ImageInfo {

	public EnumUtil.Navigation navigation;		//菜单标题
	public int imageId;			//logo图片资源
	public int bgId;			//背景图片资源
    public String desc = Strings.EMPTY_STRING;

	public ImageInfo(Context context, EnumUtil.Navigation navigation, int id1,int id2) {
        this.navigation = navigation;
        this.imageId = id1;
		this.bgId = id2;
        this.refresh(context);
	}

    public void refresh(Context context){
        if(EnumUtil.Navigation.Doc.equals(this.navigation)){
            this.desc = (SharedPreferenceUtil.getInstance(context).getInt(Constants.FILE_TYPE_TXT)
                    +SharedPreferenceUtil.getInstance(context).getInt(Constants.FILE_TYPE_DOC)
                    + SharedPreferenceUtil.getInstance(context).getInt(Constants.FILE_TYPE_ZIP))+"";
        }else{
            this.desc = SharedPreferenceUtil.getInstance(context).getInt(navigation.toString()) + "";
        }
    }
}
