package com.zy.adproj.dialog;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Alert {

    /**
     * @param context,//上下文
     * @param title,//提示文本
     * @param defalutText,//默认输入数据文本
     * **/
    public static void alertDialog(final Context context,String title,String defalutText,final IResultRepose iResultRepose){
//        View view = getLayoutInflater().inflate(R.layout.half_dialog_view, null);
//        final EditText editText = (EditText) view.findViewById(R.id.dialog_edit);

//        View view = new LinearLayout(context);
        final EditText editText = new EditText(context);
        if(defalutText != null){
            editText.setText(defalutText);
        }

        final AlertDialog dialog = new AlertDialog.Builder(context)
                .setCancelable(false)//调用这个方法时，按对话框以外的地方不起作用。按返回键也不起作用
                //.setIcon(R.mipmap.icon)//设置标题的图片
                .setTitle(title)//设置对话框的标题
                .setView(editText)
                /*.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        iResultRepose.onNegative();
                    }
                })*/
                /*.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String content = editText.getText().toString();
                        if(!content.equals("") && content != null){
                            Log.i("content","content");
                            Log.i("content vv",content + " 55");
                            dialog.dismiss();
                            iResultRepose.onPositive(content);
                        }
                        else {
                            Toast.makeText(context,"请输入授权码",Toast.LENGTH_SHORT).show();
                        }
                    }
                })*/
                .setPositiveButton("确定", null)
                .create();

//        dialog.setCanceledOnTouchOutside(false);//调用这个方法时，按对话框以外的地方不起作用。按返回键还起作用
//        dialog.setCancelable(false);//调用这个方法时，按对话框以外的地方不起作用。按返回键也不起作用

        dialog.show();

        //重写“确定”（AlertDialog.BUTTON_POSITIVE），截取监听
        dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(context,"getButton(AlertDialog.BUTTON_POSITIVE)", Toast.LENGTH_LONG).show();
                //这里可以控制是否让对话框消失
                String content = editText.getText().toString();
                if(!content.equals("") && content != null){
                    dialog.dismiss();
                    iResultRepose.onPositive(dialog,content);
                }
                else {
                    Toast.makeText(context,"请输入授权码",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    // 显示圆形的进度框
    public static void alertProgressCircle(Context context,IResultRepose iResultRepose) {
        // 创建进度条的对话框
        ProgressDialog dialog = new ProgressDialog(context);
        // 设置进度条的样式，选择圆形或条状
        // 这里这圆形的
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        // 设置标题
//        dialog.setTitle("文件下载");
        // 设置文本信息
        dialog.setMessage("正在验证......");
        // 设置是否能用后退键出对话框
        // 选择false就代表不能
        // 如果设置为false，程序可能退出不了
        dialog.setCancelable(false);
        // 显示对话框
        dialog.show();

        iResultRepose.onPositive(dialog,null);
    }

    public static interface IResultRepose{
        void onPositive(final DialogInterface dialog, String s);
        void onNegative();
    }
}
