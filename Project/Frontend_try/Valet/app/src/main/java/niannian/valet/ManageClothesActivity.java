package niannian.valet;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ThemedSpinnerAdapter;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
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
import niannian.valet.HttpService.WardrobeService;
import niannian.valet.ResponseModel.BooleanResponse;
import niannian.valet.ResponseModel.ClothesResponseList;
import niannian.valet.ResponseModel.WardrobeResponse;
import niannian.valet.ResponseModel.WardrobeResponseList;
import niannian.valet.Utils.ActivityOperationUtil;
import niannian.valet.Utils.MessageBoxUtil;
import retrofit2.Callback;
import retrofit2.Response;

public class ManageClothesActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static ManageClothesActivity activity;

    private RecyclerView clothesRecyclerView;
    private Spinner selectWardrobeSpinner;
    public Integer currentWardrobeID;
    public Integer previousWardrobeID;

    public static ArrayList<Integer> selectedIdList;

    public Pair<ArrayList<Integer>,ArrayList<String>> wardrobes;

    public RecyclerViewAdapter adapt;
    public ClothesResponseList clothesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_clothes);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_manageClothes);
        setSupportActionBar(toolbar);

        activity = this;

        selectWardrobeSpinner = (Spinner)findViewById(R.id.selectWardrobeSpinner_clothesManage);

        clothesRecyclerView =(RecyclerView)findViewById(R.id.RecyclerView);
        clothesRecyclerView.setItemViewCacheSize(20);

        selectedIdList = new ArrayList<>();

        Intent intent = getIntent();
        previousWardrobeID = -1;
        if(intent!=null)
            previousWardrobeID = intent.getIntExtra("wardrobeID",-1);

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

        //设置导航栏默认选中值
        Menu mainDrawer = ((NavigationView)findViewById(R.id.nav_view)).getMenu();
        mainDrawer.getItem(1).setChecked(true);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();

        activity = null;
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

                ArrayList<Integer> wardrobeIDs = new ArrayList<Integer>();
                ArrayList<String> wardrobeNames = new ArrayList<String>();

                for(WardrobeResponse wardrobe:wardrobeResponseList.wardrobes){
                    wardrobeIDs.add(wardrobe.getId());
                    wardrobeNames.add(wardrobe.getName());
                }

                wardrobes = new Pair<ArrayList<Integer>,ArrayList<String>>(wardrobeIDs,wardrobeNames);

                initWardrobeSnipper(wardrobes);
            }

            @Override
            public void onFailure(retrofit2.Call<WardrobeResponseList> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"网络连接失败",Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void initWardrobeSnipper(final Pair<ArrayList<Integer>,ArrayList<String>> wardrobes){
        // Setup spinner
        selectWardrobeSpinner = (Spinner) findViewById(R.id.selectWardrobeSpinner_clothesManage);
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

                ((TextView)view).setTextColor(getResources().getColor(R.color.colorWhite));//(R.color.colorWhite);

                currentWardrobeID = wardrobes.first.get(position);

                freshClothes();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        if(previousWardrobeID>=0&&wardrobes.first.contains(previousWardrobeID))
            selectWardrobeSpinner.setSelection(wardrobes.first.indexOf(previousWardrobeID));
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
        if(currentWardrobeID == null)
            return;

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
                        Toast.makeText(getBaseContext(),"If you are happy - "+ position,Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(this,Modi);
                    }
                });
            }

            @Override
            public void onFailure(retrofit2.Call<ClothesResponseList> call, Throwable t) {
//                Toast.makeText(getApplicationContext(),"网络连接失败",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void ManageClothes_Button_Click(View view){

        setSelectedIdList();

        switch (view.getId()) {

               case R.id.moveClothesButton:
//                   Toast.makeText(ManageClothesActivity.this,a,Toast.LENGTH_SHORT).show();/////

//                   WardrobeResponseList wardrobes=new WardrobeResponseList();
//                   wardrobes.wardrobes=new WardrobeResponse[10];
//                   for(Integer i=0;i<10;i++){
//                       String name="name";
//                       wardrobes.wardrobes[i]=new WardrobeResponse(i,name+String.valueOf(i));
//                   }
//                   final ArrayList<String> list = new ArrayList<>();
//                   for(Integer i=0;i<wardrobes.wardrobes.length-6;i++)
//                   {
//                       list.add(wardrobes.wardrobes[i].name);
//                   }
                   final OptionCenterDialog optionCenterDialog = new OptionCenterDialog();
                   optionCenterDialog.show(ManageClothesActivity.this, wardrobes.second);
                   optionCenterDialog.setItemClickListener(new AdapterView.OnItemClickListener() {
                       @Override
                       public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                           optionCenterDialog.dismiss();
//                           Toast.makeText(ManageClothesActivity.this,String.valueOf(position),Toast.LENGTH_SHORT).show();
                           moveClothes(wardrobes.first.get(position));
                       }
                   });

                   break;
            case R.id.deleteClothesButton:
                if(selectedIdList.isEmpty()){
                    MessageBoxUtil.showMessage(this,"删除不成功","请选择要删除的衣物");
                    return;
                }

                final OptionMaterialDialog mMaterialDialog = new OptionMaterialDialog(ManageClothesActivity.this);
                mMaterialDialog.setMessage("确定删除当前选中衣物？删除后不可逆。")
                        .setPositiveButton("确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mMaterialDialog.dismiss();

                                deleteClothes();
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
        return;
    }
    private void setSelectedIdList(){
        if(selectedIdList==null)
            selectedIdList = new ArrayList<>();

        selectedIdList.clear();

        //Toast.makeText(ManageClothesActivity.this, "获取我们选取的数据", Toast.LENGTH_SHORT).show();
        // Log.e("TAG", mGetData.getText().toString());
//        String a="";/////
        Map<Integer, Boolean> map = adapt.getMap();
        for (int i = 0; i < map.size(); i++) {
            if (map.get(i)) {
                selectedIdList.add(clothesList.clothes[i].id);
//                a+=String.valueOf(clothesList.clothes[i].id);/////
            }
        }
    }

    private void deleteClothes(){
        ClothesService service = RetrofitClient.newService(this,ClothesService.class);
        Integer[] list = new Integer[selectedIdList.size()];
        selectedIdList.toArray(list);
        retrofit2.Call<BooleanResponse> call = service.delete(list);
        call.enqueue(new Callback<BooleanResponse>() {
            @Override
            public void onResponse(retrofit2.Call<BooleanResponse> call, Response<BooleanResponse> response) {
                BooleanResponse ans = response.body();

                if(ans.getAns())
                    MessageBoxUtil.showMessage(getBaseContext(),"删除成功");
//                    Toast.makeText(getBaseContext(),"删除成功",Toast.LENGTH_SHORT).show();
                else
                    MessageBoxUtil.showMessage(getBaseContext(),"删除失败");
//                    Toast.makeText(getBaseContext(),"删除失败",Toast.LENGTH_SHORT).show();

                freshClothes();
            }

            @Override
            public void onFailure(retrofit2.Call<BooleanResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"网络连接失败",Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void moveClothes(Integer targetWardrobeID){
        if(currentWardrobeID!=null&&currentWardrobeID.equals(targetWardrobeID))
            return;

        ClothesService service = RetrofitClient.newService(this,ClothesService.class);
        Integer[] list = new Integer[selectedIdList.size()];
        selectedIdList.toArray(list);
        retrofit2.Call<BooleanResponse> call = service.changeWardrobe(list,targetWardrobeID);
        call.enqueue(new Callback<BooleanResponse>() {
            @Override
            public void onResponse(retrofit2.Call<BooleanResponse> call, Response<BooleanResponse> response) {
                BooleanResponse ans = response.body();

                if(ans.getAns())
                    callMessageBox("衣物挪动成功");
//                    MessageBoxUtil.showMessage(getApplicationContext(),"衣物挪动成功");
//                    Toast.makeText(getBaseContext(),"衣物挪动成功",Toast.LENGTH_SHORT).show();
                else
                    callMessageBox("衣物挪动失败");
//                    MessageBoxUtil.showMessage(getBaseContext(),"衣物挪动失败");
//                    Toast.makeText(getBaseContext(),"衣物挪动失败",Toast.LENGTH_SHORT).show();

                freshClothes();
            }

            @Override
            public void onFailure(retrofit2.Call<BooleanResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"网络连接失败",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void jumpToAddClothes(View view){
        Intent intent = new Intent(this, AddClothesActivity.class);
        intent.putExtra("wardrobeID",currentWardrobeID);
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
            Intent intent = new Intent(this, ManageWardrobeActivity.class);
            startActivity(intent);
            this.finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }else if(id == R.id.nav_log_out)
            ActivityOperationUtil.logOut(this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_manage_clothes);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void newSuitButton_Click(View view){
        setSelectedIdList();

        if(selectedIdList.isEmpty()) {
            MessageBoxUtil.showMessage(this,"请选中穿搭包含的衣物");
            return;
        }

        Intent intent = new Intent(this,AddNewSuitActivity.class);
        intent.putExtra("wardrobeID",currentWardrobeID);
        startActivity(intent);
    }

    public void callMessageBox(String text){
        MessageBoxUtil.showMessage(this,text);
    }
}
