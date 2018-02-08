package com.mingnong.scanappnew.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.mingnong.scanappnew.Contance;
import com.mingnong.scanappnew.R;
import com.mingnong.scanappnew.bean.ScanInputBean;
import com.mingnong.scanappnew.utils.DateUtil;
import com.mingnong.scanappnew.utils.SPUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by wyw on 2016/10/24.
 */
public class InputDialog extends Dialog {
    private final int update = 1;//recyclerview
    private final int show = 2;//网络没有数据
    private final int find = 3;//通过网络访问找到数据
    //生产日期
    Spinner etProductionYear, etProductionMonth, etProductionDay;
    //有效期
    Spinner etExpiryYear, etExpiryMonth, etExpiryDay;
    //单位
    Spinner spinnerUnit;
    //生产批号
    EditText etProductNumber;
    //采购价格
    EditText etPurchasePrice;
    //预售价格
    EditText etPresellPrice;
    //数量
    EditText etCount;
    Button btCancel, btConfirm;
    OnSelectListener listener;
    ArrayList<String> list;
    private int state;
    private String[] array;
    private ScanInputBean bean;
    private int number;
    List<String> years;
    List<String> months;
    List<String> days;
    private int yearCount = 8;//有效期年份可选个数再生产日期后推yearcount年
    private int isfirst = 0;//第一次加载初始化日期

    public InputDialog(Context context, OnSelectListener listener) {
        super(context);
        this.listener = listener;
        View parent = LayoutInflater.from(getContext()).inflate(R.layout.dialog_input, null);
        setContentView(parent);
        setTitle("以下内容不能为空");
        findViews(parent);
        bindData();
        bindListener();
//        etListening();

    }

    private void findViews(View parent) {
        etProductionYear = (Spinner) parent.findViewById(R.id.et_production_year);
        etProductionMonth = (Spinner) parent.findViewById(R.id.et_production_month);
        etProductionDay = (Spinner) parent.findViewById(R.id.et_production_day);
        etExpiryYear = (Spinner) parent.findViewById(R.id.et_expiry_year);
        etExpiryMonth = (Spinner) parent.findViewById(R.id.et_expiry_month);
        etExpiryDay = (Spinner) parent.findViewById(R.id.et_expiry_day);
        spinnerUnit = (Spinner) parent.findViewById(R.id.spinner_unit);
        etProductNumber = (EditText) parent.findViewById(R.id.et_product_number);
        etPurchasePrice = (EditText) parent.findViewById(R.id.et_purchase_price);
        etPresellPrice = (EditText) parent.findViewById(R.id.et_presell_number);
        etCount = (EditText) parent.findViewById(R.id.et_count);
        btCancel = (Button) parent.findViewById(R.id.bt_cancel);
        btConfirm = (Button) parent.findViewById(R.id.bt_confirm);
    }

