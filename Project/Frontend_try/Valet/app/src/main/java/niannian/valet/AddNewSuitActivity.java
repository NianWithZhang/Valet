package niannian.valet;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Toast;

import com.longsh.optionframelibrary.OptionCenterDialog;

import java.util.ArrayList;
import java.util.List;

public class AddNewSuitActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_suit);
    }
    public void chooseAddSuitImageMethod(View view){
        final ArrayList<String> list = new ArrayList<>();
        list.add("从相册添加图片");
        list.add("相机拍照添加图片");
        final OptionCenterDialog optionCenterDialog = new OptionCenterDialog();
        optionCenterDialog.show(AddNewSuitActivity.this, list);
        optionCenterDialog.setItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                optionCenterDialog.dismiss();
                Toast.makeText(AddNewSuitActivity.this,String.valueOf(position),Toast.LENGTH_SHORT).show();
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

    public void addSuitCompleteButtonClick(View view){
        EditText suitName=findViewById(R.id.addSuitNameEditText);
        String name=suitName.getText().toString();
        addSuit(name,ManageClothesActivity.selectedIdList);
    }

    private boolean addSuit(String name,ArrayList<Integer> clothesId){
        return true;
    }
    public void goBackPageButtonClick(View view){
        this.finish();
    }
}
