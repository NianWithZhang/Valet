package niannian.valet;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Toast;


import com.longsh.optionframelibrary.OptionCenterDialog;

import java.util.ArrayList;
import java.util.Map;

import niannian.valet.ClothesManageRecyclerView.RecyclerViewAdapter;
import niannian.valet.ResponseModel.ClothesResponseList;

public class ManageClothes extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    RecyclerView recyclerView;
    ClothesResponseList clothesList=new ClothesResponseList();
     public ArrayList<Integer> selectedIdList=new ArrayList<>();
    RecyclerViewAdapter adapt=new RecyclerViewAdapter(clothesList);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_clothes);
        //set recyclerView
        recyclerView=(RecyclerView)findViewById(R.id.RecyclerView);


        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        /////设置LayoutManager



        recyclerView.setAdapter(adapt);

        adapt.setItemClickListener(new RecyclerViewAdapter.RecyclerViewOnItemClickListener() {
            @Override
            public void onItemClickListener(View view, int position) {
                Toast.makeText(ManageClothes.this,"If you are happy - "+ position,Toast.LENGTH_SHORT).show();
                //设置选中的项
                adapt.setSelectItem(position);
            }
        });



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_manage_clothes);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_manage_clothes);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
//    public void onClick(View view){
//        switch (view.getId()) {
//            case R.id.moveClothesButton:
//                String content = "";
//                selectedIdList.clear();
//
//                Toast.makeText(ManageClothes.this, "获取我们选取的数据", Toast.LENGTH_SHORT).show();
//                // Log.e("TAG", mGetData.getText().toString());
//
//                Map<Integer, Boolean> map = adapt.getMap();
//                for (int i = 0; i < selectedIdList.size(); i++) {
//                    if (map.get(i)) {
//                        selectedIdList.add(clothesList.clothes[i].id);
//                    }
//                }
//        }
//    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.manage_clothes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean ManageClothes_Button_Click(View view){

        selectedIdList.clear();

        //Toast.makeText(ManageClothes.this, "获取我们选取的数据", Toast.LENGTH_SHORT).show();
        // Log.e("TAG", mGetData.getText().toString());
        //String a="";
        Map<Integer, Boolean> map = adapt.getMap();
        for (int i = 0; i < map.size(); i++) {
            if (map.get(i)) {
                selectedIdList.add(clothesList.clothes[i].id);
                //a+=String.valueOf(clothesList.clothes[i].id);

            }
        }

        //Toast.makeText(ManageClothes.this,a,Toast.LENGTH_SHORT).show();
        //Toast.makeText(ManageClothes.this, "获取我们选取的数据", Toast.LENGTH_SHORT).show();

        switch (view.getId()) {

               case R.id.moveClothesButton:
                   //Toast.makeText(ManageClothes.this,a,Toast.LENGTH_SHORT).show();

                   final ArrayList<String> list = new ArrayList<>();
                   list.add("标为已读");
                   list.add("置顶聊天");
                   list.add("删除该聊天");
                   final OptionCenterDialog optionCenterDialog = new OptionCenterDialog();
                   optionCenterDialog.show(ManageClothes.this, list);
                   optionCenterDialog.setItemClickListener(new AdapterView.OnItemClickListener() {
                       @Override
                       public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                           optionCenterDialog.dismiss();
                       }
                   });

                   break;
            case R.id.deleteClothesButton:

                break;
        }
        return true;
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

//        if (id == R.id.nav_camera) {
//            // Handle the camera action
//        } else if (id == R.id.nav_gallery) {
//
//        } else if (id == R.id.nav_slideshow) {
//
//        } else if (id == R.id.nav_manage) {
//
//        } else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
//
//        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_manage_clothes);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
