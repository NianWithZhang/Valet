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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by niannian on 2018/12/27.
 */

public class RegisterActivity extends AppCompatActivity {

    private EditText userIDText, passwordText;

    private Switch rememberUserSwitch;

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userIDText = (EditText)findViewById(R.id.userIdText);
        passwordText = (EditText)findViewById(R.id.passwordText);

        rememberUserSwitch = (Switch)findViewById(R.id.rememberUserSwitch);

        //获取SharedPreferences对象
        preferences = getSharedPreferences("preference", Context.MODE_PRIVATE);
        editor = preferences.edit();

        //loadPreviousUser();
    }

    public void registerButton_Click(View view)
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.server_url))//基础URL 建议以 / 结尾
                .addConverterFactory(GsonConverterFactory.create())//设置 Json 转换器
                .build();

        UserService checkUserService = retrofit.create(UserService.class);
        Call<BooleanResponse> call = checkUserService.checkUserPassword(userIDText.getText().toString(),passwordText.getText().toString());
        call.enqueue(new Callback<BooleanResponse>() {
            @Override
            public void onResponse(Call<BooleanResponse> call, Response<BooleanResponse> response) {
                //测试数据返回
                BooleanResponse userCheckAns = response.body();

                Toast.makeText(getApplicationContext(),String.valueOf(userCheckAns.getAns()),Toast.LENGTH_SHORT).show();
                System.out.println(userCheckAns.getAns());

                register(userCheckAns.getAns());
            }

            @Override
            public void onFailure(Call<BooleanResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"网络连接失败",Toast.LENGTH_SHORT).show();
                System.out.println(t.getMessage());
            }
        });

    }

    public void register(boolean ans){
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
    }
    public void goLoginPage(View view){
        Intent intent = new Intent(this, TestCameraActivity.class);
        startActivity(intent);
//        LoginActivity.this.finish();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.fade_out);
    }

}