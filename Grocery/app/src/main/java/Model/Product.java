package Model;

import android.database.Cursor;

import java.io.Serializable;

public class Product implements Serializable {
    private int id;
    private int categoryId;
    private String name;
    private String image;
    private String importDate;
    private double importPrice;
    private double price;
    private String unit;
    private int discount;
    private int quantity;
    private String description;
    private double rate;
    private int reviewers;

    public Product() {
    }

    public Product(final int categoryId, final String name, final String image, final String importDate, final double importPrice, final double price, final int discount, final int quantity, final String description, final double rate, final int reviewers) {
        this.categoryId = categoryId;
        this.name = name;
        this.image = image;
        this.importDate = importDate;
        this.importPrice = importPrice;
        this.price = price;
        this.unit = "USA";
        this.discount = discount;
        this.quantity = quantity;
        this.description = description;
        this.rate = rate;
        this.reviewers = reviewers;
    }

    public Product(final int id, final int categoryId, final String name, final String image, final String importDate, final double importPrice, final double price, final int discount, final int quantity, final String description, final double rate, final int reviewers) {
        this.id = id;
        this.categoryId = categoryId;
        this.name = name;
        this.image = image;
        this.importDate = importDate;
        this.importPrice = importPrice;
        this.unit = "USA";
        this.price = price;
        this.discount = discount;
        this.quantity = quantity;
        this.description = description;
        this.rate = rate;
        this.reviewers = reviewers;
    }

    public Product(final int id, final int categoryId, final String name, final String image, final String importDate, final double importPrice, final double price, final String unit, final int discount, final int quantity, final String description, final double rate, final int reviewers) {
        this.id = id;
        this.categoryId = categoryId;
        this.name = name;
        this.image = image;
        this.importDate = importDate;
        this.importPrice = importPrice;
        this.price = price;
        this.unit = unit;
        this.discount = discount;
        this.quantity = quantity;
        this.description = description;
        this.rate = rate;
        this.reviewers = reviewers;
    }

    public Product(final int categoryId, final String name, final String image, final String importDate, final double importPrice, final double price, final String unit, final int discount, final int quantity, final String description) {
        this.categoryId = categoryId;
        this.name = name;
        this.image = image;
        this.importDate = importDate;
        this.importPrice = importPrice;
        this.price = price;
        this.unit = unit;
        this.discount = discount;
        this.quantity = quantity;
        this.description = description;
        this.rate = 0;
        this.reviewers = 0;
    }



    public Product(Cursor cursor){
        System.out.println(cursor.getCount() + "=======================");
        this.id = cursor.getInt(cursor.getColumnIndex("id"));
        this.categoryId = cursor.getInt(cursor.getColumnIndex("categoryId"));
        this.name = cursor.getString(cursor.getColumnIndex("name"));
        this.image = cursor.getString(cursor.getColumnIndex("image"));
        this.importDate = cursor.getString(cursor.getColumnIndex("importDate"));
        this.importPrice = cursor.getDouble(cursor.getColumnIndex("importPrice"));
        this.price = cursor.getDouble(cursor.getColumnIndex("price"));
        this.unit = cursor.getString(cursor.getColumnIndex("unit"));
        this.discount = cursor.getInt(cursor.getColumnIndex("discount"));
        this.quantity = cursor.getInt(cursor.getColumnIndex("quantity"));
        this.description = cursor.getString(cursor.getColumnIndex("description"));
        this.rate = cursor.getDouble(cursor.getColumnIndex("rate"));
        this.reviewers = cursor.getInt(cursor.getColumnIndex("reviewers"));
    }

    public String getUnit() {
        return this.unit;
    }

    public void setUnit(final String unit) {
        this.unit = unit;
    }

    public int getId() {
        return this.id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public int getCategoryId() {
        return this.categoryId;
    }

    public void setCategoryId(final int categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getImage() {
        return this.image;
    }

    public void setImage(final String image) {
        this.image = image;
    }

    public String getImportDate() {
        return this.importDate;
    }

    public void setImportDate(final String importDate) {
        this.importDate = importDate;
    }

    public double getImportPrice() {
        return this.importPrice;
    }

    public void setImportPrice(final double importPrice) {
        this.importPrice = importPrice;
    }

    public double getPrice() {
        return this.price;
    }

    public void setPrice(final double price) {
        this.price = price;
    }

    public int getDiscount() {
        return this.discount;
    }

    public void setDiscount(final int discount) {
        this.discount = discount;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public void setQuantity(final int quantity) {
        this.quantity = quantity;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public double getRate() {
        return this.rate;
    }

    public void setRate(final double rate) {
        this.rate = rate;
    }

    public int getReviewers() {
        return this.reviewers;
    }

    public void setReviewers(final int reviewers) {
        this.reviewers = reviewers;
    }
}
