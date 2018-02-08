package com.mingnong.scanappnew.net.downapk;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.mingnong.scanappnew.utils.AppUtils;


/**
 * Created by wyw on 2016/7/14.
 */

public class DownloadReceiver extends BroadcastReceiver {
    public static final String Action = "com.mingnong.hunanapp.downloadDb";
    public static final String DOWNLOAD = "com.mingnong.hunanapp.download";
    public static final String DOWNLOAD_DB = "com.mingnong.hunanapp.downloadDb";
    public static final String DOWNLAOD_APK = "com.mingnong.hunanapp.downlaodApk";
    public static final String DOWNLAOD_PATCH = "com.mingnong.hunanapp.downlaodPatch";
    public static final String DOWNLAOD_DEX = "com.mingnong.hunanapp.downlaodDex";
    public static final String PATH = "com.mingnong.hunanapp.downloadPath";

    @Override
    public void onReceive(Context context, Intent intent) {
//        systemDownload(context, intent);
        String download = intent.getStringExtra(DOWNLOAD);
        String path = intent.getStringExtra(PATH);
        switch (download) {
            case DOWNLAOD_APK:
                //以.apk结尾
                Log.e("+++++++++++","AppUtils.installApk(context, path);");
                AppUtils.installApk(context, path);
                break;
        }
    }

    private void systemDownload(Context context, Intent intent) {
        DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(intent.getAction())) {
            DownloadManager.Query query = new DownloadManager.Query();
            //在广播中取出下载任务的id
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            query.setFilterById(id);
            Cursor c = manager.query(query);
            if (c.moveToFirst()) {
                //获取文件下载路径
                String filePath = c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME));
                //如果文件名不为空，说明已经存在了，拿到文件名想干嘛都好
                //获取文件名字
                String fileName = "";
                if (!TextUtils.isEmpty(filePath)) {
                    fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
                    if (fileName.endsWith(".apk")) {
                        AppUtils.installApk(context, filePath);
                    }
                }
            }
        } else if (DownloadManager.ACTION_NOTIFICATION_CLICKED.equals(intent.getAction())) {
            long[] ids = intent.getLongArrayExtra(DownloadManager.EXTRA_NOTIFICATION_CLICK_DOWNLOAD_IDS);
            //点击通知栏取消下载
            manager.remove(ids);
            Toast.makeText(context, "已经取消下载", Toast.LENGTH_SHORT).show();
        }
    }

}
