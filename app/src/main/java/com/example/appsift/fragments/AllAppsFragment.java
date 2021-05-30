package com.example.appsift.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appsift.R;
import com.example.appsift.adapter.AllAppAdapter;
import com.example.appsift.model.AppModel;
import com.example.appsift.shared.SharedPrefUtil;

import java.util.ArrayList;
import java.util.List;

public class AllAppsFragment extends Fragment {
    RecyclerView recyclerView;
    static List<AppModel> apps = new ArrayList<>();
    AllAppAdapter adapter;
    ProgressDialog progressDialog;
    static Context ctx;



    public AllAppsFragment(Context context, List<AppModel> apps) {
        this.ctx = context;
        this.apps = apps;

    }


    // TODO: Rename and change types and number of parameters
    public static AllAppsFragment newInstance(String param1, String param2) {
        AllAppsFragment fragment = new AllAppsFragment(ctx, apps);
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_all_apps2, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView =(RecyclerView) view.findViewById(R.id.allAppsList);
        adapter = new AllAppAdapter(apps, ctx);
        recyclerView.setLayoutManager(new LinearLayoutManager(ctx));
        recyclerView.setAdapter(adapter);
   /*     progressDialog = new ProgressDialog(this);
        progressDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                getInstalledApps();
            }
        });*/
    }

    @Override
    public void onResume() {
        super.onResume();
        getInstalledApps();
    }

    public void getInstalledApps() {
        List<String> prefLockedAppList = SharedPrefUtil.getInstance(ctx).getLockedAppsList();
        //List<ApplicationInfo> packageInfos = ctx.getPackageManager().getInstalledApplications(0);*/
        if(apps.size() != prefLockedAppList.size()){
            apps.clear();
        PackageManager pk = ctx.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> resolveInfoList = pk.queryIntentActivities(intent, 0);
        for (ResolveInfo resolveInfo : resolveInfoList) {
            ActivityInfo activityInfo = resolveInfo.activityInfo;
            String name = activityInfo.loadLabel(ctx.getPackageManager()).toString();
            Drawable icon = activityInfo.loadIcon(ctx.getPackageManager());
            String packageName = activityInfo.packageName;
            if (!packageName.equalsIgnoreCase("com.android.settings")) {
                if (!prefLockedAppList.isEmpty()) {
                    //check if apps is locked
                    if (prefLockedAppList.contains(packageName)) {
                        apps.add(new AppModel(name, icon, 1, packageName));
                    } else {
                        apps.add(new AppModel(name, icon, 0, packageName));
                    }
                } else {
                    apps.add(new AppModel(name, icon, 0, packageName));
                }
            } else {
                //do not add settings to app list
            }

        }
    }
    }
}