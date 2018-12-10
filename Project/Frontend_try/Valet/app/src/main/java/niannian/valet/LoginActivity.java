package niannian.valet;

import android.app.AlertDialog;
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

//        final String outputText;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://niannian.info/")//基础URL 建议以 / 结尾
                .addConverterFactory(GsonConverterFactory.create())//设置 Json 转换器
                .build();


        CheckUserService checkUserService = retrofit.create(CheckUserService.class);
        Call<UserCheckResponse> call = checkUserService.getInfo(userIdText.getText().toString(),passwordText.getText().toString());
        call.enqueue(new Callback<UserCheckResponse>() {
            @Override
            public void onResponse(Call<UserCheckResponse> call, Response<UserCheckResponse> response) {
                //测试数据返回
                UserCheckResponse userCheckResponse = response.body();
                //String outputText = String.valueOf(userCheckResponse.getAns());

                System.out.println(userCheckResponse.getAns());

//                if(userCheckResponse.getAns())
                    login(userCheckResponse.getAns());

//                Log.e("TAG", "response == " +  weatherEntity.getData().getGanmao());
            }

            @Override
            public void onFailure(Call<UserCheckResponse> call, Throwable t) {
                System.out.println(t.getMessage());
//                Log.e("TAG", "Throwable : " + t);
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
}
