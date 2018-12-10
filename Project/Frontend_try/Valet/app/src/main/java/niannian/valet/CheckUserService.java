package niannian.valet;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CheckUserService {
    @GET("api/user")
    Call<UserCheckResponse> getInfo(
            @Query("id") String id,
            @Query("password") String password
    );
}
