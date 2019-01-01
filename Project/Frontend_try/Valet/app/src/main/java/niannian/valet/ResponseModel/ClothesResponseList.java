package niannian.valet.ResponseModel;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ClothesResponseList {
    public ClothesResponse[] clothes;

    public ClothesResponseList(){
        int id=1;
        String name="abc";

        clothes = new ClothesResponse[10];
//        ClothesResponse first=new ClothesResponse(id,name,type);

        for(int i=0;i<10;i++)
            clothes[i]=new ClothesResponse(i,name+String.valueOf(i),1);

    }
}