    /**
     * 更新recyclerview 的数据源
     *
     * @param bean
     */
    public void update(ScanInputBean bean) {
        super.show();
        this.bean = bean;
        state = update;
        String[] productionDateArr = bean.getProductionDate().split("-");
        String[] expiryDateArr = bean.getExpiryDate().split("-");
        initSpinner(productionDateArr, expiryDateArr);
//        etProductionYear.setText(productionDateArr[0]);
//        etProductionMonth.setText(productionDateArr[1]);
//        etProductionDay.setText(productionDateArr[2]);
//        etExpiryYear.setText(expiryDateArr[0]);
//        etExpiryMonth.setText(expiryDateArr[1]);
//        etExpiryDay.setText(expiryDateArr[2]);
        String etUnit = bean.getUnit();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).equals(etUnit)) {
                spinnerUnit.setSelection(i);
                break;
            }
        }
        etCount.setText(bean.getCount());
        etProductNumber.setText(bean.getProductNumber());
        etPurchasePrice.setText(bean.getPurchasePrice());
        etPresellPrice.setText(bean.getPresellPrice());
    }

    public void show(ScanInputBean bean) {
        super.show();
        this.bean = bean;
        state = find;
        String[] productionDateArr = bean.getProductionDate().split("-|/");
        String[] expiryDateArr = bean.getExpiryDate().split("-|/");

//        etProductionYear.setText(productionDateArr[0]);
//        etProductionMonth.setText(productionDateArr[1]);
//        etProductionDay.setText(productionDateArr[2]);
//        etExpiryYear.setText(expiryDateArr[0]);
//        etExpiryMonth.setText(expiryDateArr[1]);
//        etExpiryDay.setText(expiryDateArr[2]);
        try {
            initSpinner(productionDateArr, expiryDateArr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String etUnit = bean.getUnit();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).equals(etUnit)) {
                spinnerUnit.setSelection(i);
                break;
            }
        }
        etCount.setText(bean.getCount());
        etProductNumber.setText(bean.getProductNumber());
        etPurchasePrice.setText(bean.getPurchasePrice());
        etPresellPrice.setText(bean.getPresellPrice());
    }

    //初始化日期选择spinner
    private void initSpinner(final String[] productionDateArr, final String[] expiryDateArr) {
        isfirst = 0;

        years = getYears(Integer.parseInt(productionDateArr[0].toString()));
        ArrayAdapter adapter1 = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, years
        );

        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        etProductionYear.setAdapter(adapter1);
        months = Arrays.asList(new String[]{String.valueOf(1), String.valueOf(2), String.valueOf(3), String.valueOf(4),
                String.valueOf(5), String.valueOf(6), String.valueOf(7), String.valueOf(8), String.valueOf(9), String.valueOf(10),
                String.valueOf(11), String.valueOf(12)});
        final ArrayAdapter adapter2 = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, months
        );
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        etProductionMonth.setAdapter(adapter2);

        etProductionYear.setSelection(years.indexOf(productionDateArr[0].toString()));
        etProductionYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int item = Integer.parseInt(etProductionYear.getSelectedItem().toString());
                List<String> yxyears = new ArrayList<String>();
                for (int i = 0; i < yearCount; i++) {
                    yxyears.add((item + i) + "");
                }
                ArrayAdapter adapter1 = new ArrayAdapter<String>(getContext(),
                        android.R.layout.simple_spinner_item,
                        yxyears);
                adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                etExpiryYear.setAdapter(adapter1);
                etExpiryYear.setSelection(2);


                etProductionMonth.setSelection(Integer.parseInt(productionDateArr[1]) - 1);
                etProductionMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        int index = months.indexOf(etProductionMonth.getSelectedItem().toString());
                        final ArrayAdapter adapter2 = new ArrayAdapter<String>(getContext(),
                                android.R.layout.simple_spinner_item,
                                months);
                        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        etExpiryMonth.setAdapter(adapter2);
                        etExpiryMonth.setSelection(index);

                        int maxday = getDaysByYearMonth(Integer.parseInt(etProductionYear.getSelectedItem().toString()),
                                Integer.parseInt(etProductionMonth.getSelectedItem().toString()));
                        days = new LinkedList<String>();
                        for (int i = 1; i < maxday; i++) {
                            days.add(i + "");
                        }
                        ArrayAdapter adapter3 = new ArrayAdapter<String>(getContext(),
                                android.R.layout.simple_spinner_item,
                                days);
                        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        etProductionDay.setAdapter(adapter3);

                        int mday = getDaysByYearMonth(Integer.parseInt(etProductionYear.getSelectedItem().toString()) + 2,
                                Integer.parseInt(etProductionMonth.getSelectedItem().toString()));
                        List mdays = new ArrayList();
                        for (int i = 1; i < mday; i++) {
                            mdays.add(i + "");
                        }
                        ArrayAdapter adapter4 = new ArrayAdapter<String>(getContext(),
                                android.R.layout.simple_spinner_item,
                                mdays);
                        adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        etExpiryDay.setAdapter(adapter4);
                        if (isfirst<2) {
                            isfirst++;
                            etProductionDay.setSelection(Integer.parseInt(productionDateArr[2]) - 1);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        etProductionDay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int day = Integer.parseInt(etProductionDay.getSelectedItem().toString());
                if (day == 1) {
                    if (Integer.parseInt(etProductionMonth.getSelectedItem().toString()) == 1) {
//                        final ArrayAdapter adapter2 = new ArrayAdapter<String>(getContext(),
//                                android.R.layout.simple_spinner_item,
//                                Arrays.asList(new String[]{String.valueOf(12)}));
//                        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                        etExpiryMonth.setAdapter(adapter2);
                        etExpiryMonth.setSelection(11);

//                        final ArrayAdapter adapter3 = new ArrayAdapter<String>(getContext(),
//                                android.R.layout.simple_spinner_item,
//                                Arrays.asList(new String[]{String.valueOf(Integer.parseInt(etProductionYear.getSelectedItem().toString()) + 3)}));
//                        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                        etExpiryYear.setAdapter(adapter3);
                        int index = etExpiryYear.getSelectedItemPosition();
                        etExpiryYear.setSelection(index + 1);

                    } else {
//                        final ArrayAdapter adapter3 = new ArrayAdapter<String>(getContext(),
//                                android.R.layout.simple_spinner_item,
//                                Arrays.asList(new String[]{String.valueOf(Integer.parseInt(etProductionMonth.getSelectedItem().toString()) - 1)}));
//                        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                        etExpiryMonth.setAdapter(adapter3);
                        int index = etExpiryMonth.getSelectedItemPosition();
                        etExpiryMonth.setSelection(index - 1);
//                        final ArrayAdapter adapter4 = new ArrayAdapter<String>(getContext(),
//                                android.R.layout.simple_spinner_item,
//                                Arrays.asList(new String[]{String.valueOf(Integer.parseInt(etProductionYear.getSelectedItem().toString()) + 2)}));
//                        adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                        etExpiryYear.setAdapter(adapter4);
                        etExpiryYear.setSelection(2);
                    }

                    day = getDaysByYearMonth(Integer.parseInt(etProductionYear.getSelectedItem().toString()) + 2,
                            Integer.parseInt(etExpiryMonth.getSelectedItem().toString())) - 1;
                } else {
//                    final ArrayAdapter adapter3 = new ArrayAdapter<String>(getContext(),
//                            android.R.layout.simple_spinner_item,
//                            Arrays.asList(new String[]{String.valueOf(Integer.parseInt(etProductionYear.getSelectedItem().toString()) + 2)}));
//                    adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                    etExpiryYear.setAdapter(adapter3);
                    etExpiryYear.setSelection(2);
//                    final ArrayAdapter adapter4 = new ArrayAdapter<String>(getContext(),
//                            android.R.layout.simple_spinner_item,
//                            Arrays.asList(new String[]{etProductionMonth.getSelectedItem().toString()}));
//                    adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                    etExpiryMonth.setAdapter(adapter4);
                    int index = etProductionMonth.getSelectedItemPosition();
                    etExpiryMonth.setSelection(index);
                    day--;
                }

//                final ArrayAdapter adapter2 = new ArrayAdapter<String>(getContext(),
//                        android.R.layout.simple_spinner_item,
//                        Arrays.asList(new String[]{String.valueOf(day)}));
//                adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                etExpiryDay.setAdapter(adapter2);
                int mday = etExpiryDay.getCount();
                etExpiryDay.setSelection(day - 1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        etExpiryYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                refreshDays();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        etExpiryMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                refreshDays();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        initEtListener();

    }

    //刷新有效日期
    private void refreshDays() {
        int mday = getDaysByYearMonth(Integer.parseInt(etExpiryYear.getSelectedItem().toString()),
                Integer.parseInt(etExpiryMonth.getSelectedItem().toString()));
        List mdays = new ArrayList();
        for (int i = 1; i < mday; i++) {
            mdays.add(i + "");
        }
        ArrayAdapter adapter4 = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item,
                mdays);
        int index = etExpiryDay.getSelectedItemPosition();
        adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        etExpiryDay.setAdapter(adapter4);
        if (index > mdays.size() - 1) {
            etExpiryDay.setSelection(mdays.size() - 1);
        } else {
            etExpiryDay.setSelection(index);
        }
    }

    //获取年的集合
    private List<String> getYears(int year) {
        Calendar calendar = Calendar.getInstance();
        int nowyear = calendar.get(Calendar.YEAR);
        List<String> years = new ArrayList<>();
        for (int i = (year - 4); i < nowyear + 1; i++) {
            years.add(i + "");
        }
        return years;
    }

    /**
     * 网络没找到数据时
     */
    public void show(String[] array) {
        super.show();
        number = 2;
        state = show;
        this.array = array;
//        etProductionYear.setText("");
//        etProductionMonth.setText("");
//        etProductionDay.setText("");
//        etExpiryYear.setText("");
//        etExpiryMonth.setText("");
//        etExpiryDay.setText("");
        String productYear = SPUtils.getInstance().getData(Contance.SP_PRODUCT_YEAR, "", String.class);
        String effectiveYear = SPUtils.getInstance().getData(Contance.SP_EFFECTIVE_YEAR, "", String.class);
        if (!TextUtils.isEmpty(effectiveYear)) {
            number = Integer.valueOf(effectiveYear);
        }
        if (!TextUtils.isEmpty(productYear) && productYear.length() == 8) {
            String[] productionDateArr = productYear.split("-|/");
            String[] expiryDateArr = productYear.split("-|/");
            initSpinner(productionDateArr, expiryDateArr);
//            setEditTextData(productYear);
//            etProductionYear.setText(productYear.substring(0, 4));
//            etProductionMonth.setText(productYear.substring(4, 6));
//            etProductionDay.setText(productYear.substring(6, 8));

        }
        etPurchasePrice.setText("0");
        etPresellPrice.setText("0");
        etCount.setText("1");
        etProductNumber.setText("");
    }

