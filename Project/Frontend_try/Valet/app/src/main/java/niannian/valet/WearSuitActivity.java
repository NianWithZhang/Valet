package niannian.valet;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import niannian.valet.HttpService.RetrofitClient;
import niannian.valet.HttpService.SuitService;
import niannian.valet.ResponseModel.ClothesResponse;
import niannian.valet.ResponseModel.ClothesResponseList;
import retrofit2.Callback;
import retrofit2.Response;

public class WearSuitActivity extends AppCompatActivity {

    private Integer id;
    private String suitName;

    private RecyclerView clothesRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //获取该Suit的ID和名称
//        savedInstanceState.getInt("id");
//        savedInstanceState.getString("name");
        Intent intent = getIntent();
        id = intent.getIntExtra("id",-1);
        suitName = intent.getStringExtra("name");

        setContentView(R.layout.activity_wear_suit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_wearSuit);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.confirmWearFab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        clothesRecyclerView = (RecyclerView)findViewById(R.id.clothesRecyclerView_wearSuit);

        setClothes();
    }

    private void setClothes(){
        SuitService service = RetrofitClient.newService(this,SuitService.class);
        retrofit2.Call<ClothesResponseList> call = service.getClothes(id);
        call.enqueue(new Callback<ClothesResponseList>() {
            @Override
            public void onResponse(retrofit2.Call<ClothesResponseList> call, Response<ClothesResponseList> response) {
                ClothesResponseList clothesResponseList = response.body();

                SuitClothesListAdapter myAdapter=new SuitClothesListAdapter(getApplicationContext(), Arrays.asList(clothesResponseList.clothes));
                StaggeredGridLayoutManager sgm=
                        new StaggeredGridLayoutManager(2,
                                StaggeredGridLayoutManager.VERTICAL);
                //瀑布流管理器。2行，排列方式为竖直排列。
                clothesRecyclerView.setLayoutManager(sgm);
                clothesRecyclerView.setAdapter(myAdapter);

                //分割线
                DividerItemDecoration itemDecoration=
                        new DividerItemDecoration(getApplicationContext(),
                                DividerItemDecoration.VERTICAL);// .VERTICAL_LIST);
                clothesRecyclerView.addItemDecoration(itemDecoration);
            }

            @Override
            public void onFailure(retrofit2.Call<ClothesResponseList> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"网络连接失败",Toast.LENGTH_SHORT).show();
            }
        });
    }

}
