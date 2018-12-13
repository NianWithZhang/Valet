package niannian.valet;

import android.Manifest;
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class TestCameraActivity extends AppCompatActivity {

    public static Uri imgUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestCameraAndStoragePermission();

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

//        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
//        photoPickerIntent.setType("image/*");
//        startActivityForResult(photoPickerIntent, 1);

        //相机请求码
        final int CAMERA_REQUEST_CODE = 2;

        File tempFile = new File(Environment.getExternalStorageDirectory().getPath(), System.currentTimeMillis() + ".jpg");
        //跳转到调用系统相机
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
//        //判断版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {   //如果在Android7.0以上,使用FileProvider获取Uri
            intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            imgUri = FileProvider.getUriForFile(this, "niannian.valet.provider", tempFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
        } else {    //否则使用Uri.fromFile(file)方法获取Uri
            imgUri = Uri.fromFile(tempFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
        }
        startActivityForResult(intent, CAMERA_REQUEST_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

        //显示图片
        ContentResolver cr = this.getContentResolver();

        try {
            Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(imgUri));
            ImageView imageView = (ImageView) findViewById(R.id.cameraImgView);
            /* 将Bitmap设定到ImageView */
            imageView.setImageBitmap(bitmap);
        } catch (FileNotFoundException e) {
            Log.e("Exception", e.getMessage(),e);
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

}
