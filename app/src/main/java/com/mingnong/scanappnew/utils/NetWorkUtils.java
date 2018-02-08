package com.mingnong.scanappnew.utils;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by wyw on 2016/7/12.
 */

public class NetWorkUtils {
    /*
     * 是否连接网络
	 * 需要添加获取网络状态的权限
	 */
    public static boolean isOpenNetwork(Context context) {
        ConnectivityManager connManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connManager.getActiveNetworkInfo() != null) {
            return connManager.getActiveNetworkInfo().isAvailable();
        }
        return false;
    }
}
