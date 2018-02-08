package com.mingnong.scanappnew.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mingnong.scanappnew.R;
import com.mingnong.scanappnew.adapter.ScanOutputAdapter;
import com.mingnong.scanappnew.bean.ScanOutputBean;
import com.mingnong.scanappnew.dialog.DeleteDialog;
import com.mingnong.scanappnew.dialog.OutputDialog;
import com.mingnong.scanappnew.utils.RecyclerItemClickSupport;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

/**
 * Created by wyw on 2016/11/16.
 */

public class OutputFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private LinearLayoutManager manager;
    private ScanOutputAdapter adapter;
    private OutputDialog dialog;
    private DeleteDialog deleteDialog;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_output,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        manager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(manager);
        adapter = new ScanOutputAdapter(getContext());
        mRecyclerView.setAdapter(adapter);
        dialog = new OutputDialog(getContext(), new OutputDialog.OnSelectListener() {

            @Override
            public void onClick(ScanOutputBean bean) {
                //没有使用
                adapter.addItem(bean);
            }

            @Override
            public void update() {
                adapter.notifyDataSetChanged();
            }
        });
        RecyclerItemClickSupport.addTo(mRecyclerView).setOnItemClickListener(new RecyclerItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, final int position, View v) {
                dialog.update(adapter.getmDatas().get(position));
            }
        });

        deleteDialog = new DeleteDialog(getContext(), new DeleteDialog.OnClickListener() {
            @Override
            public void onClick(int position) {
                if (position > 0 && adapter.getmDatas()!=null && position <= adapter.getmDatas().size()) {
                    adapter.deleteItem(position - 1);
                } else {
                    Toast.makeText(getContext(), "请输入正确的条目", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * 根据文件名字区别文件出库还是入库
     */
    public void save(boolean isUpload) {
        if (adapter.getmDatas() != null && adapter.getmDatas().size() > 0) {
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                File dir = new File(Environment.getExternalStorageDirectory(), "出入库管理"+File.separator+"出库");
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                //根据时间设置文件名字
                SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒", Locale.CHINA);
                File file = new File(dir, "出库"+format.format(new Date()) + ".txt");
                String json = getJson();
                OutputStreamWriter osw = null;
                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(file);
                    osw = new OutputStreamWriter(fos);
                    osw.write(json);
                    Toast.makeText(getContext(), "文件已经保存至" + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
                    adapter.clear();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (osw != null) {
                            osw.close();
                        }
                        if (fos != null) {
                            fos.close();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                Toast.makeText(getContext(), "请打开外置存储卡", Toast.LENGTH_LONG).show();
            }
        } else {
            if (!isUpload)
            Toast.makeText(getContext(), "请先扫描数据,然后保存", Toast.LENGTH_SHORT).show();
        }
    }

    private  String getJson() {
        String uuid = UUID.randomUUID().toString();
        for (int i = 0; i < adapter.getItemCount(); i++) {
            adapter.getmDatas().get(i).setUuid(uuid);
        }
        return new Gson().toJson(adapter.getmDatas());
    }


    public void show(ScanOutputBean bean) {
        //不需要dialog展示 直接设置到recyclerview上
//        dialog.show(bean);
        adapter.addItem(bean);
    }

    public void delete() {
        deleteDialog.show();
    }

}
