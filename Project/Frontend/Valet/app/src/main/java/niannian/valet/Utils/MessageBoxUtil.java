package niannian.valet.Utils;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;

import com.longsh.optionframelibrary.OptionMaterialDialog;

public class MessageBoxUtil {
    public static OptionMaterialDialog dialog;

    public static void showMessage(Context context, String title, String text){
        final OptionMaterialDialog mMaterialDialog = new OptionMaterialDialog(context);
        mMaterialDialog.setTitle(title)
                .setMessage(text)
                .setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMaterialDialog.dismiss();
                    }
                })
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
                .setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMaterialDialog.dismiss();
                    }
                })
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
