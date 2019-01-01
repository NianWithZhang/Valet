package niannian.valet.ResponseModel;

import android.content.Context;
import android.widget.Switch;

import java.net.Inet4Address;

import niannian.valet.R;


public class ClothesResponse extends UrlPic {

    public Integer id;

    //衣物名称
    public String name;

    //衣物类型
    public Integer type;

    //获取衣物的图片路径
    public String url(Context context){return context.getString(R.string.server_url)+"/clothespics/"+String.valueOf(id)+".jpg";}

    public ClothesResponse(int id,String name,int type){
        this.id=id;
        this.name=name;
        this.type=type;
    }
    //获取衣物的图片路径
    public static String url(Context context,Integer _id){return context.getString(R.string.server_url)+"/clothespics/"+String.valueOf(_id)+".jpg";}

    //构造函数 初始化成员变量
    public ClothesResponse(Integer _id,String _name,Integer _type){
        id = _id;
        name = _name;
        type = _type;
    }

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
