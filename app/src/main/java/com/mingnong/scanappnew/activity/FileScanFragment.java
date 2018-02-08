package com.mingnong.scanappnew.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.mingnong.scanappnew.Contance;
import com.mingnong.scanappnew.R;
import com.mingnong.scanappnew.adapter.FileScanAdapter;
import com.mingnong.scanappnew.bean.BaseMsg;
import com.mingnong.scanappnew.bean.FileBean;
import com.mingnong.scanappnew.net.RequestCallBack;
import com.mingnong.scanappnew.utils.RecyclerItemClickSupport;
import com.mingnong.scanappnew.utils.SPUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import cn.finalteam.okhttpfinal.HttpRequest;
import cn.finalteam.okhttpfinal.RequestParams;

import static android.R.attr.type;

/**
 * Created by wyw on 2016/11/16.
 */

public class FileScanFragment extends Fragment implements View.OnClickListener {
    private RecyclerView mRecyclerView;
    private FileScanAdapter adapter;
    private RelativeLayout rlNone;
    private Button btSelectAll, btUpload, btDelete;
    private ProgressDialog dialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_file_scan, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViews(view);
        bindData();
        bindListener();
    }

    private void bindData() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        final List<FileBean> mDatas = new ArrayList<>();
        //获取
        adapter = new FileScanAdapter(mDatas);
        mRecyclerView.setAdapter(adapter);
        dialog = new ProgressDialog(getContext());
    }

    private void bindListener() {
        RecyclerItemClickSupport.addTo(mRecyclerView).setOnItemClickListener(new RecyclerItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Intent intent = new Intent(getContext(), FileDetilActivity.class);
                intent.putExtra(Contance.START_ACTIVITY_DATA, adapter.getDatas().get(position).getTvFilePath());
                intent.putExtra(Contance.START_ACTIVITY_TYPE, type);
                startActivity(intent);
            }
        });
        btSelectAll.setOnClickListener(this);
        btUpload.setOnClickListener(this);
        btDelete.setOnClickListener(this);
    }

    private void findViews(View view) {
        rlNone = (RelativeLayout) view.findViewById(R.id.rl_none);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        btSelectAll = (Button) view.findViewById(R.id.bt_select_all);
        btUpload = (Button) view.findViewById(R.id.bt_upload);
        btDelete = (Button) view.findViewById(R.id.bt_delete);
    }

    @Override
    public void onResume() {
        super.onResume();
        setAdapterData();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            //显示
            setAdapterData();
        }
    }

    private void setAdapterData() {
        new Thread() {
            @Override
            public void run() {
                File inputStore = new File(Environment.getExternalStorageDirectory(), "出入库管理" + File.separator + "入库");
                if (!inputStore.exists()) {
                    inputStore.mkdirs();
                }
                final List<FileBean> mDatas = new ArrayList<>();
                for (File file : inputStore.listFiles()) {
//                    if (file.getName().contains("入库")) {
                    FileBean bean = new FileBean();
                    bean.setCheck(false).setTvFilePath(file.getAbsolutePath());
                    mDatas.add(bean);
//                    }
                }
                //出库数据
                File outputStore = new File(Environment.getExternalStorageDirectory(), "出入库管理" + File.separator + "出库");
                if (!outputStore.exists()) {
                    outputStore.mkdirs();
                }
                for (File file : outputStore.listFiles()) {
//                    if (file.getName().contains("入库")) {
                    FileBean bean = new FileBean();
                    bean.setCheck(false).setTvFilePath(file.getAbsolutePath());
                    mDatas.add(bean);
//                    }
                }
                ((MainActivity) getContext()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.setDatas(mDatas);
                        if (mDatas.size() > 0) {
                            rlNone.setVisibility(View.GONE);
                        } else {
                            rlNone.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
        }.start();
    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.bt_select_all:
                adapter.selectAll();
                break;
            case R.id.bt_delete:
                if (adapter.getItemCount() == 0) {
                    Snackbar.make(v, "没有数据了,请前往主页扫描", Snackbar.LENGTH_SHORT).show();
                    rlNone.setVisibility(View.VISIBLE);
                    return;
                }
                for (int i = 0; i < adapter.getItemCount(); i++) {
                    FileBean bean = adapter.getDatas().get(i);
                    if (bean.isCheck()) {
                        new File(bean.getTvFilePath()).delete();
                        adapter.deleteItem(i);
                        i--;
                    }
                }
                Snackbar.make(v, "刪除成功", Snackbar.LENGTH_SHORT).show();
                break;
            case R.id.bt_upload:
                if (adapter.getItemCount() == 0) {
                    Snackbar.make(v, "没有数据了,请前往主页扫描", Snackbar.LENGTH_SHORT).show();
                    rlNone.setVisibility(View.VISIBLE);
                    return;
                }
                boolean delete = false;
                List<FileBean> deleteList = new ArrayList<>();
                for (int i = 0; i < adapter.getItemCount(); i++) {
                    FileBean bean = adapter.getDatas().get(i);
                    if (bean.isCheck()) {
                        delete = true;
                        deleteList.add(bean);
                    }
                }
                if (!delete) {
                    Snackbar.make(v, "请先选择然后上传", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                dialog.show();
                uploadItem(getDeletePosition(0));
        }
    }

    private int getDeletePosition(int startPosition){
        for (int position = startPosition; position < adapter.getItemCount(); position++) {
            FileBean bean = adapter.getDatas().get(position);
            if (bean.isCheck())
                return position;
//                uploadHandler.obtainMessage(UPLOAD_ITEM_START,position,position).sendToTarget();
//                return ;
        }
        //沒有需要刪除的文件
//        uploadHandler.obtainMessage(UPLOAD_ALL).sendToTarget();
        return -1;
    }

    private final static int UPLOAD_ITEM_SUCCESS = 2;
    private Handler uploadHandler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            switch (msg.what) {
                case UPLOAD_ITEM_SUCCESS:
                    int position = (int) msg.obj;
                    uploadItem(getDeletePosition(position));
                    break;
            }
        }
    };

    private void uploadItem(final int position) {
        if (position<0){
            //上傳完畢
            dialog.dismiss();
            Snackbar.make(btUpload, "上传成功", Toast.LENGTH_SHORT).show();
            return;
        }
        RequestParams params = new RequestParams();
        params.addFormDataPart("USERID", SPUtils.getInstance().getData(Contance.USERID, "", String.class));
        final File file = new File(adapter.getDatas().get(position).getTvFilePath());
        params.addFormDataPart("fileJson", getJsonString(file));
        params.addFormDataPart("TableName", file.getName().contains("入库") ? "SY_Temporary" : "SY_TemXmarket");
        HttpRequest.post(Contance.BASE_URL + "UploadTemfile.ashx", params, new RequestCallBack<BaseMsg>() {
            @Override
            public void onFailure(Exception e) {
                dialog.dismiss();
                Snackbar.make(btUpload, e.getMessage(), Snackbar.LENGTH_SHORT).show();
            }



            @Override
            public void getData(BaseMsg baseMsg) {
                    file.delete();
                    adapter.deleteItem(position);
                    uploadHandler.obtainMessage(UPLOAD_ITEM_SUCCESS,position).sendToTarget();
            }
        });
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
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
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