//    public void setEditTextData(String str) {
//
//        String[] Arraystr = new String[3];
//        try {
//            String dateStr1 = str.substring(0, 4) + "-" + str.substring(4, 6) + "-"
//                    + str.substring(6, 8);
//            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//            Date date1 = dateFormat.parse(dateStr1);
//            Calendar cal = Calendar.getInstance();
//            cal.setTime(date1);
//            cal.set(Calendar.YEAR, cal.get(Calendar.YEAR) + number);
//            cal.set(Calendar.DATE, cal.get(Calendar.DATE) - 1);
//            Arraystr = dateFormat.format(cal.getTime()).split("-");
////                    editTextEY.setText(str[0]+str[1]+str[2]);
////            etExpiryYear.setText(Arraystr[0]);
////            etExpiryMonth.setText(Arraystr[1]);
////            etExpiryDay.setText(Arraystr[2]);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//    }

//    void etListening() {
//
//        etProductionYear.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                if (jude()) {
//                    setEditTextData(etProductionYear.getText().toString() + etNumberDigits(etProductionMonth) +
//                            etNumberDigits(etProductionDay));
//                }
//            }
//        });
//        etProductionMonth.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                if (jude()) {
//                    setEditTextData(etProductionYear.getText().toString() + etNumberDigits(etProductionMonth) +
//                            etNumberDigits(etProductionDay));
//                }
//            }
//        });
//        etProductionDay.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                if (jude()) {
//                    setEditTextData(etProductionYear.getText().toString() + etNumberDigits(etProductionMonth) +
//                            etNumberDigits(etProductionDay));
//                }
//            }
//        });
//    }
//
//    public boolean jude() {
//        if (TextUtils.isEmpty(etProductionYear.getText().toString()) && TextUtils.isEmpty(etProductionMonth.getText().toString())
//                && TextUtils.isEmpty(etProductionDay.getText().toString())) {
//            return false;
//        }
//
//        if ((etProductionYear.getText().toString() + etNumberDigits(etProductionMonth) +
//                etNumberDigits(etProductionDay)).length() != 8) {
//            return false;
//        }
//        return true;
//    }

    private void initEtListener() {
        etProductNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 8) return;
                if (s.length() >= 4) {
                    String year = String.valueOf(s).substring(0, 4);
                    if (!year.equals(etProductionYear.getSelectedItem().toString())) {
                        if (years.contains(year)) {
                            int index = years.indexOf(year);
                            etProductionYear.setSelection(index);
                        }
                    }

                    if (s.length() >= 6) {
                        String month = String.valueOf(s).substring(4, 6);
                        if (!month.equals(etProductionMonth.getSelectedItem().toString())) {
                            if (month.substring(0, 1).equals("0")) {
                                month = month.substring(1, 2);
                            }
                            if (months.contains(month)) {
                                int index = months.indexOf(month);
                                etProductionMonth.setSelection(index);
                            }
                        }
                        if (s.length() >= 8) {
                            String day = String.valueOf(s).substring(6, 8);
                            if (!day.equals(etProductionDay.getSelectedItem().toString())) {
                                if (day.substring(0, 1).equals("0")) {
                                    day = day.substring(1, 2);
                                }
                                if (days.contains(day)) {
                                    int index = days.indexOf(day);
                                    etProductionDay.setSelection(index);
                                }
                            }

                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    public String etNumberDigits(String date) {
        if (date.length() == 1) {
            return "0" + date;
        }
        if (date.length() == 2) {
            return date;
        }
        return "";
    }

    private void bindData() {
        list = new ArrayList<>();
        list.add("盒");
        list.add("袋");
        list.add("瓶");
        list.add("支");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, list);
        spinnerUnit.setAdapter(adapter);
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
                if (check() && listener != null) {
                    String produtionData = etProductionYear.getSelectedItem().toString() + "-" +
                            etNumberDigits(etProductionMonth.getSelectedItem().toString()) + "-" + etNumberDigits(etProductionDay.getSelectedItem().toString());
                    String expiryData = etExpiryYear.getSelectedItem().toString() + "-" +
                            etNumberDigits(etExpiryMonth.getSelectedItem().toString()) + "-" + etNumberDigits(etExpiryDay.getSelectedItem().toString());
                    ScanInputBean bean;
                    if (state == show) {
                        bean = new ScanInputBean(array[0], array[1], array[2], array[3], array[4], produtionData, expiryData,
                                etProductNumber.getText().toString(), spinnerUnit.getSelectedItem().toString(),
                                etPurchasePrice.getText().toString(), etPresellPrice.getText().toString());
                        listener.onClick(bean);
                    } else if (state == update) {
                        bean = InputDialog.this.bean;
                        bean.setCount(etCount.getText().toString());
                        bean.setProductionDate(produtionData);
                        bean.setExpiryDate(expiryData);
                        bean.setProductNumber(etProductNumber.getText().toString());
                        bean.setUnit(spinnerUnit.getSelectedItem().toString());
                        bean.setPurchasePrice(etPurchasePrice.getText().toString());
                        bean.setPresellPrice(etPresellPrice.getText().toString());
                        listener.update();
                    } else if (state == find) {
                        bean = InputDialog.this.bean;
                        bean.setCount(etCount.getText().toString());
                        bean.setProductionDate(produtionData);
                        bean.setExpiryDate(expiryData);
                        bean.setProductNumber(etProductNumber.getText().toString());
                        bean.setUnit(spinnerUnit.getSelectedItem().toString());
                        bean.setPurchasePrice(etPurchasePrice.getText().toString());
                        bean.setPresellPrice(etPresellPrice.getText().toString());
                        listener.onClick(bean);
                    }
//                    listener.onClick(produtionData,expiryData,etProductNumber.getText().toString(),
//                            spinnerUnit.getSelectedItem().toString(),etPurchasePrice.getText().toString(),
//                            etPresellPrice.getText().toString(),etCount.getText().toString());
                    dismiss();
                }
            }
        });
    }

    //获取年月对应的
    private int getDaysByYearMonth(int year, int month) {
        Calendar a = Calendar.getInstance();
        a.set(Calendar.YEAR, year);
        a.set(Calendar.MONTH, month - 1);
        a.set(Calendar.DATE, 1);
        a.roll(Calendar.DATE, -1);
        int maxDate = a.get(Calendar.DATE);
        return maxDate + 1;
    }


    private boolean check() {
//        if (isEmpty(etProductionYear) || length(etProductionYear) != 4) {
//            Toast.makeText(getContext(), "生产日期-年输入格式不正确,例如: 2015、2020", Toast.LENGTH_SHORT).show();
//            return false;
//        }
//        if (isEmpty(etExpiryYear) || length(etExpiryYear) != 4) {
//            Toast.makeText(getContext(), "有效日期-年输入格式不正确,例如: 2015、2020", Toast.LENGTH_SHORT).show();
//            return false;
//        }
//        if (!isEmpty(etProductionMonth)) {
//            if (Integer.valueOf(etProductionMonth.getText().toString()) > 12 || Integer.valueOf(etProductionMonth.getText().toString()) < 1) {
//                Toast.makeText(getContext(), "生产日期-月输入不合法", Toast.LENGTH_SHORT).show();
//                return false;
//            }
//        }

//        if (isEmpty(etProductionMonth) || etNumberDigits(etProductionMonth).length() != 2) {
//            Toast.makeText(getContext(), "生产日期-月输入格式不正确,例如: 03、12", Toast.LENGTH_SHORT).show();
//            return false;
//        }
//        if (isEmpty(etExpiryMonth) || length(etExpiryMonth) != 2) {
//            Toast.makeText(getContext(), "有效日期-月输入格式不正确,例如: 03、12", Toast.LENGTH_SHORT).show();
//            return false;
//        }
//        if (!isEmpty(etExpiryMonth)) {
//            if (Integer.valueOf(etExpiryMonth.getText().toString()) > 12 || Integer.valueOf(etExpiryMonth.getText().toString()) < 1) {
//                Toast.makeText(getContext(), "有效日期-月输入不合法", Toast.LENGTH_SHORT).show();
//                return false;
//            }
//        }
//        if (isEmpty(etProductionDay) || etNumberDigits(etProductionDay).length() != 2) {
//            Toast.makeText(getContext(), "生产日期-日输入格式不正确,例如: 03、14", Toast.LENGTH_SHORT).show();
//            return false;
//        }
//        if (isEmpty(etExpiryDay) || length(etExpiryDay) != 2) {
//            Toast.makeText(getContext(), "有效日期-日输入格式不正确,例如: 03、14", Toast.LENGTH_SHORT).show();
//            return false;
//        }
        if (TextUtils.isEmpty(etProductionYear.getSelectedItem().toString()) || TextUtils.isEmpty(etProductionYear.getSelectedItem().toString())
                || TextUtils.isEmpty(etProductionMonth.getSelectedItem().toString()) ||
                TextUtils.isEmpty(etProductionDay.getSelectedItem().toString()) ||
                TextUtils.isEmpty(etExpiryYear.getSelectedItem().toString()) ||
                TextUtils.isEmpty(etExpiryMonth.getSelectedItem().toString()) ||
                TextUtils.isEmpty(etExpiryDay.getSelectedItem().toString())) {
            Toast.makeText(getContext(), "生产日期或有效日期不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (isEmpty(etProductNumber)) {
            Toast.makeText(getContext(), "生产批号不能是空", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (isEmpty(etPurchasePrice)) {
            Toast.makeText(getContext(), "采购价格不能是空,没有请填写0", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (isEmpty(etPresellPrice)) {
            Toast.makeText(getContext(), "预售价格不能是空,没有请填写0", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (isEmpty(etCount)) {
            Toast.makeText(getContext(), "数量不能是空,没有请填写0", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (dotCount(etPurchasePrice.getText().toString()) > 1) {
            Toast.makeText(getContext(), "采购价格小数点只能有一个", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (dotCount(etPresellPrice.getText().toString()) > 1) {
            Toast.makeText(getContext(), "预售价格小数点只能有一个", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (etPurchasePrice.getText().toString().startsWith(".")) {
            Toast.makeText(getContext(), "采购价格不能以小数点开头", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (etPresellPrice.getText().toString().startsWith(".")) {
            Toast.makeText(getContext(), "预售价格不能以小数点开头", Toast.LENGTH_SHORT).show();
            return false;
        }
//        if (DateUtil.isValidDates(etExpiryYear.getSelectedItem().toString() + etExpiryMonth.getSelectedItem().toString() + etExpiryDay.getSelectedItem().toString())) {
//            Toast.makeText(getContext(), "有效日期-日输入不合法", Toast.LENGTH_SHORT).show();
//            return false;
//        }
//
//        if (DateUtil.isValidDates(etProductionYear.getSelectedItem().toString() + etNumberDigits(etProductionMonth.getSelectedItem().toString()) + etNumberDigits(etProductionDay.getSelectedItem().toString()))) {
//            Toast.makeText(getContext(), "生产日期-日输入不合法", Toast.LENGTH_SHORT).show();
//            return false;
//        }
        if (DateUtil.compareNewDate(etProductionYear.getSelectedItem().toString() + "/"
                + etNumberDigits(etProductionMonth.getSelectedItem().toString()) + "/" + etNumberDigits(etProductionDay.getSelectedItem().toString()))) {
            Toast.makeText(getContext(), "生产日期不能大于当前日期", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (DateUtil.compareDate(etProductionYear.getSelectedItem().toString() + "/" + etNumberDigits(etProductionMonth.getSelectedItem().toString()) + "/" + etNumberDigits(etProductionDay.getSelectedItem().toString()),
                etExpiryYear.getSelectedItem().toString() + "/" + etExpiryMonth.getSelectedItem().toString() + "/" + etExpiryDay.getSelectedItem().toString())) {
            Toast.makeText(getContext(), "生产日期不能大于有效日期", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private int dotCount(String s) {
        String[] arr = s.split("");
        int count = 0;
        for (int i = 0; i < arr.length; i++) {
            if (".".equals(arr[i])) {
                count++;
            }
        }
        return count;
    }

    private boolean isEmpty(EditText et) {
        return TextUtils.isEmpty(et.getText().toString());
    }

    private int length(EditText et) {
        return et.getText().toString().length();
    }

    public interface OnSelectListener {
        void onClick(ScanInputBean bean);

        void update();
    }
}
