package com.mingnong.scanappnew.bean;

/**
 * Created by wyw on 2016/8/10.
 */
public class BaseObjectEntity<T> extends BaseMsg {
    public T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
