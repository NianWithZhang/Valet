package niannian.valet.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.widget.Toast;

import niannian.valet.View.Activity.LoginActivity;
import niannian.valet.UserInfo.User;

public class ActivityOperationUtil {

    @Nullable
    public static Activity findActivity(Context context) {
        if (context instanceof Activity) {
            return (Activity) context;
        }
        if (context instanceof ContextWrapper) {
            ContextWrapper wrapper = (ContextWrapper) context;
            return findActivity(wrapper.getBaseContext());
        } else {
            return null;
        }
    }

    //注销操作
    public static void logOut(Activity activity){
        Intent intent = new Intent(activity,LoginActivity.class);
        intent.putExtra("autoLogin", Boolean.FALSE);
        activity.startActivity(intent);
        activity.finish();
        User.getInstance().resetUser();
        Toast.makeText(activity,"注销成功",Toast.LENGTH_SHORT).show();
    }

}
