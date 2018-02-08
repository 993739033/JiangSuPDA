package com.mingnong.scanappnew.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.mingnong.scanappnew.R;
import com.mingnong.scanappnew.bean.ScanOutputBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wyw on 2016/12/1.
 * 出库adapter
 */

public class ScanOutputAdapter extends RecyclerView.Adapter<ScanOutputAdapter.ViewHolder>{
    private List<ScanOutputBean> mDatas;
    private Context mContext;
    private LayoutInflater inflater;

    public ScanOutputAdapter(Context mContext) {
        this.mContext = mContext;
        inflater = LayoutInflater.from(mContext);
    }
    public ScanOutputAdapter(Context mContext, List<ScanOutputBean> mDatas) {
        this.mContext = mContext;
        this.mDatas=mDatas;
        inflater = LayoutInflater.from(mContext);
    }

    public void setDatas(List<ScanOutputBean> mDatas) {
        if (this.mDatas == null) {
            this.mDatas = new ArrayList<>();
        } else {
            this.mDatas = mDatas;
        }
        notifyDataSetChanged();
    }

    public void addItem(ScanOutputBean bean) {
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
            if (bean.getTradeCode().equals(mDatas.get(i).getTradeCode())) {
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

    public List<ScanOutputBean> getmDatas() {
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
    public ScanOutputAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ScanOutputAdapter.ViewHolder(inflater.inflate(R.layout.adapter_scanner_output,parent,false));
    }

    @Override
    public void onBindViewHolder(ScanOutputAdapter.ViewHolder holder, int position) {
        String s = "第 "+(position + 1) + " 条";
        holder.tvNumber.setText(s);
        holder.tvZhuiSuMa.setText(mDatas.get(position).getTradeCode());
        holder.tvEnterprise.setText(mDatas.get(position).getFProductEnterprise());
        holder.tvSupplier.setText(mDatas.get(position).getFGysmc());
        holder.tvProductName.setText(mDatas.get(position).getFProductName());
        holder.tvRelativeCertification.setText(mDatas.get(position).getFXgzsh());
        holder.tvGeneralName.setText(mDatas.get(position).getFTyName());
        holder.tvProductionDate.setText(mDatas.get(position).getFScDate());
        holder.tvExpiryDate.setText(mDatas.get(position).getFYxqDate());
        holder.tvSpecification.setText(mDatas.get(position).getFGuige());
        holder.tvUnit.setText(mDatas.get(position).getFDw());
        holder.tvAgreeWenHao.setText(mDatas.get(position).getFPzwh());
        holder.tvDrugType.setText(mDatas.get(position).getYplx());
        holder.tvCount.setText(TextUtils.isEmpty(mDatas.get(position).getFBuyNum())?
                "0":mDatas.get(position).getFBuyNum());
        holder.tvPresellPrice.setText(TextUtils.isEmpty(mDatas.get(position).getFHdjg())?
                "0":mDatas.get(position).getFHdjg());
    }

    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        //追溯码
        TextView tvZhuiSuMa;
        //生产企业
        TextView tvEnterprise;
        //供应商
        TextView tvSupplier;
        //产品名
        TextView tvProductName;
        //相关证书号
        TextView tvRelativeCertification;
        //通用名
        TextView tvGeneralName;
        //生产日期 有效期
        TextView tvProductionDate,tvExpiryDate;
        //规格 单位
        TextView tvSpecification,tvUnit;
        //批准文号 药品类型
        TextView tvAgreeWenHao,tvDrugType;
        // 数量
        TextView tvCount;
        //预售价格
        TextView tvPresellPrice;
        //第几条
        TextView tvNumber;
        public ViewHolder(View view) {
            super(view);
            tvNumber = (TextView) view.findViewById(R.id.tv_number);
            tvZhuiSuMa = (TextView) view.findViewById(R.id.tv_zhuisuma);
            tvEnterprise = (TextView) view.findViewById(R.id.tv_enterprise);
            tvSupplier = (TextView) view.findViewById(R.id.tv_supplier);
            tvProductName = (TextView) view.findViewById(R.id.tv_producte_name);
            tvRelativeCertification = (TextView) view.findViewById(R.id.tv_relative_certification);
            tvGeneralName = (TextView) view.findViewById(R.id.tv_general_name);
            tvProductionDate = (TextView) view.findViewById(R.id.tv_production_date);
            tvExpiryDate = (TextView) view.findViewById(R.id.tv_expiry_date);
            tvSpecification = (TextView) view.findViewById(R.id.tv_specification);
            tvUnit = (TextView) view.findViewById(R.id.tv_unit);
            tvAgreeWenHao = (TextView) view.findViewById(R.id.tv_agree_wenhao);
            tvDrugType = (TextView) view.findViewById(R.id.tv_drug_type);
            tvCount = (TextView) view.findViewById(R.id.tv_count);
            tvPresellPrice = (TextView) view.findViewById(R.id.tv_presell_price);
        }
    }
}
