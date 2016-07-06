package com.heartblood.heartgit.common.HttpTools;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.CustomMessageConfig;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Date;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.entity.StringEntity;

/**
 * Created by Tangerine on 16/7/5.
 */
public class UserID {

    private static  final String APP_KEY = "3b120569699fff06d95365705aad8262";
    private static  final String APP_SECRET = "77d4056a53ee";


    /**
     *
     * @return Nonce
     */
    private static String getNonce(){
        String nonce = new String();
        int a[] = new int[10];
        for(int i=0;i<a.length;i++ ) {
            a[i] = (int)(10*(Math.random()));
            nonce +=a;
        }
        return nonce;
    }

    /**
     *
     * @return curTime
     */
    private static String getCurTime(){
        return String.valueOf((new Date()).getTime() / 1000L);
    }

    /**
     *
     * @param context
     * @param accid
     * @param name
     * @param token
     */
    public static void CreateID( Context context, String accid, String name, String token){
        String nonce = getNonce();
        String curTime = getCurTime();
        String checkSum = CheckSumBuilder.getCheckSum(APP_SECRET, nonce ,curTime);
        String e = "accid="+accid+"&"+"name="+name+"&"+"token="+token;
        StringEntity entity =null;
        try {
            entity = new StringEntity(e);
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }


        HttpClient.clearHeader();
        HttpClient.addOneHeader("AppKey", APP_KEY);
        HttpClient.addOneHeader("Nonce",nonce);
        HttpClient.addOneHeader("CurTime",curTime);
        HttpClient.addOneHeader("CheckSum",checkSum);
        HttpClient.post(context,"https://api.netease.im/nimserver/user/create.action", entity, "application/x-www-form-urlencoded",new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, org.json.JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    Log.e("123",response.getJSONObject("info").getString("accid"));
                } catch (JSONException e) {

                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }

    /**
     *
     * @param context
     * @param accid
     */
    public static void refreshID(Context context,String accid){
        String nonce = getNonce();
        String curTime = getCurTime();
        String checkSum = CheckSumBuilder.getCheckSum(APP_SECRET,nonce,curTime);
        String e = "accid="+accid;
        StringEntity entity = null;
        try {
            entity = new StringEntity(e);
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        HttpClient.clearHeader();
        HttpClient.addOneHeader("AppKey",APP_KEY);
        HttpClient.addOneHeader("Nonce",nonce);
        HttpClient.addOneHeader("CurTime",curTime);
        HttpClient.addOneHeader("CheckSum",checkSum);
        HttpClient.post(context,"https://api.netease.im/nimserver/user/update.action",entity,"application/x-www-form-urlencoded",new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.e("123",Integer.toString(statusCode));
            }

        });
    }

    /**
     *
     * @param context
     * @param accid
     * @return
     */

    public static String getRefreshToken(Context context,String accid){
        final String token = null;
        String nonce = getNonce();
        String curTime = getCurTime();
        String checkSum = CheckSumBuilder.getCheckSum(APP_SECRET,nonce,curTime);
        String e = "accid="+accid;
        StringEntity entity = null;
        try {
            entity = new StringEntity(e);
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        HttpClient.clearHeader();
        HttpClient.addOneHeader("AppKey",APP_KEY);
        HttpClient.addOneHeader("Nonce",nonce);
        HttpClient.addOneHeader("CurTime",curTime);
        HttpClient.addOneHeader("CheckSum",checkSum);
        HttpClient.post(context,"https://api.netease.im/nimserver/user/refreshToken.action",entity,"application/x-www-form-urlencoded",new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                String t = null;
                try {
                    t = response.getJSONObject("info").getString("token");
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
                t = token;
            }
        });
        return token;
    }

