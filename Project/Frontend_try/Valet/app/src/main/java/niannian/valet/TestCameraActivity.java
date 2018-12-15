package niannian.valet;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TestCameraActivity extends AppCompatActivity {

    public static Uri imgUri;
    public static File imgFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestCameraAndStoragePermission();

        imgUri = null;
        imgFile = null;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_camera);
    }

    //申请相机和文件权限
    public void requestCameraAndStoragePermission(){
        //申请相机权限
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED&&ContextCompat.checkSelfPermission(this.getApplicationContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);
        }
        else if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);
        }
        else if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    1);
        }
    }

    public void testCameraButton_Click(View view) throws IOException {

//        AlertDialog alertDialog1 = new AlertDialog.Builder(this)
//                .setTitle("emmmmm")//标题
//                .setMessage(String.valueOf("测试相机"))//内容
//                .setIcon(R.mipmap.ic_launcher)//图标
//                .create();
//        alertDialog1.show();

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED||ContextCompat.checkSelfPermission(this.getApplicationContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this, "请先开启相机和存储权限", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(this, "相机启动中", Toast.LENGTH_SHORT).show();

        //相机请求码
        final int CAMERA_REQUEST_CODE = 1;

        imgFile = new File(Environment.getExternalStorageDirectory().getPath(), System.currentTimeMillis() + ".jpg");
        //跳转到调用系统相机
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
//        //判断版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {   //如果在Android7.0以上,使用FileProvider获取Uri
            intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            imgUri = FileProvider.getUriForFile(this, "niannian.valet.provider", imgFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
        } else {    //否则使用Uri.fromFile(file)方法获取Uri
            imgUri = Uri.fromFile(imgFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
        }
        startActivityForResult(intent, CAMERA_REQUEST_CODE);
    }

    public void testAlbumButton_Click(View view){
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, 2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if(resultCode!=RESULT_OK){
            Toast.makeText(this, "照片选取失败", Toast.LENGTH_SHORT).show();
            return;
        }

        //显示图片
        ContentResolver cr = this.getContentResolver();

        switch (requestCode){
            case 1:
                if(imgUri == null)
                    return;

                try {
                    Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(imgUri));
                    ImageView imageView = (ImageView) findViewById(R.id.cameraImgView);
                    /* 将Bitmap设定到ImageView */
                    imageView.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    Log.e("Exception", e.getMessage(),e);
                }
                break;
            case 2:
                imgUri = intent.getData();

                if(imgUri == null)
                    return;

                try {
                    Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(imgUri));
                    ImageView imageView = (ImageView) findViewById(R.id.cameraImgView);
                    /* 将Bitmap设定到ImageView */
                    imageView.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    Log.e("Exception", e.getMessage(),e);
                }

                String filePath = GetRealPathFromUri.getRealPathFromUri(this,imgUri);
                imgFile = new File(filePath);

                break;
        }



//        switch (requestCode) {
//            // 调用相机后返回
//            case CAMERA_REQUEST_CODE:
//                if (resultCode == RESULT_OK) {
//                    //用相机返回的照片去调用剪裁也需要对Uri进行处理
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                        Uri imgUri = FileProvider.getUriForFile(Main2Activity.this, "com.hansion.chosehead", tempFile);
//                        cropPhoto(imgUri);//裁剪图片
//                    } else {
//                        cropPhoto(Uri.fromFile(tempFile));//裁剪图片
//                    }
//                }
//                break;
//            //调用相册后返回
//            case ALBUM_REQUEST_CODE:
//                if (resultCode == RESULT_OK) {
//                    Uri uri = intent.getData();
//                    cropPhoto(uri);//裁剪图片
//                }
//                break;
//            //调用剪裁后返回
//            case CROP_REQUEST_CODE:
//                Bundle bundle = intent.getExtras();
//                if (bundle != null) {
//                    //在这里获得了剪裁后的Bitmap对象，可以用于上传
//                    Bitmap image = bundle.getParcelable("data");
//                    //设置到ImageView上
//                    mImage.setImageBitmap(image);
//                    //也可以进行一些保存、压缩等操作后上传
//                    String path = saveImage("userHeader", image);
//                    File file = new File(path);
//                    /*
//                     *上传文件的额操作
//                     */
//                }
//                break;
//        }
    }


    public void uploadButton_Click(View view){
        if(imgFile == null)
            return;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.server_url))//基础URL 建议以 / 结尾
                .addConverterFactory(GsonConverterFactory.create())//设置 Json 转换器
                .build();

        UploadClothImgService service = retrofit.create(UploadClothImgService.class);
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpg"), imgFile);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", imgFile.getName(), requestFile);
        Call<BooleanResponse> call = service.upload("1","1", body );
        call.enqueue(new Callback<BooleanResponse>() {
            @Override
            public void onResponse(Call<BooleanResponse> call, Response<BooleanResponse> response) {
//                AlertDialog alertDialog1 = new AlertDialog.Builder(getApplicationContext())
//                        .setTitle("接口返回值")//标题
//                        .setMessage(String.valueOf(response.body().getAns()))//内容
//                        .setIcon(R.mipmap.ic_launcher)//图标
//                        .create();
//                alertDialog1.show();
                if(response.body().getAns())
                    Toast.makeText(getApplicationContext(), "上传完成", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getApplicationContext(), "上传失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<BooleanResponse> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });

    }

}
