package com.zy.adproj.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.zy.adproj.banner.BannerModel;
import com.zy.adproj.http.HttpRequest;
import com.zy.adproj.http.HttpUrls;

/**
 * Description:绑定服务简单实例--服务端
 * Description: 与服务器交互的客户端
 * Description:Messenger服务端简单实例,服务端进程
 */
public class ServiceRequest extends Service {

    public static final int MSG_RECEIVE = 1;//接收信息
    public static final int MSG_INSTANCE = 2;//建立信息通信的实体
    public static final int MSG_REQUEST = 3;//进行后台请求
    public static final int MSG_VERFY = 4;//验证授权
    private final static String TAG = "service";
    // Binder given to clients
    //private final IBinder mBinder = new ServiceBinder();
    private Thread thread;
    private boolean quit;
    private boolean isRequest = false;
    private Messenger messengerClient = null;
    private JsonObject jsonObject = null;//请求列表参数

    /**
     * 创建Messenger并传入Handler实例对象
     */
    final Messenger mMessenger = new Messenger(new IncomingHandler());

    /**
     * 用于接收从客户端传递过来的数据
     */
    class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            Log.i(TAG, "IncomingHandler");
            switch (msg.what) {
                case MSG_RECEIVE:
                {
                    Log.i(TAG, "thanks,Service had receiver message from client!");
                    if(messengerClient == null){
                        //回复客户端信息,该对象由客户端传递过来
                        messengerClient = msg.replyTo;
                    }

                    //获取回复信息的消息实体
                    Message replyMsg = Message.obtain(null,ServiceRequest.MSG_RECEIVE);
                    Bundle bundle = new Bundle();
                    bundle.putString("reply","ok~,I had receiver message from you! ");
                    replyMsg.setData(bundle);

                    //向客户端发送消息
                    try {
                        messengerClient.send(replyMsg);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }

                    break;
                }
                case MSG_INSTANCE:
                {
                    if(messengerClient == null){
                        Log.i(TAG, "messengerClient 实体化");
                        //回复客户端信息,该对象由客户端传递过来
                        messengerClient = msg.replyTo;
                    }

                    //获取回复信息的消息实体
                    Message replyMsg = Message.obtain(null,ServiceRequest.MSG_INSTANCE);
                    Bundle bundle = new Bundle();
                    if(messengerClient != null){
                        bundle.putBoolean("success",true);
                        try {
                            replyMsg.setData(bundle);
                            messengerClient.send(replyMsg);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }

                    //向客户端发送消息


                    break;
                }
                case MSG_REQUEST:
                {
                    isRequest = true;
                    break;
                }
                default:
                {
                    super.handleMessage(msg);
                }
            }
        }
    }

    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     * 把Binder类返回给客户端
     */
    public class ServiceBinder extends Binder {
        public ServiceRequest getService() {
            // Return this instance of LocalService so clients can call public methods
            return ServiceRequest.this;
        }
    }

    private void requestData(){

        thread = null;

        thread = new Thread(new Runnable() {

            @Override
            public void run() {

                while (true) {
                    try {
                        if(messengerClient != null)
                        {
                            if(isRequest){

                                if(jsonObject == null){
                                    jsonObject = new JsonObject();
                                    jsonObject.addProperty("deviceId",BannerModel.UNIQUE_ID);
                                    jsonObject.addProperty("authCode",BannerModel.CodeAuth);
                                }

                                isRequest = false;
                                HttpRequest.get(HttpUrls.urlPalyerList,
                                        jsonObject,
                                        new HttpRequest.INetCallBack() {
                                            @Override
                                            public void onNetFailure() {
                                                //requestData();
                                                isRequest = true;
                                            }

                                            @Override
                                            public void onSuccess(JsonObject result) {
                                                isRequest = true;
                                                //获取回复信息的消息实体
                                                Message replyMsg = Message.obtain(null,ServiceRequest.MSG_RECEIVE);
                                                replyMsg.obj = result;
                                                //向客户端发送消息
                                                try {
                                                    messengerClient.send(replyMsg);
                                                    //requestData();
                                                } catch (RemoteException e) {
                                                    //requestData();
                                                    e.printStackTrace();
                                                }
                                            }

                                            @Override
                                            public void onError(JsonObject errorMessage) {
                                                //requestData();
                                                isRequest = true;
                                            }
                                        });

                                Thread.sleep(15000);
                            }
                            else
                            {
                                Thread.sleep(0);
                            }
                        }

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

               /* try {
                    if(messengerClient != null)
                    {
                        if(jsonObject == null){
                            jsonObject = new JsonObject();
                            jsonObject.addProperty("deviceId",BannerModel.UNIQUE_ID);
                            jsonObject.addProperty("authCode",BannerModel.CodeAuth);
                        }

                        //Log.i("isRequest","" + isRequest);

                        if(isRequest){
                            HttpRequest.get(HttpUrls.urlPalyerList,
                                    jsonObject,
                                    new HttpRequest.INetCallBack() {
                                        @Override
                                        public void onNetFailure() {
                                            requestData();
                                        }

                                        @Override
                                        public void onSuccess(JsonObject result) {
                                            //获取回复信息的消息实体
                                            Message replyMsg = Message.obtain(null,ServiceRequest.MSG_RECEIVE);
                                            replyMsg.obj = result;
                                            //向客户端发送消息
                                            try {
                                                messengerClient.send(replyMsg);
                                                requestData();
                                            } catch (RemoteException e) {
                                                requestData();
                                                e.printStackTrace();
                                            }
                                        }

                                        @Override
                                        public void onError(JsonObject errorMessage) {
                                            requestData();
                                        }
                                    });
                        }
                        else
                        {
                            requestData();
                        }
                    }
                    else
                    {
                        requestData();
                    }

                    //Thread.sleep(10000);
                }
                //catch (InterruptedException e)
                catch (Exception e)
                {
                    requestData();
                    e.printStackTrace();
                }*/


            }
        });

        thread.start();

//        thread = new Thread(new Runnable() {
//
//            @Override
//            public void run() {
//                // 每间隔一秒count加1 ，直到quit为true。
//                while (!quit) {
//                    try {
//                        if(messengerClient != null)
//                        {
//                            if(jsonObject == null){
//                                jsonObject = new JsonObject();
//                                jsonObject.addProperty("deviceId",BannerModel.UNIQUE_ID);
//                                jsonObject.addProperty("authCode",BannerModel.CodeAuth);
//                            }
//
//                            if(isRequest){
//                                HttpRequest.post(HttpUrls.urlPalyerList,
//                                        jsonObject,
//                                        new HttpRequest.INetCallBack() {
//                                            @Override
//                                            public void onNetFailure() {
//
//                                            }
//
//                                            @Override
//                                            public void onSuccess(JsonObject result) {
//                                                //获取回复信息的消息实体
//                                                Message replyMsg = Message.obtain(null,ServiceRequest.MSG_RECEIVE);
//                                                Bundle bundle = new Bundle();
//                                                replyMsg.setData(bundle);
//                                                //向客户端发送消息
//                                                try {
//                                                    messengerClient.send(replyMsg);
//                                                } catch (RemoteException e) {
//                                                    e.printStackTrace();
//                                                }
//                                            }
//
//                                            @Override
//                                            public void onError(JsonObject errorMessage) {
//
//                                            }
//                                        });
//                            }
//                        }
//
//                        //Thread.sleep(1000);
//                    }
//                    //catch (InterruptedException e)
//                    catch (Exception e)
//                    {
//                        e.printStackTrace();
//                    }
//
//                }
//            }
//        });
//
//        thread.start();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        requestData();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
//        return mBinder;
    }

    /**
     * 解除绑定时调用
     * @return
     */
    @Override
    public boolean onUnbind(Intent intent) {
        Log.i(TAG, "Service is invoke onUnbind");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "Service is invoke Destroyed");
        this.quit = true;
        super.onDestroy();
    }

}
