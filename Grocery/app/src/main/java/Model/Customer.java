package Model;


import android.database.Cursor;

import java.io.Serializable;
import java.util.Date;

public class Customer implements Serializable {
    private String id;
    private String accountId;
    private String name;
    private String avatar;
    private String phone;
    private String address;
    private Date dob;
    private boolean gender;

    public Customer() {
    }

    public Customer(final String id, final String accountId, final String name, final String avatar, final String phone, final String address, final Date dob, final boolean gender) {
        this.id = id;
        this.accountId = accountId;
        this.name = name;
        this.avatar = avatar;
        this.phone = phone;
        this.address = address;
        this.dob = dob;
        this.gender = gender;
    }

    public Customer(Cursor cursor){
        this.id = String.valueOf(cursor.getColumnIndex("id"));
        this.accountId =  String.valueOf(cursor.getColumnIndex("accountId"));;
        this.name =  String.valueOf(cursor.getColumnIndex("name"));
        this.avatar =  String.valueOf(cursor.getColumnIndex("avatar"));
        this.phone =  String.valueOf(cursor.getColumnIndex("phone"));
        this.address =  String.valueOf(cursor.getColumnIndex("address"));
        this.dob =  new Date(cursor.getColumnIndex("date"));;
        this.gender = String.valueOf(cursor.getColumnIndex("gender")) == "1";
    }

    public String getId() {
        return this.id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getAccountId() {
        return this.accountId;
    }

    public void setAccountId(final String accountId) {
        this.accountId = accountId;
    }

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getAvatar() {
        return this.avatar;
    }

    public void setAvatar(final String avatar) {
        this.avatar = avatar;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(final String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(final String address) {
        this.address = address;
    }

    public Date getDob() {
        return this.dob;
    }

    public void setDob(final Date dob) {
        this.dob = dob;
    }

    public boolean isGender() {
        return this.gender;
    }

    public void setGender(final boolean gender) {
        this.gender = gender;
    }
}