    /**
     *
     * @param context
     * @param accid
     */
    public static void blockID(Context context,String accid){
        String nonce = getNonce();
        String curTime = getCurTime();
        String checkSum = CheckSumBuilder.getCheckSum(APP_SECRET,nonce,curTime);
        String e = "accid="+accid;
        StringEntity entity = null;
        try {
            entity = new StringEntity(e);
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        HttpClient.clearHeader();
        HttpClient.addOneHeader("AppKey",APP_KEY);
        HttpClient.addOneHeader("Nonce",nonce);
        HttpClient.addOneHeader("CurTime",curTime);
        HttpClient.addOneHeader("CheckSum",checkSum);
        HttpClient.post(context,"https://api.netease.im/nimserver/user/block.action",entity,"application/x-www-form-urlencoded",new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.e("123",Integer.toString(statusCode));
            }
        });
    }

    /**
     *
     * @param context
     * @param accid
     * @param name
     */
    public static void refreshIDName(Context context ,String accid ,String name ){
        String nonce = getNonce();
        String curTime = getCurTime();
        String checkSum = CheckSumBuilder.getCheckSum(APP_SECRET,nonce,curTime);
        String e = "accid="+accid+"&"+"name="+name;
        StringEntity entity = null;
        try {
            entity = new StringEntity(e);
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }

        HttpClient.clearHeader();
        HttpClient.addOneHeader("AppKey",APP_KEY);
        HttpClient.addOneHeader("Nonce",nonce);
        HttpClient.addOneHeader("CurTime",curTime);
        HttpClient.addOneHeader("CheckSum",checkSum);
        HttpClient.post(context,"https://api.netease.im/nimserver/user/block.action",entity,"application/x-www-form-urlencoded",new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.e("123","昵称更新操作状态码:"+Integer.toString(statusCode));
            }
        });

    }

    /**
     *
     * @param context
     * @param accid
     * @param icon
     */
    public static void refreshIDIcon(Context context ,String accid ,String icon ){
        String nonce = getNonce();
        String curTime = getCurTime();
        String checkSum = CheckSumBuilder.getCheckSum(APP_SECRET,nonce,curTime);
        String e = "accid="+accid+"&"+"icon="+icon;
        StringEntity entity = null;
        try {
            entity = new StringEntity(e);
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }

        HttpClient.clearHeader();
        HttpClient.addOneHeader("AppKey",APP_KEY);
        HttpClient.addOneHeader("Nonce",nonce);
        HttpClient.addOneHeader("CurTime",curTime);
        HttpClient.addOneHeader("CheckSum",checkSum);
        HttpClient.post(context,"https://api.netease.im/nimserver/user/block.action",entity,"application/x-www-form-urlencoded",new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.e("123","头像更新操作状态码:"+Integer.toString(statusCode));
            }
        });

    }

    /**
     *
     * @param context
     * @param accid
     * @param sign
     */
    public static void refreshIDSign(Context context ,String accid ,String sign ){
        String nonce = getNonce();
        String curTime = getCurTime();
        String checkSum = CheckSumBuilder.getCheckSum(APP_SECRET,nonce,curTime);
        String e = "accid="+accid+"&"+"sign="+sign;
        StringEntity entity = null;
        try {
            entity = new StringEntity(e);
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }

        HttpClient.clearHeader();
        HttpClient.addOneHeader("AppKey",APP_KEY);
        HttpClient.addOneHeader("Nonce",nonce);
        HttpClient.addOneHeader("CurTime",curTime);
        HttpClient.addOneHeader("CheckSum",checkSum);
        HttpClient.post(context,"https://api.netease.im/nimserver/user/block.action",entity,"application/x-www-form-urlencoded",new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.e("123","签名更新操作状态码:"+Integer.toString(statusCode));
            }
        });

    }

    /**
     *
     * @param context
     * @param accid
     * @param email
     */
    public static void refreshIDEmail(Context context ,String accid ,String email ){
        String nonce = getNonce();
        String curTime = getCurTime();
        String checkSum = CheckSumBuilder.getCheckSum(APP_SECRET,nonce,curTime);
        String e = "accid="+accid+"&"+"email="+email;
        StringEntity entity = null;
        try {
            entity = new StringEntity(e);
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }

        HttpClient.clearHeader();
        HttpClient.addOneHeader("AppKey",APP_KEY);
        HttpClient.addOneHeader("Nonce",nonce);
        HttpClient.addOneHeader("CurTime",curTime);
        HttpClient.addOneHeader("CheckSum",checkSum);
        HttpClient.post(context,"https://api.netease.im/nimserver/user/block.action",entity,"application/x-www-form-urlencoded",new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.e("123","邮箱更新操作状态码:"+Integer.toString(statusCode));
            }
        });

    }

    /**
     *
     * @param context
     * @param accid
     * @param birth
     */
    public static void refreshIDBirth(Context context ,String accid ,String birth ){
        String nonce = getNonce();
        String curTime = getCurTime();
        String checkSum = CheckSumBuilder.getCheckSum(APP_SECRET,nonce,curTime);
        String e = "accid="+accid+"&"+"birth="+birth;
        StringEntity entity = null;
        try {
            entity = new StringEntity(e);
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }

        HttpClient.clearHeader();
        HttpClient.addOneHeader("AppKey",APP_KEY);
        HttpClient.addOneHeader("Nonce",nonce);
        HttpClient.addOneHeader("CurTime",curTime);
        HttpClient.addOneHeader("CheckSum",checkSum);
        HttpClient.post(context,"https://api.netease.im/nimserver/user/block.action",entity,"application/x-www-form-urlencoded",new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.e("123","生日更新操作状态码:"+Integer.toString(statusCode));
            }
        });

    }

    /**
     *
     * @param context
     * @param accid
     * @param mobile
     */
    public static void refreshIDMobile(Context context ,String accid ,String mobile ){
        String nonce = getNonce();
        String curTime = getCurTime();
        String checkSum = CheckSumBuilder.getCheckSum(APP_SECRET,nonce,curTime);
        String e = "accid="+accid+"&"+"mobile="+mobile;
        StringEntity entity = null;
        try {
            entity = new StringEntity(e);
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }

        HttpClient.clearHeader();
        HttpClient.addOneHeader("AppKey",APP_KEY);
        HttpClient.addOneHeader("Nonce",nonce);
        HttpClient.addOneHeader("CurTime",curTime);
        HttpClient.addOneHeader("CheckSum",checkSum);
        HttpClient.post(context,"https://api.netease.im/nimserver/user/block.action",entity,"application/x-www-form-urlencoded",new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.e("123","手机号更新操作状态码:"+Integer.toString(statusCode));
            }
        });

    }


    /**
     *
     * @param context
     * @param accid
     * @param gender 0表示不知，1表示男，2表示女，其他参数会报错
     */
    public static void refreshIDGender(Context context ,String accid ,String gender ){
        String nonce = getNonce();
        String curTime = getCurTime();
        String checkSum = CheckSumBuilder.getCheckSum(APP_SECRET,nonce,curTime);
        String e = "accid="+accid+"&"+"gender="+gender;
        StringEntity entity = null;
        try {
            entity = new StringEntity(e);
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }

        HttpClient.clearHeader();
        HttpClient.addOneHeader("AppKey",APP_KEY);
        HttpClient.addOneHeader("Nonce",nonce);
        HttpClient.addOneHeader("CurTime",curTime);
        HttpClient.addOneHeader("CheckSum",checkSum);
        HttpClient.post(context,"https://api.netease.im/nimserver/user/block.action",entity,"application/x-www-form-urlencoded",new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.e("123","性别更新操作状态码:"+Integer.toString(statusCode));
            }
        });

    }



    /**
     *
     * @param sessionId String,CustomId,接收方帐户ID
     * @param sessionType SessionTypeEnum,sessionType,单聊或群聊类型
     * @param content String,content,要发送的信息文本
     */
    public static void sendTextMessage(String sessionId, SessionTypeEnum sessionType, String content) {
        IMMessage message = MessageBuilder.createTextMessage(
                sessionId,
                sessionType,
                content
        );
        NIMClient.getService(MsgService.class).sendMessage(message,false);
    }


    /**
     *
     * @param sessionId String,CustomId 接收方帐户ID
     * @param sessionType SessionTypeEnum,sessionType 单聊或群聊类型
     * @param imageFile File,imageFile 要发送的图片文件
     */

    public void sendImageMessage(String sessionId, SessionTypeEnum sessionType, File imageFile) {

        IMMessage message = MessageBuilder.createImageMessage(
                sessionId,
                sessionType,
                imageFile,
                null);
        NIMClient.getService(MsgService.class).sendMessage(message,false);
    }

    /**
     *
     * @param sessionId  String,CustomId 接收方帐户ID
     * @param sessionType SessionTypeEnum,sessionType 单聊或群聊类型
     */
    public void sendTipMessage(String sessionId,SessionTypeEnum sessionType){
        IMMessage message = MessageBuilder.createTipMessage(
                sessionId,
                sessionType);
        NIMClient.getService(MsgService.class).sendMessage(message,false);

        CustomMessageConfig config = new CustomMessageConfig();
        config.enableUnreadCount = false;
    }



    /**
     *
     * @return UnreadNum,int,未读信息数目
     */
    public int getUnreadNum(){
        return  NIMClient.getService(MsgService.class).getTotalUnreadCount();
    }




    /**
     *
     * @param accid
     * @param token
     * @param loginListener
     */
    public static void LogIn(String accid, String token, final LoginListener loginListener){
        LoginInfo loginInfo = new LoginInfo(accid,token);
        RequestCallback<LoginInfo> callback = new RequestCallback<LoginInfo>() {

            @Override
            public void onSuccess(LoginInfo loginInfo) {
                loginListener.loginSuccess();

            }

            @Override
            public void onFailed(int i) {
                loginListener.loginFailed();
            }

            @Override
            public void onException(Throwable throwable) {

            }
        };
        NIMClient.getService(AuthService.class).login(loginInfo).setCallback(callback);
    }





    interface LoginListener{

        void loginSuccess();
        void loginFailed();
    }
}
