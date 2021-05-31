package Model;

import android.database.Cursor;

import java.io.Serializable;

public class Bill implements Serializable {
    private int id;
    private String customerId;
    private int payment;
    private String date;
    private int voucherId;
    private double amount;
    private String state;

    public Bill() {
    }

    public Bill(final String customerId, final int payment, final String date, final int voucherId, final double amount, final String state) {
        this.customerId = customerId;
        this.payment = payment;
        this.date = date;
        this.voucherId = voucherId;
        this.amount = amount;
        this.state = state;
    }

    public Bill(Cursor cursor){
        id = cursor.getInt(cursor.getColumnIndex("id"));
        customerId = cursor.getString(cursor.getColumnIndex("customerId"));
        payment = cursor.getInt(cursor.getColumnIndex("payment"));
        date = cursor.getString(cursor.getColumnIndex("date"));
        voucherId = cursor.getInt(cursor.getColumnIndex("voucherId"));
        amount = cursor.getDouble(cursor.getColumnIndex("amount"));
        state = cursor.getString(cursor.getColumnIndex("state"));
    }

    public int getId() {
        return this.id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public String getCustomerId() {
        return this.customerId;
    }

    public void setCustomerId(final String customerId) {
        this.customerId = customerId;
    }

    public int getPayment() {
        return this.payment;
    }

    public void setPayment(final int payment) {
        this.payment = payment;
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(final String date) {
        this.date = date;
    }

    public int getVoucherId() {
        return this.voucherId;
    }

    public void setVoucherId(final int voucherId) {
        this.voucherId = voucherId;
    }

    public double getAmount() {
        return this.amount;
    }

    public void setAmount(final double amount) {
        this.amount = amount;
    }

    public String getState() {
        return this.state;
    }

    public void setState(final String state) {
        this.state = state;
    }
}
