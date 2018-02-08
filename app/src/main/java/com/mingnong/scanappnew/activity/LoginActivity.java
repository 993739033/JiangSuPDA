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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.mingnong.scanappnew.Contance;
import com.mingnong.scanappnew.R;
import com.mingnong.scanappnew.bean.BaseMsg;
import com.mingnong.scanappnew.bean.UserBean;
import com.mingnong.scanappnew.dialog.DownloadDialog;
import com.mingnong.scanappnew.net.RequestCallBack;
import com.mingnong.scanappnew.net.downapk.DownService;
import com.mingnong.scanappnew.utils.AppUtils;
import com.mingnong.scanappnew.utils.SPUtils;
import com.mingnong.scanappnew.utils.StatusBarUtil;
import com.zhy.m.permission.MPermissions;
import com.zhy.m.permission.PermissionDenied;
import com.zhy.m.permission.PermissionGrant;

import java.io.File;
import java.net.SocketTimeoutException;

import cn.finalteam.okhttpfinal.HttpRequest;
import cn.finalteam.okhttpfinal.RequestParams;

public class LoginActivity extends AppCompatActivity {

    private EditText mAccountView;
    private EditText mPasswordView;
    private ProgressDialog dialog;
    private DownloadDialog downDialog;
    public static final int TASK_APK = 0;
    private int currentTaskType = -1;

    private DownStatus downStatus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        StatusBarUtil.setColor(this,R.color.colorPrimary);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("登录");
        mAccountView = (EditText) findViewById(R.id.user_name);
        mPasswordView = (EditText) findViewById(R.id.password);
        dialog = new ProgressDialog(this);
        dialog.setMessage("登录中");
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        downDialog = new DownloadDialog.Builder(this)
                .setStyle(ProgressDialog.STYLE_HORIZONTAL)
                .setCanCancel(false)
                .setTitle("下载")
                .create();

        downStatus = new DownStatus();

        String name = SPUtils.getInstance().getData(Contance.USERNAME, "", String.class);
        String psw = SPUtils.getInstance().getData(Contance.PASSWORD, "", String.class);
        mAccountView.setText(name);
        mPasswordView.setText(psw);


