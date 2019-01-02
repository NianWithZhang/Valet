package niannian.valet;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ThemedSpinnerAdapter;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.longsh.optionframelibrary.OptionCenterDialog;
import com.longsh.optionframelibrary.OptionMaterialDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import niannian.valet.ClothesManageRecyclerView.RecyclerViewAdapter;
import niannian.valet.HttpService.ClothesService;
import niannian.valet.HttpService.RetrofitClient;
import niannian.valet.HttpService.SuitService;
import niannian.valet.HttpService.WardrobeService;
import niannian.valet.ResponseModel.ClothesResponseList;
import niannian.valet.ResponseModel.SuitResponse;
import niannian.valet.ResponseModel.SuitResponseList;
import niannian.valet.ResponseModel.WardrobeResponse;
import niannian.valet.ResponseModel.WardrobeResponseList;
import niannian.valet.Utils.ActivityOperationUtl;
import niannian.valet.Utils.GetLocationUtil;
import retrofit2.Callback;
import retrofit2.Response;

public class ManageClothesActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView clothesRecyclerView;
    private Spinner selectWardrobeSpinner;
    public Integer currentWardrobeID;

    public static ArrayList<Integer> selectedIdList;


    public RecyclerViewAdapter adapt;
    public ClothesResponseList clothesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_clothes);

        Toolbar toolbar = (Toolbar) findViewById(R.id.manageClothesToolbar);
        setSupportActionBar(toolbar);

        selectWardrobeSpinner = (Spinner)findViewById(R.id.selectWardrobeSpinner_clothesManage);

        clothesRecyclerView =(RecyclerView)findViewById(R.id.manageWardrobeRecyclerView);

        selectedIdList = new ArrayList<>();

        initWardrobes();

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

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
//                Toast.makeText(ManageClothesActivity.this, "获取我们选取的数据", Toast.LENGTH_SHORT).show();
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


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.manage_clothes, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    private void initWardrobes(){
        WardrobeService service = RetrofitClient.newService(this,WardrobeService.class);
        retrofit2.Call<WardrobeResponseList> call = service.getUserWardrobes(User.getInstance().getId());
        call.enqueue(new Callback<WardrobeResponseList>() {
            @Override
            public void onResponse(retrofit2.Call<WardrobeResponseList> call, Response<WardrobeResponseList> response) {
                WardrobeResponseList wardrobeResponseList = response.body();

                List<Integer> wardrobeIDs = new ArrayList<Integer>();
                List<String> wardrobeNames = new ArrayList<String>();

                for(WardrobeResponse wardrobe:wardrobeResponseList.wardrobes){
                    wardrobeIDs.add(wardrobe.getId());
                    wardrobeNames.add(wardrobe.getName());
                }

                Pair<List<Integer>,List<String>> wardrobes = new Pair<List<Integer>,List<String>>(wardrobeIDs,wardrobeNames);

                initWardrobeSnipper(wardrobes);
            }

            @Override
            public void onFailure(retrofit2.Call<WardrobeResponseList> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"网络连接失败",Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void initWardrobeSnipper(final Pair<List<Integer>,List<String>> wardrobes){
        // Setup spinner
        selectWardrobeSpinner = (Spinner) findViewById(R.id.selectWardrobeSpinner);
        selectWardrobeSpinner.setAdapter(new WardrobeSpinnerAdapter(getBaseContext(),wardrobes.second));

        selectWardrobeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // When the given dropdown item is selected, show its contents in the
                // container view.
//                getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.container, TestActivity.PlaceholderFragment.newInstance(position + 1))
//                        .commit();

//                Toast.makeText(view.getContext(),"snipperItemSelected",Toast.LENGTH_SHORT).show();

//                currentWardrobeID = wardrobes.first.get(position);

//                freshClothes();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
    private class WardrobeSpinnerAdapter extends ArrayAdapter<String> implements ThemedSpinnerAdapter {
        private final ThemedSpinnerAdapter.Helper mDropDownHelper;

        public WardrobeSpinnerAdapter(Context context, List<String> objects) {
            super(context, android.R.layout.simple_list_item_1, objects);
            mDropDownHelper = new ThemedSpinnerAdapter.Helper(context);
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            View view;

            if (convertView == null) {
                // Inflate the drop down using the helper's LayoutInflater
                LayoutInflater inflater = mDropDownHelper.getDropDownViewInflater();
                view = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
            } else {
                view = convertView;
            }

            TextView textView = (TextView) view.findViewById(android.R.id.text1);
            textView.setText(getItem(position));
            textView.setTextColor(getResources().getColor(R.color.colorText));

            return view;
        }

        @Override
        public Resources.Theme getDropDownViewTheme() {
            return mDropDownHelper.getDropDownViewTheme();
        }

        @Override
        public void setDropDownViewTheme(Resources.Theme theme) {
            mDropDownHelper.setDropDownViewTheme(theme);
        }
    }
    public void freshClothes(){
        ClothesService service = RetrofitClient.newService(this,ClothesService.class);
        retrofit2.Call<ClothesResponseList> call = service.getByWardrobe(currentWardrobeID);
        call.enqueue(new Callback<ClothesResponseList>() {
            @Override
            public void onResponse(retrofit2.Call<ClothesResponseList> call, Response<ClothesResponseList> response) {
                clothesList = response.body();

                //设置LayoutManager
                LinearLayoutManager layoutManager=new LinearLayoutManager(getBaseContext());
                layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                clothesRecyclerView.setLayoutManager(layoutManager);
                adapt = new RecyclerViewAdapter(clothesList);
                clothesRecyclerView.setAdapter(adapt);

                adapt.setItemClickListener(new RecyclerViewAdapter.RecyclerViewOnItemClickListener() {
                    @Override
                    public void onItemClickListener(View view, int position) {
                        Toast.makeText(ManageClothesActivity.this,"If you are happy - "+ position,Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onFailure(retrofit2.Call<ClothesResponseList> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"网络连接失败",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public boolean ManageClothes_Button_Click(View view){

        selectedIdList.clear();

        //Toast.makeText(ManageClothesActivity.this, "获取我们选取的数据", Toast.LENGTH_SHORT).show();
        // Log.e("TAG", mGetData.getText().toString());
        String a="";/////
        Map<Integer, Boolean> map = adapt.getMap();
        for (int i = 0; i < map.size(); i++) {
            if (map.get(i)) {
                selectedIdList.add(clothesList.clothes[i].id);
                a+=String.valueOf(clothesList.clothes[i].id);/////

            }
        }

        switch (view.getId()) {

               case R.id.moveClothesButton:
                   Toast.makeText(ManageClothesActivity.this,a,Toast.LENGTH_SHORT).show();/////

                   WardrobeResponseList wardrobes=new WardrobeResponseList();
                   wardrobes.wardrobes=new WardrobeResponse[10];
                   for(Integer i=0;i<10;i++){
                       String name="name";
                       wardrobes.wardrobes[i]=new WardrobeResponse(i,name+String.valueOf(i));
                   }
                   final ArrayList<String> list = new ArrayList<>();
                   for(Integer i=0;i<wardrobes.wardrobes.length-6;i++)
                   {
                       list.add(wardrobes.wardrobes[i].name);
                   }
                   final OptionCenterDialog optionCenterDialog = new OptionCenterDialog();
                   optionCenterDialog.show(ManageClothesActivity.this, list);
                   optionCenterDialog.setItemClickListener(new AdapterView.OnItemClickListener() {
                       @Override
                       public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                           optionCenterDialog.dismiss();
                           Toast.makeText(ManageClothesActivity.this,String.valueOf(position),Toast.LENGTH_SHORT).show();
                       }
                   });

                   break;
            case R.id.deleteClothesButton:
                Toast.makeText(ManageClothesActivity.this,a,Toast.LENGTH_SHORT).show();/////
                final OptionMaterialDialog mMaterialDialog = new OptionMaterialDialog(ManageClothesActivity.this);
                mMaterialDialog.setMessage("确定删除当前选中衣物？删除后不可逆。")
                        .setPositiveButton("确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mMaterialDialog.dismiss();
                            }
                        })
                        .setNegativeButton("取消",
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        mMaterialDialog.dismiss();
                                    }
                                })
                        .setCanceledOnTouchOutside(true)
                        .setOnDismissListener(
                                new DialogInterface.OnDismissListener() {
                                    @Override
                                    public void onDismiss(DialogInterface dialog) {
                                        //对话框消失后回调
                                    }
                                })
                        .show();
                break;
        }
        return true;
    }



    public void jumpToAddClothes(View view){
        Intent intent = new Intent(this, AddClothesActivity.class);
        startActivity(intent);
//        LoginActivity.this.finish();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.fade_out);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if(id == R.id.nav_select_suit){
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
            this.finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }else if(id == R.id.nav_wardrobe){
            //TODO
            Toast.makeText(this,"TODO",Toast.LENGTH_SHORT).show();
        }else if(id == R.id.nav_log_out)
            ActivityOperationUtl.logOut(this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_manage_clothes);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void testClick(View view){
//        Toast.makeText(getBaseContext(),"hello1!!!!",Toast.LENGTH_SHORT).show();
    }
}
