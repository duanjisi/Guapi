package com.guapi.http;

import android.content.Context;
import android.text.TextUtils;

import com.ewuapp.framework.common.http.BaseSubscriber;
import com.ewuapp.framework.common.http.CallBack;
import com.ewuapp.framework.common.http.Result;
import com.ewuapp.framework.common.http.RetrofitManager;
import com.ewuapp.framework.common.http.RxSchedulers;
import com.google.gson.Gson;
import com.guapi.model.request.BindPhoneRequest;
import com.guapi.model.request.DoGPRequest;
import com.guapi.model.request.FoloowRequest;
import com.guapi.model.request.GetCodeRequest;
import com.guapi.model.request.GetFriendsRequest;
import com.guapi.model.request.LoginRequest;
import com.guapi.model.request.MoneyRequest;
import com.guapi.model.request.QueryFocusGpRequest;
import com.guapi.model.request.QueryGpRequest;
import com.guapi.model.request.QueryGpSingleRequest;
import com.guapi.model.request.QueryUserInfoBuHxRequest;
import com.guapi.model.request.RefreshOneMessageRequest;
import com.guapi.model.request.RegisterRequest;
import com.guapi.model.request.SendInviteRequest;
import com.guapi.model.request.UpdateUserRequest;
import com.guapi.model.request.UserGetCountRequest;
import com.guapi.model.response.DoGPResponse;
import com.guapi.model.response.GPResponse;
import com.guapi.model.response.GetCodeResponse;
import com.guapi.model.response.GetFriendsResponse;
import com.guapi.model.response.GpSingleRespone;
import com.guapi.model.response.LoginResponse;
import com.guapi.model.response.QueryFocusGpResponse;
import com.guapi.model.response.QueryUserInfoByHxResponse;
import com.guapi.model.response.RefreshOneMessageResponse;
import com.guapi.model.response.UserGetCountResponse;

import java.io.File;
import java.util.List;

import io.reactivex.disposables.Disposable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static com.guapi.tool.Global.TYPE_SHOW_FRIEND;

/**
 * ╭╯☆★☆★╭╯
 * 　　╰╮★☆★╭╯
 * 　　　 ╯☆╭─╯
 * 　　 ╭ ╭╯
 * 　╔╝★╚╗  ★☆╮     BUG兄，没时间了快上车    ╭☆★
 * 　║★☆★║╔═══╗　╔═══╗　╔═══╗  ╔═══╗
 * 　║☆★☆║║★　☆║　║★　☆║　║★　☆║  ║★　☆║
 * ◢◎══◎╚╝◎═◎╝═╚◎═◎╝═╚◎═◎╝═╚◎═◎╝..........
 *
 * @author Jewel
 * @version 1.0
 * @since 2017/6/23 0023
 */

public class Http {
    public static Gson gson = new Gson();
    private final static String[] KEY_PIC = {
            "pic_file1", "pic_file2", "pic_file3", "pic_file4",
            "pic_file5", "pic_file6", "pic_file7", "pic_file8", "pic_file9"
    };

    private static API getAPI() {
        return RetrofitManager.getInstance().create(API.class);
    }

    //获取验证码
    public static Disposable getCode(Context context, String phone, CallBack<GetCodeResponse> callBack) {
        GetCodeRequest request = new GetCodeRequest();
        request.setPhone(phone);
        return getAPI().getCode(gson.toJson(request))
                .compose(RxSchedulers.<GetCodeResponse>mainThread(context))
                .subscribeWith(new BaseSubscriber<>(callBack));
    }

    //获取验证码-修改密码
    public static Disposable genCodePswd(Context context, String phone, CallBack<GetCodeResponse> callBack) {
        GetCodeRequest request = new GetCodeRequest();
        request.setPhone(phone);
        return getAPI().genCodePswd(gson.toJson(request))
                .compose(RxSchedulers.<GetCodeResponse>mainThread(context))
                .subscribeWith(new BaseSubscriber<>(callBack));
    }

    //登陆
    public static Disposable login(Context context, String phone, String password, CallBack<LoginResponse> callBack) {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setPhone(phone);
        loginRequest.setPassword(password);
        loginRequest.setDevice_type("0");
        return getAPI().login(gson.toJson(loginRequest))
                .compose(RxSchedulers.<LoginResponse>mainThread(context))
                .subscribeWith(new BaseSubscriber<>(callBack));
    }

    //注册
    public static Disposable register(Context context, String phone, String password, String code, CallBack<LoginResponse> callBack) {
        RegisterRequest request = new RegisterRequest();
        request.setPhone(phone);
        request.setPassword(password);
        request.setCode(code);
        return getAPI().register(gson.toJson(request))
                .compose(RxSchedulers.<LoginResponse>mainThread(context))
                .subscribeWith(new BaseSubscriber<>(callBack));
    }

