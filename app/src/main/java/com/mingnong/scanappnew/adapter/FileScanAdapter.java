package com.mingnong.scanappnew.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.mingnong.scanappnew.R;
import com.mingnong.scanappnew.bean.FileBean;

import java.io.File;
import java.util.List;

/**
 * Created by wyw on 2016/11/16.
 */

public class FileScanAdapter extends RecyclerView.Adapter<FileScanAdapter.ViewHolder> {
    private List<FileBean> mDatas;

    public FileScanAdapter(List<FileBean> mDatas) {
        this.mDatas = mDatas;
    }

    public List<FileBean> getDatas() {
        return mDatas;
    }

    public void setDatas( List<FileBean> mDatas) {
        this.mDatas.clear();
        this.mDatas.addAll(mDatas);
        notifyDataSetChanged();
    }

    public void selectAll() {
        if (mDatas != null && mDatas.size() > 0) {
            boolean isSelect = false;
            for (int i = 0; i < mDatas.size(); i++) {
                //如果所有的都已经是true 那么之后就设置false
                if (!mDatas.get(i).isCheck()) {
                    isSelect = true;
                    break;
                }
            }
            for (int i = 0; i < mDatas.size(); i++) {
                mDatas.get(i).setCheck(isSelect);
            }
        }
        notifyDataSetChanged();
    }

    public void deleteItem(int position) {
        mDatas.remove(position);

        notifyItemRemoved(position);//显示动画效果
        notifyItemRangeChanged(position,1);
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_file_scan_item,parent,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final FileBean bean = mDatas.get(position);
        String fileName = bean.getTvFilePath().substring(bean.getTvFilePath().lastIndexOf(File.separator)+1);
        holder.tvFileName.setText(fileName);
        holder.checkBox.setChecked(bean.isCheck());
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                bean.setCheck(isChecked);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        CheckBox checkBox;
        TextView tvFileName;
        public ViewHolder(View itemView) {
            super(itemView);
            checkBox = (CheckBox) itemView.findViewById(R.id.checkbox);
            tvFileName = (TextView) itemView.findViewById(R.id.tv_file_name);
        }
    }
}
