package niannian.valet;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.io.File;
import java.io.IOException;

public class TestCameraActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_camera);
    }


    public void testCameraButton_Click(View view) throws IOException {
        AlertDialog alertDialog1 = new AlertDialog.Builder(this)
                .setTitle("emmmmm")//标题
                .setMessage(String.valueOf("测试相机"))//内容
                .setIcon(R.mipmap.ic_launcher)//图标
                .create();
        alertDialog1.show();

//        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
//        photoPickerIntent.setType("image/*");
//        startActivityForResult(photoPickerIntent, 1);

        //相机请求码
        final int CAMERA_REQUEST_CODE = 2;

        //用于保存调用相机拍照后所生成的文件
        File tempFile = new File(Environment.getExternalStorageDirectory().getPath(), System.currentTimeMillis() + ".jpg");
        //跳转到调用系统相机
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        //判断版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {   //如果在Android7.0以上,使用FileProvider获取Uri
            intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(this, "niannian.valet.fileprovider", tempFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
        } else {    //否则使用Uri.fromFile(file)方法获取Uri
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
        }
        startActivityForResult(intent, CAMERA_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
//        switch (requestCode) {
//            // 调用相机后返回
//            case CAMERA_REQUEST_CODE:
//                if (resultCode == RESULT_OK) {
//                    //用相机返回的照片去调用剪裁也需要对Uri进行处理
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                        Uri contentUri = FileProvider.getUriForFile(Main2Activity.this, "com.hansion.chosehead", tempFile);
//                        cropPhoto(contentUri);//裁剪图片
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
