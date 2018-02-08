package com.mingnong.scanappnew.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mingnong.scanappnew.Contance;
import com.mingnong.scanappnew.R;
import com.mingnong.scanappnew.bean.ScanInputBean;
import com.mingnong.scanappnew.bean.ScanInputNetworkBean;
import com.mingnong.scanappnew.bean.ScanOutputBean;
import com.mingnong.scanappnew.bean.ScanOutputNetworkBean;
import com.mingnong.scanappnew.net.RequestCallBack;
import com.mingnong.scanappnew.utils.SPUtils;
import com.mingnong.scanappnew.utils.StatusBarUtil;
import com.zhy.m.permission.MPermissions;
import com.zhy.m.permission.PermissionDenied;
import com.zhy.m.permission.PermissionGrant;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import cn.finalteam.okhttpfinal.HttpRequest;
import cn.finalteam.okhttpfinal.RequestParams;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public static final String SCN_CUST_ACTION_SCODE = "com.mingnong.scannerappnew";
    public static final String SCN_CUST_EX_SCODE = "scannerdata";

    private List<ScanInputBean> inputBeanList = new ArrayList<>();
    private List<ScanOutputBean> outputBeanList = new ArrayList<>();
    private Menu menu;
    long firstTime = 0;
    Fragment content;//当前显示的fragment
    FragmentManager fm;
    String[] tags = {"input", "output", "upload", "setting"};
    ProgressDialog dialog;
    private String[] repString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StatusBarUtil.setColor(this, R.color.colorPrimary);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("扫描App");
        setSupportActionBar(toolbar);
        initBroadcastReciever();
        try {
            getFileList();
        } catch (Exception e) {
            Toast.makeText(this, "读取本地数据失败", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
//        initBroadcastReciever();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        stateCheck(savedInstanceState);
        //权限判断
        if (Build.VERSION.SDK_INT >= 23) {
            if (!MPermissions.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, 100)) {
                MPermissions.requestPermissions(this, 100, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE});
            }
        }
        dialog = new ProgressDialog(this);
        dialog.setMessage("加载中");
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
    }


    //获取本地保存的数据
    public void getFileList() throws Exception {
        inputBeanList.clear();
        outputBeanList.clear();

        new Thread() {
            @Override
            public void run() {
                File inputStore = new File(Environment.getExternalStorageDirectory(), "出入库管理" + File.separator + "入库");
                if (!inputStore.exists()) {
                    inputStore.mkdirs();
                }
                for (File file : inputStore.listFiles()) {
                    String json = getFileContent(file);
                    json = json.substring(1, json.length() - 1);
                    try {
                        ScanInputBean bean1 = new Gson().fromJson(json, ScanInputBean.class);
                        inputBeanList.add(bean1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                File outputStore = new File(Environment.getExternalStorageDirectory(), "出入库管理" + File.separator + "出库");
                if (!outputStore.exists()) {
                    outputStore.mkdirs();
                }
                for (File file : outputStore.listFiles()) {
                    String json = getFileContent(file);
                    json = json.substring(1, json.length() - 1);
                    ScanOutputBean bean = new Gson().fromJson(json, ScanOutputBean.class);
                    outputBeanList.add(bean);
                }

            }
        }.start();
    }

    //获取文件信息
    public String getFileContent(File file) {
        BufferedReader br = null;
        StringBuffer sb = null;
        try {
            br = new BufferedReader(new FileReader(file));
            String readline = "";
            sb = new StringBuffer();
            while ((readline = br.readLine()) != null) {
                sb.append(readline);
            }
            System.out.println("读取成功：" + sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    /**
     * 初始化广播接收器，AUTOID系列安卓产品上的系统软件扫描工具相对应
     */
    private void initBroadcastReciever() {
        // 发送广播到扫描工具内的应用设置项
        Intent intent = new Intent("com.android.scanner.service_settings");
        // 修改扫描工具内应用设置中的开发者项下的广播名称
        intent.putExtra("action_barcode_broadcast", SCN_CUST_ACTION_SCODE);
        // 修改扫描工具内应用设置下的条码发送方式为 "广播"
        intent.putExtra("barcode_send_mod e", "BROADCAST");
        // 修改扫描工具内应用设置下的结束符为"NONE"
        intent.putExtra("endchar", "NONE");

        sendBroadcast(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver();

    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(receiver);
    }

    /**
     * 注册广播接收器
     */
    private void registerReceiver() {
        IntentFilter filter = new IntentFilter(SCN_CUST_ACTION_SCODE);
        registerReceiver(receiver, filter);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        MPermissions.onRequestPermissionsResult(this, 100, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @PermissionGrant(100)
    public void permissionSuccess() {
        Toast.makeText(this, "权限申请成功", Toast.LENGTH_SHORT).show();
    }

    @PermissionDenied(100)
    public void permissionFail() {
        Toast.makeText(this, "权限申请失败,程序将不能正常运行", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //当有扫描按钮的时候 一定是在入库 或者 出库界面
//        if (id == R.id.action_scan){
//            startActivityForResult(new Intent(this, CaptureActivity.class),2300);
//        }
        if (id == R.id.action_delete) {
            InputFragment inputFragment = (InputFragment) fm.findFragmentByTag(tags[0]);
            OutputFragment outputFragment = (OutputFragment) fm.findFragmentByTag(tags[1]);
            if (type == INPUT) {
                inputFragment.delete();//显示对话框
            } else if (type == OUTPUT) {
                outputFragment.delete();//显示对话框
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private static final int INPUT = 0;//入库
    private static final int OUTPUT = 1;//出库
    private int type = INPUT;//出库还是入库

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        try {
            getFileList();
        } catch (Exception e) {
            Toast.makeText(this, "获取本地数据失败", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.item_input) {
            type = INPUT;
            //入库
            Fragment to = fm.findFragmentByTag(tags[0]);
            if (to == null) {
                to = new InputFragment();
            }
            switchContent(content, to, 0);
            menu.setGroupVisible(R.id.group_scan, true);
//            menu.setGroupVisible(R.id.group_file,false);
//            mainFragment.getFab().setVisibility(View.VISIBLE);
        } else if (id == R.id.item_output) {
            type = OUTPUT;
            //出库
            Fragment to = fm.findFragmentByTag(tags[1]);
            if (to == null) {
                to = new OutputFragment();
            }
            switchContent(content, to, 1);
            menu.setGroupVisible(R.id.group_scan, true);
//            menu.setGroupVisible(R.id.group_file,false);
//            mainFragment.getFab().setVisibility(View.VISIBLE);
        } else if (id == R.id.item_upload) {
            //保存数据
            InputFragment inputFragment = (InputFragment) fm.findFragmentByTag(tags[0]);
            OutputFragment outputFragment = (OutputFragment) fm.findFragmentByTag(tags[1]);
            if (inputFragment != null) {
                inputFragment.save(true);
            }
            if (outputFragment != null) {
                outputFragment.save(true);
            }

            //上传页面
//            Fragment to = new FileScanFragment();
            Fragment to = fm.findFragmentByTag(tags[2]);
            if (to == null) {
                to = new FileScanFragment();
            }
            switchContent(content, to, 2);
            menu.setGroupVisible(R.id.group_scan, false);
//            menu.setGroupVisible(R.id.group_file,true);
        } else if (id == R.id.item_setting) {
            //设置页面
            Fragment to = fm.findFragmentByTag(tags[3]);
            if (to == null) {
                to = new SettingFragment();
            }
            switchContent(content, to, 3);
            menu.setGroupVisible(R.id.group_scan, false);
//            menu.setGroupVisible(R.id.group_file,false);
//            mainFragment.getFab().setVisibility(View.GONE);
        } else if (id == R.id.item_save) {
            InputFragment inputFragment = (InputFragment) fm.findFragmentByTag(tags[0]);
            OutputFragment outputFragment = (OutputFragment) fm.findFragmentByTag(tags[1]);
            if (inputFragment == null && outputFragment == null) {
                Toast.makeText(this, "请打开入库或出库界面", Toast.LENGTH_SHORT).show();
                return true;
            }
            if (inputFragment != null) {
                inputFragment.save(false);
            }
            if (outputFragment != null) {
                outputFragment.save(false);
            }

        } else if (id == R.id.re_login) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return true;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * fragment 切换
     *
     * @param from
     * @param to
     */
    public void switchContent(Fragment from, Fragment to, int position) {
        if (content != to && from != to) {
            content = to;
//            if (!to.isHidden()) {
//                return;
//            }
            FragmentTransaction transaction = fm.beginTransaction();
            transaction.setCustomAnimations(R.anim.push_left_in,
                    R.anim.fade_out);
            if (!to.isAdded()) { // 先判断是否被add过
                transaction.hide(from)
                        .add(R.id.layout_main, to, tags[position]).commit(); // 隐藏当前的fragment，add下一个到Activity中
            } else {
//                Toast.makeText(this, "已经处于当前界面", Toast.LENGTH_SHORT).show();
                transaction.hide(from).show(to).commit(); // 隐藏当前的fragment，显示下一个
            }
        }
    }

    /**
     * 状态检测 用于内存不足的时候保证fragment不会重叠
     *
     * @param savedInstanceState
     */
    private void stateCheck(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            fm = getSupportFragmentManager();
            FragmentTransaction fts = fm.beginTransaction();
            InputFragment mainfragment = new InputFragment();
            content = mainfragment;
            fts.add(R.id.layout_main, mainfragment, tags[0]);
            fts.commit();
        } else {
            InputFragment inputFragment = (InputFragment) getSupportFragmentManager().findFragmentByTag(tags[0]);
            OutputFragment outputFragment = (OutputFragment) getSupportFragmentManager().findFragmentByTag(tags[1]);
            FileScanFragment fileScanFragment = (FileScanFragment) getSupportFragmentManager().findFragmentByTag(tags[2]);
            SettingFragment settingFragment = (SettingFragment) getSupportFragmentManager().findFragmentByTag(tags[3]);
            content = inputFragment;
            fm = getSupportFragmentManager();
            fm.beginTransaction().show(inputFragment).hide(outputFragment).hide(fileScanFragment)
                    .hide(settingFragment).commit();
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            long secondTime = System.currentTimeMillis();

            if (secondTime - firstTime > 800) {// 如果两次按键时间间隔大于800毫秒，则不退出
                Toast.makeText(MainActivity.this, "再按一次退出程序...",
                        Toast.LENGTH_SHORT).show();
                firstTime = secondTime;// 更新firstTime
                return true;
            } else {
//                secheFile();
                System.exit(0);// 否则退出程序
            }
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) return;
        //            201610270012800019960407，恩诺沙星粉（水产用），兽药字（2012）100589107，日升昌生物，025-84276544
        String result = data.getStringExtra("result");
        if (!TextUtils.isEmpty(result)) {
            ///201512270003700019840759，注射用硫酸链霉素，兽药字（2015）220442658，泰信，0825-5895608
            final String[] replaceString;
            try {
                replaceString = result.replaceAll("，", ",").split(",");
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }

            if (type == INPUT) {
                final InputFragment inputFragment = (InputFragment) fm.findFragmentByTag(tags[0]);
                //入库
                RequestParams params = new RequestParams();
                params.addFormDataPart("httpType", "1");
                params.addFormDataPart("USERID", SPUtils.getInstance().getData(Contance.USERID, "", String.class));
                params.addFormDataPart("tradeCode", replaceString[0]);
                dialog.show();
                HttpRequest.post(Contance.BASE_URL + "getNYBProduct.ashx", params, new RequestCallBack<ScanInputNetworkBean>() {
                    @Override
                    public void onFailure(Exception e) {
                        dialog.dismiss();
                        Toast.makeText(MainActivity.this, "错误: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }


                    @Override
                    public void getData(ScanInputNetworkBean bean) {
                        dialog.dismiss();
                        if (bean.getErrCode() == 0) {
                            ScanInputNetworkBean.DataBean beanData = bean.getData();
                            for (ScanInputBean bean1 : inputBeanList) {
                                if (beanData.getTradeCode() == bean1.getZhuiSuMa()) {
                                    Toast.makeText(MainActivity.this, "当前数据已在上传数据中！无法重复添加", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }

                            ScanInputBean scanInputBean = new ScanInputBean();
                            scanInputBean.setTradeCodeList(beanData.getTradeCode());
                            scanInputBean.setName(beanData.getRows().get(0).getQyname());//企业名称
                            scanInputBean.setPhone(replaceString[4]);//电话
                            scanInputBean.setExpiryDate(beanData.getRows().get(0).getSxrq());//有效期
                            scanInputBean.setProductionDate(beanData.getRows().get(0).getScrq());//生产日期
                            scanInputBean.setZhuiSuMa(replaceString[0]);//追溯码
                            scanInputBean.setUnit(beanData.getRows().get(0).getMinpackunit());//单位
                            scanInputBean.setTongYongMing(beanData.getRows().get(0).getCpname());//通用名
                            scanInputBean.setWenHao(beanData.getRows().get(0).getPzwh());//批准文号
                            scanInputBean.setProductNumber(beanData.getRows().get(0).getPh());//生产批号
                            scanInputBean.setYplxname(beanData.getRows().get(0).getYplxname());//隐藏字段 抗生素
                            scanInputBean.setPresellPrice("0");//预售价格
                            scanInputBean.setPurchasePrice("0");//采购价格
                            //计算数量 计算规则问后台
                            try {
                                String[] split = beanData.getRows().get(0).getTagratio().split(":");
                                if (beanData.getRows().get(0).getTmjb() == 1) {
                                    scanInputBean.setCount("1");//数量
                                } else {
                                    //Integer.valueOf(split[split.length - 1]) 最后一个 1:2:4:16
                                    scanInputBean.setCount(String.valueOf(Integer.valueOf(split[split.length - 1]) /
                                            Integer.valueOf(split[split.length - beanData.getRows().get(0).getTmjb()])));//数量
                                }
                            } catch (Exception e) {
                                Toast.makeText(MainActivity.this, "数量解析出错", Toast.LENGTH_SHORT).show();
                            }
                            //没有预售价格 没有购买价格
                            inputFragment.show(scanInputBean);
                        } else {
                            Toast.makeText(MainActivity.this, bean.getErrMsg(), Toast.LENGTH_SHORT).show();
                            inputFragment.show(replaceString);
                        }
                    }
                });
            } else {
                final OutputFragment outputFragment = (OutputFragment) fm.findFragmentByTag(tags[1]);
                //出库
                RequestParams params = new RequestParams();
                params.addFormDataPart("USERID", SPUtils.getInstance().getData(Contance.USERID, "", String.class));
                params.addFormDataPart("tradeCode", replaceString[0]);
                dialog.show();
                HttpRequest.post(Contance.BASE_URL + "GetSY_RgodownThe.ashx", params, new RequestCallBack<ScanOutputNetworkBean>() {
                    @Override
                    public void onFailure(Exception e) {
                        dialog.dismiss();
                        Toast.makeText(MainActivity.this, "错误: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }


                    @Override
                    public void getData(ScanOutputNetworkBean bean) {
                        dialog.dismiss();
                        if (bean.getErrCode() == 0) {
                            for (ScanOutputBean bean1 : outputBeanList) {
                                if (bean.getData().getTradeCode() == bean1.getTradeCode()) {
                                    Toast.makeText(MainActivity.this, "当前数据已在上传数据中！无法重复添加", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }
                            outputFragment.show(bean.getData());
                        } else {
                            Toast.makeText(MainActivity.this, bean.getErrMsg(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    //打印扫描后的数据信息
    private void printLog(ScanInputBean bean) {
        String Tag = "inputbean";
        Log.d(Tag, "追溯码:" + bean.getZhuiSuMa());
        Log.d(Tag, "通用名:" + bean.getTongYongMing());
        Log.d(Tag, "批准文号:" + bean.getWenHao());
        Log.d(Tag, "企业名称:" + bean.getName());
        Log.d(Tag, "数量:" + bean.getCount());
        Log.d(Tag, "生产日期:" + bean.getProductionDate());
        Log.d(Tag, "有效期:" + bean.getExpiryDate());
        Log.d(Tag, "生产批号:" + bean.getProductNumber());
        Log.d(Tag, "购买价格:" + bean.getPurchasePrice());
        Log.d(Tag, "预售价格:" + bean.getPresellPrice());
        Log.d(Tag, "抗生素:" + bean.getYplxname());
        Log.d(Tag, "tradeCodeList:" + bean.getTradeCodeList());
        Log.d(Tag, "同一个文件的上传的标识:" + bean.getUuid());
    }

    /**
     * 广播接收器
     */
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
//            if (intent.getAction().equals(SCN_CUST_ACTION_SCODE)){
            String result;
            try {
                result = intent.getStringExtra(SCN_CUST_EX_SCODE);
//                    Toast.makeText(context, "收到消息"+result, Toast.LENGTH_SHORT).show();
                if (!TextUtils.isEmpty(result)) {
                    ///201512270003700019840759，注射用硫酸链霉素，兽药字（2015）220442658，泰信，0825-5895608
                    final String[] replaceString;
                    try {
                        replaceString = result.replaceAll("，", ",").split(",");
                        repString = replaceString;
                    } catch (Exception e) {
                        e.printStackTrace();
                        return;
                    }
                    char num[] = replaceString[0].trim().toCharArray();//把字符串转换为字符数组
                    for (int i = 0; i < num.length; i++) {
                        if (!Character.isDigit(num[i])) {
                            Toast.makeText(MainActivity.this, "二维码不符合条件:" + result, Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }

                    if (type == INPUT) {
                        final InputFragment inputFragment = (InputFragment) fm.findFragmentByTag(tags[0]);
                        //入库
                        RequestParams params = new RequestParams();
                        params.addFormDataPart("httpType", "1");
                        params.addFormDataPart("USERID", SPUtils.getInstance().getData(Contance.USERID, "", String.class));
//                            params.addFormDataPart("tradeCode", "32132132");
                        params.addFormDataPart("ApprovalNum", repString[2]);
                        params.addFormDataPart("tradeCode", replaceString[0]);
                        dialog.show();
                        HttpRequest.post(Contance.BASE_URL + "getNYBProductT.ashx", params, new RequestCallBack<ScanInputNetworkBean>() {
                            @Override
                            public void onFailure(Exception e) {
                                dialog.dismiss();
                                if (e.getMessage().contains("Internal Server Error")) {
                                    Toast.makeText(MainActivity.this, "没有查到当前药品数据,请重新扫描", Toast.LENGTH_SHORT).show();
                                } else if (e.getMessage().contains("请求失败:  服务器无响应 请重新请求")) {
                                    Toast.makeText(MainActivity.this, "服务器无响应", Toast.LENGTH_SHORT).show();
                                } else if (e instanceof SocketTimeoutException) {
                                    Toast.makeText(MainActivity.this, "网络没有连接", Toast.LENGTH_SHORT).show();
                                } else {

                                    initDialog();
//                                    Toast.makeText(MainActivity.this, "错误: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }

                            }


                            @Override
                            public void getData(ScanInputNetworkBean bean) {
                                dialog.dismiss();
                                if (bean.getErrCode() == 0) {
                                    ScanInputNetworkBean.DataBean beanData = bean.getData();
                                    for (ScanInputBean bean1 : inputBeanList) {
                                        if (beanData.getTradeCode().equals(bean1.getZhuiSuMa())) {
                                            Toast.makeText(MainActivity.this, "当前数据已在上传数据中！无法重复添加", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                    }
                                    ScanInputBean scanInputBean = new ScanInputBean();
                                    scanInputBean.setTradeCodeList(beanData.getTradeCode());
                                    scanInputBean.setName(beanData.getRows().get(0).getQyname());//企业名称
                                    scanInputBean.setPhone(replaceString[4]);//电话
                                    scanInputBean.setExpiryDate(beanData.getRows().get(0).getSxrq());//有效期
                                    scanInputBean.setProductionDate(beanData.getRows().get(0).getScrq());//生产日期
                                    scanInputBean.setZhuiSuMa(replaceString[0]);//追溯码
                                    scanInputBean.setUnit(beanData.getRows().get(0).getMinpackunit());//单位
                                    scanInputBean.setTongYongMing(beanData.getRows().get(0).getCpname());//通用名
                                    scanInputBean.setWenHao(beanData.getRows().get(0).getPzwh());//批准文号
                                    scanInputBean.setProductNumber(beanData.getRows().get(0).getPh());//生产批号
                                    scanInputBean.setYplxname(beanData.getRows().get(0).getYplxname());//隐藏字段 抗生素
                                    scanInputBean.setPresellPrice("0");//预售价格
                                    scanInputBean.setPurchasePrice("0");//采购价格
                                    //计算数量 计算规则问后台
                                    try {
                                        String[] split = beanData.getRows().get(0).getTagratio().split(":");
                                        if (beanData.getRows().get(0).getTmjb() == 1) {
                                            scanInputBean.setCount("1");//数量
                                        } else {
                                            //Integer.valueOf(split[split.length - 1]) 最后一个 1:2:4:16
                                            scanInputBean.setCount(String.valueOf(Integer.valueOf(split[split.length - 1]) /
                                                    Integer.valueOf(split[split.length - beanData.getRows().get(0).getTmjb()])));//数量
                                        }
                                    } catch (Exception e) {
                                        Toast.makeText(MainActivity.this, "数量解析出错", Toast.LENGTH_SHORT).show();
                                    }
                                    printLog(scanInputBean);//打印log日志
                                    //没有预售价格 没有购买价格
                                    inputFragment.show(scanInputBean);
                                } else {
                                    Toast.makeText(MainActivity.this, bean.getErrMsg(), Toast.LENGTH_SHORT).show();
                                    inputFragment.show(replaceString);
                                }
                            }
                        });
                    } else {
                        final OutputFragment outputFragment = (OutputFragment) fm.findFragmentByTag(tags[1]);
                        //出库
                        RequestParams params = new RequestParams();
                        params.addFormDataPart("USERID", SPUtils.getInstance().getData(Contance.USERID, "", String.class));
                        params.addFormDataPart("tradeCode", replaceString[0]);
                        dialog.show();
                        //GetSY_RgodownThe.ashx  getNYBProductT.ashx
                        HttpRequest.post(Contance.BASE_URL + "GetSY_RgodownThe.ashx", params, new RequestCallBack<ScanOutputNetworkBean>() {
                            @Override
                            public void onFailure(Exception e) {
                                dialog.dismiss();
                                if (e.getMessage().contains("Internal Server Error")) {
                                    Toast.makeText(MainActivity.this, "没有查到当前药品库存", Toast.LENGTH_SHORT).show();
                                } else if (e.getMessage().contains("请求失败:  服务器无响应 请重新请求")) {
                                    Toast.makeText(MainActivity.this, "服务器无响应", Toast.LENGTH_SHORT).show();
                                } else if (e instanceof SocketTimeoutException) {
                                    Toast.makeText(MainActivity.this, "网络没有连接", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(MainActivity.this, "错误: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void getData(ScanOutputNetworkBean bean) {
                                dialog.dismiss();
                                if (bean.getErrCode() == 0) {
                                    for (ScanOutputBean bean1 : outputBeanList) {
                                        if (bean.getData().getTradeCode().equals(bean1.getTradeCode())) {
                                            Toast.makeText(MainActivity.this, "当前数据已在上传数据中！无法重复添加", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                    }
                                    outputFragment.show(bean.getData());
                                } else {
                                    Toast.makeText(MainActivity.this, bean.getErrMsg(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
            } catch (Exception e) {
                Toast.makeText(context, "数据获取失败", Toast.LENGTH_SHORT).show();
            }
        }
    };

//    /201512270003700019840759，注射用硫酸链霉素，兽药字（2015）220442658，泰3信，0825-5895608

    //请求不到数据时 初始dialog
    private void initDialog() {
        final InputFragment inputFragment = (InputFragment) fm.findFragmentByTag(tags[0]);
        ScanInputBean scanInputBean = new ScanInputBean();
        scanInputBean.setTradeCodeList(repString[0]);
        scanInputBean.setName(repString[3]);//企业名称
        scanInputBean.setPhone(repString[4]);//电话
        String yxrq = new String(Integer.parseInt(repString[0].substring(0, 4)) + 2 + "-" + repString[0].substring(4, 6) + "-" + repString[0].substring(6, 8));
        String scrq = new String(repString[0].substring(0, 4) + "-" + repString[0].substring(4, 6) + "-" + repString[0].substring(6, 8));
        scanInputBean.setExpiryDate(yxrq);//有效期
        scanInputBean.setProductionDate(scrq);//生产日期
        for (ScanInputBean bean1 : inputBeanList) {
            if (repString[0].equals(bean1.getZhuiSuMa())) {
                Toast.makeText(MainActivity.this, "当前数据已在上传数据中！无法重复添加", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        scanInputBean.setZhuiSuMa(repString[0]);//追溯码
        scanInputBean.setUnit("盒");//单位
        scanInputBean.setTongYongMing(repString[1]);//通用名
        scanInputBean.setWenHao(repString[2]);//批准文号
        scanInputBean.setProductNumber(repString[0].substring(0, 9));//生产批号
        scanInputBean.setYplxname("");//隐藏字段 抗生素
        scanInputBean.setPresellPrice("0");//预售价格
        scanInputBean.setPurchasePrice("0");//采购价格
        scanInputBean.setCount("1");//数量
        printLog(scanInputBean);//打印log日志
        //没有预售价格 没有购买价格
        inputFragment.show(scanInputBean);
    }

}
