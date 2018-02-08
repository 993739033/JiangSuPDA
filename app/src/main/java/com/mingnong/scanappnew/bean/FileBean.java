package com.mingnong.scanappnew.bean;

/**
 * Created by wyw on 2016/11/16.
 * 文件上传的界面实体类
 */

public class FileBean {
    private String tvFilePath;
    private boolean check;

    public String getTvFilePath() {
        return tvFilePath;
    }

    public FileBean setTvFilePath(String tvFilePath) {
        this.tvFilePath = tvFilePath;
        return this;
    }

    public boolean isCheck() {
        return check;
    }

    public FileBean setCheck(boolean check) {
        this.check = check;
        return this;
    }
}
