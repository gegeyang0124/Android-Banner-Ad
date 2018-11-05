package com.zy.adproj.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.zy.adproj.MainActivity;

/**
 * Created by Administrator on 2017/1/5.
 * 当Android启动时，会发出一个系统广播，内容为ACTION_BOOT_COMPLETED，它的字符串常量表示为android.intent.action.BOOT_COMPLETED。
 * 只要在程序中“捕捉”到这个消息，再启动之即可。记住，Android框架说：Don't call me, I'll call you back。
 * 我们要做的是等到接收这个消息，而实现的手段就是实现一个BroadcastReceiver。
 */


public class LaunchReceiver extends BroadcastReceiver {

    private final String ACTION = "android.intent.action.BOOT_COMPLETED";
    @Override
    public void onReceive(Context context, Intent intent)
    {
        // TODO Auto-generated method stub

//        Toast.makeText(context,"FFFF",Toast.LENGTH_SHORT).show();
        if (intent.getAction().equals(ACTION));
        {
            Intent intent2 = new Intent(context, MainActivity.class);
            intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent2);
            Log.d("DEBUG", "开机自动服务自动启动...");

            //Intent intentService = new Intent();
            //intentService.setClass(context, MyService.class);
            //context.startService(intentService);

        }

    }
}
