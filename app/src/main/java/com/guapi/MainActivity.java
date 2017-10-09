package com.guapi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMapOptions;
import com.amap.api.maps2d.AMapUtils;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.UiSettings;
import com.amap.api.maps2d.model.BitmapDescriptor;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.Circle;
import com.amap.api.maps2d.model.CircleOptions;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.ewuapp.framework.common.http.CallBack;
import com.ewuapp.framework.common.http.Constants;
import com.ewuapp.framework.common.http.Result;
import com.ewuapp.framework.common.utils.CompatUtil;
import com.ewuapp.framework.common.utils.GlideUtil;
import com.ewuapp.framework.common.utils.IntentUtil;
import com.ewuapp.framework.presenter.Impl.BasePresenterImpl;
import com.ewuapp.framework.presenter.Impl.BaseViewPresenterImpl;
import com.ewuapp.framework.view.BaseActivity;
import com.ewuapp.framework.view.widget.HintDialog;
import com.ewuapp.framework.view.widget.ToolBarView;
import com.google.gson.Gson;
import com.guapi.auth.LoginActivity;
import com.guapi.http.Http;
import com.guapi.main.CatchActivity;
import com.guapi.main.ChooseTypeActivity;
import com.guapi.main.FindHBDialog;
import com.guapi.model.ActionEntity;
import com.guapi.model.response.GPResponse;
import com.guapi.model.response.LoginResponse;
import com.guapi.tool.Global;
import com.guapi.tool.JPushUtil;
import com.guapi.tool.PreferenceKey;
import com.guapi.tool.Utils;
import com.guapi.usercenter.FriendActivity;
import com.guapi.usercenter.NewMessageActivity;
import com.guapi.usercenter.SettingActivity;
import com.guapi.usercenter.UserCenterActivity;
import com.guapi.usercenter.UserGuideActivity;
import com.guapi.usercenter.chat.ChatActivity;
import com.guapi.util.PermissonUtil.PermissionUtil;
import com.guapi.util.SensorEventHelper;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMContactListener;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.library.im.Constant;
import com.library.im.controller.HxHelper;
import com.library.im.db.InviteMessgeDao;
import com.library.im.domain.InviteMessage;
import com.listener.PermissionListener;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.orhanobut.hawk.Hawk;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.zijing.sharesdk.BitmapUtil;
import com.zijing.sharesdk.ShareCallBack;
import com.zijing.sharesdk.ShareDialog;
import com.zijing.sharesdk.ShareSdkUtil;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

import static com.guapi.tool.Global.TYPE_HB;
import static com.guapi.tool.Global.TYPE_SHOW_ALL;
import static com.guapi.tool.Global.TYPE_SHOW_FRIEND;

