package com.blue.sky.mobile.manager.video;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import com.blue.sky.common.activity.BaseFragmentActivity;
import com.blue.sky.common.utils.UIHelp;
import com.blue.sky.control.astuetz.PagerSlidingTabStrip;
import com.blue.sky.mobile.manager.R;
import com.blue.sky.mobile.manager.video.local.VideoLocalFragment;
import com.blue.sky.mobile.manager.video.network.VideoOnlineFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2014/10/13.
 */
public class VideoMainActivity extends BaseFragmentActivity {

    private PagerSlidingTabStrip tabs;
    private ViewPager pager;
    private TabPagerAdapter adapter;
    public List<Fragment> fragments = new ArrayList<Fragment>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_activity_main);
        fragments.add(new VideoLocalFragment());
        fragments.add(new VideoOnlineFragment());

        tabs = (PagerSlidingTabStrip) findViewById(R.id.video_pager_tabs);
        pager = (ViewPager) findViewById(R.id.video_pager);
        pager.setOffscreenPageLimit(2);
        adapter = new TabPagerAdapter(getSupportFragmentManager(), pager, fragments);
        pager.setAdapter(adapter);
        tabs.setViewPager(pager);
        tabs.setTabsStyle(getResources().getDisplayMetrics());

        UIHelp.setHeaderMenuView(this, "视频播放");
    }


    public class TabPagerAdapter extends PagerAdapter implements ViewPager.OnPageChangeListener {

        private List<Fragment> fragments; // 每个Fragment对应一个Page
        private FragmentManager fragmentManager;
        private ViewPager viewPager; // viewPager对象
        private int currentPageIndex = 0; // 当前page索引（切换之前）

        private OnExtraPageChangeListener onExtraPageChangeListener; // ViewPager切换页面时的额外功能添加接口

        public TabPagerAdapter(FragmentManager fragmentManager, ViewPager viewPager, List<Fragment> fragments) {
            this.fragments = fragments;
            this.fragmentManager = fragmentManager;
            this.viewPager = viewPager;
            this.viewPager.setAdapter(this);
            this.viewPager.setOnPageChangeListener(this);
        }

        private final String[] TITLES = {"本地视频", "网络视频"};

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return view == o;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(fragments.get(position).getView()); // 移出viewpager两边之外的page布局
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Fragment fragment = fragments.get(position);
            if (!fragment.isAdded()) { // 如果fragment还没有added
                FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.add(fragment, "tab_" + position);
                ft.commit();
                /**
                 * 在用FragmentTransaction.commit()方法提交FragmentTransaction对象后
                 * 会在进程的主线程中，用异步的方式来执行。
                 * 如果想要立即执行这个等待中的操作，就要调用这个方法（只能在主线程中调用）。
                 * 要注意的是，所有的回调和相关的行为都会在这个调用中被执行完成，因此要仔细确认这个方法的调用位置。
                 */
                fragmentManager.executePendingTransactions();
            }

            if (fragment.getView().getParent() == null) {
                container.addView(fragment.getView()); // 为viewpager增加布局
            }

            return fragment.getView();
        }

        /**
         * 当前page索引（切换之前）
         *
         * @return
         */
        public int getCurrentPageIndex() {
            return currentPageIndex;
        }

        public OnExtraPageChangeListener getOnExtraPageChangeListener() {
            return onExtraPageChangeListener;
        }

        /**
         * 设置页面切换额外功能监听器
         *
         * @param onExtraPageChangeListener
         */
        public void setOnExtraPageChangeListener(OnExtraPageChangeListener onExtraPageChangeListener) {
            this.onExtraPageChangeListener = onExtraPageChangeListener;
        }

        @Override
        public void onPageScrolled(int i, float v, int i2) {
            if (null != onExtraPageChangeListener) { // 如果设置了额外功能接口
                onExtraPageChangeListener.onExtraPageScrolled(i, v, i2);
            }
        }

        @Override
        public void onPageSelected(int i) {
            fragments.get(currentPageIndex).onPause(); // 调用切换前Fargment的onPause()
            if (fragments.get(i).isAdded()) {
                fragments.get(i).onResume(); // 调用切换后Fargment的onResume()
            }
            currentPageIndex = i;

            if (null != onExtraPageChangeListener) { // 如果设置了额外功能接口
                onExtraPageChangeListener.onExtraPageSelected(i);
            }

        }

        @Override
        public void onPageScrollStateChanged(int i) {
            if (null != onExtraPageChangeListener) { // 如果设置了额外功能接口
                onExtraPageChangeListener.onExtraPageScrollStateChanged(i);
            }
        }
    }


    /**
     * page切换额外功能接口
     */
    public class OnExtraPageChangeListener {
        public void onExtraPageScrolled(int i, float v, int i2) {
        }

        public void onExtraPageSelected(int i) {
        }

        public void onExtraPageScrollStateChanged(int i) {
        }
    }
}