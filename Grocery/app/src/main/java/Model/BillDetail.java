package Model;

import android.database.Cursor;

import java.io.Serializable;

public class BillDetail implements Serializable {
    private int id;
    private int billId;
    private int productId;
    private int quantity;
    private double price;
    private double amount;

    public BillDetail() {
    }

    public BillDetail(final int billId, final int productId, final int quantity, final double price, final double amount) {
        this.billId = billId;
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
        this.amount = amount;
    }

    public BillDetail(final int id, final int billId, final int productId, final int quantity, final double price, final double amount) {
        this.id = id;
        this.billId = billId;
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
        this.amount = amount;
    }

    public BillDetail(Cursor cursor){
        id = cursor.getInt(cursor.getColumnIndex("id"));
        billId = cursor.getInt(cursor.getColumnIndex("billId"));
        productId = cursor.getInt(cursor.getColumnIndex("productId"));
        quantity = cursor.getInt(cursor.getColumnIndex("quantity"));
        price = cursor.getDouble(cursor.getColumnIndex("price"));
        amount = cursor.getDouble(cursor.getColumnIndex("amount"));
    }

    public int getId() {
        return this.id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public int getBillId() {
        return this.billId;
    }

    public void setBillId(final int billId) {
        this.billId = billId;
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

    public double getPrice() {
        return this.price;
    }

    public void setPrice(final double price) {
        this.price = price;
    }

    public double getAmount() {
        return this.amount;
    }

    public void setAmount(final double amount) {
        this.amount = amount;
    }
}
