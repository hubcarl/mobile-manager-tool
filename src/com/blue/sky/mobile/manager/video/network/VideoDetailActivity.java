package com.blue.sky.mobile.manager.video.network;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.blue.sky.common.activity.BaseActivity;
import com.blue.sky.common.utils.ImageLoadUtil;
import com.blue.sky.common.utils.Strings;
import com.blue.sky.common.utils.TimeUtil;
import com.blue.sky.common.utils.UIHelp;
import com.blue.sky.mobile.manager.R;
import com.blue.sky.mobile.manager.video.common.VideoInfo;
import com.blue.sky.mobile.manager.video.common.VideoPlayerActivity;
import com.blue.sky.mobile.manager.video.common.VideoUtil;
import com.wole56.sdk.Video;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2014/11/2.
 */
public class VideoDetailActivity extends BaseActivity {

    private TextView txtOpt;
    private String path;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_detail_info);

        final VideoInfo videoInfo = (VideoInfo)getIntent().getSerializableExtra("video");


        setImageViewByResId(R.id.item_icon,videoInfo.getbImageUrl(), R.drawable.bg_video_big);
        setTextView(R.id.item_title, videoInfo.getTitle());
        setTextView(R.id.item_time, "时长:" + TimeUtil.parseTime(videoInfo.getTime()));
        setTextView(R.id.item_hit, "热度:" + videoInfo.getPlayTimes());

        txtOpt = (TextView)findViewById(R.id.btn_opt);
        txtOpt.setText("视频加载中,请稍后...");
        txtOpt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (path != null) {
                   Map<String,String> params = new HashMap<String,String>();
                   params.put("path", path);
                   params.put("title", videoInfo.getTitle());
                   startActivity(VideoPlayerActivity.class, params);
               }
            }
        });

        UIHelp.setHeaderMenuView(this, videoInfo.getTitle());

        new HttpAsyncTask().execute(videoInfo.getId());
    }

    public class HttpAsyncTask extends AsyncTask<String, String, Map<String,String>> {

        @Override
        protected Map<String,String> doInBackground(String... params) {
            JSONObject json = Video.getVideoAddress(VideoDetailActivity.this, params[0]);
            Log.i(">>>url", json.toString());
            return VideoUtil.getVideoUrl(json);
        }

        @Override
        protected void onPostExecute(Map<String,String> urlMapping) {
            if(urlMapping.containsKey("qvga")){
                path = urlMapping.get("qvga");
            }else if(urlMapping.containsKey("vga")){
                path = urlMapping.get("vga");
            }else if(urlMapping.containsKey("wvga")){
                path = urlMapping.get("wvga");
            }else if(urlMapping.containsKey("m_qvga")){
                path = urlMapping.get("m_qvga");
            }else if(urlMapping.containsKey("m_wvga")){
                path = urlMapping.get("m_wvga");
            }else{
                path = urlMapping.get("qqvga");
            }
            if(Strings.isEmpty(path)){
                txtOpt.setText("啊啊,该视频无法播放!");
            }else{
                txtOpt.setText("播  放");
            }
            super.onPostExecute(urlMapping);
        }
    }
}