        findViewById(R.id.bt_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
                //检查版本号 判断是否更新
                checkVersion();
            }
        });
        //权限判断
        if (Build.VERSION.SDK_INT >= 23) {
            if (!MPermissions.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, 100)) {
                MPermissions.requestPermissions(this, 100, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA});
            }
        } else {
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        MPermissions.onRequestPermissionsResult(this, 100, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    //检查版本
    private void checkVersion(){
        String versionName = AppUtils.getAppVersionName(this);
        RequestParams params = new RequestParams();
        params.addFormDataPart("version", versionName);
        HttpRequest.post("http://36.111.192.50:8888/JiangSuAPP/HtmlAshx/GetVersionPDA.ashx", params, new RequestCallBack<BaseMsg>() {
            @Override
            public void onFailure(Exception e) {
                if (e.getMessage().contains("Internal Server Error")) {
                    dialog.dismiss();
                    Toast.makeText(LoginActivity.this, "获取版本号失败", Toast.LENGTH_SHORT).show();
                } else if (e.getMessage().contains("请求失败:  服务器无响应 请重新请求")) {
                    dialog.dismiss();
                    Toast.makeText(LoginActivity.this, "服务器无响应", Toast.LENGTH_SHORT).show();
                } else if (e instanceof SocketTimeoutException) {
                    dialog.dismiss();
                    Toast.makeText(LoginActivity.this, "网络没有连接", Toast.LENGTH_SHORT).show();
                } else {
                    attemptLogin();
                }
            }

            @Override
            public void getData(BaseMsg data) {
                dialog.dismiss();
                addTask(currentTaskType);
            }
        });

    }

    public void addTask(int type) {
        this.currentTaskType = type;
        Intent i = new Intent(DownService.ADD_DOWNTASK);
        i.setAction(DownService.ADD_DOWNTASK);
        i.putExtra("name", "JiangSuPDA.apk");
        i.putExtra("path", Environment.getExternalStorageDirectory().getAbsolutePath());
        i.putExtra("url", "http://36.111.192.50:8888/JiangSuAPP/Version/JiangSuPDA.apk");
//        i.putExtra("url", "http://36.111.192.50:8888/JiangSuAPPCS/UserDatabase/JiangSu.apk");
        i.setPackage(DownService.PACKAGE);
        startService(i);
        downDialog.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter f = new IntentFilter();
        f.addAction(DownService.TASK_STARTDOWN);
        f.addAction(DownService.UPDATE_DOWNSTAUS);
        f.addAction(DownService.TASKS_CHANGED);
        f.addAction(DownService.TASKS_ERROR);
        f.addAction(DownService.UPDATE_COMPLETE);
        registerReceiver(downStatus, new IntentFilter(f));
    }

    @Override
    public void onStop() {
        super.onStop();
        unregisterReceiver(downStatus);
    }


    private class DownStatus extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case DownService.UPDATE_DOWNSTAUS:
                    downDialog.setProgress((int) intent.getIntExtra("completesize", 0));
                    downDialog.setMax((int) intent.getIntExtra("totalsize", -1));
                    break;
                case DownService.TASK_STARTDOWN:
                    downDialog.setProgress((int) intent.getLongExtra("completesize", 0));
                    downDialog.setMax((int) intent.getLongExtra("totalsize", -1));
                    break;
                case DownService.TASKS_CHANGED:
                    break;
                case DownService.UPDATE_COMPLETE:
                    downDialog.dismiss();
                    String fileDir = intent.getStringExtra("fileDir");
                    String fileName = intent.getStringExtra("fileName");
                    if (fileName.endsWith(".apk")) {
                        //安装apk
                        AppUtils.installApk(context, fileDir + File.separator + fileName);
                    }
                    break;
                case DownService.TASKS_ERROR:
                    downDialog.dismiss();
                    Toast.makeText(context, "下载失败!", Toast.LENGTH_SHORT).show();
                    break;

            }
        }
    }

    @PermissionGrant(100)
    public void permissionSuccess() {
        Toast.makeText(this, "权限申请成功", Toast.LENGTH_SHORT).show();
    }
    @PermissionDenied(100)
    public void permissionFail() {
        Toast.makeText(this, "权限申请失败,程序将不能正常运行", Toast.LENGTH_SHORT).show();
    }

    private void attemptLogin() {
        // Reset errors.
        mAccountView.setError(null);
        mPasswordView.setError(null);

        final String account = mAccountView.getText().toString();
        final String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;
        if (TextUtils.isEmpty(account)) {
            mAccountView.setError(getString(R.string.error_field_required));
            focusView = mAccountView;
            cancel =true;
        }
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
            dialog.dismiss();
        } else {
            dialog.show();
            RequestParams params = new RequestParams();
            params.addFormDataPart("uAccount", account);
            params.addFormDataPart("pwd", password);
            HttpRequest.post(Contance.BASE_URL + "Login.ashx", params, new RequestCallBack<UserBean>() {
                @Override
                public void onFailure(Exception e) {
                    dialog.dismiss();
                    mPasswordView.setError(e.getMessage());
                    SPUtils.getInstance().saveData(Contance.ISLOGIN,false);
                }

                @Override
                public void getData(UserBean user) {
                    dialog.dismiss();
                    SPUtils.getInstance().saveData(Contance.ISLOGIN,true);
                    SPUtils.getInstance().saveData(Contance.USERNAME,account);
                    SPUtils.getInstance().saveData(Contance.PASSWORD,password);
                    SPUtils.getInstance().saveData(Contance.USERID,user.getData().getUSERID()+"");
                    Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this,MainActivity.class));
                    finish();
                }
            });
        }
    }
}

