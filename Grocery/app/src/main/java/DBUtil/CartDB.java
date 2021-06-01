package DBUtil;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import javax.activation.DataHandler;
import javax.activation.DataSource;

import Model.Account;
import Model.Cart;
import Model.Product;

public class CartDB extends DatabaseHandler {

    private static final String TABLE_CART = "CART";
    private static final String KEY_ID = "id";
    private static final String KEY_CUS_ID = "customerId";
    private static final String KEY_PRODUCT_ID = "productId";
    private static final String KEY_QUANTITY = "quantity";
    private static final String KEY_AMOUNT = "amount";
    private static final String KEY_STATE = "state";
    private Context mContext;

    public CartDB(Context context) {
        super(context);
        mContext = context;
    }

    public void add(Cart cart){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_CUS_ID, cart.getCustomerId());
        values.put(KEY_PRODUCT_ID, cart.getProductId());
        values.put(KEY_QUANTITY, cart.getQuantity());
        values.put(KEY_AMOUNT, cart.getAmount());
        values.put(KEY_STATE, cart.isState());
        db.insert(TABLE_CART, null, values);
        db.close();
    }

    public Cart get(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_CART, new String[]{ KEY_ID, KEY_CUS_ID, KEY_PRODUCT_ID, KEY_QUANTITY, KEY_AMOUNT, KEY_STATE},
                KEY_ID + "=?", new String[]{ String.valueOf(id) }, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0)
            cursor.moveToFirst();
        else
            return null;
        return new Cart(cursor);
    }

    public Cart get(String customerId, int productId){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_CART, new String[]{ KEY_ID, KEY_CUS_ID, KEY_PRODUCT_ID, KEY_QUANTITY, KEY_AMOUNT, KEY_STATE},
                KEY_CUS_ID + "=? AND " + KEY_PRODUCT_ID + "=?", new String[]{ customerId, String.valueOf(productId) }, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0)
            cursor.moveToFirst();
        else
            return null;
        return new Cart(cursor);
    }

    public int getProductId(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_CART, new String[]{ KEY_PRODUCT_ID},
                KEY_ID + "=?", new String[]{ String.valueOf(id) }, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0)
            cursor.moveToFirst();
        else
            return -1;
        return cursor.getInt(cursor.getColumnIndex("productId"));

    }

    public List<Cart> get(String customerId){
        List<Cart> carts = new ArrayList<Cart>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_CART, new String[]{ KEY_ID, KEY_CUS_ID, KEY_PRODUCT_ID, KEY_QUANTITY, KEY_AMOUNT, KEY_STATE},
                KEY_CUS_ID + "=?", new String[]{ customerId }, null, null, null, null);
        if (!cursor.moveToFirst()) {
            return carts;
        }
        do {
            Cart cart = new Cart(cursor);
            carts.add(cart);
        } while (cursor.moveToNext());
        return carts;
    }

    public List<Cart> get(){
        List<Cart> carts = new ArrayList<Cart>();
        String query = "SELECT * FROM " + TABLE_CART;
        SQLiteDatabase db = this.getWritableDatabase();
        try (Cursor cursor = db.rawQuery(query, null)) {
            if (!cursor.moveToFirst()) {
                return carts;
            }
            do {
                Cart cart = new Cart(cursor);
                carts.add(cart);
            } while (cursor.moveToNext());
        }
        return carts;
    }

    public int update(Cart cart){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, cart.getId());
        values.put(KEY_CUS_ID, cart.getCustomerId());
        values.put(KEY_PRODUCT_ID, cart.getProductId());
        values.put(KEY_QUANTITY, cart.getQuantity());
        values.put(KEY_AMOUNT, cart.getAmount());
        values.put(KEY_STATE, cart.isState());
        return db.update(TABLE_CART, values, KEY_ID + "=?", new String[]{String.valueOf(cart.getId())});
    }

    public void delete(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CART, KEY_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
    }

    public void delete(String customerId, int productId){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CART, KEY_CUS_ID + "=? AND " + KEY_PRODUCT_ID + "=?", new String[]{customerId, String.valueOf(productId)});
        db.close();
    }

    public void delete(Cart cart){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CART, KEY_ID + "=?", new String[]{String.valueOf(cart.getId())});
        db.close();
    }

    public int getCount(){
        String query = "SELECT * FROM " + TABLE_CART;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        cursor.close();
        return cursor.getCount();
    }

    public double getAmount(String customerId){
        ProductDB productDB = new ProductDB(mContext);
        List<Cart> carts = get(customerId);
        double sum = 0;
        for (Cart cart: carts){
            if (cart.isState()){
                Product product = productDB.get(cart.getProductId());
                sum += cart.getQuantity() * (product.getPrice() * (1 - (double)product.getDiscount()/100));
            }
        }
        return sum;
    }

}
