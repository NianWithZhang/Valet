package niannian.valet.HttpService;

import niannian.valet.ResponseModel.ClothesResponseList;
import niannian.valet.ResponseModel.SuitResponseList;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ClothesService {
    //获取衣橱对应的衣物列表
    @GET("api/clothes/wardrobe")
    Call<ClothesResponseList> getByWardrobe(
            @Query("id") Integer id
    );
}
