package com.mingnong.scanappnew.net;

import com.google.gson.Gson;
import com.mingnong.scanappnew.bean.BaseMsg;

import java.lang.reflect.ParameterizedType;

import cn.finalteam.okhttpfinal.BaseHttpRequestCallback;
import okhttp3.Headers;
import okhttp3.Response;

/**
 * Created by wyw on 2016/11/3.
 */

public abstract class RequestCallBack<T extends BaseMsg> extends BaseHttpRequestCallback<T> {

    public void onResponse(Response httpResponse, String response, Headers headers) {
        try {
            if (httpResponse.isSuccessful()) {
                T t = null;
                t = new Gson().fromJson(response,
                        (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
                if (t.getErrCode() == 0) {
                    getData(t);
                }
                else {
                    onFailure(new Exception("请求失败:  " + t.getErrMsg()));
                }
            }
        } catch (Exception e) {
            onFailure(new Exception("请求失败:  服务器无响应 请重新请求"));
        }
    }

    public void onFailure(int errorCode, String msg) {
        onFailure(new Exception(msg));
    }


    public abstract void onFailure(Exception e);

    public abstract void getData(T t);
}
