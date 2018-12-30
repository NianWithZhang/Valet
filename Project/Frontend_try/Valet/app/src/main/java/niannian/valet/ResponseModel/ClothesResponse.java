package niannian.valet.ResponseModel;

import android.widget.Switch;

import niannian.valet.R;


public class ClothesResponse implements UrlPic {

    public int id;

    //衣物名称
    public String name;

    //衣物类型
    public int type;

    //获取衣物的图片路径
    public String url(){return R.string.server_url+"\\clothespics\\"+String.valueOf(id)+".jpg";}

    //获取衣物类型名称
    public String getTypeName(){
        switch(type){
            case 0:return "帽子";
            case 1:return "外套";
            case 2:return "内衫";
            case 3:return "裤裙";
            case 4:return "袜子";
            case 5:return "鞋子";
        }
        return null;
    }
}
