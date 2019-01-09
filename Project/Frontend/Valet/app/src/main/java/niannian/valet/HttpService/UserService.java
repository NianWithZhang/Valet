package niannian.valet.HttpService;


import niannian.valet.ResponseModel.BooleanResponse;
import niannian.valet.ResponseModel.TaobaoItemResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface UserService {
    //尝试登录 检查用户名密码
    @GET("api/user")
    Call<BooleanResponse> checkUserPassword(
            @Query("id") String id,
            @Query("password") String password
    );

    //新建用户 返回值表示用户名是否重复
    @POST("api/user")
    Call<BooleanResponse> addUser(@Query("id") String id,
    @Query("password") String password);

    //获取推荐宝贝信息
    @PUT("api/user")
    Call<TaobaoItemResponse> getRecommend(@Query("id") String id);

}
