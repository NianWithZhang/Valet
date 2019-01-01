package niannian.valet.HttpService;

import niannian.valet.ResponseModel.BooleanResponse;
import niannian.valet.ResponseModel.ClothesResponseList;
import niannian.valet.ResponseModel.SuitResponseList;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface SuitService {
    //获取穿搭建议
    @GET("api/suit/advice")
    Call<SuitResponseList> getAdvices(
            @Query("id") Integer id,
            @Query("latitude") Double latitude,
            @Query("longitude") Double longitude
    );

    //获取指定衣橱的所有穿搭列表以及冷暖信息
    @GET("api/suit/wardrobe")
    Call<SuitResponseList> getByWardrobe(
            @Query("wardrobe_id") Integer id,
            @Query("temperature") Double temperature
    );

    //获取穿搭对用的所有衣物列表
    @GET("api/suit")
    Call<ClothesResponseList> getClothes(
            @Query("id") Integer id
    );

    //选择今日穿着
    @PUT("api/suit")
    Call<BooleanResponse> wear(
            @Query("id") Integer id
    );

    //删除指定穿搭
    @DELETE("api/suit")
    Call<BooleanResponse> delete(
            @Query("id") Integer id
    );
}
