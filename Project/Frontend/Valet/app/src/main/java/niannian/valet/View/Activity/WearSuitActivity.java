package niannian.valet.View.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Objects;

import niannian.valet.HttpService.RetrofitClient;
import niannian.valet.HttpService.SuitService;
import niannian.valet.R;
import niannian.valet.RecyclerViewAdapter.SuitClothesListAdapter;
import niannian.valet.ResponseModel.BooleanResponse;
import niannian.valet.ResponseModel.ClothesResponseList;
import retrofit2.Callback;
import retrofit2.Response;

public class WearSuitActivity extends AppCompatActivity {

    private Integer id;

    private RecyclerView clothesRecyclerView;

    private static WearSuitActivity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = this;

        //获取该Suit的ID和名称
//        savedInstanceState.getInt("id");
//        savedInstanceState.getString("name");
        Intent intent = getIntent();
        id = intent.getIntExtra("id",-1);
        String suitName = intent.getStringExtra("name");

        setContentView(R.layout.activity_wear_suit);
        Toolbar toolbar = findViewById(R.id.toolbar_wearSuit);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        //设置穿搭名称title
        ((TextView)findViewById(R.id.suitNameText_wearSuit)).setText(suitName);

        //设置确认今日穿搭按钮
        FloatingActionButton fab = findViewById(R.id.confirmWearFab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

                SuitService service = RetrofitClient.newService(view.getContext(),SuitService.class);
                retrofit2.Call<BooleanResponse> call = service.wear(id);
                call.enqueue(new Callback<BooleanResponse>() {
                    @Override
                    public void onResponse(@NonNull retrofit2.Call<BooleanResponse> call, @NonNull Response<BooleanResponse> response) {
                        BooleanResponse booleanResponse = response.body();

                        if(Objects.requireNonNull(booleanResponse).getAns()){
                            Toast.makeText(getBaseContext(),"今天的衣服选好啦",Toast.LENGTH_SHORT).show();
                            ((Activity)view.getContext()).finish();
                        }else
                            Toast.makeText(getBaseContext(),"出现异常 请退出后重试",Toast.LENGTH_SHORT).show();

//                        ((Activity)view.getContext()).finish();
                    }

                    @Override
                    public void onFailure(@NonNull retrofit2.Call<BooleanResponse> call, @NonNull Throwable t) {
                        Toast.makeText(getApplicationContext(),"网络连接失败",Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });

        clothesRecyclerView = findViewById(R.id.clothesRecyclerView_wearSuit);
        clothesRecyclerView.setItemViewCacheSize(20);

        setClothes();
    }

    @Override
    protected void onStop(){
        activity = null;
        super.onStop();
    }

    private void setClothes(){
        SuitService service = RetrofitClient.newService(this,SuitService.class);
        retrofit2.Call<ClothesResponseList> call = service.getClothes(id);
        call.enqueue(new Callback<ClothesResponseList>() {
            @Override
            public void onResponse(@NonNull retrofit2.Call<ClothesResponseList> call, @NonNull Response<ClothesResponseList> response) {
                ClothesResponseList clothesResponseList = response.body();

                SuitClothesListAdapter myAdapter=new SuitClothesListAdapter(getApplicationContext(), Arrays.asList(Objects.requireNonNull(clothesResponseList).clothes));
                StaggeredGridLayoutManager sgm=
                        new StaggeredGridLayoutManager(2,
                                StaggeredGridLayoutManager.VERTICAL);
                //瀑布流管理器。2行，排列方式为竖直排列。
                clothesRecyclerView.setLayoutManager(sgm);
                clothesRecyclerView.setAdapter(myAdapter);

//                //分割线
//                DividerItemDecoration itemDecoration=
//                        new DividerItemDecoration(getApplicationContext(),
//                                DividerItemDecoration.VERTICAL);// .VERTICAL_LIST);
//                clothesRecyclerView.addItemDecoration(itemDecoration);
            }

            @Override
            public void onFailure(@NonNull retrofit2.Call<ClothesResponseList> call, @NonNull Throwable t) {
                Toast.makeText(getApplicationContext(),"网络连接失败",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void deleteSuitButton_Click(View view){
        SuitService service = RetrofitClient.newService(this,SuitService.class);
        retrofit2.Call<BooleanResponse> call = service.delete(id);
        call.enqueue(new Callback<BooleanResponse>() {
            @Override
            public void onResponse(@NonNull retrofit2.Call<BooleanResponse> call, @NonNull Response<BooleanResponse> response) {
                BooleanResponse booleanResponse = response.body();

                //成功删除穿搭
                if(Objects.requireNonNull(booleanResponse).getAns()){
                    MainActivity.activity.freshSuits();
                    Toast.makeText(getBaseContext(),"删除成功",Toast.LENGTH_SHORT).show();
                    returnButton_wearSuit_Click();
                }else
                    Toast.makeText(getBaseContext(),"出错 请退出后重试",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(@NonNull retrofit2.Call<BooleanResponse> call, @NonNull Throwable t) {
                Toast.makeText(getApplicationContext(),"网络连接失败",Toast.LENGTH_SHORT).show();
            }
        });
    }

    //返回按钮被按下
    private void returnButton_wearSuit_Click(){
        this.finish();
    }

}
