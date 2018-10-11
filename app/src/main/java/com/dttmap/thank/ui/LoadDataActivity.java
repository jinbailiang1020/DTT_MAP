package com.dttmap.thank.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.dttmap.thank.R;
import com.dttmap.thank.bean.AddressBean;
import com.dttmap.thank.permission.BaseActivity;
import com.dttmap.thank.permission.PermissionUtil;
import com.dttmap.thank.utils.DataUtil;
import com.dttmap.thank.utils.ExcelUtil;
import com.dttmap.thank.utils.FileUtil;
import com.dttmap.thank.utils.MapUtil;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class LoadDataActivity extends BaseActivity {

    private TextView tv_path;
    private boolean canUpdate;
    private List<AddressBean> datas = new ArrayList<>();
    private ProgressDialog dialog;
    private int size;
    private boolean isUpdateAll;
    private String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_data);
        tv_path = findViewById(R.id.tv_path);
        PermissionUtil.requestPermission();
    }

    public void selectFile(View view) {
        canUpdate = false;
        PermissionUtil.requestPermission();
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            startActivityForResult(Intent.createChooser(intent, "Select a File to Upload"), 111);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(this, "Please install a File Manager.", Toast.LENGTH_SHORT).show();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 111) {
                Uri uri = data.getData();
                try {
                    path = FileUtil.getPath(LoadDataActivity.this, uri);
                    tv_path.setText("文件路径：" + path);
                    datas.clear();
                    datas.addAll(ExcelUtil.onImport(this, path));//todo
                    canUpdate = true;
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                    Toast.makeText(this, " 处理文件异常", Toast.LENGTH_SHORT).show();
                }

            }
        }
    }


    public void updateNew(View view) {
        if (canUpdate) {
            isUpdateAll = false;
        } else {
            Toast.makeText(this, " 请选择excel文件", Toast.LENGTH_SHORT).show();
        }
    }

    public void updateAll(View view) {
        if (canUpdate) {
            isUpdateAll = true;
            update();
        } else {
            Toast.makeText(this, " 请选择excel文件", Toast.LENGTH_SHORT).show();
        }
    }

    private void update() {
        if (datas.isEmpty()) {
            Toast.makeText(this, "数据为空", Toast.LENGTH_SHORT).show();
            return;
        }
        size = datas.size();
        dialog = new ProgressDialog(this);
        dialog.setMessage("加载坐标，请稍等。。。");
        dialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (final AddressBean bean :
                        datas) {
                    String url = MapUtil.getLatAndLngByAddress(bean);
                    OkHttpUtils
                            .get()
                            .url(url)
//                    .addParams("username", "hyman")
//                    .addParams("password", "123")
                            .build()
                            .execute(new StringCallback() {
                                @Override
                                public void onError(Request request, Exception e) {
                                    Toast.makeText(LoadDataActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                    size--;
                                    finishData();
                                }

                                @Override
                                public void onResponse(String response) {
                                    size--;
                                    try {
                                        JSONObject obj = new JSONObject(response);
                                        if (obj.getInt("status") == 0) {
                                            JSONObject result = obj.getJSONObject("result");
                                            JSONObject location = result.getJSONObject("location");
                                            double lng = location.getDouble("lng");
                                            double lat = location.getDouble("lat");
                                            bean.setLng(lng + "");
                                            bean.setLat(lat + "");
                                        } else {
                                            Toast.makeText(LoadDataActivity.this, bean.getPersonName() + ":" + bean.getAddress() + ")查不出", Toast.LENGTH_LONG).show();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
//                                    Toast.makeText(MapActivity.this, response, Toast.LENGTH_LONG).show();
                                    finishData();
                                }
                            });
                }
            }
        }).start();
    }

    private void finishData() {
        if (size == 0) {
            if (!isUpdateAll) {
//
            }
            ExcelUtil.onExport(this, datas);
            dialog.dismiss();
        }
    }

    public void back(View view) {
        finish();
    }

    public void hello(View view) {
        Toast.makeText(this, "hello , day day up！", Toast.LENGTH_LONG).show();
    }


}
