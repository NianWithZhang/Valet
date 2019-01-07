package niannian.valet.HttpService;

import niannian.valet.ResponseModel.BooleanResponse;
import niannian.valet.ResponseModel.ClothesInfoResponse;
import niannian.valet.ResponseModel.ClothesResponseList;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ClothesService {
    //获取衣橱对应的衣物列表
    @GET("api/clothes/wardrobe")
    Call<ClothesResponseList> getByWardrobe(
            @Query("id") Integer id
    );

    //获取指定衣物详细信息
    @GET("api/clothes")
    Call<ClothesInfoResponse> get(
            @Query("id") Integer id
    );

    //添加衣物
    @Multipart
    @POST("api/clothes")
    Call<BooleanResponse> add(
            @Query("wardrobe_id") Integer wardrobe_id,
            @Query("name") String name,
            @Query("type") Integer type,
            @Query("thickness") Integer thickness,
            @Part MultipartBody.Part pic
    );

    //修改衣物
    @Multipart
    @PUT("api/clothes/modify")
    Call<BooleanResponse> modify(
            @Query("id") Integer id,
            @Query("name") String name,
            @Query("type") Integer type,
            @Query("thickness") Integer thickness,
            @Part MultipartBody.Part pic
    );

    //修改衣物 不修改图片
    @PUT("api/clothes/modify")
    Call<BooleanResponse> modifyWithoutPic(
            @Query("id") Integer id,
            @Query("name") String name,
            @Query("type") Integer type,
            @Query("thickness") Integer thickness
    );

    //删除衣物
    @DELETE("api/clothes/many")
    Call<BooleanResponse> delete(
            @Query("ids") Integer[] ids
    );

    //更换衣橱
    @PUT("api/clothes/many")
    Call<BooleanResponse> changeWardrobe(
            @Query("clothes_ids") Integer[] clothes_ids,
            @Query("wardrobe_id") Integer wardrobe_id
    );
}
