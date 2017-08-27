package com.example.birthdayreminder;

/**
 * Created by yihan on 2017/8/20.
 */

public class Contact implements java.io.Serializable{

    private long id;
    private String name;
    private String birthday;
    private String email;

    public Contact(){}

    public Contact(String name, String email, String birthday){
        this.name = name;
        this.birthday = birthday;
        this.email = email;
    }

    public Contact(long id, String name, String email, String birthday){
        this.id = id;
        this.name = name;
        this.birthday = birthday;
        this.email = email;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
