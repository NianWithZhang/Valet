package niannian.valet.UserInfo;

import android.util.Pair;

public class User {

    public String id;
    public String password;

    private static final User instance = new User();

    //私有的构造函数 避免被实例化
    private User(){}

    public static User getInstance(){
        return instance;
    }

    public void setUserInfo(String _id,String _password){
        id = _id;
        password = _password;
    }

    public Pair<String,String> getUserInfo(){
        if(id == null|| password == null)
            return null;

        return new Pair<>(id,password);
    }

    public String getId(){
        return id;
    }

    public void resetUser(){
        id = null;
        password = null;
    }
}

