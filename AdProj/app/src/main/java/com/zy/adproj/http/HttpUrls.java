package com.zy.adproj.http;

public class HttpUrls {

    public static String IP_AUTH_DEFAULT = "http://47.92.220.162:8080";//授权IP地址 默认
    public static String IP_AD_DEFAULT = "http://47.92.220.162:8080";//广告IP地址 默认
    public static String IP_AUTH = "";
    public static String IP_AD = "";
    public static String urlPalyerList = IP_AD + "/wframe-web/devicePlay/deviceArrange";//获取播放列表
    public static String urlRegist = IP_AD + "/wframe-web/device/regist";//设备注册接口
    public static String urlAuth = IP_AUTH + "/wframe-web/device/auth";//设备授权接口
//    public static String urlAuth = "http://VoIP.lexinvip.com:8081/lx_yyt/api/user/login";//设备授权接口
//    public static String urlRegist = "http://VoIP.lexinvip.com:8081/lx_yyt/api/user/login";//设备授权接口

    public static void init(){
        urlPalyerList = IP_AD + "/wframe-web/devicePlay/deviceArrange";//获取播放列表
        //urlPalyerList = "http://yyt.lexin580.com:8080/app_config/ad.json";//获取播放列表
        urlRegist = IP_AD + "/wframe-web/device/regist";//设备注册接口
        urlAuth = IP_AUTH + "/wframe-web/device/auth";//设备授权接口
    }
}
