package com.guapi.http;

import com.ewuapp.framework.common.http.Result;
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

import io.reactivex.Flowable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;


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

public interface API {
    //发送验证码
    @POST("api/auth/register/genCode.do")
    Flowable<GetCodeResponse> getCode(@Query("data") String request);

    //修改密码-发送手机验证码
    @POST("api/auth/register/genPwdCode.do")
    Flowable<GetCodeResponse> genCodePswd(@Query("data") String request);

    //登陆
    @POST("api/auth/login.do")
    Flowable<LoginResponse> login(@Query("data") String request);

    //注册
    @POST("api/auth/register.do")
    Flowable<LoginResponse> register(@Query("data") String request);

    //修改密码-更改密码
    @POST("api/auth/pwd/change.do")
    Flowable<Result> changePwd(@Query("data") String request);

    //推出登陆
    @POST("api/auth/logout.do")
    Flowable<Result> logout();


    //用户藏红包
    @Multipart
    @POST("api/gp/createGp.do")
    Flowable<Result> createHB(
            @Part() MultipartBody.Part key_file,
            @Part("line") RequestBody line,
            @Part("data") RequestBody data
    );

    //用户藏瓜皮图片
    @Multipart
    @POST("api/gp/createGp.do")
    Flowable<Result> createGp(
            @Part() MultipartBody.Part key_file,
            @Part() MultipartBody.Part[] picFile,
            @Part("line") RequestBody line,
            @Part("data") RequestBody data
    );

    //用户藏瓜皮图片
    @Multipart
    @POST("api/gp/createGp.do")
    Flowable<Result> createVideo(
            @Part() MultipartBody.Part key_file,
            @Part() MultipartBody.Part video,
            @Part() MultipartBody.Part video_pic,
            @Part("line") RequestBody line,
            @Part("data") RequestBody data
    );

    //完善个人资料
    @Multipart
    @POST("api/profile/update.do")
    Flowable<Result> update(
            @Part() MultipartBody.Part[] picFile,
            @Part("data") RequestBody data
    );

    //查询瓜皮，包括红包，图片，视频
    @POST("api/gp/queryGp.do")
    Flowable<GPResponse> queryGp(@Query("data") String request);

    //查询单个瓜皮，包括红包，图片，视频
    @POST("api/gp/queryGpById.do")
    Flowable<GpSingleRespone> queryGpById(@Query("data") String request);

    //操作瓜皮，包括抢红包，评论，点赞
    @POST("api/gp/doGp.do")
    Flowable<DoGPResponse> doGp(@Query("data") String request);

    //获取对应组别的好友
    @POST("api/friend/getFriends.do")
    Flowable<GetFriendsResponse> getFriends(@Query("data") String request);

    //5.首页及好友关系
    @POST("api/friend/getCount.do")
    Flowable<UserGetCountResponse> getCount(@Query("data") String request);

    //通过环信id查询用户信息
    @POST("api/friend/queryUserInforByHx.do")
    Flowable<QueryUserInfoByHxResponse> queryUserInforByHx(@Query("data") String request);

    //关注
    @POST("api/friend/add.do")
    Flowable<Result> add(@Query("data") String request);

    //取消关注
    @POST("api/friend/removeFocus.do")
    Flowable<Result> removeFocus(@Query("data") String request);

    //完善手机号
    @POST("api/auth/registerPhone.do")
    Flowable<Result> registerPhone(@Query("data") String request);

    //刷新最新一条消息
    @POST("api/sms/refreshOne.do")
    Flowable<RefreshOneMessageResponse> refreshOne(@Query("data") String request);

    //查询后台消息
    @POST("api/sms/refresh.do")
    Flowable<RefreshOneMessageResponse> refresh(@Query("data") String request);

    //查询后台消息
    @POST("api/friend/sendInvite.do")
    Flowable<Result> sendInvite(@Query("data") String request);

    //查询瓜皮
    @POST("api/gp/queryFocusGp.do")
    Flowable<QueryFocusGpResponse> queryFocusGp(@Query("data") String request);

}
