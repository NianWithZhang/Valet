package niannian.valet;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import niannian.valet.HttpService.RetrofitClient;
import niannian.valet.HttpService.UserService;
import niannian.valet.ResponseModel.BooleanResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText userIDText, passwordText;

    private Switch rememberUserSwitch;

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userIDText = (EditText)findViewById(R.id.userIdText);
        passwordText = (EditText)findViewById(R.id.confirmPasswordText);

        rememberUserSwitch = (Switch)findViewById(R.id.rememberUserSwitch);

        //获取SharedPreferences对象
        preferences = getSharedPreferences("preference", Context.MODE_PRIVATE);
        editor = preferences.edit();

        loadPreviousUser();
    }

    public void loginButton_Click(View view)
    {
        UserService checkUserService = RetrofitClient.newService(this,UserService.class);
        Call<BooleanResponse> call = checkUserService.checkUserPassword(userIDText.getText().toString(),passwordText.getText().toString());
        call.enqueue(new Callback<BooleanResponse>() {
            @Override
            public void onResponse(Call<BooleanResponse> call, Response<BooleanResponse> response) {
                BooleanResponse userCheckAns = response.body();

                login(userCheckAns.getAns());
            }

            @Override
            public void onFailure(Call<BooleanResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"网络连接失败",Toast.LENGTH_SHORT).show();
                System.out.println(t.getMessage());
            }
        });

    }

    public void login(boolean ans){
        if(!ans) {
            //用户名或密码错误则自动删除之前记录的登陆信息
            editor.putString("userid",null);
            editor.putString("password",null);
            editor.commit();

            Toast.makeText(this,"用户名或密码错误",Toast.LENGTH_SHORT).show();
            return;
        }

        User.getInstance().setUserInfo(userIDText.getText().toString(),passwordText.getText().toString());

        if(editor!=null){
            if(rememberUserSwitch.isChecked()){
                editor.putString("userid", User.getInstance().id);
                editor.putString("password",User.getInstance().password);

                editor.commit();
            }else{
                editor.putString("userid",null);
                editor.putString("password",null);
                editor.commit();
            }
        }

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
//        LoginActivity.this.finish();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.fade_out);
    }

    public void tryChangeActivityButton_Click(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
//        LoginActivity.this.finish();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.fade_out);
    }
    public void tryChangeActivityButton1_Click(View view){
        Intent intent = new Intent(this, ManageClothesActivity.class);
        startActivity(intent);
//        LoginActivity.this.finish();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.fade_out);
    }

    private void loadPreviousUser(){
        //LoginActivity不关 所以不用检查
//        //检查当前User是否已登陆用户
//        Pair<String,String> UserInfo = User.getInstance().getUserInfo();
//        if(UserInfo!=null){
//            userIDText.setText(UserInfo.first);
//            passwordText.setText(UserInfo.second);
//
//            return;
//        }

//        User.getInstance().resetUser();

        String userid = preferences.getString("userid",null);
        String password = preferences.getString("password",null);
        if(userid == null||password == null) {
            userid = "";
            password = "";
        }

        userIDText.setText(userid);
        passwordText.setText(password);
    }

    public void registerButton_Click(View view){
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
//        LoginActivity.this.finish();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.fade_out);
    }

    public void passwordVisibleButton_Click(View view){
        //text = 1
        //textPassword = 129
        if(passwordText.getInputType()==129)
            passwordText.setInputType(1);
        else
            passwordText.setInputType(129);
    }

}
