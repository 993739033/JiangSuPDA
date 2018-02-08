package com.mingnong.scanappnew.bean;

import java.io.Serializable;

/**
 * Created by wyw on 2016/8/10.
 */
public class BaseMsg implements Serializable {

    private int errCode;
    private String errMsg;

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public int getErrCode() {
        return errCode;
    }

    public void setErrCode(int errCode) {
        this.errCode = errCode;
    }
}
