package com.blue.sky.common.http;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import com.blue.sky.common.utils.NetWorkHelper;

import java.util.List;

/**
 * Created by sky on 2015/1/3.
 */
public class HttpCacheClient {

    public final static int MSG_CACHE = 1;
    public final static int MSG_HTTP = 2;

    private IHttpCache httpCache;
    private Context context;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MSG_CACHE:
                    httpCache.response(msg.obj, MSG_CACHE);
                    break;

                case MSG_HTTP:
                    httpCache.response(msg.obj, MSG_HTTP);
                    break;
            }
        }
    };

    public HttpCacheClient(Context context, IHttpCache httpCache){
        this.context = context;
        this.httpCache = httpCache;
    }

    public void execute(final Object...params){
        new Thread(new Runnable() {
            @Override
            public void run() {
                loadData(mHandler, params);
            }
        }).start();
    }

    protected void loadData(Handler mHandler, Object... params){
        loadCache(mHandler,params);
        if(NetWorkHelper.isConnect(context)){
            loadHttp(mHandler, params);
        }
    }

    protected Object loadCache(Handler mHandler, Object... params){
        List list = httpCache.loadCache(params);
        Message msg = Message.obtain();
        msg.what = MSG_CACHE;
        msg.obj = list;
        mHandler.sendMessage(msg);
        return list;
    }

    protected void loadHttp(Handler mHandler,Object... params){

        List list = httpCache.loadHttp(params);
        Message msg = Message.obtain();
        msg.what = MSG_HTTP;
        msg.obj = list;
        mHandler.sendMessage(msg);
        // 缓存数据
        httpCache.cache(list);
    }

}
