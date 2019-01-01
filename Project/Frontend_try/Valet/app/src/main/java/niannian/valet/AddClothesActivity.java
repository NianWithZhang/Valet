package niannian.valet;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ThemedSpinnerAdapter;
import android.support.v7.widget.Toolbar;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.longsh.optionframelibrary.OptionCenterDialog;

import java.net.Inet4Address;
import java.util.ArrayList;
import java.util.List;

public class AddClothesActivity extends AppCompatActivity {

    private Spinner selectWardrobeSnipper;
    private List<String> clothesType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_clothes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });


        clothesType=new ArrayList<>();
        clothesType.add( 0,"帽子");
        clothesType.add( 1,"外套");
        clothesType.add( 2,"内衫");
        clothesType.add( 3,"裤裙");
        clothesType.add( 4,"袜子");
        clothesType.add( 5,"鞋子");


        initAddClothesTypeSnipper(clothesType);
    }

    public void chooseAddClothesImageMethod(View view){
        final ArrayList<String> list = new ArrayList<>();
        list.add("从相册添加图片");
        list.add("相机拍照添加图片");
        final OptionCenterDialog optionCenterDialog = new OptionCenterDialog();
        optionCenterDialog.show(AddClothesActivity.this, list);
        optionCenterDialog.setItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                optionCenterDialog.dismiss();
                Toast.makeText(AddClothesActivity.this,String.valueOf(position),Toast.LENGTH_SHORT).show();
                switch(position) {
                    case 0:
                        //callcamere
                        break;
                    case 1:
                        //callcamere
                        break;

                }
            }
        });
    }


    private void initAddClothesTypeSnipper(final List<String> clothesType){

        // Setup spinner
        selectWardrobeSnipper = (Spinner) findViewById(R.id.chooseClothesTypeSpinner);
        selectWardrobeSnipper.setAdapter(new AddClothesActivity.addClothesTypeSpinnerAdapter(this,clothesType));

        selectWardrobeSnipper.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // When the given dropdown item is selected, show its contents in the
                // container view.
//                getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.container, TestActivity.PlaceholderFragment.newInstance(position + 1))
//                        .commit();

//                Toast.makeText(view.getContext(),"snipperItemSelected",Toast.LENGTH_SHORT).show();

                //freshClothes(wardrobes.first.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
    private class addClothesTypeSpinnerAdapter extends ArrayAdapter<String> implements ThemedSpinnerAdapter {
        private final ThemedSpinnerAdapter.Helper mDropDownHelper;

        public addClothesTypeSpinnerAdapter(Context context, List<String> objects) {
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

    //用户添加完成衣物信息
    public void addClothesCompleteButtonClick(View view){
        Integer wardrobeId=0;

        Integer type=selectWardrobeSnipper.getSelectedItemPosition();

        EditText clothesName=findViewById(R.id.addClothesNameEditText);
        String name=clothesName.getText().toString();


        SeekBar thicknessBar=findViewById(R.id.addClothesThicknessSeekBar);
        Integer thickness=thicknessBar.getProgress();

        addClothes(wardrobeId,name,type,thickness);
        Toast.makeText(AddClothesActivity.this,String.valueOf(type)+" "+name+" "+String.valueOf(thickness),Toast.LENGTH_SHORT).show();

    }
    //执行添加衣物操作
    private boolean addClothes(Integer wardrobeId,String name,Integer type,Integer thickness){
        return true;
    }

    public void goBackPageButtonClick(View view){
        this.finish();
    }

}
