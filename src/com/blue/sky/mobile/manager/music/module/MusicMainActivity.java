package com.blue.sky.mobile.manager.music.module;

/**
 *
 */

import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.view.*;
import com.blue.sky.common.activity.BaseFragmentActivity;
import com.blue.sky.common.utils.UIHelp;
import com.blue.sky.control.slidinguppanel.SlidingUpPanelLayout;
import com.blue.sky.mobile.manager.R;
import com.blue.sky.mobile.manager.music.IApolloService;
import com.blue.sky.mobile.manager.music.helpers.utils.ApolloUtils;
import com.blue.sky.mobile.manager.music.helpers.utils.MusicUtils;
import com.blue.sky.mobile.manager.music.module.local.MusicLoveListFragment;
import com.blue.sky.mobile.manager.music.module.local.MusicAllListFragment;
import com.blue.sky.mobile.manager.music.module.network.activity.MusicOnlineFragment;
import com.blue.sky.mobile.manager.music.service.ApolloService;
import com.blue.sky.mobile.manager.music.service.MusicService;
import com.blue.sky.mobile.manager.music.service.ServiceToken;
import com.blue.sky.mobile.manager.music.ui.adapters.PagerAdapter;
import com.blue.sky.mobile.manager.music.ui.adapters.ScrollingTabsAdapter;
import com.blue.sky.mobile.manager.music.ui.widgets.ScrollableTabView;


public class MusicMainActivity extends BaseFragmentActivity{

    private SlidingUpPanelLayout mPanel;

    private ServiceToken mToken;

    private boolean isAlreadyStarted = false;

    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        // Landscape mode on phone isn't ready
        if (!ApolloUtils.isTablet(this))
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Scan for music
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

        // Layout
        setContentView(R.layout.music_library_browser);

        //mBActionbar =(MusicBottomFragment) getSupportFragmentManager().findFragmentById(R.id.bottom_action_bar_network);

        //mBActionbar.setUpQueueSwitch(this);

        mPanel = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);

        mPanel.setEnabled(false);

        UIHelp.setHeaderMenuView(this,"音乐播放");

        initPager();

        startService(new Intent(this, MusicService.class));
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    /**
     * Initiate ViewPager and PagerAdapter
     */
    public void initPager() {
        // Initiate PagerAdapter
        PagerAdapter mPagerAdapter = new PagerAdapter(getSupportFragmentManager());

        mPagerAdapter.addFragment(new MusicAllListFragment());
        mPagerAdapter.addFragment(new MusicOnlineFragment());
        mPagerAdapter.addFragment(new MusicLoveListFragment());

        // Initiate ViewPager
        ViewPager mViewPager = (ViewPager)findViewById(R.id.viewPager);
        mViewPager.setPageMargin(getResources().getInteger(R.integer.viewpager_margin_width));
        mViewPager.setPageMarginDrawable(R.drawable.music_viewpager_margin);
        mViewPager.setOffscreenPageLimit(mPagerAdapter.getCount());
        mViewPager.setAdapter(mPagerAdapter);
        // Tabs
        initScrollableTabs(mViewPager);
    }

    /**
     * Initiate the tabs
     */
    public void initScrollableTabs(ViewPager mViewPager) {
        ScrollableTabView mScrollingTabs = (ScrollableTabView)findViewById(R.id.scrollingTabs);
        ScrollingTabsAdapter mScrollingTabsAdapter = new ScrollingTabsAdapter(this);
        mScrollingTabs.setAdapter(mScrollingTabsAdapter,mPanel);
        mScrollingTabs.setViewPager(mViewPager);
    }
}
