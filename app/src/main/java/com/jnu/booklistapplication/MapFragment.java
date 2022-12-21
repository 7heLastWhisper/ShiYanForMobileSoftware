package com.jnu.booklistapplication;

import android.app.Notification;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.IpSecManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.GroundOverlayOptions;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.jnu.booklistapplication.data.DataLoader;
import com.jnu.booklistapplication.data.ShopLocation;
import com.jnu.booklistapplication.utils.BitmapUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import javax.net.ssl.HttpsURLConnection;

public class MapFragment extends Fragment {
    public static final int WHAT_DATA_OK = 1000;
    MapView mapView;

    public MapFragment() {
        // Required empty public constructor
    }

    public static MapFragment newInstance(String param1, String param2) {
        MapFragment fragment = new MapFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);
        mapView = rootView.findViewById(R.id.baidu_map_view);
        LatLng centerPoint = new LatLng(22.255925, 113.541112);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.map_marker);
        bitmap = new BitmapUtil().zoomImg(bitmap, 150, 150);
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(bitmap);

        Handler handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                if (msg.what == WHAT_DATA_OK) {
                    ArrayList<ShopLocation> shopLocations = new ArrayList<>();
                    shopLocations = new DataLoader().parsonJson(msg.getData().getString("data"));
                    try {
                        for (int i = 0; i < shopLocations.size(); i++) {
                            //设置纬度和经度
                            LatLng centerPoint = new LatLng(shopLocations.get(i).getLatitude(), shopLocations.get(i).getLongitude());
                            //添加图标
                            MarkerOptions markerOptions = new MarkerOptions();//标记物
                            markerOptions.position(centerPoint).icon(bitmapDescriptor);//设置图标
                            mapView.getMap().addOverlay(markerOptions);//设置生效
                            //添加文字
                            OverlayOptions textOption = new TextOptions().bgColor(0xAAFFFF00).fontSize(30)
                                    .fontColor(0xFFFF00FF).text(shopLocations.get(i).getName()).rotate(0).position(centerPoint);
                            mapView.getMap().addOverlay(textOption);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                //通过网络获取坐标信息
                try {
                    String str = new DataLoader().download("http://file.nidama.net/class/mobile_develop/data/bookstore2022.json");
                    Message message = new Message();
                    message.what = WHAT_DATA_OK;
                    Bundle bundle = new Bundle();
                    bundle.putString("data", str);
                    message.setData(bundle);
                    handler.sendMessage(message);
                    Log.i("test", str);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(runnable).start();//线程启动读取网络数据


        MapStatus mapStatus = new MapStatus.Builder().target(centerPoint).zoom(18).build();
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mapStatus);
        mapView.getMap().setMapStatus(mapStatusUpdate);

//        //定义Ground的显示地理范围
//        LatLng northeast = new LatLng(22.259225,113.544712);
//        LatLng southwest = new LatLng(22.253074,113.537777);
//        LatLngBounds bounds = new LatLngBounds.Builder()
//                .include(northeast)
//                .include(southwest)
//                .build();
//
//        //定义Ground显示的图片
//        BitmapDescriptor bdGround = BitmapDescriptorFactory.fromResource(R.drawable.xiaoxun);
//        //定义GroundOverlayOptions对象
//        OverlayOptions ooGround = new GroundOverlayOptions()
//                .positionFromBounds(bounds)
//                .image(bdGround)
//                .transparency(0.6f); //覆盖物透明度
//
//        //在地图中添加Ground覆盖物
//       mapView.getMap().addOverlay(ooGround);

        //响应事件
        mapView.getMap().setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Toast.makeText(getContext(), "Marker被点击了！", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        return rootView;
    }

    //管理地图的生命周期
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }
}