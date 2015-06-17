package pl.tkomp.addplaces;

/**
 * Created by dryja.staz on 2015-06-16.
 */
public class User {
    private String pass, login;
    private int user_id;
    private static User instance;

    User() {}

    User(String a, String b, int c) {
        pass=a;
        login=b;
        user_id = c;
    }
    public static synchronized User getInstance(){
        if(instance==null){
            instance=new User();
        }
        return instance;
    }

    public int getId() {
        return user_id;
    }

    public String getPass() {
        return pass;
    }

    public String getLogin() {
        return login;
    }

    public void setId(int i) {
        user_id = i;
    }

    public void setPass(String i) {pass = i; }

    public void setLogin(String i) { login = i; }
}