package DBUtil;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import Model.Product;

public class ProductDB extends DatabaseHandler{
    private static final String TABLE_PRODUCT = "PRODUCT";
    private static final String KEY_ID = "id";
    private static final String KEY_CATEGORY_ID = "categoryId";
    private static final String KEY_NAME = "name";
    private static final String KEY_IMAGE = "image";
    private static final String KEY_IMPORT_DATE = "importDate";
    private static final String KEY_IMPORT_PRICE = "importPrice";
    private static final String KEY_PRICE = "price";
    private static final String KEY_DISCOUNT = "discount";
    private static final String KEY_QUANTITY = "quantity";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_RATE = "rate";
    private static final String KEY_REVIEWERS = "reviewers";

    public ProductDB(Context context) {
        super(context);
    }

    public void add(Product product){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_CATEGORY_ID, product.getCategoryId());
        values.put(KEY_NAME, product.getName());
        values.put(KEY_IMAGE, product.getImage());
        values.put(KEY_IMPORT_DATE, product.getImportDate());
        values.put(KEY_IMPORT_PRICE, product.getImportPrice());
        values.put(KEY_PRICE, product.getPrice());
        values.put(KEY_DISCOUNT, product.getDiscount());
        values.put(KEY_QUANTITY, product.getQuantity());
        values.put(KEY_DESCRIPTION, product.getDescription());
        values.put(KEY_RATE, product.getRate());
        values.put(KEY_REVIEWERS, product.getReviewers());
        db.insert(TABLE_PRODUCT, null, values);
        db.close();
    }

    public Product get(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PRODUCT, new String[]{ KEY_ID, KEY_CATEGORY_ID, KEY_NAME, KEY_IMAGE, KEY_IMPORT_DATE, KEY_IMPORT_PRICE, KEY_PRICE, KEY_DISCOUNT,
                        KEY_QUANTITY, KEY_DESCRIPTION, KEY_RATE, KEY_REVIEWERS},
                KEY_ID + "=?", new String[]{ String.valueOf(id) }, null, null, null, null);

        if (cursor != null && cursor.getCount() > 0)
            cursor.moveToFirst();
        else
            return null;
        return new Product(cursor);
    }

    public List<Product> get(){
        List<Product> products = new ArrayList<Product>();
        String query = "SELECT * FROM " + TABLE_PRODUCT;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
//        if (cursor == null || cursor.getCount() < 1)
//            return null;
        if (cursor.moveToFirst()){
            do{
                Product product = new Product(cursor);
                products.add(product);
            }while (cursor.moveToNext());
        }
        return products;
    }

    public int update(Product product){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_CATEGORY_ID, product.getCategoryId());
        values.put(KEY_NAME, product.getName());
        values.put(KEY_IMAGE, product.getImage());
        values.put(KEY_IMPORT_DATE, product.getImportDate());
        values.put(KEY_IMPORT_PRICE, product.getImportPrice());
        values.put(KEY_PRICE, product.getPrice());
        values.put(KEY_DISCOUNT, product.getDiscount());
        values.put(KEY_QUANTITY, product.getQuantity());
        values.put(KEY_DESCRIPTION, product.getDescription());
        values.put(KEY_RATE, product.getRate());
        values.put(KEY_REVIEWERS, product.getReviewers());
        return db.update(TABLE_PRODUCT, values, KEY_ID + "=?", new String[]{String.valueOf(product.getId())});
    }

    public void delete(Product product){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PRODUCT, KEY_ID + "=?", new String[]{String.valueOf(product.getId())});
        db.close();
    }

    public void delete(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PRODUCT, KEY_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
    }

    public int getCount(){
        String query = "SELECT * FROM " + TABLE_PRODUCT;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        cursor.close();
        return cursor.getCount();
    }
}
