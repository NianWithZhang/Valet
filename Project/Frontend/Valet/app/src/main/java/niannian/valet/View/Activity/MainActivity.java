package niannian.valet.View.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ThemedSpinnerAdapter;
import android.support.v7.widget.Toolbar;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.longsh.optionframelibrary.OptionMaterialDialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import niannian.valet.HttpService.RetrofitClient;
import niannian.valet.HttpService.SuitService;
import niannian.valet.HttpService.UserService;
import niannian.valet.HttpService.WardrobeService;
import niannian.valet.R;
import niannian.valet.RecyclerViewAdapter.WearSuitRecyclerViewAdapter;
import niannian.valet.ResponseModel.SuitResponse;
import niannian.valet.ResponseModel.SuitResponseList;
import niannian.valet.ResponseModel.TaobaoItemResponse;
import niannian.valet.ResponseModel.UrlPic;
import niannian.valet.ResponseModel.WardrobeResponse;
import niannian.valet.ResponseModel.WardrobeResponseList;
import niannian.valet.ResponseModel.WeatherInfo;
import niannian.valet.UserInfo.User;
import niannian.valet.Utils.ActivityOperationUtil;
import niannian.valet.Utils.GetLocationUtil;
import niannian.valet.View.Fragment.BestSuitFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    public static MainActivity activity;

    private Integer bestSuitNum;

    private Menu mainDrawer;
    private Spinner selectWardrobeSpinner;
    private Toolbar toolbar;
    private ViewPager bestSuitsViewPager;
    private RecyclerView allSuitsRecyclerView;

    public Integer currentWardrobeID;

    private SensorManager sensorManager;
    private Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //弹出推荐宝贝信息
        pushRecommend();

        activity = this;

        //设置左导航栏抽屉
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        initWardrobes();

        //设置导航栏默认选中值
        mainDrawer = ((NavigationView)findViewById(R.id.nav_view)).getMenu();
        mainDrawer.getItem(0).setChecked(true);

        //设置传感器
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        //设置界面缓冲池大小
        allSuitsRecyclerView = findViewById(R.id.allSuitsRecyclerView_main);
        allSuitsRecyclerView.setItemViewCacheSize(10);
        bestSuitsViewPager = findViewById(R.id.bestSuitsViewPager);
        bestSuitsViewPager.setOffscreenPageLimit(5);
    }

    @Override
    protected void onResume(){
        super.onResume();
        if (sensorManager != null){
            sensorManager.registerListener(sensorEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        }

    }

    private SensorEventListener sensorEventListener = new SensorEventListener(){
        public void onSensorChanged(SensorEvent event) {
            float[] values = event.values;
            float x = values[0]; // x轴方向的重力加速度，向右为正
            float y = values[1]; // y轴方向的重力加速度，向前为正
            float z = values[2]; // z轴方向的重力加速度，向上为正

            int medumValue = 36;
            if(Math.abs(x) > medumValue || Math.abs(y) > medumValue || Math.abs(z) > medumValue){

                setRandomBestSuit();

                vibrator.vibrate(200);

            }


        }
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }

    };

    @Override
    protected void onDestroy(){
        super.onDestroy();

        activity = null;
    }

    private void initWardrobes(){
        WardrobeService service = RetrofitClient.newService(this,WardrobeService.class);
        retrofit2.Call<WardrobeResponseList> call = service.getUserWardrobes(User.getInstance().getId());
        call.enqueue(new Callback<WardrobeResponseList>() {
            @Override
            public void onResponse(retrofit2.Call<WardrobeResponseList> call, Response<WardrobeResponseList> response) {
                WardrobeResponseList wardrobeResponseList = response.body();

                if(wardrobeResponseList.wardrobes.length == 0)
                {

                    Intent intent = new Intent(getApplicationContext(),ManageWardrobeActivity.class);
                    startActivity(intent);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    return;
                }

                List<Integer> wardrobeIDs = new ArrayList<Integer>();
                List<String> wardrobeNames = new ArrayList<String>();

                for(WardrobeResponse wardrobe:wardrobeResponseList.wardrobes){
                    wardrobeIDs.add(wardrobe.getId());
                    wardrobeNames.add(wardrobe.getName());
                }

                Pair<List<Integer>,List<String>> wardrobes = new Pair<List<Integer>,List<String>>(wardrobeIDs,wardrobeNames);

                setBestSuitsViewPager();

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
        selectWardrobeSpinner.setAdapter(new WardrobeSpinnerAdapter(toolbar.getContext(),wardrobes.second));

        selectWardrobeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                currentWardrobeID = wardrobes.first.get(position);

                freshSuits();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }


    public void freshSuits(){

        //获取位置信息
        Location location = GetLocationUtil.getLocatioon(this,this);

        //刷新最合适的几件穿搭
        SuitService service = RetrofitClient.newService(this,SuitService.class);

        retrofit2.Call<SuitResponseList> call;

        if(location!=null)
            call = service.getAdvices(currentWardrobeID,location.getLatitude(),location.getLongitude());
        else
            call = service.getAdvices(currentWardrobeID,-1D,-1D);

        call.enqueue(new Callback<SuitResponseList>() {
            @Override
            public void onResponse(retrofit2.Call<SuitResponseList> call, Response<SuitResponseList> response) {
                SuitResponseList suitResponseList = response.body();

                List<Pair<Integer,String>> suits = new ArrayList<Pair<Integer,String>>();

                for(SuitResponse suit:suitResponseList.suits)
                    suits.add(new Pair<Integer,String>(suit.getId(),suit.getName()));

                //如果推荐衣物列表为空
                if(suits.isEmpty())
                    suits.add(new Pair<Integer, String>(-1,"没有合适的衣服穿了"));

                updateBestSuits(suits);

                freshWeather(suitResponseList.weather);

                //设置所有穿搭需要先获取天气
                setAllSuits(currentWardrobeID,suitResponseList.weather.temperature);
            }

            @Override
            public void onFailure(retrofit2.Call<SuitResponseList> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"网络连接失败",Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void setBestSuitsViewPager(){
        if(bestSuitsViewPager == null)
            return;

        bestSuitsViewPager.setAdapter(new BestSuitsPagerAdapter(getSupportFragmentManager()));
    }
    private void updateBestSuits(List<Pair<Integer,String>> suits){

        bestSuitNum = suits.size();

        ((BestSuitsPagerAdapter)bestSuitsViewPager.getAdapter()).updateDatas(suits);

    }

    public class BestSuitsPagerAdapter extends FragmentPagerAdapter {

        private FragmentTransaction mCurTransaction = null;

        private List<Pair<Integer,String>> mFragmentPair;

        private FragmentManager mFragmentManager;
        private Fragment mCurrentPrimaryItem = null;

        public BestSuitsPagerAdapter(FragmentManager fm) {
            super(fm);
            this.mFragmentManager = fm;
            this.mFragmentPair = new ArrayList<Pair<Integer,String>>();//mFragmentPair;
        }

        public void updateDatas(List<Pair<Integer,String>> mFragmentPair){
            this.mFragmentPair = mFragmentPair;
            notifyDataSetChanged();
        }

        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            if (this.mCurTransaction == null) {
                this.mCurTransaction = this.mFragmentManager.beginTransaction();
            }

            long itemId = this.getItemId(position);
//            String name = makeFragmentName(container.getId(), itemId);
//            Fragment fragment = this.mFragmentManager.findFragmentByTag(name);
//            if (fragment != null) {
//                this.mCurTransaction.attach(fragment);
//            } else {
                Fragment fragment = this.getItem(position);
                this.mCurTransaction.add(container.getId(), fragment, makeFragmentName(container.getId(), itemId));
//            }

            if (fragment != mCurrentPrimaryItem) {
                fragment.setMenuVisibility(false);
                fragment.setUserVisibleHint(false);
            }

//            container.addView(fragment.getView());

            return fragment;
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = BestSuitFragment.newInstance(mFragmentPair.get(position).first,mFragmentPair.get(position).second);
            return fragment;
        }

        @Override
        public int getCount() {
            return mFragmentPair.size();
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            if (this.mCurTransaction == null) {
                this.mCurTransaction = this.mFragmentManager.beginTransaction();
            }

            container.removeView(((Fragment)object).getView());
        }

        private String makeFragmentName(int viewId, long id) {
            return "android:switcher:" + viewId + ":" + id;
        }

        public void finishUpdate(@NonNull ViewGroup container) {
            if (this.mCurTransaction != null) {
                this.mCurTransaction.commitNowAllowingStateLoss();
                this.mCurTransaction = null;
            }
        }
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

    private void freshWeather(WeatherInfo weather){
        TextView temperatureText = findViewById(R.id.temperatureText);
        TextView windInfoText = findViewById(R.id.windInfoText);
        TextView wearingAdviceText = findViewById(R.id.wearingAdviceText);

        temperatureText.setText(weather.tempStr);
        windInfoText.setText(weather.weather+"  "+weather.wind);
        wearingAdviceText.setText(weather.dressingAdvice);
    }

    //刷新所有穿搭列表
    private void setAllSuits(final Integer wardrobeID, Double temperature){
        SuitService service = RetrofitClient.newService(this,SuitService.class);
        retrofit2.Call<SuitResponseList> call = service.getByWardrobe(wardrobeID,temperature);
        call.enqueue(new Callback<SuitResponseList>() {
            @Override
            public void onResponse(retrofit2.Call<SuitResponseList> call, Response<SuitResponseList> response) {
                SuitResponseList suitResponseList = response.body();

                List<Pair<Integer,String>> suits = new ArrayList<Pair<Integer,String>>();

                for(SuitResponse suit:suitResponseList.suits)
                    suits.add(new Pair<Integer,String>(suit.getId(),suit.getName()));

                allSuitsRecyclerView = (RecyclerView)findViewById(R.id.allSuitsRecyclerView_main);

                allSuitsRecyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()){
                    @Override
                    public boolean canScrollVertically() {
                        return false;
                    }
                });
                WearSuitRecyclerViewAdapter myAdapter = new WearSuitRecyclerViewAdapter(getBaseContext(), Arrays.asList(suitResponseList.suits));
                allSuitsRecyclerView.setAdapter(myAdapter);

                allSuitsRecyclerView.setNestedScrollingEnabled(false);
                allSuitsRecyclerView.setHasFixedSize(true);
            }

            @Override
            public void onFailure(retrofit2.Call<SuitResponseList> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"网络连接失败",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void wearNewSuitButton_Click(View view){
        Intent intent = new Intent(this,ManageClothesActivity.class);
        if(currentWardrobeID!=null)
        intent.putExtra("wardrobeID",currentWardrobeID);
        startActivity(intent);
        this.finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if(id == R.id.nav_clothes){
            Intent intent = new Intent(this,ManageClothesActivity.class);
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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setRandomBestSuit(){
        if(bestSuitNum==null)
            return;

        Random ra =new Random();

        bestSuitsViewPager.setCurrentItem(ra.nextInt(bestSuitNum));
    }


    private boolean isAppInstalled(Context context, String uri) {
        PackageManager pm = context.getPackageManager();
        boolean installed = false;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            installed = false;
        }
        return installed;
    }


    private void pushRecommend(){

        UserService service = RetrofitClient.newService(this,UserService.class);
        retrofit2.Call<TaobaoItemResponse> call = service.getRecommend(User.getInstance().getId());
        call.enqueue(new Callback<TaobaoItemResponse>() {
            @Override
            public void onResponse(Call<TaobaoItemResponse> call, Response<TaobaoItemResponse> response) {
                final TaobaoItemResponse recommendItem = response.body();

                if(recommendItem!=null&&!recommendItem.itemUrl.isEmpty()&&!recommendItem.picUrl.isEmpty()){

                    final OptionMaterialDialog mMaterialDialog = new OptionMaterialDialog(MainActivity.this);
                    final ImageView itemImage=new ImageView(getBaseContext());
                    itemImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(getApplicationContext(),"正在打开你的淘宝",Toast.LENGTH_SHORT).show();

                            String url2 = recommendItem.itemUrl;
                            if (isAppInstalled(getApplicationContext(), "com.taobao.taobao")) {
                                Intent intent2 = getPackageManager().getLaunchIntentForPackage("com.taobao.taobao");
                                intent2.setAction("android.intent.action.VIEW");
                                intent2.setClassName("com.taobao.taobao", "com.taobao.tao.detail.activity.DetailActivity");
                                Uri uri = Uri.parse(url2);
                                intent2.setData(uri);
                                startActivity(intent2);
                            }else
                                Toast.makeText(getApplicationContext(),"你还没有安装淘宝噢",Toast.LENGTH_SHORT).show();
                        }
                    });
                    UrlPic.setImage(itemImage,"http:"+recommendItem.picUrl);
                    mMaterialDialog
                            .setContentView(itemImage)
                            .setTitle("猜你喜欢")
                            .setNegativeButton("不感兴趣",
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

                }
            }

            @Override
            public void onFailure(retrofit2.Call<TaobaoItemResponse> call, Throwable t) {
            }
        });



    }


}
