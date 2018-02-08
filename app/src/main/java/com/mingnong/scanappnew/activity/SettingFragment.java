package com.mingnong.scanappnew.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.mingnong.scanappnew.Contance;
import com.mingnong.scanappnew.R;
import com.mingnong.scanappnew.utils.SPUtils;

/**
 * Created by wyw on 2016/11/16.
 */

public class SettingFragment extends Fragment implements View.OnClickListener {

    EditText etProductionYear,etEffectiveYear;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_setting,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etProductionYear = (EditText) view.findViewById(R.id.et_production_year);
        etEffectiveYear = (EditText) view.findViewById(R.id.et_effective_year);
        view.findViewById(R.id.bt_save).setOnClickListener(this);
        etProductionYear.setText(SPUtils.getInstance().getData(Contance.SP_PRODUCT_YEAR, "", String.class));
        etEffectiveYear.setText(SPUtils.getInstance().getData(Contance.SP_EFFECTIVE_YEAR, "2", String.class));
    }

    @Override
    public void onClick(View view) {
        if (!checkeffectiveYear()) {
            return;
        }
        if (!checkProcutionYear()) {
            return;
        }
        SPUtils.getInstance().saveData(Contance.SP_EFFECTIVE_YEAR,etEffectiveYear.getText().toString());
        SPUtils.getInstance().saveData(Contance.SP_PRODUCT_YEAR,etProductionYear.getText().toString());
        ((InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(((MainActivity) getContext()).getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
                Snackbar.make(view,"保存成功",Snackbar.LENGTH_SHORT).show();
    }

    private boolean checkProcutionYear() {
        String s = etProductionYear.getText().toString();
        if (TextUtils.isEmpty(s)) {
            return true;
        }
        if (s.length() != 8) {
            Toast.makeText(getContext(), "请输入生产日期正确格式 例如:20160305", Toast.LENGTH_SHORT).show();
            return false;
        }
        String month = s.substring(4, 6);
        String day = s.substring(6, 8);
        if (Double.valueOf(month) > 12) {
            Toast.makeText(getContext(), "日期格式月份不能大于12", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (Double.valueOf(day) > 31) {
            Toast.makeText(getContext(), "日期格式天数不能大于31", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean checkeffectiveYear() {
        String s = etEffectiveYear.getText().toString();
        if (TextUtils.isEmpty(s)) {
            Toast.makeText(getContext(), "有效年限不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        //判断>0
        if (s.startsWith("0")) {
            Toast.makeText(getContext(), "不能以0开头", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
