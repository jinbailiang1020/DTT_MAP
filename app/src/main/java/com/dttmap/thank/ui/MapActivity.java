package com.dttmap.thank.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.dttmap.thank.base.Constant;
import com.dttmap.thank.bean.AddressBean;
import com.dttmap.thank.utils.DataUtil;
import com.dttmap.thank.utils.ExcelUtil;
import com.dttmap.thank.utils.ListDataSave;
import com.dttmap.thank.utils.MapUtil;
import com.dttmap.thank.R;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MapActivity extends AppCompatActivity {
    private MapView mMapView;
    private BaiduMap mBaiduMap;

    // 定位相关
    LocationClient mLocClient;
    private MyLocationConfiguration.LocationMode mCurrentMode;
    BitmapDescriptor mCurrentMarker;
    public MyLocationListenner myListener;
    private int size;

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                    Toast.makeText(MapActivity.this, (String) msg.obj, Toast.LENGTH_LONG).show();
//                    T.setText((String) msg.obj);
                    break;

                case 2://加载数据完成
                    dialog.dismiss();
                    showMap();
                    break;

                default:
                    break;
            }
        }
    };
    private ProgressDialog dialog;
    private ListDataSave save;

    private TextView area;
    private TextView custmer;
    private TextView personName;
    private TextView address;
    private TextView point;
    private ArrayList<AddressBean> datas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        mMapView = (MapView) findViewById(R.id.mmap);
        mBaiduMap = mMapView.getMap();
        initView();
        myListener = new MyLocationListenner();

        // 定位初始化
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);// 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        //这个要写
        option.setAddrType("all");
//              option.setIsNeedAddress(true);
//            option.setIsNeedAddress(true);
        mLocClient.setLocOption(option);
        mLocClient.start();

//        loadData();

/*        save = new ListDataSave(this, "data");
        List<AddressBean> aa = save.getDataList("dataItem");
        if (save.getDataList("dataItem") == null || aa.isEmpty()) {
            initData();
        } else {
            DataUtil.addressList.clear();
            DataUtil.addressList.addAll(aa);
            showMap();
        }*/

    }

    private void loadData() {
        if (!datas.isEmpty()) return;
        File dir = new File(Constant.EXCEL_Path);
        if (!dir.exists()) {
//            Toast.makeText(MapActivity.this, "请先点击右上角选择Excel文件", Toast.LENGTH_LONG).show();
            startActivity(new Intent(this, LoadDataActivity.class));
            return;
        }
        dialog = new ProgressDialog(this);
        dialog.setMessage("加载坐标，请稍等。。。");
        dialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                datas.clear();
                datas.addAll(ExcelUtil.onImport(MapActivity.this, Constant.EXCEL_FILE_Path));
                handler.sendEmptyMessage(2);
            }
        }).start();


    }

    private void initView() {
        area = (TextView) findViewById(R.id.area);
        custmer = (TextView) findViewById(R.id.custmer);
        personName = (TextView) findViewById(R.id.personName);
        address = (TextView) findViewById(R.id.address);
        point = (TextView) findViewById(R.id.point);

    }

    private void initData() {


//        size = DataUtil.addressList.size();
        dialog = new ProgressDialog(this);
        dialog.setMessage("加载坐标，请稍等。。。");
        dialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (final AddressBean bean :
                        DataUtil.addressList) {
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
                                    Toast.makeText(MapActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
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
                                            Toast.makeText(MapActivity.this, bean.getPersonName() + ":" + bean.getAddress() + ")查不出", Toast.LENGTH_LONG).show();
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
            save.setDataList("dataItem", DataUtil.addressList);
            dialog.dismiss();
            showMap();
        }
    }

    private void showMap() {
        for (AddressBean bean :
                datas) {
            getLocationByLL(Double.valueOf(bean.getLat()), Double.valueOf(bean.getLng()));
            changeUI(bean);
        }
    }

    public void getLocationByLL(double la, double lg) {
        //地理坐标的数据结构
        LatLng latLng = new LatLng(la, lg);
        //描述地图状态将要发生的变化,通过当前经纬度来使地图显示到该位置
        MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
        mBaiduMap.setMapStatus(msu);
    }


    public void changeUI(AddressBean bean) {
        // 定义Marker坐标点
        LatLng point = new LatLng(Double.valueOf(bean.getLat()), Double.valueOf(bean.getLng()));
        // 构建Marker图标
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.mipmap.icon_end);
        // 构建MarkerOption，用于在地图上添加Marker
        OverlayOptions options = new MarkerOptions().position(point).icon(bitmapDescriptor);
        // 在地图上添加Marker，并显示
        //mBaiduMap.addOverlay(options);
        Marker marker = (Marker) (mBaiduMap.addOverlay(options));

        // 设置额外的信息
        Bundle bundle = new Bundle();
        bundle.putSerializable("deviceSN", bean);
        marker.setExtraInfo(bundle);

        //定义地图状态
        MapStatus mapStatus = new MapStatus.Builder().target(point).zoom(18).build();
        //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mapStatus);
        //改变地图状态
        mBaiduMap.setMapStatus(mapStatusUpdate);

        /**
         * 地图标注点的点击监听接口
         */
        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                AddressBean bean = (AddressBean) marker.getExtraInfo().get("deviceSN");
//                Toast.makeText(MapActivity.this.getApplicationContext(), s + "被点击了！", Toast.LENGTH_SHORT).show();
                showDetail(bean);
                return false;
            }
        });
    }

    private void showDetail(AddressBean bean) {
        area.setText("区域：" + bean.getArea());
        custmer.setText("客户：" + bean.getCustmer());
        personName.setText("业务员：" + bean.getPersonName());
        address.setText("地址：" + bean.getAddress());
        point.setText("坐标：[" + bean.getLat() + "," + bean.getLng() + "]");

    }

    public void update(View view) {
        startActivity(new Intent(MapActivity.this, LoadDataActivity.class));
    }

    public void exit(View view) {
        Toast.makeText(this, "see you !", Toast.LENGTH_LONG).show();
        finish();
    }

    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null)
                return;
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            LatLng ll = new LatLng(location.getLatitude(),
                    location.getLongitude());
            MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);

            //北京市sdafasdfaslfjslakdfkl复兴门外大街--北京市西城区复兴门外大街6-2号---6-2号
            //打出來就是這個。看到了嗎
            System.out.println(location.getCity() + "sdafasdfaslfjslakdfkl" + location.getStreet() + "--" + location.getAddrStr() + "---" + location.getStreetNumber());
            Message msg = Message.obtain();
            msg.what = 1;
            msg.obj = location.getAddrStr();
            handler.sendMessage(msg);
            mLocClient.stop();
        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        loadData();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        // 退出时销毁定位
        mLocClient.stop();
        // 关闭定位图层
        super.onDestroy();
    }

}
