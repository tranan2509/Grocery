package Model;

import android.database.Cursor;

import java.io.Serializable;

public class Voucher implements Serializable {
    private int id;
    private String name;
    private int discount;
    private int condition;
    private String startDate;
    private String endDate;
    private boolean state;

    public Voucher() {
    }

    public Voucher(final String name, final int discount, final int condition, final String startDate, final String endDate) {
        this.name = name;
        this.discount = discount;
        this.condition = condition;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Voucher(final String name, final int discount, final int condition, final String startDate, final String endDate, final boolean state) {
        this.name = name;
        this.discount = discount;
        this.condition = condition;
        this.startDate = startDate;
        this.endDate = endDate;
        this.state = state;
    }

    public Voucher(Cursor cursor){
        id = cursor.getInt(cursor.getColumnIndex("id"));
        name = cursor.getString(cursor.getColumnIndex("name"));
        discount = cursor.getInt(cursor.getColumnIndex("discount"));
        condition = cursor.getInt(cursor.getColumnIndex("condition"));
        startDate = cursor.getString(cursor.getColumnIndex("startDate"));
        endDate = cursor.getString(cursor.getColumnIndex("endDate"));
        state = cursor.getString(cursor.getColumnIndex("state")).equals("1");
    }

    public int getId() {
        return this.id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public int getDiscount() {
        return this.discount;
    }

    public void setDiscount(final int discount) {
        this.discount = discount;
    }

    public int getCondition() {
        return this.condition;
    }

    public void setCondition(final int condition) {
        this.condition = condition;
    }

    public String getStartDate() {
        return this.startDate;
    }

    public void setStartDate(final String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return this.endDate;
    }

    public void setEndDate(final String endDate) {
        this.endDate = endDate;
    }

    public boolean isState() {
        return this.state;
    }

    public void setState(final boolean state) {
        this.state = state;
    }
}
