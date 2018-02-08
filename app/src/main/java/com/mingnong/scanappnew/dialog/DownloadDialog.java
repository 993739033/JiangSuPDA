package com.mingnong.scanappnew.dialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by wyw on 2016/8/30.
 * 下载的对话框
 */
public class DownloadDialog extends ProgressDialog {


    public void setRetryVisivible(int visivible) {
        getButton(DialogInterface.BUTTON_POSITIVE).setVisibility(visivible);
    }

    public DownloadDialog(Context context) {
        super(context);
    }

    public DownloadDialog(Context context, int theme) {
        super(context, theme);
    }

    public static final class Params{
        private String title="下载";
        private String message="下载中";
        private int style = ProgressDialog.STYLE_HORIZONTAL;
        private boolean cancelable =false;
        private OnClickListener retryListener;
    }
    public static class Builder{
        private Context context;
        private DownloadDialog.Params params;

        public Builder(Context context){
            this.context =context;
            params = new Params();
        }

        public DownloadDialog.Builder setTitle(String title) {
            params.title = title;
            return this;
        }
        public DownloadDialog.Builder OnClickListener(OnClickListener retryListener) {
            params.retryListener = retryListener;
            return this;
        }
        public DownloadDialog.Builder setMessage(String message) {
            params.message = message;
            return this;
        }
        public DownloadDialog.Builder setStyle(int style) {
            params.style = style;
            return this;
        }
        public DownloadDialog.Builder setCanCancel(boolean cancelable) {
            params.cancelable = cancelable;
            return this;
        }
        public DownloadDialog create(){
            DownloadDialog dialog = new DownloadDialog(context);
            dialog.setProgressNumberFormat("%1d KB/%2d KB");
            dialog.setTitle(params.title);
            dialog.setMessage(params.message);
            dialog.setCancelable(params.cancelable);
            dialog.setProgressStyle(params.style);
//            dialog.setButton(DialogInterface.BUTTON_POSITIVE,"重试",params.retryListener);
            return dialog;
        }
    }
}
