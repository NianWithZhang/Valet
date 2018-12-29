package niannian.valet;

import niannian.valet.ResponseModel.BooleanResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SuitService {
    @GET("api/user")
    Call<BooleanResponse> checkUserPassword(
            @Query("id") String id,
            @Query("password") String password
    );
}
