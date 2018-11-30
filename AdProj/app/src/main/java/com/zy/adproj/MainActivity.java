package com.zy.adproj;

import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.provider.Settings;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.Toast;
import android.widget.VideoView;

import com.danikula.videocache.HttpProxyCacheServer;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.zy.adproj.banner.Banner;
import com.zy.adproj.banner.BannerModel;
import com.zy.adproj.banner.BannerMul;
import com.zy.adproj.banner.BannerPage;
import com.zy.adproj.banner.BannerViewAdapter;
import com.zy.adproj.dialog.Alert;
import com.zy.adproj.http.HttpRequest;
import com.zy.adproj.http.HttpUrls;
import com.zy.adproj.preferences.PreferenceSet;
import com.zy.adproj.service.ServiceRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static MainActivity base;

    private BannerMul banner;
    private List<String> list;
    private PreferenceSet preferenceSet;
    private static boolean b = true;

//    private ServiceRequest mService;
    /**
     * 与服务端交互的Messenger
     */
    static Messenger mService = null;
    private boolean mBound = false;

    /**
     * 用于接收服务器返回的信息
     */
    private Messenger mRecevierReplyMsg = new Messenger(new ReceiverReplyMsgHandler());

    private static class ReceiverReplyMsgHandler extends Handler {
        private static final String TAG = "zy client";

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                //接收服务端回复
                case ServiceRequest.MSG_RECEIVE:
                {
                    //Log.i(TAG, "receiver message from service:");
                    JsonObject jsonObject = (JsonObject)msg.obj;

                    //if(BannerModel.isChange != jsonObject.get("desc").getAsString() && b){//java中的==是用来判断对象所使用的内存地址是不是同一个，进而判断是不是同一个对象。
                    if(!jsonObject.get("desc").getAsString().equals(BannerModel.isChange) && b){//判断两个String对象是不是相同，而不是去判断两个String对象是不是同一个对象
                        b = true;
                        BannerModel.isChange = jsonObject.get("desc").getAsString();
                        base.initView(jsonObject.getAsJsonObject("data"));
                    }

                    break;
                }
                case ServiceRequest.MSG_INSTANCE:
                {
                    //Log.i(TAG, "receiver message from service:");
                    /*if(msg.getData().getBoolean("sucess")){
                        base.verfyAuthCode();
                    }*/
//                    base.verfyAuthCode();
                    try {
                        Message replyMsg = Message
                                .obtain(null,ServiceRequest.MSG_REQUEST);
                        mService.send(replyMsg);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                }
                default:
                {
                    super.handleMessage(msg);
                }
            }
        }
    }

    private void initMessenger(){
        // 创建与服务交互的消息实体Message
        Message msg = Message.obtain(null, ServiceRequest.MSG_INSTANCE, 0, 0);
        //把接收服务器端的回复的Messenger通过Message的replyTo参数传递给服务端
        msg.replyTo = mRecevierReplyMsg;
        try {
            //发送消息
            mService.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void initData(){

        HttpProxyCacheServer proxy = MApplication.getProxy(getApplicationContext());

//        String proxyUrl = proxy.getProxyUrl("http://fs.mv.web.kugou.com/201805151034/301b4249052e4f77917f02c1903e3370/G131/M06/0D/00/ww0DAFr5qtqACRUoAh-sVLABkV8377.mp4");
//        String proxyUrl2 = proxy.getProxyUrl("http://fs.mv.web.kugou.com/201805151530/498716f6f332829687bbf077e252a083/G133/M05/1F/19/xQ0DAFrwJs6AHgs6AbSCfVQb4IQ631.mp4");
        String proxyUrl2 = proxy.getProxyUrl("http://yyt.lexin580.com:8080/app_config/ztj08.mp4");
        String proxyUrl = proxy.getProxyUrl("http://yyt.lexin580.com:8080/lx_yyt/upload/video/152333847013885374202_src.mp4");

        list = new ArrayList<>();
        list.add(proxyUrl);
        list.add(proxyUrl2);
        list.add("http://img2.imgtn.bdimg.com/it/u=3817131034,1038857558&fm=27&gp=0.jpg");
        list.add("http://img1.imgtn.bdimg.com/it/u=4194723123,4160931506&fm=200&gp=0.jpg");
        list.add("http://img5.imgtn.bdimg.com/it/u=1812408136,1922560783&fm=27&gp=0.jpg");
    }

    private void initView(JsonObject jsonObject){
        try {
            HttpProxyCacheServer proxy = MApplication.getProxy(getApplicationContext());

            JsonArray jsonArray = jsonObject.getAsJsonArray("data");

            BannerModel.mode = jsonObject.get("mode").getAsInt();
            JsonElement jsonElement = jsonObject.get("screenPercent1");
            if(jsonElement != null
                    && !jsonElement.toString()
                    .equalsIgnoreCase("null")){
                BannerModel.screenPercent1 = BannerModel.SCREEN_PERCENT - jsonObject.get("screenPercent1").getAsFloat() * BannerModel.SCREEN_PERCENT;
            }
            if(jsonObject.has("screenPercent2")
                    && !jsonObject.get("screenPercent2").getAsString()
                    .equalsIgnoreCase("null")){
                BannerModel.screenPercent2 = BannerModel.SCREEN_PERCENT - jsonObject.get("screenPercent2").getAsFloat() * BannerModel.SCREEN_PERCENT;
            }
            List<BannerModel> listbm = new ArrayList<>();

            Log.i("mode",BannerModel.mode + "");
            Log.i("size",jsonArray.size() + "");
            for (int i = 0; i < BannerModel.mode && i < jsonArray.size(); i++){
                JsonObject jsonObject1 = jsonArray.get(i).getAsJsonObject();

                BannerModel bannerModel = new BannerModel();
                //bannerModel.imgDelyed = jsonObject1.get("imgDelyed").getAsInt();
                bannerModel.index = jsonObject1.get("index").getAsInt();

                List<BannerPage> listAddress = new ArrayList<>();
                JsonArray jsonArray1 = jsonObject1.getAsJsonArray("data");
                for (int j = 0; j < jsonArray1.size(); j++){
                    JsonObject jsonObject2 = jsonArray1.get(j).getAsJsonObject();
                    String url = jsonObject2.get("url").getAsString();
                    BannerPage bannerPage = new BannerPage();

                    if (MimeTypeMap.getFileExtensionFromUrl(url).equalsIgnoreCase("mp4"))
                    {
                        //listAddress.add(proxy.getProxyUrl(url));
                        bannerPage.url = proxy.getProxyUrl(url);
                    }
                    else
                    {
                        bannerPage.url = url;
                        bannerPage.imgDelyed = jsonObject2.get("imgDelyed").getAsInt();
                        //listAddress.add(url);
                    }

                    listAddress.add(bannerPage);
                }

                bannerModel.list = listAddress;

                listbm.add(bannerModel);
            }

            banner.setDataList(listbm,BannerModel.mode);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

//    private void verfyAuthCode(){
//        BannerModel.CodeAuth = preferenceSet.getStr(PreferenceSet.CodeAuth,null);
//        HttpUrls.IP_AD = preferenceSet.getStr(PreferenceSet.IP_AD,"");
//        HttpUrls.IP_AUTH = preferenceSet.getStr(PreferenceSet.IP_AUTH,"");
//
//        if(BannerModel.CodeAuth == null){
//
//            Alert.alertDialog(this,"请输入设备授权地址",
//                    HttpUrls.IP_AUTH == "" ? HttpUrls.IP_AUTH_DEFAULT : HttpUrls.IP_AUTH,
//                    new Alert.IResultRepose() {
//                        @Override
//                        public void onPositive(DialogInterface dialog,String s) {
//                            HttpUrls.IP_AUTH = s;
//
//                            Alert.alertDialog(base,"请输入广告地址",
//                                    HttpUrls.IP_AD == "" ? HttpUrls.IP_AD_DEFAULT : HttpUrls.IP_AD,
//                                    new Alert.IResultRepose() {
//                                        @Override
//                                        public void onPositive(DialogInterface dialog,String s) {
//                                            HttpUrls.IP_AD = s;
//                                            Alert.alertDialog(base,"请输入授权码",null, new Alert.IResultRepose() {
//                                                @Override
//                                                public void onPositive(DialogInterface dialog,String s) {
//                                                    try {
//
//                                                        BannerModel.CodeAuth = s;
//
//                                                        Alert.alertProgressCircle(MainActivity.this,
//                                                                new Alert.IResultRepose() {
//                                                                    @Override
//                                                                    public void onPositive(final DialogInterface dialog,String s) {
//                                                                        Log.i("onSuccess","string");
//                                                                        HttpUrls.init();
//                                                                        JsonObject jsonObject = new JsonObject();
//                                                                        //jsonObject.addProperty("username","15915899123");
//                                                                        //jsonObject.addProperty("password","19841018");
//                                                                        jsonObject.addProperty("deviceId",BannerModel.UNIQUE_ID);
//                                                                        jsonObject.addProperty("authCode",BannerModel.CodeAuth);
//
//                                                                        HttpRequest.get(HttpUrls.urlAuth,
//                                                                                jsonObject,
//                                                                                new HttpRequest.INetCallBack() {
//                                                                                    @Override
//                                                                                    public void onNetFailure() {
//
//                                                                                    }
//
//                                                                                    @Override
//                                                                                    public void onSuccess(JsonObject result) {
//                                                                                        Log.i("onSuccess",result.toString());
//                                                                                        dialog.dismiss();
//
//                                                                                        preferenceSet.setStr(PreferenceSet.CodeAuth,BannerModel.CodeAuth);
//                                                                                        preferenceSet.setStr(PreferenceSet.IP_AUTH,HttpUrls.IP_AUTH);
//                                                                                        preferenceSet.setStr(PreferenceSet.IP_AD,HttpUrls.IP_AD);
//
//                                                                                        JsonObject jsonObject = new JsonObject();
//                                                                                        //jsonObject.addProperty("username","15915899123");
//                                                                                        //jsonObject.addProperty("password","19841018");
//                                                                                        jsonObject.addProperty("deviceId",BannerModel.UNIQUE_ID);
//                                                                                        jsonObject.addProperty("code",BannerModel.CodeAuth);
//                                                                                        HttpRequest.get(HttpUrls.urlRegist,
//                                                                                                jsonObject,
//                                                                                                new HttpRequest.INetCallBack() {
//                                                                                                    @Override
//                                                                                                    public void onNetFailure() {
//
//                                                                                                    }
//
//                                                                                                    @Override
//                                                                                                    public void onSuccess(JsonObject result) {
//                                                                                                        //向service发送消息
//                                                                                                        try {
//                                                                                                            Message replyMsg = Message
//                                                                                                                    .obtain(null,ServiceRequest.MSG_REQUEST);
//                                                                                                            mService.send(replyMsg);
//                                                                                                        } catch (RemoteException e) {
//                                                                                                            e.printStackTrace();
//                                                                                                        }
//                                                                                                    }
//
//                                                                                                    @Override
//                                                                                                    public void onError(JsonObject errorMessage) {
//
//                                                                                                    }
//                                                                                                });
//                                                                                    }
//
//                                                                                    @Override
//                                                                                    public void onError( JsonObject errorMessage) {
//                                                                                        dialog.dismiss();
//                                                                                        initMessenger();
//                                                                                    }
//                                                                                });
//
//
//                                                                    }
//
//                                                                    @Override
//                                                                    public void onNegative() {
//
//                                                                    }
//                                                                });
//
//                                                    }
//                                                    catch (Exception e){
//
//                                                    }
//                                                }
//
//                                                @Override
//                                                public void onNegative() {
//
//                                                }
//                                            });
//                                        }
//
//                                        @Override
//                                        public void onNegative() {
//
//                                        }
//                                    });
//
//
//                        }
//
//                        @Override
//                        public void onNegative() {
//
//                        }
//                    });
//
//        }
//        else
//        {
//            HttpUrls.init();
//
//            //向service发送消息
//            try {
//                Message replyMsg = Message
//                        .obtain(null,ServiceRequest.MSG_REQUEST);
//                mService.send(replyMsg);
//            } catch (RemoteException e) {
//                e.printStackTrace();
//            }
//        }
//    }

    public void onClick(View view){

       /* if (view.getId() == R.id.bt){
            HttpProxyCacheServer proxy = MApplication.getProxy(getApplicationContext());
            String proxyUrl = proxy.getProxyUrl("http://fs.mv.web.kugou.com/201805161024/f6afd843dc2f8537ecd3989c61ba90ad/G129/M03/0D/01/IYcBAFr5Wf6AUjHcAa3LVmX3WWw629.mp4");
            String proxyUrl2 = proxy.getProxyUrl("http://fs.mv.web.kugou.com/201805161041/718bff9a0e01a650c78c71056eb91139/G127/M06/0B/15/vw0DAFrvus2AJpAvAjnbY2rw5mw659.mp4");

            list = new ArrayList<>();
            list.add(proxyUrl2);
            list.add("http://img1.imgtn.bdimg.com/it/u=344091145,309580146&fm=27&gp=0.jpg");
            list.add("http://img2.imgtn.bdimg.com/it/u=3817131034,1038857558&fm=27&gp=0.jpg");
            list.add(proxyUrl);
            list.add("http://img3.imgtn.bdimg.com/it/u=1399356699,3785361628&fm=27&gp=0.jpg");
        }else {
            list = new ArrayList<>();
        }
        banner.dataChange(list);*/
    }

    /** Defines callbacks for service binding, passed to bindService() */
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
//            ServiceRequest.ServiceBinder binder = (ServiceRequest.ServiceBinder) service;
//            mService = binder.getService();

            /**
             * 通过服务端传递的IBinder对象,创建相应的Messenger
             * 通过该Messenger对象与服务端进行交互
             */
            mService = new Messenger(service);
            mBound = true;
            initMessenger();
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
            mService = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BannerModel.UNIQUE_ID =  Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
//        BannerModel.UNIQUE_ID = "256";
        banner = (BannerMul) findViewById(R.id.banner);

        base = this;
        preferenceSet = new PreferenceSet(this);

        /*initData();
        initView();*/
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Bind to LocalService
        Intent intent = new Intent(this, ServiceRequest.class);
        bindService(intent, mConnection, BIND_AUTO_CREATE);
    }


    @Override
    protected void onResume(){
        super.onResume();
    }

    @Override
    protected void onStop(){
        super.onStop();
        // Unbind from the service
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
    }

    @Override
    protected void onDestroy()
    {
        banner.destroy();
       /* banner2.destroy();
        banner3.destroy();*/
        super.onDestroy();
    }

}
