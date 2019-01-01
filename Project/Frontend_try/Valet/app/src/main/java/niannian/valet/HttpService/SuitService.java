package niannian.valet.HttpService;

import niannian.valet.ResponseModel.BooleanResponse;
import niannian.valet.ResponseModel.ClothesResponseList;
import niannian.valet.ResponseModel.SuitResponseList;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SuitService {
    //获取穿搭建议
    @GET("api/suit/advice")
    Call<SuitResponseList> getAdvices(
            @Query("id") Integer id,
            @Query("latitude") Double latitude,
            @Query("longitude") Double longitude
    );

    //获取穿搭对用的所有衣物列表
    @GET("api/suit")
    Call<ClothesResponseList> getClothes(
            @Query("id") Integer id
    );
}