    //修改密码-更改密码
    public static Disposable changePwd(Context context, String phone, String password, String code, CallBack<Result> callBack) {
        RegisterRequest request = new RegisterRequest();
        request.setPhone(phone);
        request.setPassword(password);
        request.setCode(code);
        return getAPI().changePwd(gson.toJson(request))
                .compose(RxSchedulers.<Result>mainThread(context))
                .subscribeWith(new BaseSubscriber<>(callBack));
    }

    //推出登陆
    public static Disposable logout(Context context, CallBack<Result> callBack) {
        return getAPI().logout()
                .compose(RxSchedulers.<Result>mainThread(context))
                .subscribeWith(new BaseSubscriber<>(callBack));
    }

    // 查询瓜皮，包括红包，图片，视频
    public static Disposable queryGp(Context context, double lat, double lng, String dist, String shownType, CallBack<GPResponse> callBack) {
        QueryGpRequest request = new QueryGpRequest();
        request.setDist(dist);
        request.setLat(lat);
        request.setLng(lng);
        request.setIfFocus(TextUtils.equals(shownType, TYPE_SHOW_FRIEND) ? "Y" : "N");
        return getAPI().queryGp(gson.toJson(request))
                .compose(RxSchedulers.mainThread(context))
                .subscribeWith(new BaseSubscriber<>(callBack));
    }

    // 查询单个瓜皮，包括红包，图片，视频
    public static Disposable queryGpById(Context context, String gpid, CallBack<GpSingleRespone> callBack) {
        QueryGpSingleRequest request = new QueryGpSingleRequest();
        request.setGpId(gpid);
        return getAPI().queryGpById(gson.toJson(request))
                .compose(RxSchedulers.mainThread(context))
                .subscribeWith(new BaseSubscriber<>(callBack));
    }

    //获取对应组别的好友
    public static Disposable getFriends(Context context, int type, CallBack<GetFriendsResponse> callBack) {
        GetFriendsRequest getFriendsRequest = new GetFriendsRequest();
        getFriendsRequest.setType(type);
        return getAPI().getFriends(gson.toJson(getFriendsRequest))
                .compose(RxSchedulers.<GetFriendsResponse>mainThread(context))
                .subscribeWith(new BaseSubscriber<>(callBack));
    }

    //获取对应组别的好友
    public static Disposable getCount(Context context, String user_id, CallBack<UserGetCountResponse> callBack) {
        UserGetCountRequest userGetCountRequest = new UserGetCountRequest();
        userGetCountRequest.setUser_id(user_id);
        return getAPI().getCount(gson.toJson(userGetCountRequest))
                .compose(RxSchedulers.<UserGetCountResponse>mainThread(context))
                .subscribeWith(new BaseSubscriber<>(callBack));
    }

    //更新个人资料
    public static Disposable update(Context context,
                                    String nickname,
                                    int sex,//0 男，1 女
                                    String province,
                                    String city,
                                    String note,//签名
                                    int age,
                                    String lableInfor,//标签
                                    String birthday,
                                    CallBack<Result> callBack,
                                    List<Integer> list,
                                    File... files) {
        MultipartBody.Part[] parts = new MultipartBody.Part[files.length];
        for (int i = 0; i < list.size(); i++) {
            RequestBody file1Body = RequestBody.create(MediaType.parse("multipart/form-data"), files[list.get(i)]);
            parts[i] = MultipartBody.Part.createFormData(KEY_PIC[list.get(i)], files[list.get(i)].getName(), file1Body);
        }
        UpdateUserRequest updateUserRequest = new UpdateUserRequest();
        updateUserRequest.setNickname(nickname);
        updateUserRequest.setSex(sex);
        updateUserRequest.setProvince(province);
        updateUserRequest.setCity(city);
        updateUserRequest.setNote(note);
        updateUserRequest.setLableInfor(lableInfor);
        updateUserRequest.setAge(age);
        updateUserRequest.setBirthday(birthday);
        RequestBody dataBody = RequestBody.create(MediaType.parse("multipart/form-data"), gson.toJson(updateUserRequest));
        return getAPI().update(parts, dataBody)
                .compose(RxSchedulers.<Result>mainThread(context))
                .subscribeWith(new BaseSubscriber<>(callBack));
    }

