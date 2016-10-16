package com.blue.sky.mobile.manager.apk;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import com.blue.sky.common.activity.BaseFragmentActivity;
import com.blue.sky.common.adapter.TabPagerAdapter;
import com.blue.sky.common.sdk.SDK;
import com.blue.sky.common.utils.UIHelp;
import com.blue.sky.control.astuetz.PagerSlidingTabStrip;
import com.blue.sky.mobile.manager.R;
import com.blue.sky.mobile.manager.apk.fragment.ApkFragment;
import com.blue.sky.mobile.manager.apk.fragment.AppFragment;
import com.blue.sky.mobile.manager.apk.fragment.RunningAppFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sky on 2014/10/13.
 */
public class ApkMainActivity extends BaseFragmentActivity {

    private PagerSlidingTabStrip tabs;
    private ViewPager pager;
    private TabPagerAdapter adapter;
    private List<Fragment> fragments = new ArrayList<Fragment>();
    private final String[] titles = { "已安装", "安装包","运行中"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_activity_main);
        fragments.add(new AppFragment());
        fragments.add(new ApkFragment());
        fragments.add(new RunningAppFragment());

        //fragments.add(getAdFragment(SDK.WDJ_AD_APP_LIST_APP,"APP"));
        //fragments.add(getAdFragment(SDK.WDJ_AD_APP_LIST_GAME,"GAME"));

        tabs = (PagerSlidingTabStrip) findViewById(R.id.video_pager_tabs);
        pager = (ViewPager) findViewById(R.id.video_pager);
        adapter = new TabPagerAdapter(getSupportFragmentManager(),pager, fragments, titles);
        pager.setAdapter(adapter);
        tabs.setViewPager(pager);
        tabs.setTabsStyle(getResources().getDisplayMetrics());
        UIHelp.setHeaderMenuView(this,"应用程序管理");

        SDK.initWDJSDK(this);
    }

//    private Fragment getAdFragment(String tag, String category){
//        AppListFragment localTabsFragment = new AppListFragment();
//        Bundle localBundle = new Bundle();
//        localBundle.putString("category", category);
//        localBundle.putInt("detail_container_id", R.id.container);
//        localBundle.putString("detail_back_stack_name", "");
//        localBundle.putString("tag", tag);
//        localTabsFragment.setArguments(localBundle);
//        return localTabsFragment;
//    }
}