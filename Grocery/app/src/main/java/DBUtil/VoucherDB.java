package DBUtil;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import Model.Customer;
import Model.Product;
import Model.Voucher;

public class VoucherDB extends DatabaseHandler{

    private static final String TABLE_VOUCHER = "VOUCHER";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_DISCOUNT = "discount";
    private static final String KEY_CONDITION = "condition";
    private static final String KEY_START_DATE = "startDate";
    private static final String KEY_END_DATE = "endDate";
    private static final String KEY_STATE = "state";

    public VoucherDB(Context context){
        super(context);
    }

    public void add(Voucher voucher){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, voucher.getName());
        values.put(KEY_DISCOUNT, voucher.getDiscount());
        values.put(KEY_CONDITION, voucher.getCondition());
        values.put(KEY_START_DATE, voucher.getStartDate());
        values.put(KEY_END_DATE, voucher.getEndDate());
        values.put(KEY_STATE, voucher.isState());

        db.insert(TABLE_VOUCHER, null, values);
        db.close();
    }

    public Voucher get(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_VOUCHER, new String[]{ KEY_ID, KEY_NAME, KEY_DISCOUNT, KEY_CONDITION, KEY_START_DATE, KEY_END_DATE, KEY_STATE },
                KEY_ID + "=?", new String[]{ String.valueOf(id) }, null, null, null, null);

        if (cursor != null && cursor.getCount() > 0)
            cursor.moveToFirst();
        else
            return null;
        return new Voucher(cursor);
    }

    public Voucher get(String name){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_VOUCHER, new String[]{ KEY_ID, KEY_NAME, KEY_DISCOUNT, KEY_CONDITION, KEY_START_DATE, KEY_END_DATE, KEY_STATE },
                KEY_NAME + "=?", new String[]{ name }, null, null, null, null);

        if (cursor != null && cursor.getCount() > 0)
            cursor.moveToFirst();
        else
            return null;
        return new Voucher(cursor);
    }

    public List<Voucher> get(){
        List<Voucher> vouchers = new ArrayList<Voucher>();
        String query = "SELECT * FROM " + TABLE_VOUCHER;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (!cursor.moveToFirst()) {
            return vouchers;
        }
        do{
            Voucher voucher = new Voucher(cursor);
            vouchers.add(voucher);
        }while (cursor.moveToNext());
        return vouchers;
    }

    public int update(Voucher voucher){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, voucher.getName());
        values.put(KEY_DISCOUNT, voucher.getDiscount());
        values.put(KEY_CONDITION, voucher.getCondition());
        values.put(KEY_START_DATE, voucher.getStartDate());
        values.put(KEY_END_DATE, voucher.getEndDate());
        values.put(KEY_STATE, voucher.isState());
        return db.update(TABLE_VOUCHER, values, KEY_ID + "=?", new String[]{String.valueOf(voucher.getId())});
    }

    public void delete(Voucher voucher){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_VOUCHER, KEY_ID + "=?", new String[]{String.valueOf(voucher.getId())});
        db.close();
    }

    public void delete(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_VOUCHER, KEY_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
    }

    public int getCount(){
        String query = "SELECT * FROM " + TABLE_VOUCHER;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        cursor.close();
        return cursor.getCount();
    }
}
