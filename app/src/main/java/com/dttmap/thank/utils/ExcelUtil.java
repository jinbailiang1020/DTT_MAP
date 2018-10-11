package com.dttmap.thank.utils;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import com.dttmap.thank.base.Constant;
import com.dttmap.thank.bean.AddressBean;
import com.dttmap.thank.bean.UserExcelBean;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import top.eg100.code.excel.jxlhelper.ExcelManager;

/**
 * Created by Administrator on 2018/10/11/011.
 */

public class ExcelUtil {



    public static List<AddressBean> onImport(Context context, String url) {
        List<AddressBean> users = new ArrayList<>();
        try {
//            InputStream in = context.getResources().getAssets().open(url);
            InputStream in = new FileInputStream(new File(url));
            long t1 = System.currentTimeMillis();
//            FileInputStream excelStream = new FileInputStream("users.xls");
            ExcelManager excelManager = new ExcelManager();
            users.addAll(excelManager.fromExcel(in, AddressBean.class));
            long t2 = System.currentTimeMillis();
            double time = (t2 - t1) / 1000.0D;
            Toast.makeText(context, "读到User个数:" + users.size() + "\n用时:" + time + "秒", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(context, "读取异常", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        return users;
    }

    @SuppressWarnings("unused")
    public static void onExport(Context context, List<AddressBean> datas) {
        //实际使用的时候，不要在主线程做操作，demo的数据比较少
        try {
            long t1 = System.currentTimeMillis();
            File dir = new File( Constant.EXCEL_Path);
            if (!dir.exists()) {
                dir.mkdirs();
            }else{
                dir.delete();
                dir.mkdirs();
            }
            ExcelManager excelManager = new ExcelManager();
            OutputStream excelStream = new FileOutputStream(Constant.EXCEL_FILE_Path);

            boolean success = excelManager.toExcel(excelStream, datas);
            long t2 = System.currentTimeMillis();

            double time = (t2 - t1) / 1000.0D;
            if (success) {
//                mExportResult.setText("导出成功：在存储卡根目录:\nexcel.demo/export/users.xls" + "\n用时:" + time + "秒");
                Toast.makeText(context, "导出成功：在存储卡根目录:\n邓婷婷/users.xls" + "\n用时:" + time + "秒", Toast.LENGTH_SHORT).show();
            } else {
//                mExportResult.setText("导出失败");
                Toast.makeText(context, "导出失败", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
//            mExportResult.setText("导出异常");
            Toast.makeText(context, "导出异常", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    void ads() {
        try {
            long t1 = System.currentTimeMillis();
            List<AddressBean> users = new ArrayList<>();
            for (int i = 1; i <= 150; i++) {
                AddressBean u = new AddressBean();
//                u.setName("大到飞起来" + i);
//                u.setMobile("手机号" + i);
//                u.setSex("男");
//                u.setAddress("地点" + i);
//                u.setMemo("备注" + i);
//                u.setOther("其他信息" + i);
                users.add(u);
            }
            ExcelManager excelManager = new ExcelManager();
            OutputStream excelStream = new FileOutputStream("usersExport.xls");

            boolean success = excelManager.toExcel(excelStream, users);
            long t2 = System.currentTimeMillis();
            double time = (t2 - t1) / 1000.0D;
            if (success) {
                System.out.println("导出成功\n用时:" + time + "秒");
            } else {
                System.out.println("导出失败");
            }

        } catch (Exception e) {
            System.err.println("导出异常");
            e.printStackTrace();
        }
    }
}
