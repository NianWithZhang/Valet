package niannian.valet;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface UploadClothImgService {
    @Multipart
    @POST("api/cloth")
    Call<Boolean> upload(@Query("id") String userID,
                         @Part("file\"; filename=\"cloth.jpg\"") RequestBody img);
}
