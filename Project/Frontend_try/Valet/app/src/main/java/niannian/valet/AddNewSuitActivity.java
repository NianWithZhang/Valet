package niannian.valet;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.longsh.optionframelibrary.OptionCenterDialog;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import niannian.valet.HttpService.RetrofitClient;
import niannian.valet.HttpService.SuitService;
import niannian.valet.ResponseModel.BooleanResponse;
import niannian.valet.Utils.MessageBoxUtil;
import niannian.valet.Utils.PermissionUtil;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddNewSuitActivity extends AppCompatActivity {

    //相机请求码
    public final int CAMERA_REQUEST_CODE = 1;
    //相册请求码
    public final int ALBUM_REQUEST_CODE = 2;

    //穿搭名称长度最大值
    public final int SUIT_NAME_LENGTH_LIMIT = 8;

    private Integer wardrobeID;

    public Uri imgUri;
    public File imgFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_suit);

        Intent intent = getIntent();
        if(intent!=null)
            wardrobeID = intent.getIntExtra("wardrobeID",-1);
        if(wardrobeID == -1)
            Toast.makeText(this,"出现异常 请退出后重试",Toast.LENGTH_SHORT).show();

        //获取相机权限
        PermissionUtil.requestCameraStoragePermission(this);
    }

    public void chooseAddSuitImageMethod(View view){
        final ArrayList<String> list = new ArrayList<>();
        list.add("相机拍照添加图片");
        list.add("从相册添加图片");
        final OptionCenterDialog optionCenterDialog = new OptionCenterDialog();
        optionCenterDialog.show(AddNewSuitActivity.this, list);
        optionCenterDialog.setItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                optionCenterDialog.dismiss();
                Toast.makeText(AddNewSuitActivity.this,String.valueOf(position),Toast.LENGTH_SHORT).show();
                switch(position) {
                    case 0:
                        cameraSetImage();
                        break;
                    case 1:
                        albumSetImage();
                        break;

                }
            }
        });
    }

    private void cameraSetImage(){
        Toast.makeText(this, "相机启动中", Toast.LENGTH_SHORT).show();

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
    private void albumSetImage(){
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, ALBUM_REQUEST_CODE);
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
            case CAMERA_REQUEST_CODE:
                if(imgUri == null)
                    return;

                try {
                    Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(imgUri));
                    ImageView imageView = (ImageView) findViewById(R.id.addSuitImageButton);
                    /* 将Bitmap设定到ImageView */
                    imageView.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    Log.e("Exception", e.getMessage(),e);
                }
                break;
            case ALBUM_REQUEST_CODE:
                imgUri = intent.getData();

                if(imgUri == null)
                    return;

                try {
                    Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(imgUri));
                    ImageView imageView = (ImageView) findViewById(R.id.addSuitImageButton);
                    /* 将Bitmap设定到ImageView */
                    imageView.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    Log.e("Exception", e.getMessage(),e);
                }

                String filePath = GetRealPathFromUri.getRealPathFromUri(this,imgUri);
                imgFile = new File(filePath);

                break;
        }
    }

    public void addSuitCompleteButtonClick(View view){
        EditText suitName=findViewById(R.id.addSuitNameEditText);
        String name=suitName.getText().toString();

        if(name.isEmpty()) {
            MessageBoxUtil.showMessage(this,"创建失败","请给您的新穿搭起个名吧 :)");
            return;
        }else if(name.length()>SUIT_NAME_LENGTH_LIMIT){
            MessageBoxUtil.showMessage(this,"名字起太长啦","仅支持不超过"+String.valueOf(SUIT_NAME_LENGTH_LIMIT)+"个字");
            return;
        }

        addSuit(name,ManageClothesActivity.selectedIDList);
    }

    private void addSuit(String name,ArrayList<Integer> clothesId){
        if(imgFile == null){
            MessageBoxUtil.showMessage(this,"请上传穿搭照片","给你的穿搭拍张照吧 :)");
            return;
        }

        if(ManageClothesActivity.selectedIDList ==null)
            return;
        Integer[] list = new Integer[ManageClothesActivity.selectedIDList.size()];
        ManageClothesActivity.selectedIDList.toArray(list);

        SuitService service = RetrofitClient.newService(this,SuitService.class);
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpg"), imgFile);
        MultipartBody.Part body = MultipartBody.Part.createFormData("pic", imgFile.getName(), requestFile);

        Call<BooleanResponse> call = service.add(name,wardrobeID,list,body);
        call.enqueue(new Callback<BooleanResponse>() {
            @Override
            public void onResponse(Call<BooleanResponse> call, Response<BooleanResponse> response) {

                if(response.body().getAns()){
                    Toast.makeText(getApplicationContext(), "穿搭添加成功", Toast.LENGTH_SHORT).show();
                    goBackPageButton_addSuit_Click(null);
                }
                else
                    Toast.makeText(getApplicationContext(), "上传失败 请退出后重试", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<BooleanResponse> call, Throwable t) {
                Toast.makeText(getBaseContext(),"网络连接失败",Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void goBackPageButton_addSuit_Click(View view){
        this.finish();
    }

}
