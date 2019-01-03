package niannian.valet;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ThemedSpinnerAdapter;
import android.util.Log;
import android.util.TimeUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.longsh.optionframelibrary.OptionCenterDialog;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import niannian.valet.HttpService.ClothesService;
import niannian.valet.HttpService.RetrofitClient;
import niannian.valet.ResponseModel.BooleanResponse;
import niannian.valet.ResponseModel.ClothesInfoResponse;
import niannian.valet.ResponseModel.ClothesResponse;
import niannian.valet.Utils.MessageBoxUtil;
import niannian.valet.Utils.PermissionUtil;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Multipart;


public class EditClothesActivity extends AppCompatActivity {

    //相机请求码
    public final int CAMERA_REQUEST_CODE = 1;
    //相册请求码
    public final int ALBUM_REQUEST_CODE = 2;

    //衣物名称长度最大值
    public final int CLOTHES_NAME_LENGTH_LIMIT = 10;

    private Spinner selectWardrobeSnipper;
    private List<String> clothesType;

    private Integer clothesID = -1;

    public Uri imgUri;
    public File imgFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_clothes);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

//        setTheme(R.style.AppTheme_AddClothesActivity);

        //加载传入的wardrobeID
        Intent intent = getIntent();

        if(intent!=null){
            clothesID = intent.getIntExtra("clothesID",-1);
        }

        if(clothesID < 0)
        {
            Toast.makeText(this,"信息获取失败 请退出后重试",Toast.LENGTH_SHORT).show();
            this.finish();
        }


        //初始化ClothesType列表
        clothesType=new ArrayList<>();
        clothesType.add( 0,"帽子");
        clothesType.add( 1,"外套");
        clothesType.add( 2,"内衫");
        clothesType.add( 3,"裤裙");
        clothesType.add( 4,"袜子");
        clothesType.add( 5,"鞋子");


        initEditClothesTypeSnipper(clothesType);

        //初始化衣物信息
        initClothes();

        //获取相机权限
        PermissionUtil.requestCameraStoragePermission(this);
    }


    public void editClothesImageButton_Click(View view){
        if(!PermissionUtil.checkCameraStoragePermission(this))
            return;

        final ArrayList<String> list = new ArrayList<>();
        list.add("相机拍照添加图片");
        list.add("从相册添加图片");
        final OptionCenterDialog optionCenterDialog = new OptionCenterDialog();
        optionCenterDialog.show(EditClothesActivity.this, list);
        optionCenterDialog.setItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                optionCenterDialog.dismiss();
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
                    ImageView imageView = (ImageView) findViewById(R.id.editClothesImageButton);
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
                    ImageView imageView = (ImageView) findViewById(R.id.editClothesImageButton);
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


    private void initEditClothesTypeSnipper(final List<String> clothesType){

        // Setup spinner
        selectWardrobeSnipper = (Spinner) findViewById(R.id.editClothesTypeSpinner);
        selectWardrobeSnipper.setAdapter(new EditClothesActivity.editClothesTypeSpinnerAdapter(this,clothesType));

        selectWardrobeSnipper.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // When the given dropdown item is selected, show its contents in the
                // container view.
//                getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.container, TestActivity.PlaceholderFragment.newInstance(position + 1))
//                        .commit();

//                Toast.makeText(view.getContext(),"snipperItemSelected",Toast.LENGTH_SHORT).show();

                //freshClothes(wardrobes.first.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
    private class editClothesTypeSpinnerAdapter extends ArrayAdapter<String> implements ThemedSpinnerAdapter {
        private final ThemedSpinnerAdapter.Helper mDropDownHelper;

        public editClothesTypeSpinnerAdapter(Context context, List<String> objects) {
            super(context, android.R.layout.simple_list_item_1, objects);
            mDropDownHelper = new ThemedSpinnerAdapter.Helper(context);
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            View view;

            if (convertView == null) {
                // Inflate the drop down using the helper's LayoutInflater
                LayoutInflater inflater = mDropDownHelper.getDropDownViewInflater();
                view = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
            } else {
                view = convertView;
            }

            TextView textView = (TextView) view.findViewById(android.R.id.text1);
            textView.setText(getItem(position));
            textView.setTextColor(getResources().getColor(R.color.colorText));

            return view;
        }

        @Override
        public Resources.Theme getDropDownViewTheme() {
            return mDropDownHelper.getDropDownViewTheme();
        }

        @Override
        public void setDropDownViewTheme(Resources.Theme theme) {
            mDropDownHelper.setDropDownViewTheme(theme);
        }
    }

    //用户添加完成衣物信息
    public void editClothesCompleteButtonClick(View view){
        if(clothesID==null){
            Toast.makeText(this,"信息获取失败 请退出后重试",Toast.LENGTH_SHORT).show();
            return;
        }

        EditText clothesName=findViewById(R.id.editClothesNameEditText);
        String name=clothesName.getText().toString();

        if(name.length()> CLOTHES_NAME_LENGTH_LIMIT)
        {
            MessageBoxUtil.showMessage(this,"衣服名字太长啦","仅支持不超过"+String.valueOf(CLOTHES_NAME_LENGTH_LIMIT)+"个字 :)");

            return;
        }else if(name.length()<1){
            MessageBoxUtil.showMessage(this,"名称不能为空","给你的衣服起个名字吧 :)");

            return;
        }

        Integer type=selectWardrobeSnipper.getSelectedItemPosition();

        SeekBar thicknessBar=findViewById(R.id.editClothesThicknessSeekBar);
        Integer thickness=thicknessBar.getProgress();

        editClothes(clothesID,name,type,thickness);
    }
    //执行添加衣物操作
    private void editClothes(Integer clothesID,String name,Integer type,Integer thickness){
//        if(imgFile == null){
//            MessageBoxUtil.showMessage(this,"请上传衣物照片","给你的衣服拍张照吧 :)");
//            return;
//        }

        ClothesService service = RetrofitClient.newService(this,ClothesService.class);



        Call<BooleanResponse> call;

        if(imgFile == null){
            call = service.modifyWithoutPic(clothesID,name,type,thickness);
        }else{
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpg"), imgFile);
            MultipartBody.Part body = MultipartBody.Part.createFormData("pic", imgFile.getName(), requestFile);
            call = service.modify(clothesID,name,type,thickness,body);
        }

        call.enqueue(new Callback<BooleanResponse>() {
            @Override
            public void onResponse(Call<BooleanResponse> call, Response<BooleanResponse> response) {

                if(response.body().getAns()){
                    Toast.makeText(getApplicationContext(), "衣物修改成功", Toast.LENGTH_SHORT).show();
                    ManageClothesActivity.activity.freshClothes();
                    editGoBackPageButtonClick(null);
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

    private void initClothes(){
        if(clothesID<0)
            return;

        ClothesResponse.setImage((ImageView)findViewById(R.id.editClothesImageButton),ClothesResponse.url(this,clothesID));

        ClothesService service = RetrofitClient.newService(this,ClothesService.class);
        Call<ClothesInfoResponse> call = service.get(clothesID);
        call.enqueue(new Callback<ClothesInfoResponse>() {
            @Override
            public void onResponse(Call<ClothesInfoResponse> call, Response<ClothesInfoResponse> response) {
                ClothesInfoResponse clothesInfo = response.body();

                ((TextView)findViewById(R.id.editClothesNameEditText)).setText(clothesInfo.name);

                selectWardrobeSnipper.setSelection(clothesInfo.type);

                SeekBar thicknessBar=findViewById(R.id.editClothesThicknessSeekBar);
                thicknessBar.setProgress(clothesInfo.thickness);

                TextView lastWearingTimeText = (TextView)findViewById(R.id.lastWearingTimeText);
                TextView wearingFrequenctText = (TextView)findViewById(R.id.wearingFrequencyText);

//                Date date = new Date();
//                lastWearingTimeText.setText(String.valueOf(date.compareTo(clothesInfo.lastWearingTime)));
                lastWearingTimeText.setText(clothesInfo.lastWearingTime.toString());
                wearingFrequenctText.setText(clothesInfo.wearingFrequency.toString());
            }

            @Override
            public void onFailure(Call<ClothesInfoResponse> call, Throwable t) {
                Toast.makeText(getBaseContext(),"网络连接失败",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void editGoBackPageButtonClick(View view){
        this.finish();
    }

}
