package niannian.valet;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
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
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.BitmapCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private List<Pair<Integer,String>> wardrobes;
//    private

    RecyclerView suitRecyclerView;

    private Menu mainDrawer;
    private Spinner selectWardrobeSnipper;
    private Toolbar toolbar;
    private ViewPager bestSuitsViewPager;

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

        mainDrawer = ((NavigationView)findViewById(R.id.nav_view)).getMenu();
        mainDrawer.getItem(0).setChecked(true);

//        suitRecyclerView = (RecyclerView)findViewById(R.id.suitRecyclerView);
//        //设置布局管理器
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
//        suitRecyclerView.setLayoutManager(linearLayoutManager);

        initWardrobeSnipper(new String[]{
                "Wardr 1",
                "Wa 2",
                "WardrobeName123123123 3",
        });

        setBestSuitsViewPager();

        //设置左导航栏抽屉
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void initWardrobeSnipper(String[] wardrobeNames){
        // Setup spinner
        selectWardrobeSnipper = (Spinner) findViewById(R.id.selectWardrobeSpinner);
        selectWardrobeSnipper.setAdapter(new WardrobeSpinnerAdapter(toolbar.getContext(),wardrobeNames));

        selectWardrobeSnipper.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // When the given dropdown item is selected, show its contents in the
                // container view.
//                getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.container, TestActivity.PlaceholderFragment.newInstance(position + 1))
//                        .commit();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void setBestSuitsViewPager(){
        // Setup viewPager
        List<Pair<String,String>> fragments = new ArrayList<Pair<String,String>>();
        fragments.add(new Pair<String,String>("testFragment0","https://i1.hdslb.com/bfs/archive/28091e573523fd7666e36b9f9194d6a1d27a876c.jpg@160w_100h.jpg"));
        fragments.add(new Pair<String,String>("testFragment1","https://i1.hdslb.com/bfs/archive/058e858056ba4fbb008d4337ca47ffcf802217ec.jpg@160w_100h.webp"));

        bestSuitsViewPager = (ViewPager) findViewById(R.id.bestSuitsViewPager);
        bestSuitsViewPager.setAdapter(new BestSuitsPagerAdapter(getSupportFragmentManager(),fragments));
    }
    public class BestSuitsPagerAdapter extends FragmentPagerAdapter {

        private  List<Pair<String,String>> mFragmentPair;
        public BestSuitsPagerAdapter(FragmentManager fm, List<Pair<String,String>> mFragmentPair) {
            super(fm);
            this.mFragmentPair = mFragmentPair;
        }

        @Override
        public Fragment getItem(int position) {
            return FuckFragment.newInstance("hello","hi");//mFragmentPair.get(position).second);
        }

        @Override
        public int getCount() {
            return mFragmentPair.size();
        }

//        @Override
//        public CharSequence getPageTitle(int position) {
//            return  mFragmentPair.get(position).first;
//        }
    }

    public static class BestSuitFragment1 extends Fragment {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
        private static final String URL = "https://i2.hdslb.com/bfs/archive/931203046d1dba2b69c472e9a6ff294e47491609.jpg@160w_100h.jpg";

        //    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;
        private String url;

        private OnFragmentInteractionListener mListener;

        public BestSuitFragment1() {
            // Required empty public constructor
        }

        // TODO: Rename and change types and number of parameters
        public static BestSuitFragment1 newInstance(String _url) {
            BestSuitFragment1 fragment = new BestSuitFragment1();
            Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
            args.putString(URL,_url);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            if (getArguments() != null) {
                url = getArguments().getString(URL);
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
            }
        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            View view = inflater.inflate(R.layout.fragment_best_suit, container, false);
//        setImage((ImageView)view.findViewById(R.id.testImage));
            return view;
        }

//        private void setImage(ImageView image)
//        {
//            OkHttpUtils.get().url(url).tag(image)
//                    .build()
//                    .connTimeOut(20000).readTimeOut(20000).writeTimeOut(20000)
//                    .execute(new BitmapCallback() {
//                        @Override
//                        public void onError(Call call, Exception e) {
//
//                        }
//
//                        @Override
//                        public void onResponse(Call call, Bitmap bitmap) {
//                            ImageView image = (ImageView)call.request().tag();
//                            image.setImageBitmap(bitmap);
//                        }
//                    });
//        }


        // TODO: Rename method, update argument and hook method into UI event
        public void onButtonPressed(Uri uri) {
            if (mListener != null) {
                mListener.onFragmentInteraction(uri);
            }
        }

        @Override
        public void onAttach(Context context) {
            super.onAttach(context);
            if (context instanceof niannian.valet.BestSuitFragment.OnFragmentInteractionListener) {
                mListener = (OnFragmentInteractionListener) context;
            } else {
                throw new RuntimeException(context.toString()
                        + " must implement OnFragmentInteractionListener");
            }
        }

        @Override
        public void onDetach() {
            super.onDetach();
            mListener = null;
        }

        /**
         * This interface must be implemented by activities that contain this
         * fragment to allow an interaction in this fragment to be communicated
         * to the activity and potentially other fragments contained in that
         * activity.
         * <p>
         * See the Android Training lesson <a href=
         * "http://developer.android.com/training/basics/fragments/communicating.html"
         * >Communicating with Other Fragments</a> for more information.
         */
        public interface OnFragmentInteractionListener {
            // TODO: Update argument type and name
            void onFragmentInteraction(Uri uri);
        }
    }

    private class WardrobeSpinnerAdapter extends ArrayAdapter<String> implements ThemedSpinnerAdapter {
        private final ThemedSpinnerAdapter.Helper mDropDownHelper;

        public WardrobeSpinnerAdapter(Context context, String[] objects) {
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

    private List<Pair<Integer,String>> getWardrobesAndWeather(int userID){


return null;
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
}