    //用户藏瓜皮，红包
    public static Disposable createHB(Context context,
                                      String type,
                                      String money,
                                      String count,
                                      String showType,
                                      String message,
                                      double lat, double lng,
                                      String line,
                                      CallBack<Result> callBack,
                                      File pointFile) {

        RequestBody pointBody = RequestBody.create(MediaType.parse("multipart/form-data"), pointFile);
        MultipartBody.Part pointPart = MultipartBody.Part.createFormData("key_file", pointFile.getName(), pointBody);


        RequestBody lineBody = RequestBody.create(MediaType.parse("multipart/form-data"), line);

        MoneyRequest moneyRequest = new MoneyRequest();
        moneyRequest.setType(type);
        moneyRequest.setMoney(money);
        moneyRequest.setCount(count);
        moneyRequest.setShowType(showType);
        moneyRequest.setMessage(message);
        moneyRequest.setLine(line);
        moneyRequest.setLat(lat);
        moneyRequest.setLng(lng);
        RequestBody dataBody = RequestBody.create(MediaType.parse("multipart/form-data"), gson.toJson(moneyRequest));

        return getAPI().createHB(pointPart, lineBody, dataBody)
                .compose(RxSchedulers.<Result>mainThread(context))
                .subscribeWith(new BaseSubscriber<>(callBack));
    }

    //用户藏瓜皮图片
    public static Disposable createPic(Context context,
                                       String type,
                                       String message,
                                       double lat, double lng,
                                       String line,
                                       CallBack<Result> callBack,
                                       File pointFile,
                                       File... files) {

        RequestBody pointBody = RequestBody.create(MediaType.parse("multipart/form-data"), pointFile);
        MultipartBody.Part pointPart = MultipartBody.Part.createFormData("key_file", pointFile.getName(), pointBody);


        MultipartBody.Part[] parts = new MultipartBody.Part[files.length];
        for (int i = 0; i < files.length; i++) {
            RequestBody file1Body = RequestBody.create(MediaType.parse("multipart/form-data"), files[i]);
            parts[i] = MultipartBody.Part.createFormData(KEY_PIC[i], files[i].getName(), file1Body);
        }
//        Map<String, RequestBody> requestBodyMap = new HashMap<>();
//        if (files != null) {
//            for (int i = 0; i < files.length; i++) {
//                File file = files[i];
//                RequestBody file1Body = RequestBody.create(MediaType.parse("multipart/form-data"), file);
//                requestBodyMap.put(KEY_PIC[i], file1Body);
//            }
//        }

        RequestBody lineBody = RequestBody.create(MediaType.parse("multipart/form-data"), line);

        MoneyRequest moneyRequest = new MoneyRequest();
        moneyRequest.setType(type);
        moneyRequest.setMessage(message);
        moneyRequest.setLat(lat);
        moneyRequest.setLng(lng);
        moneyRequest.setLine(line);
        RequestBody dataBody = RequestBody.create(MediaType.parse("multipart/form-data"), gson.toJson(moneyRequest));

        return getAPI().createGp(pointPart, parts, lineBody, dataBody)
                .compose(RxSchedulers.<Result>mainThread(context))
                .subscribeWith(new BaseSubscriber<>(callBack));
    }

    //用户藏瓜皮图片
    public static Disposable createVideo(Context context,
                                         String type,
                                         String message,
                                         double lat, double lng,
                                         String line,
                                         CallBack<Result> callBack, File videFile,
                                         File pointFile, File videoPicFile) {

        RequestBody pointBody = RequestBody.create(MediaType.parse("multipart/form-data"), pointFile);
        MultipartBody.Part pointPart = MultipartBody.Part.createFormData("key_file", pointFile.getName(), pointBody);

        RequestBody videoBody = RequestBody.create(MediaType.parse("multipart/form-data"), videFile);
        MultipartBody.Part videoPart = MultipartBody.Part.createFormData("video", videFile.getName(), videoBody);

        RequestBody videoPicBody = RequestBody.create(MediaType.parse("multipart/form-data"), videoPicFile);
        MultipartBody.Part videoPicPart = MultipartBody.Part.createFormData("video_pic", videoPicFile.getName(), videoPicBody);

        RequestBody lineBody = RequestBody.create(MediaType.parse("multipart/form-data"), line);

        MoneyRequest moneyRequest = new MoneyRequest();
        moneyRequest.setType(type);
        moneyRequest.setMessage(message);
        moneyRequest.setLat(lat);
        moneyRequest.setLng(lng);
        moneyRequest.setLine(line);
        RequestBody dataBody = RequestBody.create(MediaType.parse("multipart/form-data"), gson.toJson(moneyRequest));

        return getAPI().createVideo(pointPart, videoPart, videoPicPart, lineBody, dataBody)
                .compose(RxSchedulers.<Result>mainThread(context))
                .subscribeWith(new BaseSubscriber<>(callBack));
    }

