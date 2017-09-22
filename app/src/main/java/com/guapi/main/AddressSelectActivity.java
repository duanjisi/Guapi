package com.guapi.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMapOptions;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.UiSettings;
import com.amap.api.maps2d.model.BitmapDescriptor;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.Circle;
import com.amap.api.maps2d.model.CircleOptions;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.ewuapp.framework.presenter.Impl.BasePresenterImpl;
import com.ewuapp.framework.presenter.Impl.BaseViewPresenterImpl;
import com.ewuapp.framework.view.BaseActivity;
import com.guapi.R;
import com.guapi.main.adapter.PointAdapter;
import com.guapi.tool.Utils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Johnny on 2017-09-06.
 */
public class AddressSelectActivity extends BaseActivity<BasePresenterImpl, BaseViewPresenterImpl> implements
        LocationSource,
        AMapLocationListener,
        PoiSearch.OnPoiSearchListener {
    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.query)
    EditText et_words;
    @Bind(R.id.search_clear)
    ImageButton searchClear;
    @Bind(R.id.tv_search)
    TextView tvSearch;
    @Bind(R.id.map)
    MapView mapView;
    @Bind(R.id.listview)
    ListView listview;
    private InputMethodManager inputMethodManager;
    public static final String LOCATION_MARKER_FLAG = "mylocation";
    private static final int STROKE_COLOR = Color.argb(180, 3, 145, 255);
    private static final int FILL_COLOR = Color.argb(10, 0, 0, 180);
    private double distance;
    private boolean isFirst = true;
    private Circle mCircle;
    private AMap aMap;
    private UiSettings mUiSettings;
    private Marker mLocMarker;
    private AMapLocationClient mLocationClient = null;
    private AMapLocationClientOption mLocationClientOption = null;

    private PointAdapter pointAdapter;
    private PoiResult poiResult; // poi返回的结果
    private int currentPage = 0;// 当前页面，从0开始计数
    private PoiSearch.Query query;// Poi查询条件类
    private LatLonPoint lp;//

    private PoiSearch poiSearch;
    private List<PoiItem> poiItems;// poi数据
    private AMapLocation location;
    private String keyWord;
    private String city, area, curCity, curGeohash;

    @NonNull
    @Override
    protected BasePresenterImpl getPresent() {
        return new BasePresenterImpl(getSupportFragmentManager());
    }

    @NonNull
    @Override
    protected BaseViewPresenterImpl getViewPresent() {
        return new BaseViewPresenterImpl();
    }

    @Override
    protected int getContentViewID() {
        return R.layout.activity_address_select;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        setupMap(savedInstanceState);
        initListView();
        hideSoftKeyboard();
    }

    protected void hideSoftKeyboard() {
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null)
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private void initListView() {
        pointAdapter = new PointAdapter(context);
        listview.setAdapter(pointAdapter);
        listview.setOnItemClickListener(new itemClickListener());
    }

    private void setupMap(Bundle bundle) {
        mapView.onCreate(bundle);
        aMap = mapView.getMap();
        if (aMap != null) {
            mUiSettings = aMap.getUiSettings();
            mUiSettings
                    .setLogoPosition(AMapOptions.LOGO_POSITION_BOTTOM_LEFT);// 设置地图logo显示在左下方
            mUiSettings.setZoomPosition(AMapOptions.ZOOM_POSITION_RIGHT_CENTER);
            aMap.setLocationSource(this);
            aMap.setMyLocationEnabled(true);
            aMap.postInvalidate();
        }
    }

    private void setupMapClient() {
        if (mLocationClient == null) {
            //初始化定位
            mLocationClient = new AMapLocationClient(this);
            //初始化定位参数
            mLocationClientOption = new AMapLocationClientOption();
            //设置定位回调监听
            mLocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationClientOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位时间间隔
            mLocationClientOption.setInterval(1500);
            //设置定位参数
            mLocationClient.setLocationOption(mLocationClientOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mLocationClient.startLocation();//启动定位
        }
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        setupMapClient();
    }

    @Override
    public void deactivate() {
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
            mLocationClient.onDestroy();
        }
        mLocationClient = null;
    }

    private class itemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            PoiItem poiItem = (PoiItem) adapterView.getItemAtPosition(i);
            if (poiItem != null) {
                String address = poiItem.getTitle();
//                String geohash = poiItem.getLatLonPoint().getLongitude() + "-" + poiItem.getLatLonPoint().getLatitude();
                LatLonPoint point = poiItem.getLatLonPoint();
                Intent intent = new Intent();
                intent.putExtra("lat", point.getLatitude());
                intent.putExtra("lng", point.getLongitude());
                intent.putExtra("address", address);
                setResult(RESULT_OK, intent);
//                EventBus.getDefault().post(new ActionEntity(Constants.Action.CHANGE_ADDRESS,
//                        new PointEntity(point.getLatitude(), point.getLongitude(), address)));
                finish();
            }
        }
    }

    @OnClick({R.id.iv_back, R.id.search_clear, R.id.tv_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.search_clear:
                et_words.setText("");
                break;
            case R.id.tv_search:
                keyWord = et_words.getText().toString().trim();
                doSearchQuery();
                break;
        }
    }

    /**
     * 开始进行poi搜索
     */
    /**
     * 开始进行poi搜索
     */
    protected void doSearchQuery() {
        if (TextUtils.isEmpty(city) && location != null) {
            initData(location);
        }
        currentPage = 0;
        query = new PoiSearch.Query(keyWord, "", city);// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
        query.setPageSize(20);// 设置每页最多返回多少条poiitem
        query.setPageNum(currentPage);// 设置查第一页

        if (lp != null) {
            poiSearch = new PoiSearch(this, query);
            poiSearch.setOnPoiSearchListener(this);
            poiSearch.setBound(new PoiSearch.SearchBound(lp, 5000, true));//
            // 设置搜索区域为以lp点为圆心，其周围5000米范围
            poiSearch.searchPOIAsyn();// 异步搜索
        }
    }

    private void initData(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (lp == null) {
                lp = new LatLonPoint(aMapLocation.getLatitude(), aMapLocation.getLongitude());
            }
            city = aMapLocation.getCity();
        }
    }

    @Override
    public void onLocationChanged(AMapLocation mapLocation) {
        if (mapLocation != null && mapLocation.getErrorCode() == 0) {
            LatLng location = new LatLng(mapLocation.getLatitude(), mapLocation.getLongitude());
            if (isFirst) {
                isFirst = false;
                scalePoint(mapLocation);
                addCircle(location, mapLocation.getAccuracy());//添加定位精度圆
                addMarker(location);//添加定位图标

                keyWord = mapLocation.getPoiName();
                city = mapLocation.getCity();
                if (lp == null) {
                    lp = new LatLonPoint(mapLocation.getLatitude(), mapLocation.getLongitude());
                }
                doSearchQuery();
            } else {
                mCircle.setCenter(location);
                mCircle.setRadius(30);
                mLocMarker.setPosition(location);
            }
        } else {
            String errText = "定位失败," + mapLocation.getErrorCode() + ": " + mapLocation.getErrorInfo();
            Log.i("info", "errText:" + errText);
        }
    }


    private void scalePoint(AMapLocation aMapLocation) {
        LatLng latLng = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());
        aMap.moveCamera(CameraUpdateFactory.changeLatLng(latLng));
        if (distance != 0) {
            aMap.moveCamera(CameraUpdateFactory.zoomTo(Utils.getZoomRank(distance)));
        } else {
            aMap.moveCamera(CameraUpdateFactory.zoomTo(19));
        }
    }

    private void addCircle(LatLng latlng, double radius) {
        CircleOptions options = new CircleOptions();
        options.strokeWidth(1f);
        options.fillColor(FILL_COLOR);
        options.strokeColor(STROKE_COLOR);
        options.center(latlng);
//        options.radius(radius);
        options.radius(30);
        mCircle = aMap.addCircle(options);
    }

    private void addMarker(LatLng latlng) {
        if (mLocMarker != null) {
            return;
        }
        Bitmap bMap = BitmapFactory.decodeResource(this.getResources(),
                R.drawable.dtan);
        BitmapDescriptor des = BitmapDescriptorFactory.fromBitmap(bMap);
        MarkerOptions options = new MarkerOptions();
        options.icon(des);
        options.anchor(0.5f, 0.5f);
        options.position(latlng);
        mLocMarker = aMap.addMarker(options);
        mLocMarker.setTitle(LOCATION_MARKER_FLAG);
    }

    @Override
    public void onPoiSearched(PoiResult result, int rcode) {
        if (rcode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getQuery() != null) {
                // 搜索poi的结果
                if (result.getQuery().equals(query)) {// 是否是同一条
                    poiResult = result;
                    poiItems = poiResult.getPois();// 取得第一页的poiitem数据，页数从数字0开始
                    pointAdapter.initData(poiItems);
                }
            } else {
                showToast("没数据!", true);
            }
        } else {
            showToast(rcode, true);
        }
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }


    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mapView != null)
            mapView.onDestroy();
        if (mLocationClient != null) {
            mLocationClient.onDestroy();
        }
    }
}
