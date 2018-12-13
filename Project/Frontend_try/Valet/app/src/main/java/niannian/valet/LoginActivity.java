package niannian.valet;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    private EditText userIdText, passwordText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userIdText = (EditText)findViewById(R.id.userIdText);
        passwordText = (EditText)findViewById(R.id.passwordText);
    }

    public void loginButton_Click(View view)
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.server_url))//基础URL 建议以 / 结尾
                .addConverterFactory(GsonConverterFactory.create())//设置 Json 转换器
                .build();

        CheckUserService checkUserService = retrofit.create(CheckUserService.class);
        Call<BooleanResponse> call = checkUserService.checkUserPassword(userIdText.getText().toString(),passwordText.getText().toString());
        call.enqueue(new Callback<BooleanResponse>() {
            @Override
            public void onResponse(Call<BooleanResponse> call, Response<BooleanResponse> response) {
                //测试数据返回
                BooleanResponse userCheckAns = response.body();

                System.out.println(userCheckAns.getAns());

                login(userCheckAns.getAns());
            }

            @Override
            public void onFailure(Call<BooleanResponse> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });

    }

    public void login(boolean ans){
        AlertDialog alertDialog1 = new AlertDialog.Builder(this)
                .setTitle("接口返回值")//标题
                .setMessage(String.valueOf(ans))//内容
                .setIcon(R.mipmap.ic_launcher)//图标
                .create();
        alertDialog1.show();
    }

    public void tryChangeActivityButton_Click(View view){
        Intent intent = new Intent(this, TestCameraActivity.class);
        startActivity(intent);
//        LoginActivity.this.finish();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.fade_out);
    }
}
