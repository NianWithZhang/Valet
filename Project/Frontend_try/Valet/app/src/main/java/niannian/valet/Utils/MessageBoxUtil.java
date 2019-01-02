package niannian.valet.Utils;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;

import com.longsh.optionframelibrary.OptionMaterialDialog;

public class MessageBoxUtil {
    public static void showMessage(Context context, String title, String text){
        final OptionMaterialDialog mMaterialDialog = new OptionMaterialDialog(context);
        mMaterialDialog.setTitle(title)
//                .setTitleTextColor(R.color.colorPrimary)
//                .setTitleTextSize((float) 22.5)
                .setMessage(text)
//                .setMessageTextColor(R.color.colorPrimary)
//                .setMessageTextSize((float) 16.5)
//                .setPositiveButtonTextColor(R.color.colorAccent)
//                .setNegativeButtonTextColor(R.color.colorPrimary)
//                .setPositiveButtonTextSize(15)
//                .setNegativeButtonTextSize(15)
                .setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMaterialDialog.dismiss();
                    }
                })
//                    .setNegativeButton("取消",
//                            new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    mMaterialDialog.dismiss();
//                                }
//                            })
                .setCanceledOnTouchOutside(true)
                .setOnDismissListener(
                        new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                //对话框消失后回调
                            }
                        })
                .show();
    }

    public static void showMessage(Context context, String text){
        final OptionMaterialDialog mMaterialDialog = new OptionMaterialDialog(context);
        mMaterialDialog.setMessage(text)
//                .setMessageTextColor(R.color.colorPrimary)
//                .setMessageTextSize((float) 16.5)
//                .setPositiveButtonTextColor(R.color.colorAccent)
//                .setNegativeButtonTextColor(R.color.colorPrimary)
//                .setPositiveButtonTextSize(15)
//                .setNegativeButtonTextSize(15)
                .setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMaterialDialog.dismiss();
                    }
                })
//                    .setNegativeButton("取消",
//                            new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    mMaterialDialog.dismiss();
//                                }
//                            })
                .setCanceledOnTouchOutside(true)
                .setOnDismissListener(
                        new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                //对话框消失后回调
                            }
                        })
                .show();
    }
}