public class MainActivity extends BaseActivity<BasePresenterImpl, BaseViewPresenterImpl> implements
        LocationSource,
        AMapLocationListener,
        AMap.OnMarkerClickListener,
        AMap.OnMapLoadedListener,
        AMap.OnCameraChangeListener,
        AMap.OnMapClickListener {
    @Bind(R.id.titleBar)
    ToolBarView toolBarView;
    @Bind(R.id.map)
    MapView mapView;
    @Bind(R.id.iv_type)
    ImageView ivType;
    public static final String LOCATION_MARKER_FLAG = "mylocation";
    private static final int STROKE_COLOR = Color.argb(180, 3, 145, 255);
    private static final int FILL_COLOR = Color.argb(10, 0, 0, 180);
    private Circle mCircle;
    private AMap aMap;
    private UiSettings mUiSettings;
    private SensorEventHelper mSensorHelper;
    private AMapLocationClient mLocationClient = null;
    private AMapLocationClientOption mLocationClientOption = null;
    private AMapLocation userLocation;
    private Marker mLocMarker;
    private Drawer drawer = null;
    private View drawerContentView;
    private List<Marker> markerList = new ArrayList<>();
    private int raduis = 0;
    private Gson gson = new Gson();
    private boolean hasQueryGP = false;
    private boolean isFirst = true;
    private String currentShowType = TYPE_SHOW_ALL; // 所有人可看
    // 账号在别处登录
    public boolean isConflict = false;
    // 账号被移除
    private boolean isCurrentAccountRemoved = false;
    private InviteMessgeDao inviteMessgeDao;
    private BroadcastReceiver broadcastReceiver;
    private LocalBroadcastManager broadcastManager;
    private String WEIXIN_APP_ID = "wxb3893f7dffa1bd57";
    private IWXAPI iwxapi;
    private double distance;
    private boolean isFirstRequest = true;
    private PermissionListener permissionListener;

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
        return R.layout.activity_main;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        HxHelper.isMainActivityRun = true;
//        mapView.onCreate(savedInstanceState);
        iwxapi = WXAPIFactory.createWXAPI(context, WEIXIN_APP_ID);
        iwxapi.registerApp(WEIXIN_APP_ID);
//        setupMap(savedInstanceState);
//        setupMapClient();
//        setupDrawer();
        EMClient.getInstance().contactManager().setContactListener(new MyContactListener());
//        AppConstants.UPDATE_MAIN_FRAME = false;
        inviteMessgeDao = new InviteMessgeDao(this);
        //注册local广播接收者，用于接收demohelper中发出的群组联系人的变动通知
        registerBroadcastReceiver();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        if (Hawk.get(Constant.ACCOUNT_CONFLICT, false)) {
            Hawk.remove(Constant.ACCOUNT_CONFLICT);
            showConflictDialog(R.string.Logoff_notification, R.string.connect_conflict);
        } else if (Hawk.get(Constant.ACCOUNT_REMOVED, false)) {
            Hawk.remove(Constant.ACCOUNT_REMOVED);
            showConflictDialog(R.string.Remove_the_notification, R.string.em_user_remove);
        }
        getPermission(savedInstanceState);
    }

    private void getPermission(Bundle bundle) {
        permissionListener = new PermissionListener() {
            @Override
            public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
                PermissionUtil.onRequestPermissionsResult(this, requestCode, permissions, permissionListener);
            }

            @Override
            public void onRequestPermissionSuccess() {
                setupMap(bundle);
            }

            @Override
            public void onRequestPermissionError() {
                showToast("请给予定位权限", true);
            }
        };
        PermissionUtil
                .with(this)
                .permissions(
                        PermissionUtil.PERMISSIONS_GROUP_LOACATION //定位授权
                ).request(permissionListener);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionUtil.onRequestPermissionsResult(this, requestCode, permissions, permissionListener);
    }

    @Override
    protected void setUpToolbar() {
        super.setUpToolbar();

        toolBarView.setTitleText(CompatUtil.getString(this, R.string.home));
        toolBarView.setDrawable(ToolBarView.TEXT_LEFT, R.mipmap.ic_drawer);
        toolBarView.setDrawable(ToolBarView.TEXT_RIGHT, R.mipmap.ic_add1);

        toolBarView.setOnLeftClickListener(new ToolBarView.OnBarLeftClickListener() {
            @Override
            public void onLeftClick(View v) {
                if (drawer == null) {
                    setupDrawer();
                }
                drawer.openDrawer();
            }
        });

        toolBarView.setOnRightClickListener(new ToolBarView.OnBarRightClickListener() {
            @Override
            public void onRightClick(View v) {
//                IntentUtil.startActivity(MainActivity.this, ImageTestActivity.class, false);
                IntentUtil.startActivity(MainActivity.this, ChooseTypeActivity.class, false);
                overridePendingTransition(R.anim.slide_in_from_bottom, 0);
            }
        });
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

//        mLocationClientOption = new AMapLocationClientOption();
//        mLocationClient = new AMapLocationClient(getApplicationContext());
//        mLocationClient.setLocationListener(this);
//        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
//        mLocationClientOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
//        //设置定位间隔,单位毫秒,默认为2000ms，最低1000ms。
//        mLocationClientOption.setInterval(10000);
//        //设置是否返回地址信息（默认返回地址信息）
//        mLocationClientOption.setNeedAddress(true);
//        //单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
//        mLocationClientOption.setHttpTimeOut(10000);
//        mLocationClient.setLocationOption(mLocationClientOption);
//        // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
//        // 注意设置合适的定位时间的间隔（最小间隔支持为1000ms），并且在合适时间调用stopLocation()方法来取消定位请求
//        // 在定位结束后，在合适的生命周期调用onDestroy()方法
//        // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
//        //启动定位
//        mLocationClient.startLocation();
    }

    private void setupMap(Bundle bundle) {
        mapView.onCreate(bundle);
        aMap = mapView.getMap();
        if (aMap != null) {
            mSensorHelper = new SensorEventHelper(this);
            if (mSensorHelper != null) {
                mSensorHelper.registerSensorListener();
            }
//            aMap.getUiSettings().setZoomControlsEnabled(false);
            mUiSettings = aMap.getUiSettings();
            mUiSettings
                    .setLogoPosition(AMapOptions.LOGO_POSITION_BOTTOM_LEFT);// 设置地图logo显示在左下方
            mUiSettings.setZoomPosition(AMapOptions.ZOOM_POSITION_RIGHT_CENTER);
//            aMap.moveCamera(com.amap.api.maps2d.CameraUpdateFactory.zoomTo(18f));
//            aMap.setOnMarkerClickListener(this);
//            showLocationStyle(aMap);

            aMap.setOnMapClickListener(this);
            aMap.setOnMarkerClickListener(this);//marker点击事件监听接口。
            aMap.setOnCameraChangeListener(this);
            aMap.setOnMapLoadedListener(this);//设置地图加载监听
            // 设置定位监听
            aMap.setLocationSource(this);
            aMap.setMyLocationEnabled(true);
            aMap.postInvalidate();
        }
    }

    private void showGuaPi(String type, GPResponse.GpListBean bean) {
        MarkerOptions markerOption = new MarkerOptions();
        markerOption.position(new LatLng(bean.getLat(), bean.getLng()));

        markerOption.draggable(false);//设置Marker可拖动
        int resource;
        if (TextUtils.equals(type, TYPE_HB)) {
            resource = R.mipmap.ic_hb;
        } else if (TextUtils.equals(type, Global.TYPE_PIC)) {
            resource = R.drawable.ic_pic_small;
        } else {
            resource = R.drawable.ic_video_small;
        }
        markerOption.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                .decodeResource(getResources(), resource)));
        // 将Marker设置为贴地显示，可以双指下拉地图查看效果

        if (mapView == null || mapView.getMap() == null) {
            return;
        }
        Marker marker = mapView.getMap().addMarker(markerOption);
        marker.setObject(bean);
        markerList.add(marker);
    }

    private void showLocationStyle(AMap aMap) {
        MyLocationStyle locationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        locationStyle.interval(5000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。

        //定位蓝点提供8种模式
//        locationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_SHOW);//只定位一次。
//        locationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE);//定位一次，且将视角移动到地图中心点。
        locationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW);//连续定位、且将视角移动到地图中心点，定位蓝点跟随设备移动。（1秒1次定位）
//        locationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_MAP_ROTATE);//连续定位、且将视角移动到地图中心点，地图依照设备方向旋转，定位点会跟随设备移动。（1秒1次定位）
//        locationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）默认执行此种模式。
        //以下三种模式从5.1.0版本开始提供
//        locationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER);//连续定位、蓝点不会移动到地图中心点，定位点依照设备方向旋转，并且蓝点会跟随设备移动。
//        locationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW_NO_CENTER);//连续定位、蓝点不会移动到地图中心点，并且蓝点会跟随设备移动。
//        locationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_MAP_ROTATE_NO_CENTER);//连续定位、蓝点不会移动到地图中心点，地图依照设备方向旋转，并且蓝点会跟随设备移动。

//        locationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_add1));
        aMap.setMyLocationStyle(locationStyle);//设置定位蓝点的Style
        //aMap.getUiSettings().setMyLocationButtonEnabled(true);设置默认定位按钮是否显示，非必需设置。
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
    }

    private void setupDrawer() {
        drawerContentView = LayoutInflater.from(this).inflate(R.layout.nav_content_main, null);
        drawer = new DrawerBuilder(this)
                .withActivity(this)
                .withCustomView(drawerContentView)
                .withSliderBackgroundColor(CompatUtil.getColor(this, android.R.color.white))
                .build();
        drawerContentView.findViewById(R.id.layout_home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.closeDrawer();
            }
        });
        drawerContentView.findViewById(R.id.layout_msg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(NewMessageActivity.class);
            }
        });
        drawerContentView.findViewById(R.id.layout_friend).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(FriendActivity.class);
            }
        });
        drawerContentView.findViewById(R.id.layout_setting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startForResult(null, FINSH_ACTIVITY, SettingActivity.class);
            }
        });
        drawerContentView.findViewById(R.id.ll_user_data).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                LoginResponse loginResponse = Hawk.get(PreferenceKey.LoginResponse, null);
                bundle.putString("user_id", loginResponse.getUser().getUid());
                bundle.putString("from", "MainActivity");
                bundle.putString("destName", loginResponse.getUser().getNickname());
                startActivity(bundle, UserCenterActivity.class);
                drawer.closeDrawer();
            }
        });
        drawerContentView.findViewById(R.id.layout_invite).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//邀请好友
                inviteShare();
            }
        });
        drawerContentView.findViewById(R.id.layout_guide).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//新手引导
                startActivity(UserGuideActivity.class);
            }
        });
        drawerContentView.findViewById(R.id.layout_exit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//推出登陆
                logout();
            }
        });
        LoginResponse loginResponse = Hawk.get(PreferenceKey.LoginResponse);
        ((TextView) drawerContentView.findViewById(R.id.tv_user)).setText(loginResponse.getUser().getNickname());
        GlideUtil.loadPicture(loginResponse.getUser().getAvatarUrl(), (ImageView) drawerContentView.findViewById(R.id.iv_user));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == FINSH_ACTIVITY) {
            finish();
        }
    }

    private void logout() {
        addDisposable(Http.logout(context, new CallBack<Result>() {
            @Override
            public void handlerSuccess(Result data) {
                Hawk.remove(PreferenceKey.HAS_LOGIN);
                HxHelper.getInstance().logout(context, new EMCallBack() {
                    @Override
                    public void onSuccess() {
                        finish();
                        JPushUtil.get().clearAlias();
                        startActivity(LoginActivity.class);
                    }

                    @Override
                    public void onError(int i, String s) {
                    }

                    @Override
                    public void onProgress(int i, String s) {
                    }
                });

            }

            @Override
            public void fail(int code, String message) {
                if (code == Constants.NET_CODE_NEED_LOGIN) {
                    IntentUtil.startActivity(MainActivity.this, LoginActivity.class, false);
                } else {
                    showMessage(message);
                }
            }
        }));
    }

    private void inviteShare() {
        ShareSdkUtil.share(this, new ShareCallBack() {
            @Override
            public void callBack(ShareDialog.ShareType type) {
                if (type == ShareDialog.ShareType.Wechat) {
                    WXWebpageObject wxWebpageObject = new WXWebpageObject();
                    String url = "http://120.26.94.214:8080/static/share.html";
                    wxWebpageObject.webpageUrl = url;
                    WXMediaMessage msg = new WXMediaMessage(wxWebpageObject);
                    msg.title = "瓜皮分享";
                    msg.description = "";
                    Bitmap thumb = BitmapFactory.decodeResource(context.getResources(), R.mipmap.logo);
                    msg.thumbData = BitmapUtil.bitmapToBytes(thumb);
                    SendMessageToWX.Req req = new SendMessageToWX.Req();
                    req.transaction = String.valueOf(System.currentTimeMillis());
                    req.message = msg;
                    req.scene = SendMessageToWX.Req.WXSceneSession;
                    iwxapi.sendReq(req);
                } else if (type == ShareDialog.ShareType.WechatMoment) {
                    WXWebpageObject wxWebpageObject = new WXWebpageObject();
                    String url = "http://120.26.94.214:8080/static/share.html";
                    wxWebpageObject.webpageUrl = url;
                    WXMediaMessage msg = new WXMediaMessage(wxWebpageObject);
                    msg.title = "瓜皮分享";
                    msg.description = "";
                    Bitmap thumb = BitmapFactory.decodeResource(context.getResources(), R.mipmap.logo);
                    msg.thumbData = BitmapUtil.bitmapToBytes(thumb);
                    SendMessageToWX.Req req = new SendMessageToWX.Req();
                    req.transaction = String.valueOf(System.currentTimeMillis());
                    req.message = msg;
                    req.scene = SendMessageToWX.Req.WXSceneTimeline;
                    iwxapi.sendReq(req);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
        if (mSensorHelper != null) {
            mSensorHelper.registerSensorListener();
        }
        EMClient.getInstance().chatManager().addMessageListener(messageListener);
//        getGP(userLocation);
        setupDrawer();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
        if (mSensorHelper != null) {
            mSensorHelper.unRegisterSensorListener();
            mSensorHelper.setCurrentMarker(null);
            mSensorHelper = null;
        }
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(Constant.ACCOUNT_CONFLICT, isConflict);
        outState.putBoolean(Constant.ACCOUNT_REMOVED, isCurrentAccountRemoved);
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.getBooleanExtra(Constant.ACCOUNT_CONFLICT, false)) {
            showConflictDialog(R.string.Logoff_notification, R.string.connect_conflict);
        } else if (intent.getBooleanExtra(Constant.ACCOUNT_REMOVED, false)) {
            showConflictDialog(R.string.Remove_the_notification, R.string.em_user_remove);
        }
    }

    @Override
    protected void onDestroy() {
        HxHelper.isMainActivityRun = false;
        super.onDestroy();
        if (mapView != null)
            mapView.onDestroy();
        if (mLocationClient != null) {
            mLocationClient.onDestroy();
        }
    }

    @Override
    public void onLocationChanged(AMapLocation mapLocation) {
        userLocation = mapLocation;
        if (mapLocation != null && mapLocation.getErrorCode() == 0) {
            BaseApp.getInstance().setUserLocation(mapLocation);
            LatLng location = new LatLng(mapLocation.getLatitude(), mapLocation.getLongitude());
            if (isFirst) {
                isFirst = false;
                scalePoint(mapLocation);
                addCircle(location, mapLocation.getAccuracy());//添加定位精度圆
                addMarker(location);//添加定位图标
                mSensorHelper.setCurrentMarker(mLocMarker);//定位图标旋转
            } else {
                mCircle.setCenter(location);
                mCircle.setRadius(50);
                mLocMarker.setPosition(location);
            }
            Log.i("info", "================onLocationChanged()");
            if (System.currentTimeMillis() - lastTime > 1500) {
                lastTime = System.currentTimeMillis();
                double lat = mapLocation.getLatitude();
                double lng = mapLocation.getLongitude();
                getGP(lat, lng);
            }
//            if (mapLocation.getErrorCode() == 0) {
//                BaseApp.getInstance().setUserLocation(mapLocation);
//                Timber.d("定位地址:%s", mapLocation.getAddress());
//                Timber.d("定位坐标:%1s, %2s", mapLocation.getLatitude(), mapLocation.getLongitude());
//                if (!hasQueryGP) {
//                    hasQueryGP = true;
//                    getGP(mapLocation);
//                }
//            } else {
//                //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
//                Timber.e("location Error, ErrCode:"
//                        + mapLocation.getErrorCode() + ", errInfo:"
//                        + mapLocation.getErrorInfo());
//            }
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
        options.radius(50);
        mCircle = aMap.addCircle(options);
    }

    private void addMarker(LatLng latlng) {
        if (mLocMarker != null) {
            return;
        }
        Bitmap bMap = BitmapFactory.decodeResource(this.getResources(),
                R.drawable.navi_map_gps_locked);//gps_point
        BitmapDescriptor des = BitmapDescriptorFactory.fromBitmap(bMap);
        MarkerOptions options = new MarkerOptions();
        options.icon(des);
        options.anchor(0.5f, 0.5f);
        options.position(latlng);
        mLocMarker = aMap.addMarker(options);
        mLocMarker.setTitle(LOCATION_MARKER_FLAG);
    }

    //    AMapLocation mapLocation
    private void getGP(double lat, double lng) {
//        if (mapLocation == null) {
//            return;
//        }
        Http.queryGp(this, lat,
                lng, "1000", currentShowType, new CallBack<GPResponse>() {
                    @Override
                    public void handlerSuccess(GPResponse data) {
                        hasQueryGP = true;
                        for (Marker marker : markerList) {
                            marker.destroy();
                        }
                        if (data != null && data.getGpList() != null) {
                            List<GPResponse.GpListBean> gpListBeen = data.getGpList();
                            for (GPResponse.GpListBean bean : gpListBeen) {
                                Log.i("info", "===============Is_find:" + bean.getIs_find());
                                showGuaPi(bean.getType(), bean);
                            }
                        }
                    }

                    @Override
                    public void fail(int code, String message) {
                        hasQueryGP = false;
                        if (code == Constants.NET_CODE_NEED_LOGIN) {
                            IntentUtil.startActivity(MainActivity.this, LoginActivity.class, false);
                        }
                    }
                });
    }

    @OnClick(R.id.iv_location)
    public void reLocation() {
//        hasQueryGP = false;
//        setupMap();
        backOlderPosition();
    }


    private void backOlderPosition() {
        if (userLocation != null) {
            LatLng latLng = new LatLng(userLocation.getLatitude(), userLocation.getLongitude());
            aMap.moveCamera(CameraUpdateFactory.changeLatLng(latLng));
            aMap.moveCamera(CameraUpdateFactory.zoomTo(19));
        }
    }

    @OnClick(R.id.iv_type)
    public void changeType() {
        if (TextUtils.equals(currentShowType, TYPE_SHOW_ALL)) {
            currentShowType = TYPE_SHOW_FRIEND;
            ivType.setImageResource(R.mipmap.ic_all);
        } else {
            currentShowType = TYPE_SHOW_ALL;
            ivType.setImageResource(R.mipmap.ic_show_friend);
        }
        getGP(userLocation.getLatitude(), userLocation.getLongitude());
    }

    @Subscribe()
    public void error() {

    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        GPResponse.GpListBean bean = (GPResponse.GpListBean) marker.getObject();
        float distance = 10000;
        if (userLocation != null) {
            LatLng latLng1 = new LatLng(userLocation.getLatitude(), userLocation.getLongitude());
            LatLng latLng2 = new LatLng(bean.getLat(), bean.getLng());
            distance = AMapUtils.calculateLineDistance(latLng1, latLng2);
        }
        bean.distance = distance;
        if (distance > 50) {
            showHBDialog(bean);
        } else {
            Bundle bundle = new Bundle();
            bundle.putSerializable(Global.KEY_OBJ, bean);
//            IntentUtil.startActivityWithBundle(this, GPCommentActivity.class, bundle, false);
            IntentUtil.startActivityWithBundle(this, CatchActivity.class, bundle, false);
        }
        return false;
    }

    private void showHBDialog(GPResponse.GpListBean bean) {
        FindHBDialog dialog = new FindHBDialog(this, bean);
        dialog.show();
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {

    }

    private long lastTime = 0;

    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {
        if (System.currentTimeMillis() - lastTime > 1500) {
            lastTime = System.currentTimeMillis();
            float scale = aMap.getScalePerPixel();
            raduis = (int) (scale * Utils.getScreenPx(context) / 2);//半径
            double lat = cameraPosition.target.latitude;
            double lng = cameraPosition.target.longitude;
            getGP(lat, lng);
        }
    }

    @Subscribe
    public void onMessageEvent(ActionEntity event) {
        if (event != null) {
            String action = event.getAction();
            Log.i("info", "=====================onMessageEvent00:" + action);
            if (action.equals("com.guapi.refresh.map")) {
                if (userLocation != null) {
                    getGP(userLocation.getLatitude(), userLocation.getLongitude());
                }
            }
        }
    }

    @Override
    public void onMapClick(LatLng latLng) {

    }

    @Override
    public void onMapLoaded() {

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

    public class MyContactListener implements EMContactListener {
        @Override
        public void onContactAdded(String username) {
        }

        @Override
        public void onContactDeleted(final String username) {
            runOnUiThread(new Runnable() {
                public void run() {
                    if (ChatActivity.instance != null && ChatActivity.instance.hxname != null &&
                            username.equals(ChatActivity.instance.hxname)) {
                        String st10 = getResources().getString(R.string.have_you_removed);
                        ChatActivity.instance.finish();
                    }
                }
            });
        }

        @Override
        public void onContactInvited(String username, String reason) {
        }

        @Override
        public void onContactAgreed(String username) {
        }

        @Override
        public void onContactRefused(String username) {
        }

    }

    private void registerBroadcastReceiver() {
        broadcastManager = LocalBroadcastManager.getInstance(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.ACTION_CONTACT_CHANAGED);
        intentFilter.addAction(Constant.ACTION_GROUP_CHANAGED);
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
//                updateUnreadLabel();
//                updateUnreadAddressLable();
//                if (currentFragment != null && currentFragment instanceof ChatListFragment) {
//                    // 当前页面如果为聊天历史页面，刷新此页面
//                    ((ChatListFragment) currentFragment).refresh();
//                } else if (currentFragment instanceof ContactsFragment) {
//
//                }
            }
        };
        broadcastManager.registerReceiver(broadcastReceiver, intentFilter);
    }

    HintDialog conflictBuilder;

    private void showConflictDialog(int title, int content) {
        HxHelper.getInstance().logout(MainActivity.this, false, null);
        Hawk.remove(PreferenceKey.LoginResponse);
        Hawk.remove(PreferenceKey.SESSIONID);
        Hawk.remove(PreferenceKey.HAS_LOGIN);
        String st = getResources().getString(title);
        conflictBuilder = new HintDialog(context, getResources().getString(content), new String[]{"取消", "确定"});
        conflictBuilder.setCallback(new HintDialog.Callback() {
            @Override
            public void callback() {
                conflictBuilder = null;
                finish();
                Hawk.put(PreferenceKey.HAS_LOGIN, false);
                startActivity(null, LoginActivity.class);
            }

            @Override
            public void cancle() {

            }
        });
        conflictBuilder.show();
    }

    EMMessageListener messageListener = new EMMessageListener() {
        @Override
        public void onMessageReceived(List<EMMessage> messages) {
            // 提示新消息
            for (EMMessage message : messages) {
                HxHelper.getInstance().getNotifier().onNewMsg(message);
            }
//            refreshUIWithMessage();
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> messages) {
        }

        @Override
        public void onMessageReadAckReceived(List<EMMessage> messages) {
        }

        @Override
        public void onMessageDeliveryAckReceived(List<EMMessage> message) {
        }

        @Override
        public void onMessageChanged(EMMessage message, Object change) {
        }
    };

    /**
     * 获取未读申请与通知消息
     *
     * @return
     */
    public int getUnreadAddressCountTotal() {
        int unreadAddressCountTotal = 0;
        unreadAddressCountTotal = inviteMessgeDao.getUnreadMessagesCount();
        return unreadAddressCountTotal;
    }

    /**
     * 获取未读消息数
     *
     * @return
     */
    public int getUnreadMsgCountTotal() {
        int unreadMsgCountTotal = 0;
        int chatroomUnreadMsgCount = 0;
        unreadMsgCountTotal = EMClient.getInstance().chatManager().getUnreadMsgsCount();
        for (EMConversation conversation : EMClient.getInstance().chatManager().getAllConversations().values()) {
            if (conversation.getType() == EMConversation.EMConversationType.ChatRoom)
                chatroomUnreadMsgCount = chatroomUnreadMsgCount + conversation.getUnreadMsgCount();
        }
        return unreadMsgCountTotal - chatroomUnreadMsgCount;
    }

    /**
     * 保存提示新消息
     *
     * @param msg
     */
    private void notifyNewIviteMessage(InviteMessage msg) {
        saveInviteMsg(msg);
        // 提示有新消息
        HxHelper.getInstance().getNotifier().viberateAndPlayTone(null);
        // 刷新bottom bar消息未读数
//        updateUnreadAddressLable();
        // 刷新好友页面ui
//        if (currentFragment instanceof ChatListFragment)
//            ((ChatListFragment) currentFragment).refresh();
    }

    /**
     * 保存邀请等msg
     *
     * @param msg
     */
    private void saveInviteMsg(InviteMessage msg) {
        // 保存msg
        inviteMessgeDao.saveMessage(msg);
        //保存未读数，这里没有精确计算
        inviteMessgeDao.saveUnreadMessageCount(1);
    }

}
