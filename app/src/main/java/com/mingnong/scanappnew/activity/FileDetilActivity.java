package com.mingnong.scanappnew.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mingnong.scanappnew.Contance;
import com.mingnong.scanappnew.R;
import com.mingnong.scanappnew.adapter.ScanInputAdapter;
import com.mingnong.scanappnew.adapter.ScanOutputAdapter;
import com.mingnong.scanappnew.bean.ScanInputBean;
import com.mingnong.scanappnew.bean.ScanOutputBean;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class FileDetilActivity extends SwipeBackActivity {
    private RecyclerView mRecyclerView;
    private LinearLayoutManager manager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_detil);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            setStatusBarColor(getResources().getColor(R.color.colorPrimary,getTheme() ));
        } else {
            setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("文件详情");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        manager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(manager);
        File file = new File(getIntent().getStringExtra(Contance.START_ACTIVITY_DATA));
        RecyclerView.Adapter adapter = null;
        if (file.getName().contains("入库")) {
            try {
                adapter = new ScanInputAdapter(this, readAndParseInput(file.getAbsolutePath()));
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (file.getName().contains("出库")) {
            //等于1 代表出库
            try {
                adapter = new ScanOutputAdapter(this, readAndParseOutput(file.getAbsolutePath()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        mRecyclerView.setAdapter(adapter);

    }

    /**
     * 入库
     */
    private List<ScanInputBean> readAndParseInput(String path) throws Exception{
        File file = new File(path);
        if (!file.exists()) {
            return new ArrayList<>();
        }
        return new Gson().fromJson(getJsonString(file), new TypeToken<List<ScanInputBean>>() {}.getType());
    }
    /**
     * 出库
     */
    private List<ScanOutputBean> readAndParseOutput(String path) throws Exception{
        File file = new File(path);
        if (!file.exists()) {
            return new ArrayList<>();
        }
        return new Gson().fromJson(getJsonString(file), new TypeToken<List<ScanOutputBean>>() {}.getType());
    }
    private String getJsonString(File file) {
        FileInputStream fis = null;
        InputStreamReader isr = null;
        StringBuilder builder = new StringBuilder();
        try {
            fis = new FileInputStream(file);
            isr = new InputStreamReader(fis);
            int len = 0;
            char[] buf = new char[1024];
            while ((len = isr.read(buf)) != -1) {
                builder.append(new String(buf, 0, len));
            }
        } catch ( Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if (isr != null) {
                    isr.close();
                }
                if (fis != null) {
                    fis.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return builder.toString();
    }
}
