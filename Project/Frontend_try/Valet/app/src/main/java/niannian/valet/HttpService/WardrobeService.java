package niannian.valet.HttpService;

import niannian.valet.ResponseModel.BooleanResponse;
import niannian.valet.ResponseModel.WardrobeResponseList;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WardrobeService {
    @GET("api/wardrobe")
    Call<WardrobeResponseList> getUserWardrobes(
            @Query("id") String id
    );
}
