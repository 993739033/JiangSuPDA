package com.mingnong.scanappnew.net.downapk;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.OkHttpClient;

/**
 * Created by wyw on 2017/3/30.
 */

public class DownService extends Service {
    public static final String ADD_DOWNTASK = "com.wyw.jiangsu.downtaskadd";
    public static final String RETRY_DOWNTASK = "com.wyw.jiangsu.downtaskretry";
    public static final String UPDATE_DOWNSTAUS = "com.wyw.jiangsu.updatedown";
    public static final String UPDATE_COMPLETE = "com.wyw.jiangsu.complete";
    public static final String TASK_STARTDOWN = "com.wyw.jiangsu.taskstart";
    public static final String TASKS_CHANGED = "com.wyw.jiangsu.taskchanges";
    public static final String TASKS_ERROR = "com.wyw.jiangsu.error";
    public static final String PACKAGE = "com.mingnong.scanappnew";


    private static ArrayList<String> prepareTaskList = new ArrayList<>();
    private static ArrayList<DownloadTask> tasks = new ArrayList<>();
    private DownloadTask currentTask;
    private ExecutorService executorService;
    private boolean isError;
    private DownloadTaskListener listener = new DownloadTaskListener() {
        @Override
        public void onPrepare(DownloadTask downloadTask) {

        }

        @Override
        public void onStart(DownloadTask downloadTask) {
            Intent intent = new Intent(TASK_STARTDOWN);
            intent.putExtra("completesize", downloadTask.getCompletedSize());
            intent.putExtra("totalsize", downloadTask.getTotalSize());
            intent.setPackage(PACKAGE);
            sendBroadcast(intent);
        }

        @Override
        public void onDownloading(DownloadTask downloadTask) {
            Intent intent = new Intent(UPDATE_DOWNSTAUS);
            intent.putExtra("completesize", Math.round(downloadTask.getCompletedSize() * 1f / 1024));
            intent.putExtra("totalsize", Math.round(downloadTask.getTotalSize() * 1.0f / 1024));
            intent.setPackage(PACKAGE);
            sendBroadcast(intent);
        }

        @Override
        public void onPause(DownloadTask downloadTask) {
            sendIntent(TASKS_CHANGED);
            if (prepareTaskList.size() > 0) {
                if (currentTask != null) {
                    prepareTaskList.remove(currentTask.getId());
                    tasks.remove(currentTask);
                }
            }
            currentTask = null;
            startTask();
        }

        @Override
        public void onCancel(DownloadTask downloadTask) {
            sendIntent(TASKS_CHANGED);
            if (prepareTaskList.size() > 0) {
                if (currentTask != null) {
                    prepareTaskList.remove(currentTask.getId());
                    tasks.remove(currentTask);
                }

            }
            currentTask = null;
            startTask();
        }

        @Override
        public void onCompleted(DownloadTask downloadTask) {
            Intent intent = new Intent(UPDATE_COMPLETE);
            intent.putExtra("fileDir", downloadTask.getSaveDirPath());
            intent.putExtra("fileName", downloadTask.getFileName());
            intent.setPackage(PACKAGE);
            sendBroadcast(intent);
            sendIntent(TASKS_CHANGED);
            if (prepareTaskList.size() > 0) {
                if (currentTask != null) {
                    prepareTaskList.remove(currentTask.getId());
                    tasks.remove(currentTask);
                }
            }
            currentTask = null;
            startTask();
        }

        @Override
        public void onError(DownloadTask downloadTask, int errorCode) {
            Intent intent = new Intent(UPDATE_DOWNSTAUS);
            intent.setPackage(PACKAGE);
            sendBroadcast(intent);
            startTask();
            currentTask = null;
        }
    };

    private void startTask() {
        if (currentTask != null) {
            return;
        }
        if (prepareTaskList.size() > 0) {
            DownloadTask task = tasks.get(0);
            executorService.submit(task);
            currentTask = task;
            sendIntent(TASKS_CHANGED);
        }
    }

    private void sendIntent(String action) {
        Intent intent = new Intent();
        intent.setAction(action);
        intent.setPackage(PACKAGE);
        sendBroadcast(intent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        executorService = Executors.newSingleThreadExecutor();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null) {
            return super.onStartCommand(intent, flags, startId);
        }
        String action = null;
        try {
            action = intent.getAction();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (action == null) {
            return super.onStartCommand(intent, flags, startId);
        }
        switch (action) {
            case ADD_DOWNTASK:
                String name = intent.getStringExtra("name");
                String artist = intent.getStringExtra("path");
                String url = intent.getStringExtra("url");
                addDownloadTask(name, artist, url);
                break;
            case RETRY_DOWNTASK:
                //清空正在下载的任务
                prepareTaskList.clear();
                tasks.clear();
                executorService.shutdown();
                String name1 = intent.getStringExtra("name");
                String artist1 = intent.getStringExtra("path");
                String url1 = intent.getStringExtra("url");
                addDownloadTask(name1, artist1, url1);
                break;
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void addDownloadTask(String name, String path, String url) {
        DownloadTask task = new DownloadTask((url).hashCode() + "",new OkHttpClient(), this, url,
                path, name, listener);
        tasks.add(task);

        prepareTaskList.add(task.getId());
        Toast.makeText(this, "已加入到下载", Toast.LENGTH_SHORT).show();
        startTask();

    }

    public interface DownloadTaskListener {
        void onPrepare(DownloadTask downloadTask);

        void onStart(DownloadTask downloadTask);

        void onDownloading(DownloadTask downloadTask);

        void onPause(DownloadTask downloadTask);

        void onCancel(DownloadTask downloadTask);

        void onCompleted(DownloadTask downloadTask);

        void onError(DownloadTask downloadTask, int errorCode);

        int DOWNLOAD_ERROR_FILE_NOT_FOUND = -1;
        int DOWNLOAD_ERROR_IO_ERROR = -2;
    }
}
