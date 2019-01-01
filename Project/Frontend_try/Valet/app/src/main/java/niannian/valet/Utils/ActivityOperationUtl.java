package niannian.valet.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.support.annotation.Nullable;
import android.widget.Toast;

import niannian.valet.User;

public class ActivityOperationUtl {

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
        activity.finish();
        User.getInstance().resetUser();
        Toast.makeText(activity,"注销成功",Toast.LENGTH_SHORT).show();
    }

}
