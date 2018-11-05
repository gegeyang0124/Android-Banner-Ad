package com.zy.adproj.banner;

import java.util.List;

public class BannerModel {
    public static final int MONE_ONE = 1;//轮播模式1
    public static final int MONE_TWO = 2;//轮播模式2
    public static final int MONE_THREE = 3;//轮播模式3

    public static final float SCREEN_PERCENT = 10;//屏幕总的权重百分比
    public static float screenPercent1 = 5;//屏幕一占屏幕的大小（大于1屏有效）
    public static float screenPercent2 = 5;//屏幕二占屏幕的大小（大于2屏有效）

    public static String isChange = null;//返回数据的指纹。即当数据没有变化的时候不会发生变化，可以帮助前端快速判断数据是否发生变化

    public static String UNIQUE_ID = null;//设备ID
    public static String CodeAuth = null;//授权码
    public static String CodeDevice = null;//后台的设备编码
    public static int mode = 1;//当前轮播模式

    //public List<String> list;//当前轮播列表
    public List<BannerPage> list;//当前轮播列表
    public int imgDelyed = 1000;//图片轮播延时时间
    public Banner banner = null;//轮播UI
    public int index = -1;//当前插播地址

}
