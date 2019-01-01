package niannian.valet.ResponseModel;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.BitmapCallback;

import okhttp3.Call;

public abstract class UrlPic {
    public abstract String url(Context context);

    public static void setImage(ImageView image,String url)
    {
        OkHttpUtils.get().url(url).tag(image)
                .build()
                .connTimeOut(20000).readTimeOut(20000).writeTimeOut(20000)
                .execute(new BitmapCallback() {
                    @Override
                    public void onError(Call call, Exception e) {

                    }

                    @Override
                    public void onResponse(Call call, Bitmap bitmap) {
                        ImageView image = (ImageView)call.request().tag();
                        image.setImageBitmap(bitmap);
                    }
                });
    }

    public void setImage(Context context,ImageView image)
    {
        OkHttpUtils.get().url(url(context)).tag(image)
                .build()
                .connTimeOut(20000).readTimeOut(20000).writeTimeOut(20000)
                .execute(new BitmapCallback() {
                    @Override
                    public void onError(Call call, Exception e) {

                    }

                    @Override
                    public void onResponse(Call call, Bitmap bitmap) {
                        ImageView image = (ImageView)call.request().tag();
                        image.setImageBitmap(bitmap);
                    }
                });
    }
}
