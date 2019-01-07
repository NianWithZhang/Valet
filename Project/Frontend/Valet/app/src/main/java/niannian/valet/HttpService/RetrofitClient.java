package niannian.valet.HttpService;

import android.content.Context;

import niannian.valet.R;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    public static <T> T newService(Context context, final Class<T> serviceClass){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(context.getString(R.string.server_url))//基础URL 建议以 / 结尾
                .addConverterFactory(GsonConverterFactory.create())//设置 Json 转换器
                .build();

        return retrofit.create(serviceClass);
    }

}
