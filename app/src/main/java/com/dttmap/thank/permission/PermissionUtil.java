package com.dttmap.thank.permission;

import android.Manifest;

import java.util.List;

public class PermissionUtil {

    public static void requestPermission(){//Manifest.permission.CALL_PHONE,
        BaseActivity.requestRunTimePermission(new String[]{ Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE}
                , new PermissionListener() {
                    @Override
                    public void onGranted() {

                    }

                    @Override
                    public void onGranted(List<String> grantedPermission) {

                    }

                    @Override
                    public void onDenied(List<String> deniedPermission) {

                    }
                });
    }
}