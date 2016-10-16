package com.blue.sky.common.upload;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import com.blue.sky.common.http.HttpAsyncClient;
import com.blue.sky.common.http.HttpResponseCallback;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;

import java.io.ByteArrayOutputStream;
import java.util.List;

/**
 * Created by Administrator on 2014/11/10.
 */
public class ImageUpload {


    private final static String UPLOAD_IMAGE_URL="";

    public static void upload(String path){

        HttpParams params = new BasicHttpParams();
        params.setParameter("image", bitmapToString(path));
        new HttpAsyncClient(UPLOAD_IMAGE_URL, params, new HttpResponseCallback() {
            @Override
            public void onSuccess(String response){

            }

            @Override
            public void onError(String response){
                Log.e(">>>loadOnlineMusic onError:", response);
            }
        }).execute();
    }

    public static void upload(List<String> images){

    }

    /**
     * 将bitmap转换成base64字符串
     *
     * @param path
     * @return base64 字符串
     */
    public static String bitmapToString(String path){
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        //该方法用来压缩图片，第一个参数为图片格式，第二个参数为截取图片的保留率，如当前为90，则保留之前图片90%的区域
        bitmap.compress(Bitmap.CompressFormat.PNG,90,outputStream );
        byte[] imageByte = outputStream.toByteArray();

        //得到图片的String
        return Base64.encodeToString(imageByte, Base64.DEFAULT);
    }

    /**
     * 将base64转换成bitmap图片
     *
     * @param string base64字符串
     * @return bitmap
     */
    public static Bitmap stringToBitmap(String string) {
        // 将字符串转换成Bitmap类型
        Bitmap bitmap = null;
        try {
            byte[] bitmapArray;
            bitmapArray = Base64.decode(string, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0,
                    bitmapArray.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
