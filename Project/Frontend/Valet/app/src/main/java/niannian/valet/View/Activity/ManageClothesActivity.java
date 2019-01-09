package niannian.valet.View.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
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

import niannian.valet.R;
import niannian.valet.RecyclerViewAdapter.ClothesRecyclerViewAdapter;
import niannian.valet.HttpService.ClothesService;
import niannian.valet.HttpService.RetrofitClient;
import niannian.valet.HttpService.WardrobeService;
import niannian.valet.ResponseModel.BooleanResponse;
import niannian.valet.ResponseModel.ClothesResponse;
import niannian.valet.ResponseModel.ClothesResponseList;
import niannian.valet.ResponseModel.WardrobeResponse;
import niannian.valet.ResponseModel.WardrobeResponseList;
import niannian.valet.UserInfo.User;
import niannian.valet.Utils.ActivityOperationUtil;
import niannian.valet.Utils.MessageBoxUtil;
import retrofit2.Callback;
import retrofit2.Response;

public class ManageClothesActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static ManageClothesActivity activity;

    private RecyclerView clothesRecyclerView;
    private Spinner selectWardrobeSpinner;
    public Integer currentWardrobeID = -1;
    private TabLayout tabLayout;

    public Integer previousWardrobeID;

    public static ArrayList<Integer> selectedIDList;

    public Pair<ArrayList<Integer>,ArrayList<String>> wardrobes;

    public ClothesRecyclerViewAdapter adapt;
    public ClothesResponseList clothesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_clothes);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_manageClothes);
        setSupportActionBar(toolbar);
        tabLayout = (TabLayout) findViewById(R.id.clothesTypeTabs);

        activity = this;

        selectWardrobeSpinner = (Spinner)findViewById(R.id.selectWardrobeSpinner_clothesManage);

        clothesRecyclerView =(RecyclerView)findViewById(R.id.RecyclerView);
        clothesRecyclerView.setItemViewCacheSize(1000);

        selectedIDList = new ArrayList<>();

        Intent intent = getIntent();
        previousWardrobeID = -1;
        if(intent!=null)
            previousWardrobeID = intent.getIntExtra("wardrobeID",-1);

        initWardrobes();

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            /**
             * Tab 进入选中状态时被调用
             */
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(wardrobes.first.isEmpty())
                    return;

                chooseClothesTypeTabClick(tab.getPosition());
            }
            /**
             * Tab 离开选中状态时回调
             */
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            /**
             * Tab 已经被选中又被选中时回调
             */
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

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

    public void chooseClothesTypeTabClick(int position){

        int clothesType=position-1;
        ArrayList<ClothesResponse> typeClothesList=new ArrayList<>();
        if(clothesType == -1){
            for(int i=0;i<clothesList.clothes.length;i++)
            typeClothesList.add(clothesList.clothes[i]);
        }
            else{
            for(int i=0;i<clothesList.clothes.length;i++)
            {
                if(clothesList.clothes[i].type==clothesType) {
                    typeClothesList.add(clothesList.clothes[i]);
                }
            }
        }

        final ClothesResponse[] list = new ClothesResponse[typeClothesList.size()];
        typeClothesList.toArray(list);

        LinearLayoutManager layoutManager=new LinearLayoutManager(getBaseContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        clothesRecyclerView.setLayoutManager(layoutManager);
        adapt = new ClothesRecyclerViewAdapter(new ClothesResponseList(list),selectedIDList);
        clothesRecyclerView.setAdapter(adapt);

        adapt.setItemClickListener(new ClothesRecyclerViewAdapter.RecyclerViewOnItemClickListener() {
            @Override
            public void onItemClickListener(View view, int position) {
                editClothes(list[position].id);
            }
        });


    }

    private void initWardrobes(){
        selectedIDList = new ArrayList<>();

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
        selectWardrobeSpinner.setAdapter(new WardrobeSpinnerAdapter(getBaseContext(),wardrobes.second));

        selectWardrobeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

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

        clothesList = new ClothesResponseList(new ClothesResponse[0]);
        tabLayout.getTabAt(0).select();

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
                adapt = new ClothesRecyclerViewAdapter(clothesList,selectedIDList);
                clothesRecyclerView.setAdapter(adapt);

                adapt.setItemClickListener(new ClothesRecyclerViewAdapter.RecyclerViewOnItemClickListener() {
                    @Override
                    public void onItemClickListener(View view, int position) {
                        editClothes(clothesList.clothes[position].id);
                    }
                });
            }

            @Override
            public void onFailure(retrofit2.Call<ClothesResponseList> call, Throwable t) {
            }
        });
    }

    public void ManageClothes_Button_Click(View view){

//        setSelectedIDList();

        switch (view.getId()) {

               case R.id.moveClothesButton:
                   if(selectedIDList.isEmpty()){
                       MessageBoxUtil.showMessage(this,"挪动不成功","请选择要挪动的衣物");
                       return;
                   }

                   final OptionCenterDialog optionCenterDialog = new OptionCenterDialog();
                   optionCenterDialog.show(ManageClothesActivity.this, wardrobes.second);
                   optionCenterDialog.setItemClickListener(new AdapterView.OnItemClickListener() {
                       @Override
                       public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                           optionCenterDialog.dismiss();
                           moveClothes(wardrobes.first.get(position));
                       }
                   });

                   break;
            case R.id.deleteClothesButton:
                if(selectedIDList.isEmpty()){
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

    private void deleteClothes(){
        ClothesService service = RetrofitClient.newService(this,ClothesService.class);
        Integer[] list = new Integer[selectedIDList.size()];
        selectedIDList.toArray(list);
        retrofit2.Call<BooleanResponse> call = service.delete(list);
        call.enqueue(new Callback<BooleanResponse>() {
            @Override
            public void onResponse(retrofit2.Call<BooleanResponse> call, Response<BooleanResponse> response) {
                BooleanResponse ans = response.body();

                if(ans.getAns())
                    MessageBoxUtil.showMessage(getBaseContext(),"删除成功");
                else
                    MessageBoxUtil.showMessage(getBaseContext(),"删除失败");

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
        Integer[] list = new Integer[selectedIDList.size()];
        selectedIDList.toArray(list);
        retrofit2.Call<BooleanResponse> call = service.changeWardrobe(list,targetWardrobeID);
        call.enqueue(new Callback<BooleanResponse>() {
            @Override
            public void onResponse(retrofit2.Call<BooleanResponse> call, Response<BooleanResponse> response) {
                BooleanResponse ans = response.body();

                if(ans.getAns())
                    callMessageBox("衣物挪动成功");
                else
                    callMessageBox("衣物挪动失败");

                freshClothes();
            }

            @Override
            public void onFailure(retrofit2.Call<BooleanResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"网络连接失败",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void editClothes(Integer id){
        Intent intent = new Intent(this,EditClothesActivity.class);
        intent.putExtra("clothesID",id);
        startActivity(intent);
    }

    public void addClothesButton_Click(View view){
        if(currentWardrobeID == null||currentWardrobeID==-1)
        {
            MessageBoxUtil.showMessage(this,"先去添加你的衣橱吧 :)");
            return;
        }

        Intent intent = new Intent(this, AddClothesActivity.class);
        intent.putExtra("wardrobeID",currentWardrobeID);
        startActivity(intent);
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
        else if(id == R.id.nav_exit)
            this.finish();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_manage_clothes);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void newSuitButton_Click(View view){
        if(currentWardrobeID == null||currentWardrobeID < 0) {
            MessageBoxUtil.showMessage(this,"先去添加你的衣橱吧 :)");
            return;
        }

        if(selectedIDList.isEmpty()) {
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
