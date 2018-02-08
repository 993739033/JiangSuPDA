package com.mingnong.scanappnew.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.mingnong.scanappnew.R;
import com.mingnong.scanappnew.bean.ScanInputBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wyw on 2016/10/20.
 * 入库的adapter
 */
public class ScanInputAdapter extends RecyclerView.Adapter<ScanInputAdapter.ViewHolder> {
    private List<ScanInputBean> mDatas;
    private Context mContext;
    private LayoutInflater inflater;

    public ScanInputAdapter(Context mContext) {
        this.mContext = mContext;
        inflater = LayoutInflater.from(mContext);
    }
    public ScanInputAdapter(Context mContext, List<ScanInputBean> mDatas) {
        this.mContext = mContext;
        this.mDatas=mDatas;
        inflater = LayoutInflater.from(mContext);
    }

    public void setDatas(List<ScanInputBean> mDatas) {
        if (this.mDatas == null) {
            this.mDatas = new ArrayList<>();
        } else {
            this.mDatas = mDatas;
        }
        notifyDataSetChanged();
    }

    public void addItem(ScanInputBean bean) {
        if (this.mDatas == null) {
            this.mDatas = new ArrayList<>();
        }
//        if (this.mDatas.size() >= 50) {
//            Toast.makeText(mContext, "数据太多了,请先保存", Toast.LENGTH_SHORT).show();
//            return;
//        }
        //追溯码唯一判断
        boolean isRepeat = false;
        int repeatPosition = 0;
        for (int i = 0; i < mDatas.size(); i++) {
            if (bean.getZhuiSuMa().equals(mDatas.get(i).getZhuiSuMa())) {
                isRepeat =true;
                repeatPosition =  i +1;
                break;
            }
        }
        if (!isRepeat) {
            this.mDatas.add(bean);
            notifyDataSetChanged();
        } else {
            Toast.makeText(mContext, "输入的追溯码与第"+repeatPosition+"条重复", Toast.LENGTH_SHORT).show();
        }
    }

    public List<ScanInputBean> getmDatas() {
        if (this.mDatas == null) {
            this.mDatas = new ArrayList<>();
        }
        return mDatas;
    }

    public void clear() {
        this.mDatas.clear();
        notifyDataSetChanged();
    }

    public void deleteItem(int position) {
        if (this.mDatas == null) {
            this.mDatas = new ArrayList<>();
        }
        mDatas.remove(position);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.adapter_scanner_input,parent,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String s = "第 "+(position + 1) + " 条";
        holder.tvNumber.setText(s);
        holder.tvName.setText(mDatas.get(position).getName());
        holder.tvPhone.setText(mDatas.get(position).getPhone());
        holder.tvTongYongMing.setText(mDatas.get(position).getTongYongMing());
        holder.tvWenHao.setText(mDatas.get(position).getWenHao());
        holder.tvZhuiSuMa.setText(mDatas.get(position).getZhuiSuMa());
        holder.tvCount.setText(mDatas.get(position).getCount());
        holder.tvProdutionDate.setText(mDatas.get(position).getProductionDate());
        holder.tvExpiryDate.setText(mDatas.get(position).getExpiryDate());
        holder.tvProdutionNumber.setText(mDatas.get(position).getProductNumber());
        holder.tvUnit.setText(mDatas.get(position).getUnit());
        holder.tvPurchasePrice.setText(mDatas.get(position).getPurchasePrice());
        holder.tvPresellPrice.setText(mDatas.get(position).getPresellPrice());
    }

    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvZhuiSuMa,tvTongYongMing,tvWenHao,tvName,tvPhone,tvNumber,tvCount,
            tvProdutionDate,tvExpiryDate,tvProdutionNumber,tvUnit,tvPurchasePrice,tvPresellPrice;
        public ViewHolder(View view) {
            super(view);
            tvNumber = (TextView) view.findViewById(R.id.tv_number);
            tvZhuiSuMa = (TextView) view.findViewById(R.id.tv_zhuisuma);
            tvTongYongMing = (TextView) view.findViewById(R.id.tv_tongyongming);
            tvWenHao = (TextView) view.findViewById(R.id.tv_wenhao);
            tvName = (TextView) view.findViewById(R.id.tv_name);
            tvPhone = (TextView) view.findViewById(R.id.tv_phone);
            tvCount = (TextView) view.findViewById(R.id.tv_count);
            tvProdutionDate = (TextView) view.findViewById(R.id.tv_production_date);
            tvExpiryDate = (TextView) view.findViewById(R.id.tv_expiry_date);
            tvProdutionNumber = (TextView) view.findViewById(R.id.tv_produte_number);
            tvUnit = (TextView) view.findViewById(R.id.tv_unit);
            tvPurchasePrice = (TextView) view.findViewById(R.id.tv_purchase_price);
            tvPresellPrice = (TextView) view.findViewById(R.id.tv_presell_price);
        }
    }
}
