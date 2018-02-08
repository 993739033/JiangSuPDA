package com.mingnong.scanappnew.net.downapk;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by wyw on 2017/3/30.
 */

class DownloadTask implements Runnable {
    private String id;
    private OkHttpClient client;
    private Context mContext;
    private long totalSize;
    private long completedSize;
    private String url;
    private String saveDirPath;
    private String fileName;
    private RandomAccessFile file;
    private int UPDATE_SIZE = 50 * 1024;    // The database is updated once every 50k
    private int downloadStatus = DownloadStatus.DOWNLOAD_STATUS_INIT;
    private DownService.DownloadTaskListener listener;


    public DownloadTask(String id, OkHttpClient client, Context mContext, String url,
                        String saveDirPath, String fileName, DownService.DownloadTaskListener listener) {
        this.saveDirPath = saveDirPath;
        this.id = id;
        this.client = client;
        this.mContext = mContext;
        this.url = url;
        this.fileName = fileName;
        this.listener = listener;
    }

    @Override
    public void run() {
        downloadStatus = DownloadStatus.DOWNLOAD_STATUS_PREPARE;
        //  id = (saveDirPath + fileName).hashCode() + "";

        onPrepare();

        InputStream inputStream = null;
        BufferedInputStream bis = null;
        try {
            file = new RandomAccessFile(saveDirPath+File.separator + fileName, "rwd");
            long fileLength = file.length();
            if (fileLength > totalSize) {
                completedSize = 0;
                totalSize = 0;
            }
            downloadStatus = DownloadStatus.DOWNLOAD_STATUS_START;
            onStart();
            Request request = new Request.Builder()
                    .url(url)
                    .header("RANGE", "bytes=" + completedSize + "-")//  Http value set breakpoints RANGE
                    .addHeader("Referer", url)
                    .build();
            Log.e("comlesize", completedSize + "");
            file.seek(completedSize);
            Response response = client.newCall(request).execute();
            ResponseBody responseBody = response.body();
            if (responseBody != null) {
                downloadStatus = DownloadStatus.DOWNLOAD_STATUS_DOWNLOADING;
                if (totalSize <= 0)
                    totalSize = responseBody.contentLength();

                inputStream = responseBody.byteStream();
                bis = new BufferedInputStream(inputStream);
                byte[] buffer = new byte[4 * 1024];
                int length = 0;
                int buffOffset = 0;
                while ((length = bis.read(buffer)) > 0 && downloadStatus != DownloadStatus.DOWNLOAD_STATUS_CANCEL && downloadStatus != DownloadStatus.DOWNLOAD_STATUS_PAUSE) {
                    file.write(buffer, 0, length);
                    completedSize += length;
                    buffOffset += length;
                    if (buffOffset >= UPDATE_SIZE) {
                        // Update download information database
                        buffOffset = 0;
                        onDownloading();
                    }
                }
                onDownloading();
            }
        } catch (FileNotFoundException e) {
            downloadStatus = DownloadStatus.DOWNLOAD_STATUS_ERROR;
            onError(DownService.DownloadTaskListener.DOWNLOAD_ERROR_FILE_NOT_FOUND);
            return;
//            e.printStackTrace();
        } catch (IOException e) {
            downloadStatus = DownloadStatus.DOWNLOAD_STATUS_ERROR;
            onError(DownService.DownloadTaskListener.DOWNLOAD_ERROR_IO_ERROR);
            return;
        } finally {
            //String  nP = fileName.substring(0, path.length() - 5);
            if (bis != null) try {
                bis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (inputStream != null) try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (file != null) try {
                file.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        if (totalSize == completedSize) {
            String path = saveDirPath + fileName;
            File file = new File(path);

            downloadStatus = DownloadStatus.DOWNLOAD_STATUS_COMPLETED;
            Uri contentUri = Uri.fromFile(file);
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, contentUri);
            mContext.sendBroadcast(mediaScanIntent);
        }

        switch (downloadStatus) {
            case DownloadStatus.DOWNLOAD_STATUS_COMPLETED:
                onCompleted();
                break;
            case DownloadStatus.DOWNLOAD_STATUS_PAUSE:
                onPause();
                break;
            case DownloadStatus.DOWNLOAD_STATUS_CANCEL:
                File temp = new File(saveDirPath + fileName);
                if (temp.exists()) temp.delete();
                onCancel();
                break;
        }
    }

    private void onPrepare() {
        if (listener == null) {
            return;
        }
        listener.onPrepare(this);
    }
    private void onStart() {
        if (listener == null) {
            return;
        }
        listener.onStart(this);
    }
    private void onDownloading() {
        if (listener == null) {
            return;
        }
        listener.onDownloading(this);
    }

    private void onCompleted() {
        if (listener == null) {
            return;
        }
        listener.onCompleted(this);
    }

    private void onPause() {
        if (listener == null) {
            return;
        }
        listener.onPause(this);
    }

    private void onCancel() {
        if (listener == null) {
            return;
        }
        listener.onCancel(this);
    }

    private void onError(int errorCode) {
        if (listener == null) {
            return;
        }
        listener.onError(this,errorCode);
    }

    public void setDownloadListener(DownService.DownloadTaskListener listener) {
        Log.e("downtask", (listener == null) + "");
        if (listener != null) this.listener = listener;
    }

    public String getSaveDirPath() {
        return saveDirPath;
    }

    public void setSaveDirPath(String saveDirPath) {
        this.saveDirPath = saveDirPath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getTotalSize() {
        return totalSize;
    }

    public long getCompletedSize() {
        return completedSize;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
