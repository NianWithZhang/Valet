package niannian.valet.HttpService;


import niannian.valet.ResponseModel.BooleanResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
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


}
