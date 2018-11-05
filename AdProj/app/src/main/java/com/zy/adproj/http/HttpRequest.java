package com.zy.adproj.http;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class HttpRequest {

    /**
     * Http网络请求，请求后台接口
     * @param type String,//请求类型，GET/POST
     * @param url String,//请求接口地址
     * @param jsonObject JsonObject,//请求参数
     * @param callback INetCallBack,//回调函数
     * **/
    public static void request(String type, String url,JsonObject jsonObject, final INetCallBack callback){

        RequestBody body = null;
        if(type == "POST"){
            if(jsonObject == null){
                jsonObject =  new JsonObject();
            }
            Gson mGson = new Gson();
            String params = mGson.toJson(jsonObject);
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");//提交json
//            MediaType JSON =  MediaType.parse("text/x-markdown; charset=utf-8");//提交字符串
            body = RequestBody.create(JSON, params);
        }
        else
        {
            Set<Map.Entry<String, JsonElement>> entrySet =  jsonObject.entrySet();
            int c = 0;
            for(Map.Entry<String, JsonElement> typeMap : entrySet){
                String str = jsonObject.get(typeMap.getKey()).getAsString();
                if(c == 0){
                    url += "?";
                }
                else
                {
                    url += "&";
                }

                c++;
                url += typeMap.getKey() + "=" + str;
            }
        }

        final JsonObject requestJson = jsonObject;
        final String url2 = url;

        //step 1: 创建一个OkHttpClick对象
        OkHttpClient okHttpClient  = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10,TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .build();

        //step 2: 创建  FormBody.Builder
      /*  FormBody formBody = new FormBody.Builder()
                .add("name", "dsd")
                .build();*/

        //step 3: 创建请求
       /* Request request = new Request.Builder().url(url)
                .post(formBody)
                .build();*/
        Request.Builder requestBuilder = new Request.Builder().url(url);
        requestBuilder.addHeader("Content-Type","application/json");
        requestBuilder.addHeader("Accept","application/json");
        //requestBuilder.addHeader("deviceType","2");
        //可以省略，默认是GET请求
        requestBuilder.method(type, body);

        // step 3：创建 Call 对象
        Call call = okHttpClient.newCall(requestBuilder.build());

        //step 4: 开始异步请求 建立联系
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // TODO: 17-1-4  请求失败
                callback.onNetFailure();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // TODO: 17-1-4 请求成功

                //打印服务端返回结果
                //获得返回体
                ResponseBody body = response.body();
                String strObj = body.string();// 返回的是string 类型

                //判断请求是否成功
                if(response.isSuccessful())
                {

                    JsonObject returnData = new JsonParser().parse(strObj).getAsJsonObject();

                    Log.i("url",url2);
                    Log.i("requestJson",requestJson.toString());
                    Log.i("result",returnData.toString());
//                    Log.i("retData",returnData.getAsJsonObject("retData").toString());
//                    Log.i("TAG",strObj);

                    if(returnData.get("success").getAsBoolean()){
//                    if(true){
                        callback.onSuccess(returnData);
                    }
                    else
                    {
                        callback.onError(null);
                    }

                }
                else
                {
                    callback.onError(null);
                }
            }
        });
    }

    /**
     * Http网络请求，请求后台接口 GET请求
     * @param url String,//请求接口地址
     * @param callback Callback,//回调函数
     * **/
    public static void get(String url,INetCallBack callback) {
       request("GET",url,null,callback);
    }

    /**
     * Http网络请求，请求后台接口 GET请求
     * @param url String,//请求接口地址
     * @param jsonObject JsonObject,//请求参数
     * @param callback INetCallBack,//回调函数
     * **/
//    public static void get(String url,Map<Object, Object> map,INetCallBack callback) {
    public static void get(String url,JsonObject jsonObject,INetCallBack callback) {
        request("GET",url,jsonObject,callback);
    }

    /**
     * Http网络请求，请求后台接口 POST请求
     * @param url String,//请求接口地址
     * @param callback INetCallBack,//回调函数
     * **/
    public static void post(String url,INetCallBack callback) {
        request("POST",url,null,callback);
    }

    /**
     * Http网络请求，请求后台接口 POST请求
     * @param url String,//请求接口地址
     * @param jsonObject JsonObject,//请求参数
     * @param callback INetCallBack,//回调函数
     * **/
    public static void post(String url,JsonObject jsonObject,INetCallBack callback) {
        request("POST",url,jsonObject,callback);
    }

    /**
     * 回调接口
     */
    public static interface INetCallBack {

        //网络请求失败，没连网
        void onNetFailure();

        //网络请求成功
        void onSuccess(JsonObject result);

        //网络请求成功，后台服务器有误
        void onError(JsonObject errorMessage);

    }

}
