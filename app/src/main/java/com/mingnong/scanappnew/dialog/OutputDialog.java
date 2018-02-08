package com.mingnong.scanappnew.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mingnong.scanappnew.R;
import com.mingnong.scanappnew.bean.ScanOutputBean;

/**
 * Created by wyw on 2016/10/24.
 * 出库dialog
 */
public class OutputDialog extends Dialog {
    private final int update =1;//recyclerview
    private final int find = 2;//通过网络访问找到数据
    //追溯码
    private TextView tvZhuiSuMa;
    //生产企业
    private TextView tvEnterprise;
    //供应商
    private TextView tvSupplier;
    //产品名
    private TextView tvProductName;
    //相关证书号
    private TextView tvRelativeCertification;
    //通用名
    private TextView tvGeneralName;
    //生产日期 有效期
    private TextView tvProductionDate,tvExpiryDate;
    //规格 单位
    private TextView tvSpecification,tvUnit;
    //批准文号 药品类型
    private TextView tvAgreeWenHao,tvDrugType;
    // 数量
    private TextView tvCount;
    //预售价格
    private EditText etPresellPrice;
    private Button btCancel,btConfirm;
    OnSelectListener listener;
    private int state;
    private ScanOutputBean bean;

    public OutputDialog(Context context, OnSelectListener listener) {
        super(context);
        this.listener = listener;
        View parent = LayoutInflater.from(getContext()).inflate(R.layout.dialog_output,null);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(parent);
        findViews(parent);
        bindData();
        bindListener();
    }

    private void findViews(View parent) {
        //追溯码
        tvZhuiSuMa = (TextView) parent.findViewById(R.id.tv_zhuisuma);
        //生产企业
        tvEnterprise = (TextView) parent.findViewById(R.id.tv_enterprise);
        //供应商
        tvSupplier = (TextView) parent.findViewById(R.id.tv_supplier);
        //产品名
        tvProductName = (TextView) parent.findViewById(R.id.tv_producte_name);
        //相关证书号
        tvRelativeCertification = (TextView) parent.findViewById(R.id.tv_relative_certification);
        //通用名
        tvGeneralName = (TextView) parent.findViewById(R.id.tv_general_name);
        //生产日期 有效期
        tvProductionDate = (TextView) parent.findViewById(R.id.tv_production_date);
        tvExpiryDate = (TextView) parent.findViewById(R.id.tv_expiry_date);
        //规格 单位
        tvSpecification = (TextView) parent.findViewById(R.id.tv_specification);
        tvUnit = (TextView) parent.findViewById(R.id.tv_unit);
        //批准文号 药品类型
        tvAgreeWenHao = (TextView) parent.findViewById(R.id.tv_agree_wenhao);
        tvDrugType = (TextView) parent.findViewById(R.id.tv_drug_type);
        // 数量
        tvCount = (TextView) parent.findViewById(R.id.tv_count);
        //预售价格
        etPresellPrice = (EditText) parent.findViewById(R.id.et_presell_price);
        btCancel = (Button) parent.findViewById(R.id.bt_cancel);
        btConfirm = (Button) parent.findViewById(R.id.bt_confirm);
    }

    /**
     * 更新recyclerview 的数据源
     * @param bean
     */
    public void update(ScanOutputBean bean) {
        super.show();
        this.bean = bean;
        state = update;
        tvZhuiSuMa.setText(bean.getTradeCode());
        tvEnterprise.setText(bean.getFProductEnterprise());
        tvSupplier.setText(bean.getFGysmc());
        tvProductName.setText(bean.getFProductName());
        tvRelativeCertification.setText(bean.getFXgzsh());
        tvGeneralName.setText(bean.getFTyName());
        tvProductionDate.setText(bean.getFScDate());
        tvExpiryDate.setText(bean.getFYxqDate());
        tvSpecification.setText(bean.getFGuige());
        tvUnit.setText(bean.getFDw());
        tvAgreeWenHao.setText(bean.getFPzwh());
        tvDrugType.setText(bean.getYplx());
        tvCount.setText(TextUtils.isEmpty(bean.getFBuyNum())?"0":bean.getFBuyNum());
        etPresellPrice.setText(TextUtils.isEmpty(bean.getFHdjg())?"0":bean.getFHdjg());
    }

    public void show(ScanOutputBean bean) {
        super.show();
        this.bean = bean;
        state = find;
        tvZhuiSuMa.setText(bean.getTradeCode());
        tvEnterprise.setText(bean.getFProductEnterprise());
        tvSupplier.setText(bean.getFGysmc());
        tvProductName.setText(bean.getFProductName());
        tvRelativeCertification.setText(bean.getFXgzsh());
        tvGeneralName.setText(bean.getFTyName());
        tvProductionDate.setText(bean.getFScDate());
        tvExpiryDate.setText(bean.getFYxqDate());
        tvSpecification.setText(bean.getFGuige());
        tvUnit.setText(bean.getFDw());
        tvAgreeWenHao.setText(bean.getFPzwh());
        tvDrugType.setText(bean.getYplx());
        tvCount.setText(TextUtils.isEmpty(bean.getFBuyNum())?"0":bean.getFBuyNum());
        etPresellPrice.setText(TextUtils.isEmpty(bean.getFHdjg())?"0":bean.getFHdjg());
    }

    private void bindData() {
    }

    private void bindListener() {
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        btConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (check() && listener!=null) {
                    bean.setFHdjg(etPresellPrice.getText().toString());
                    if (state == update) {
                        listener.update();
                    } else if (state == find){
                        listener.onClick(bean);
                    }
                    dismiss();
                }
            }
        });
    }

    private boolean check() {
        if (TextUtils.isEmpty(etPresellPrice.getText().toString()) ||
                etPresellPrice.getText().toString().startsWith(".") ||
                etPresellPrice.getText().toString().startsWith("00")) {
            Toast.makeText(getContext(), "价格输入不符合规则", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    public interface OnSelectListener{
        void onClick(ScanOutputBean bean);

        void update();
    }
}
