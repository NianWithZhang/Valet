package niannian.valet;

import android.content.Context;
import android.content.res.Resources;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import niannian.valet.HttpService.RetrofitClient;
import niannian.valet.HttpService.SuitService;
import niannian.valet.HttpService.WardrobeService;
import niannian.valet.ResponseModel.SuitResponse;
import niannian.valet.ResponseModel.SuitResponseList;
import niannian.valet.ResponseModel.WardrobeResponse;
import niannian.valet.ResponseModel.WardrobeResponseList;
import niannian.valet.ResponseModel.WeatherInfo;
import niannian.valet.Utils.GetLocationUtil;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

//    public WeatherInfo weather;

    private Menu mainDrawer;
    private Spinner selectWardrobeSnipper;
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

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

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

            int medumValue = 19;
            if(Math.abs(x) > medumValue || Math.abs(y) > medumValue || Math.abs(z) > medumValue){

                Toast.makeText(MainActivity.this,String.valueOf(x)+" "+String.valueOf(y)+" "+String.valueOf(z),Toast.LENGTH_SHORT).show();
                vibrator.vibrate(200);

            }


        }
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }

    };

    @Override
    protected void onPostResume() {
        super.onPostResume();

        freshSuits();
//        initWardrobes();
    }

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
        selectWardrobeSnipper = (Spinner) findViewById(R.id.selectWardrobeSpinner);
        selectWardrobeSnipper.setAdapter(new WardrobeSpinnerAdapter(toolbar.getContext(),wardrobes.second));

        selectWardrobeSnipper.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // When the given dropdown item is selected, show its contents in the
                // container view.
//                getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.container, TestActivity.PlaceholderFragment.newInstance(position + 1))
//                        .commit();

//                Toast.makeText(view.getContext(),"snipperItemSelected",Toast.LENGTH_SHORT).show();

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
        retrofit2.Call<SuitResponseList> call = service.getAdvices(currentWardrobeID,location.getLatitude(),location.getLongitude());
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
        bestSuitsViewPager = (ViewPager) findViewById(R.id.bestSuitsViewPager);
        bestSuitsViewPager.setAdapter(new BestSuitsPagerAdapter(getSupportFragmentManager()));
        bestSuitsViewPager.setOffscreenPageLimit(2);
    }
    private void updateBestSuits(List<Pair<Integer,String>> suits){
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
            //对还没有OnCreateView的fragment进行getView会崩
//            CardView bestSuitCard = fragment.getView().findViewById(R.id.bestSuitCard);
//            bestSuitCard.setOnClickListener(new BestSuitSelectListener(getBaseContext()));
            return fragment;
        }

        @Override
        public int getCount() {
            return mFragmentPair.size();
        }

//        @Override
//        public boolean isViewFromObject(View view,Object object){
//            return ((Fragment)object).getView() == view;
////            BestSuitFragment suit = (BestSuitFragment)object;
////
////            CharSequence temp = view.findViewById(R.id.bestSuitImage).getContentDescription();
////
////            return suit.id.toString().equals(view.findViewById(R.id.bestSuitImage).getContentDescription());
//        }


        @Override
        public int getItemPosition(Object object) {
//            mCurTransaction.detach((Fragment) object);
            return POSITION_NONE;
        }
//        @Override
//        public CharSequence getPageTitle(int position) {
//            return  mFragmentPair.get(position).first;
//        }

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

//        public void setPrimaryItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
//            Fragment fragment = (Fragment)object;
//            if (fragment != this.mCurrentPrimaryItem) {
//                if (this.mCurrentPrimaryItem != null) {
//                    this.mCurrentPrimaryItem.setMenuVisibility(false);
//                    this.mCurrentPrimaryItem.setUserVisibleHint(false);
//                }
//
//                fragment.setMenuVisibility(true);
//                fragment.setUserVisibleHint(true);
//                this.mCurrentPrimaryItem = fragment;
//            }
//        }

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

//                StaggeredGridLayoutManager sgm=
//                        new StaggeredGridLayoutManager(1,
//                                StaggeredGridLayoutManager.VERTICAL);
                allSuitsRecyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()){
                    @Override
                    public boolean canScrollVertically() {
                        //解决ScrollView里存在多个RecyclerView时滑动卡顿的问题
                        //如果你的RecyclerView是水平滑动的话可以重写canScrollHorizontally方法
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
        Toast.makeText(this,"选择新穿搭",Toast.LENGTH_SHORT).show();
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

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

//    public class BestSuitSelectListener implements View.OnClickListener{
////        private Context context;
//
////        BestSuitSelectListener(Context context){
////            this.context = context;
////        }
//
//        public void onClick(View view){
//            Integer id = Integer.valueOf(((ImageView)view.findViewById(R.id.bestSuitImage)).getContentDescription().toString());
//
//            Toast.makeText(getBaseContext(),"id = "+id.toString(),Toast.LENGTH_SHORT);
//        }
//    }
}
