package com.dttmap.thank.utils;

import com.dttmap.thank.bean.AddressBean;

import java.io.UnsupportedEncodingException;

/**
 * Created by Administrator on 2018/8/22/022.
 */

public class MapUtil {
    public static final String ak = "RK8EC6KQwOqqID0IykQ5XN08nzNcKKe7";
    public static final String mcode = "4C:D2:F8:0B:76:F7:10:69:7D:4E:64:C6:6D:27:C1:3F:A8:52:3D:D4;com.dttmap.thank";

    public static String getLatAndLngByAddress(AddressBean bean) {
        String addr = "";
        try {
            addr = java.net.URLEncoder.encode(bean.getAddress(), "UTF-8");//编码
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return String.format("http://api.map.baidu.com/geocoder/v2/?"
                + "address=%s&ak=" + ak + "&output=json&mcode=" + mcode, addr);

    }
}
