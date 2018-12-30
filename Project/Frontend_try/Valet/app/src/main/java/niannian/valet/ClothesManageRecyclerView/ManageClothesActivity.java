package niannian.valet.ClothesManageRecyclerView;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import niannian.valet.ResponseModel.Clothes;
import niannian.valet.R;
//import android.widget.Spinner;

public class ManageClothesActivity extends Activity {
    //private ArrayAdapter<String> arr_adapter;
    RecyclerView recyclerView;
    List<Clothes> Fruits=new ArrayList<>();

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //init();
        recyclerView=(RecyclerView)findViewById(R.id.RecyclerView);


        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        //设置LayoutManager


        RecyclerViewAdapter adapt=new RecyclerViewAdapter(Fruits);
        recyclerView.setAdapter(adapt);
    }
}
