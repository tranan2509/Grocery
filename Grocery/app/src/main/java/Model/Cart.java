package Model;

import android.database.Cursor;

import java.io.Serializable;

public class Cart implements Serializable {

    private int id;
    private String customerId;
    private int productId;
    private int quantity;
    private double amount;
    private boolean state;

    public Cart() {
    }

    public Cart(final String customerId, final int productId, final int quantity, final double amount, final boolean state) {
        this.customerId = customerId;
        this.productId = productId;
        this.quantity = quantity;
        this.amount = amount;
        this.state = state;
    }

    public Cart(final String customerId, final int productId, final int quantity, final double amount) {
        this.customerId = customerId;
        this.productId = productId;
        this.quantity = quantity;
        this.amount = amount;
        this.state = true;
    }

    public Cart(Cursor cursor){

        this.id = cursor.getInt(cursor.getColumnIndex("id"));
        this.customerId =  cursor.getString(cursor.getColumnIndex("customerId"));;
        this.productId =  cursor.getInt(cursor.getColumnIndex("productId"));
        this.quantity =  cursor.getInt(cursor.getColumnIndex("quantity"));
        this.amount =  cursor.getDouble(cursor.getColumnIndex("amount"));
        this.state =  cursor.getString(cursor.getColumnIndex("state")).equals("1");
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

    public int getQuantity() {
        return this.quantity;
    }

    public void setQuantity(final int quantity) {
        this.quantity = quantity;
    }

    public double getAmount() {
        return this.amount;
    }

    public void setAmount(final double amount) {
        this.amount = amount;
    }

    public boolean isState() {
        return this.state;
    }

    public void setState(final boolean state) {
        this.state = state;
    }
}
