package DBUtil;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import Model.Customer;

public class CustomerDB extends DatabaseHandler{

    private static final String TABLE_CUSTOMER = "CUSTOMER";
    private static final String KEY_ID = "id";
    private static final String KEY_ACC_ID = "accountId";
    private static final String KEY_NAME = "name";
    private static final String KEY_AVATAR = "avatar";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_DOB = "dob";
    private static final String KEY_GENDER = "gender";

    public CustomerDB(Context context){
        super(context);
    }

    public void add(Customer customer){
        SQLiteDatabase db = this.getWritableDatabase();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");

        ContentValues values = new ContentValues();
        values.put(KEY_ID, customer.getId());
        values.put(KEY_ACC_ID, customer.getAccountId());
        values.put(KEY_NAME, customer.getName());
        values.put(KEY_AVATAR, customer.getAvatar());
        values.put(KEY_PHONE, customer.getPhone());
        values.put(KEY_ADDRESS, customer.getAddress());
        values.put(KEY_DOB, customer.getDob());
        values.put(KEY_GENDER, customer.isGender());

        db.insert(TABLE_CUSTOMER, null, values);
        db.close();
    }

    public Customer getCustomer(String id, boolean isAccountId){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_CUSTOMER, new String[]{KEY_ID, KEY_ACC_ID, KEY_NAME, KEY_AVATAR, KEY_PHONE, KEY_ADDRESS, KEY_DOB, KEY_GENDER},
                KEY_ACC_ID + "=?", new String[]{id}, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0)
            cursor.moveToFirst();
        else
            return null;
        return new Customer(cursor);
    }

    public Customer getCustomer(String id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_CUSTOMER, new String[]{KEY_ID, KEY_ACC_ID, KEY_NAME, KEY_AVATAR, KEY_PHONE, KEY_ADDRESS, KEY_DOB, KEY_GENDER},
                KEY_ID + "=?", new String[]{id}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        return new Customer(cursor);
    }

    public List<Customer> getCustomers(){
        List<Customer> customers = new ArrayList<Customer>();
        String query = "SELECT * FROM " + TABLE_CUSTOMER;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()){
            do {
                Customer customer = new Customer(cursor);
                customers.add(customer);
            }while (cursor.moveToNext());
        }
        return customers;
    }

    public int update(Customer customer){
        SQLiteDatabase db = this.getWritableDatabase();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        ContentValues values = new ContentValues();
        values.put(KEY_ID, customer.getId());
        values.put(KEY_ACC_ID, customer.getAccountId());
        values.put(KEY_NAME, customer.getName());
        values.put(KEY_AVATAR, customer.getAvatar());
        values.put(KEY_PHONE, customer.getPhone());
        values.put(KEY_ADDRESS, customer.getAddress());
        values.put(KEY_DOB, customer.getDob());
        values.put(KEY_GENDER, customer.isGender());
        return db.update(TABLE_CUSTOMER, values, KEY_ID + "=?", new String[]{customer.getId()});
    }

    public void delete(Customer customer){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CUSTOMER, KEY_ID + "=?", new String[]{customer.getId()});
        db.close();
    }

    public void delete(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CUSTOMER, KEY_ID + "=?", new String[]{id});
        db.close();
    }

    public int getCount(){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_CUSTOMER;
        Cursor cursor = db.rawQuery(query, null);
        return cursor.getCount();
    }
}
