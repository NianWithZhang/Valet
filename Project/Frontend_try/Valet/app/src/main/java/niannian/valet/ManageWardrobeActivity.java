package niannian.valet;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.longsh.optionframelibrary.OptionMaterialDialog;

import java.util.ArrayList;
import java.util.Map;

import niannian.valet.ClothesManageRecyclerView.WardrobeRecyclerViewAdapter;
import niannian.valet.HttpService.RetrofitClient;
import niannian.valet.HttpService.WardrobeService;
import niannian.valet.ResponseModel.BooleanResponse;
import niannian.valet.ResponseModel.WardrobeResponseList;
import niannian.valet.Utils.ActivityOperationUtil;
import niannian.valet.Utils.MessageBoxUtil;
import retrofit2.Callback;
import retrofit2.Response;

public class ManageWardrobeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //衣橱名称长度最大值
    public final static int WARDROBE_NAME_LIMIT = 5;

    private RecyclerView wardrobeRecyclerView;

    public ArrayList<Integer> selectedIdList;
    public WardrobeRecyclerViewAdapter adapt;
    public WardrobeResponseList wardrobeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_wardrobe);
        wardrobeRecyclerView =(RecyclerView)findViewById(R.id.manageWardrobeRecyclerView);
        Toolbar toolbar = (Toolbar) findViewById(R.id.manageWardrobeToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        selectedIdList=new ArrayList<>();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.manageWardrobeDrawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        initWardrobes();

        //设置导航栏默认选中值
        Menu mainDrawer = ((NavigationView)findViewById(R.id.nav_view)).getMenu();
        mainDrawer.getItem(2).setChecked(true);
    }

    private void initWardrobes(){
        WardrobeService service = RetrofitClient.newService(this,WardrobeService.class);
        retrofit2.Call<WardrobeResponseList> call = service.getUserWardrobes(User.getInstance().getId());
        call.enqueue(new Callback<WardrobeResponseList>() {
            @Override
            public void onResponse(retrofit2.Call<WardrobeResponseList> call, Response<WardrobeResponseList> response) {
                wardrobeList = response.body();

                initWardrobesManageWardrobe();
            }

            @Override
            public void onFailure(retrofit2.Call<WardrobeResponseList> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"网络连接失败",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void initWardrobesManageWardrobe(){
        LinearLayoutManager layoutManager=new LinearLayoutManager(getBaseContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        wardrobeRecyclerView.setLayoutManager(layoutManager);
        adapt = new WardrobeRecyclerViewAdapter(wardrobeList);
        wardrobeRecyclerView.setAdapter(adapt);

        adapt.setItemClickListener(new WardrobeRecyclerViewAdapter.wardrobeRecyclerViewOnItemClickListener() {
            @Override
            public void onItemClickListener(View view, int position) {
                Intent intent = new Intent(getBaseContext(),ManageClothesActivity.class);
                intent.putExtra("wardrobeID",wardrobeList.wardrobes[position].id);
                startActivity(intent);
                ActivityOperationUtil.findActivity(view.getContext()).finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.manageWardrobeDrawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.manage_wardrobe, menu);
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
//        if (id == R.id.manageWardrobeAction_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

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
        }else if(id == R.id.nav_clothes){
            Intent intent = new Intent(this, ManageClothesActivity.class);
            startActivity(intent);
            this.finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }else if(id == R.id.nav_log_out)
            ActivityOperationUtil.logOut(this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.manageWardrobeDrawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void ManageWardrobe_Button_Click(View view){
        setSelectedIdList();

        switch (view.getId()) {

            case R.id.addWardrobeImageButton:
                final OptionMaterialDialog mMaterialDialog = new OptionMaterialDialog(ManageWardrobeActivity.this);
                final EditText name=new EditText(getBaseContext());
                mMaterialDialog
                        .setContentView(name)
                        .setTitle("请输入要添加的衣橱名称：")
                        .setPositiveButton("确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mMaterialDialog.dismiss();
                                addWardrobe(name.getText().toString());
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
            case R.id.deleteWardrobeButton:
                if(selectedIdList.isEmpty()) {
                    MessageBoxUtil.showMessage(view.getContext(),"请选中要删除的衣橱");
                    return;
                }

//                Toast.makeText(ManageWardrobeActivity.this,a,Toast.LENGTH_SHORT).show();/////
                final OptionMaterialDialog mMaterialDialogDelete = new OptionMaterialDialog(ManageWardrobeActivity.this);
                mMaterialDialogDelete.setTitle("确认删除").setMessage("删除操作不可逆哦")
                        .setPositiveButton("确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mMaterialDialogDelete.dismiss();
                                deleteWardrobe();
                            }
                        })
                        .setNegativeButton("取消",
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        mMaterialDialogDelete.dismiss();
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
                selectedIdList.add(wardrobeList.wardrobes[i].id);
//                a+=String.valueOf(wardrobeList.wardrobes[i].id);/////
            }
        }
    }
    private void addWardrobe(String name){
        if(name.isEmpty()){
            MessageBoxUtil.showMessage(this,"名称不能为空","给你的新衣橱起个名字吧 :)");
            return;
        }else if(name.length()>WARDROBE_NAME_LIMIT){
            MessageBoxUtil.showMessage(this,"名字太长啦","仅支持不超过"+String.valueOf(WARDROBE_NAME_LIMIT)+"个字");
            return;
        }

        WardrobeService service = RetrofitClient.newService(this,WardrobeService.class);
        retrofit2.Call<BooleanResponse> call = service.add(User.getInstance().getId(),name);
        call.enqueue(new Callback<BooleanResponse>() {
            @Override
            public void onResponse(retrofit2.Call<BooleanResponse> call, Response<BooleanResponse> response) {
                BooleanResponse ans = response.body();

                if(ans.getAns())
                    MessageBoxUtil.showMessage(getBaseContext(),"创建成功");
//                    Toast.makeText(getBaseContext(),"删除成功",Toast.LENGTH_SHORT).show();
                else
                    MessageBoxUtil.showMessage(getBaseContext(),"出错啦 退出后重新试试吧");
//                    Toast.makeText(getBaseContext(),"删除失败",Toast.LENGTH_SHORT).show();

                initWardrobes();
            }

            @Override
            public void onFailure(retrofit2.Call<BooleanResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"网络连接失败",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteWardrobe(){
        WardrobeService service = RetrofitClient.newService(this,WardrobeService.class);
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
                    MessageBoxUtil.showMessage(getBaseContext(),"出错啦 退出后重新试试吧");
//                    Toast.makeText(getBaseContext(),"删除失败",Toast.LENGTH_SHORT).show();

                initWardrobes();
            }

            @Override
            public void onFailure(retrofit2.Call<BooleanResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"网络连接失败",Toast.LENGTH_SHORT).show();
            }
        });
    }


}
