package niannian.valet.ResponseModel;

public class WardrobeResponse {
    //衣橱ID
    public final Integer id;

    //衣橱名称
    public final String name;

    public Integer getId(){
        return id;
    }

    public String getName(){
        return name;
    }
    public WardrobeResponse(Integer id,String name){
        this.id=id;
        this.name=name;

    }

}
