package com.blue.sky.mobile.manager.apk.adapter;

import android.app.ActivityManager;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.blue.sky.common.component.util.PackageUtils;
import com.blue.sky.common.utils.KillService;
import com.blue.sky.common.utils.UIHelp;
import com.blue.sky.mobile.manager.R;
import com.blue.sky.mobile.manager.apk.model.AppInfo;
import com.blue.sky.mobile.manager.apk.utils.AppUtil;

import java.util.List;


public class AppListAdapter extends BaseAdapter {

    private Context context;
    private List<AppInfo> list;
    private int type;

    public AppListAdapter(Context context, List<AppInfo> list, int type) {
        super();
        this.context = context;
        this.list = list;
        this.type = type;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View rootView, ViewGroup parent) {
        ViewHolder holder;
        if (rootView == null) {
            holder = new ViewHolder();
            rootView = View.inflate(context, R.layout.apk_app_list_item, null);
            holder.icon =(ImageView) rootView.findViewById(R.id.app_icon);
            holder.name = (TextView) rootView.findViewById(R.id.app_name);
            holder.packageName = (TextView) rootView.findViewById(R.id.app_package);
            holder.size = (TextView) rootView.findViewById(R.id.app_size);
            holder.version = (TextView) rootView.findViewById(R.id.app_version);
            holder.status  = (TextView) rootView.findViewById(R.id.app_status);
            holder.btnOpt = (TextView) rootView.findViewById(R.id.btn_opt);
            rootView.setTag(holder);
        } else {
            holder = (ViewHolder) rootView.getTag();
        }
        final AppInfo appInfo = list.get(position);
        holder.icon.setImageDrawable(appInfo.getIcon());
        holder.name.setText(appInfo.getAppName());
        holder.packageName.setText(appInfo.getPackageName());
        holder.size.setText("大小:" + appInfo.getSize());
        holder.version.setText("版本:" +appInfo.getVersionName());

        if(type==2){
            holder.btnOpt.setText("清理");
            holder.btnOpt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    KillService.kill(appInfo.getPackageName());
                    ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
                    am.killBackgroundProcesses(appInfo.getPackageName());
                    list.remove(appInfo);
                    AppListAdapter.this.notifyDataSetChanged();
                }
            });
        }else{
            holder.btnOpt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int flag = PackageUtils.uninstall(context, appInfo.getPackageName());
                    if (flag == PackageUtils.DELETE_SUCCEEDED) {
                        list.remove(appInfo);
                        AppListAdapter.this.notifyDataSetChanged();
                        UIHelp.showToast(context, "[" + appInfo.getAppName() + "]卸载成功!");
                    } else {
                        PackageUtils.uninstallNormal(context, appInfo.getPackageName());
                    }
                }
            });
        }
        return rootView;
    }

    class ViewHolder {
        ImageView icon;
        TextView name;
        TextView size;
        TextView version;
        TextView packageName;
        TextView status;
        TextView btnOpt;
    }

}