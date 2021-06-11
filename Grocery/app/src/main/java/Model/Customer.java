package Model;


import android.database.Cursor;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Customer implements Serializable {
    private String id;
    private String accountId;
    private String name;
    private String avatar;
    private String phone;
    private String address;
    private String dob;
    private boolean gender;
    private int point;

    public Customer() {
    }

    public Customer(final String id, final String accountId, final String name, final String avatar, final String phone, final String address, final String dob, final boolean gender) {
        this.id = id;
        this.accountId = accountId;
        this.name = name;
        this.avatar = avatar;
        this.phone = phone;
        this.address = address;
        this.dob = dob;
        this.gender = gender;
        this.point = 0;
    }

    public Customer(Cursor cursor){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        this.id = cursor.getString(cursor.getColumnIndex("id"));
        this.accountId =  cursor.getString(cursor.getColumnIndex("accountId"));;
        this.name =  cursor.getString(cursor.getColumnIndex("name"));
        this.avatar =  cursor.getString(cursor.getColumnIndex("avatar"));
        this.phone =  cursor.getString(cursor.getColumnIndex("phone"));
        this.address =  cursor.getString(cursor.getColumnIndex("address"));
        this.dob = cursor.getString(cursor.getColumnIndex("dob"));
        this.gender = cursor.getString(cursor.getColumnIndex("gender")).equals("1");
        this.point = cursor.getInt(cursor.getColumnIndex("point"));
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

    public String getDob() {
        return this.dob;
    }

    public void setDob(final String dob) {
        this.dob = dob;
    }

    public boolean isGender() {
        return this.gender;
    }

    public void setGender(final boolean gender) {
        this.gender = gender;
    }

    public int getPoint() {
        return this.point;
    }

    public void setPoint(final int point) {
        this.point = point;
    }
}
