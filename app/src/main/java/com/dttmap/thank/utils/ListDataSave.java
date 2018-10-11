package com.dttmap.thank.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.dttmap.thank.bean.AddressBean;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ListDataSave {
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    public ListDataSave(Context mContext, String preferenceName) {
        preferences = mContext.getSharedPreferences(preferenceName, Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    /**
     * 保存List
     *
     * @param tag
     * @param datalist
     */
    public <T> void setDataList(String tag, List<T> datalist) {
        if (null == datalist || datalist.size() <= 0)
            return;

        Gson gson = new Gson();
        //转换成json数据，再保存
        String strJson = gson.toJson(datalist);
        editor.clear();
        editor.putString(tag, strJson);
        Log.i("data", strJson);
        editor.commit();

    }

    /**
     * 获取List
     *
     * @param tag
     * @return
     */
    public List<AddressBean> getDataList(String tag) {
        List<AddressBean> datalist = new ArrayList();
        String strJson = preferences.getString(tag, null);
        if (strJson != null)
            Log.i("data", strJson);
        Gson g = new Gson();
        try {
            JSONArray arr = new JSONArray(strJson);
            for (int i = 0; i < arr.length(); i++) {
                JSONObject obj = (JSONObject) arr.get(i);
                AddressBean bean = g.fromJson(obj.toString(), AddressBean.class);
                datalist.add(bean);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return datalist;

    }
}