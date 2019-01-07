package niannian.valet.ResponseModel;

import android.content.Context;

import niannian.valet.R;

public class SuitResponse extends UrlPic {

    //穿搭编号
    public Integer id;

    //穿搭名称
    public String name;

    //穿搭评价
    public String evaluation;

    //获取穿搭的图片路径
    public String url(Context context){return context.getString(R.string.server_url)+"/suitpics/"+String.valueOf(id)+".jpg";}

    //获取穿搭的图片路径
    public static String url(Context context, Integer _id){return context.getString(R.string.server_url)+"/suitpics/"+String.valueOf(_id)+".jpg";}

    public Integer getId(){return id;}

    public String getName(){return name;}
}
