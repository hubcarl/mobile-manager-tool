package com.blue.sky.mobile.manager.doc;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import com.blue.sky.common.activity.BaseFragmentActivity;
import com.blue.sky.common.adapter.TabPagerAdapter;
import com.blue.sky.common.utils.Constants;
import com.blue.sky.common.utils.UIHelp;
import com.blue.sky.control.astuetz.PagerSlidingTabStrip;
import com.blue.sky.mobile.manager.R;
import com.blue.sky.mobile.manager.doc.fragment.DocumentFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2014/10/13.
 */
public class DocumentMainActivity extends BaseFragmentActivity {

    private PagerSlidingTabStrip tabs;
    private ViewPager pager;
    private TabPagerAdapter adapter;
    private List<Fragment> fragments = new ArrayList<Fragment>();
    private final String[] titles = { "文本文档", "办公文档","压缩文档"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_activity_main);

        fragments.add(new DocumentFragment(Constants.FILE_TYPE_TXT));
        fragments.add(new DocumentFragment(Constants.FILE_TYPE_DOC));
        fragments.add(new DocumentFragment(Constants.FILE_TYPE_ZIP));

        tabs = (PagerSlidingTabStrip) findViewById(R.id.video_pager_tabs);
        pager = (ViewPager) findViewById(R.id.video_pager);
        adapter = new TabPagerAdapter(getSupportFragmentManager(),pager, fragments, titles);
        pager.setAdapter(adapter);
        tabs.setViewPager(pager);
        tabs.setTabsStyle(getResources().getDisplayMetrics());

        UIHelp.setHeaderMenuView(this,"文档管理");
    }
}