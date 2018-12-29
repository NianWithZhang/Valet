package niannian.valet.ResponseModel;

import niannian.valet.R;


public class Clothes implements UrlPic {

    public int id;

    //衣物名称
    public String name;

    //衣物类型
    public int type;

    //获取衣物的图片路径
    public String url(){return R.string.server_url+"\\clothespic\\"+String.valueOf(id)+".jpg";}
}
