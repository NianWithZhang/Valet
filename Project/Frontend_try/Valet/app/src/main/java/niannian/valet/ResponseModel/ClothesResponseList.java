package niannian.valet.ResponseModel;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ClothesResponseList {
    public ClothesResponse[] clothes;
    public ClothesResponseList(){
        int id=1;
        String name="abc";
        int type=1;

        clothes = new ClothesResponse[10];
        ClothesResponse first=new ClothesResponse(id,name,type);
        clothes[0]=first;
    }
}
