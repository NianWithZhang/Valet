package niannian.valet.View.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import niannian.valet.HttpService.RetrofitClient;
import niannian.valet.HttpService.UserService;
import niannian.valet.R;
import niannian.valet.ResponseModel.BooleanResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by niannian on 2018/12/27.
 */

public class RegisterActivity extends AppCompatActivity {

    private EditText userIDText, passwordText,confirmPasswordText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userIDText = (EditText)findViewById(R.id.userIdText_register);
        passwordText = (EditText)findViewById(R.id.passwordText_register);
        confirmPasswordText = (EditText)findViewById(R.id.confirmPasswordText_register);
    }

    //按下注册按钮
    public void registerButton_Click(View view)
    {
        if(!checkUserPasswordLegal())
            return;

        register();
    }

    //检查用户名和密码是否合法 并提示用户
    private boolean checkUserPasswordLegal(){
        String userID = userIDText.getText().toString();
        String password = passwordText.getText().toString();
        String confirmPassword = confirmPasswordText.getText().toString();

        if(userID==null||userID.length()<1){
            Toast.makeText(this,"用户名长度过小",Toast.LENGTH_SHORT).show();
            return false;
        }else if(password==null||password.length()<6){
            Toast.makeText(this,"密码长度最小为6位",Toast.LENGTH_SHORT).show();
            return false;
        }else if(confirmPassword==null||!password.equals(confirmPassword)){
            Toast.makeText(this,"密码输入不一致",Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    public void register(){
        UserService userService = RetrofitClient.newService(this,UserService.class);
        Call<BooleanResponse> call = userService.addUser(userIDText.getText().toString(),passwordText.getText().toString());
        call.enqueue(new Callback<BooleanResponse>() {
            @Override
            public void onResponse(Call<BooleanResponse> call, Response<BooleanResponse> response) {
                BooleanResponse registerAns = response.body();

                registerCallback(registerAns.getAns());
            }

            @Override
            public void onFailure(Call<BooleanResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"网络连接失败",Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void registerCallback(boolean status){
        if(status){
            Toast.makeText(this,"注册成功",Toast.LENGTH_SHORT).show();
            backLoginButton_Click(null);
        }else{
            Toast.makeText(this,"用户名已被注册",Toast.LENGTH_SHORT).show();
        }
    }
    public void backLoginButton_Click(View view){
        this.finish();
//        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.fade_out);
    }

    public void passwordVisibleButton_Click(View view){
        //text = 1
        //textPassword = 129
        if(passwordText.getInputType()==129)
            passwordText.setInputType(1);
        else
            passwordText.setInputType(129);
    }
    public void confirmPasswordVisibleButton_Click(View view){
        //text = 1
        //textPassword = 129
        if(confirmPasswordText.getInputType()==129)
            confirmPasswordText.setInputType(1);
        else
            confirmPasswordText.setInputType(129);
    }

}