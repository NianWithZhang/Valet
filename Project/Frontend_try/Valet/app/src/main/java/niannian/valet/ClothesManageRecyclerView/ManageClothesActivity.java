package niannian.valet.ClothesManageRecyclerView;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import niannian.valet.Data.ClothesImformation;
import niannian.valet.R;
//import android.widget.Spinner;

public class ManageClothesActivity extends Activity {
    private ArrayAdapter<String> arr_adapter;
    RecyclerView recyclerView;
    List<ClothesImformation> Fruits=new ArrayList<>();

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
////

        arr_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data_list);
        //设置样式
        arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器
        //spinner.setAdapter(arr_adapter);
    }
}
