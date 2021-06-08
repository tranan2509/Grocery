package Model;

import android.database.Cursor;

import java.io.Serializable;

public class Review implements Serializable {
    private int id;
    private String customerId;
    private int productId;
    private int rate;
    private String description;
    private String date;
    private boolean state;

    public Review() {
    }

    public Review(final String customerId, final int productId, final int rate, final String description, final String date) {
        this.customerId = customerId;
        this.productId = productId;
        this.rate = rate;
        this.description = description;
        this.date = date;
        this.state = true;
    }

    public Review(final String customerId, final int productId, final int rate, final String description, final String date, final boolean state) {
        this.customerId = customerId;
        this.productId = productId;
        this.rate = rate;
        this.description = description;
        this.date = date;
        this.state = state;
    }

    public Review(Cursor cursor){
        id = cursor.getInt(cursor.getColumnIndex("id"));
        customerId = cursor.getString(cursor.getColumnIndex("customerId"));
        productId = cursor.getInt(cursor.getColumnIndex("productId"));
        rate = cursor.getInt(cursor.getColumnIndex("rate"));
        description = cursor.getString(cursor.getColumnIndex("description"));
        date = cursor.getString(cursor.getColumnIndex("date"));
        state = cursor.getString(cursor.getColumnIndex("state")).equals("1");
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

    public int getProductId() {
        return this.productId;
    }

    public void setProductId(final int productId) {
        this.productId = productId;
    }

    public int getRate() {
        return this.rate;
    }

    public void setRate(final int rate) {
        this.rate = rate;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(final String date) {
        this.date = date;
    }

    public boolean isState() {
        return this.state;
    }

    public void setState(final boolean state) {
        this.state = state;
    }
}
