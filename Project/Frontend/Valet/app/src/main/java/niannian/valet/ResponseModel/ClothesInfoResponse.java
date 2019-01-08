package niannian.valet.ResponseModel;

import java.sql.Date;
import java.sql.Time;

public class ClothesInfoResponse {
    //衣物ID
    public Integer id;

    //衣物名称
    public String name;

    //衣物类型
    public Integer type;

    //衣物厚度
    public Integer thickness;

    //衣物的最后穿着时间
    public Integer lastWearingTime;

    //衣物的穿着次数
    public Integer wearingFrequency;

}