    //获取对应组别的好友
    public static Disposable doGP(Context context, String id, String type, String desc, CallBack<DoGPResponse> callBack) {
        DoGPRequest doGPRequest = new DoGPRequest();
        doGPRequest.id = id;
        doGPRequest.type = type;
        if (!TextUtils.isEmpty(desc)) {
            doGPRequest.desc = desc;
        }
        return getAPI().doGp(gson.toJson(doGPRequest))
                .compose(RxSchedulers.<DoGPResponse>mainThread(context))
                .subscribeWith(new BaseSubscriber<>(callBack));
    }

    //通过环信id，获取好友信息
    public static Disposable queryUserInforByHx(Context context, String hx_id, CallBack<QueryUserInfoByHxResponse> callBack) {
        QueryUserInfoBuHxRequest queryUserInfoBuHxRequest = new QueryUserInfoBuHxRequest();
        queryUserInfoBuHxRequest.setHx_id(hx_id);
        return getAPI().queryUserInforByHx(gson.toJson(queryUserInfoBuHxRequest))
                .compose(RxSchedulers.<QueryUserInfoByHxResponse>mainThread(context))
                .subscribeWith(new BaseSubscriber<>(callBack));
    }

    //关注
    public static Disposable add(Context context, String destUids, CallBack<Result> callBack) {
        FoloowRequest foloowRequest = new FoloowRequest();
        foloowRequest.setDestUids(destUids);
        return getAPI().add(gson.toJson(foloowRequest))
                .compose(RxSchedulers.<Result>mainThread(context))
                .subscribeWith(new BaseSubscriber<>(callBack));
    }

    //取消关注
    public static Disposable removeFocus(Context context, String destUids, CallBack<Result> callBack) {
        FoloowRequest foloowRequest = new FoloowRequest();
        foloowRequest.setDestUids(destUids);
        return getAPI().removeFocus(gson.toJson(foloowRequest))
                .compose(RxSchedulers.<Result>mainThread(context))
                .subscribeWith(new BaseSubscriber<>(callBack));
    }

    //完善手机号
    public static Disposable registerPhone(Context context, String code, String phone, CallBack<Result> callBack) {
        BindPhoneRequest bindPhoneRequest = new BindPhoneRequest();
        bindPhoneRequest.setCode(code);
        bindPhoneRequest.setPhone(phone);
        return getAPI().registerPhone(gson.toJson(bindPhoneRequest))
                .compose(RxSchedulers.<Result>mainThread(context))
                .subscribeWith(new BaseSubscriber<>(callBack));
    }

    //刷新最新一条消息
    public static Disposable refreshOne(Context context, String ms_type, CallBack<RefreshOneMessageResponse> callBack) {
        RefreshOneMessageRequest refreshOneMessageRequest = new RefreshOneMessageRequest();
        refreshOneMessageRequest.setMs_type(ms_type);
        return getAPI().refreshOne(gson.toJson(refreshOneMessageRequest))
                .compose(RxSchedulers.<RefreshOneMessageResponse>mainThread(context))
                .subscribeWith(new BaseSubscriber<>(callBack));
    }

    //刷新最新一条消息
    public static Disposable refresh(Context context, String ms_type, CallBack<RefreshOneMessageResponse> callBack) {
        RefreshOneMessageRequest refreshOneMessageRequest = new RefreshOneMessageRequest();
        refreshOneMessageRequest.setMs_type(ms_type);
        return getAPI().refresh(gson.toJson(refreshOneMessageRequest))
                .compose(RxSchedulers.<RefreshOneMessageResponse>mainThread(context))
                .subscribeWith(new BaseSubscriber<>(callBack));
    }

    //发送瓜皮邀请短信
    public static Disposable sendInvite(Context context, String phone, String name, CallBack<Result> callBack) {
        SendInviteRequest sendInviteRequest = new SendInviteRequest();
        sendInviteRequest.setPhone(phone);
        sendInviteRequest.setName(name);
        return getAPI().sendInvite(gson.toJson(sendInviteRequest))
                .compose(RxSchedulers.<Result>mainThread(context))
                .subscribeWith(new BaseSubscriber<>(callBack));
    }

    //查询瓜皮
    public static Disposable queryFocusGp(Context context, String type, String user_id, CallBack<QueryFocusGpResponse> callBack) {
        QueryFocusGpRequest queryFocusGpRequest = new QueryFocusGpRequest();
        queryFocusGpRequest.setType(type);
        queryFocusGpRequest.setUser_id(user_id);
        return getAPI().queryFocusGp(gson.toJson(queryFocusGpRequest))
                .compose(RxSchedulers.<QueryFocusGpResponse>mainThread(context))
                .subscribeWith(new BaseSubscriber<>(callBack));
    }

}
