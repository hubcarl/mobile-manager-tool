
package com.blue.sky.mobile.manager.music.ui.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import com.blue.sky.mobile.manager.R;

public class ScrollingTabsAdapter implements TabAdapter {

    private final Activity activity;
    private String[] mTitles;

    public ScrollingTabsAdapter(Activity act) {
        activity = act;
        mTitles = activity.getResources().getStringArray(R.array.tab_titles);
    }

    @Override
    public View getView(int position) {
        LayoutInflater inflater = activity.getLayoutInflater();
        Button tab = (Button)inflater.inflate(R.layout.music_tabs, null);
        tab.setWidth(activity.getResources().getDisplayMetrics().widthPixels/3);
        tab.setText(mTitles[position]);
        return tab;
    }
}
