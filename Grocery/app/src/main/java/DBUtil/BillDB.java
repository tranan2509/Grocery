package DBUtil;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import Model.Bill;
import Model.Voucher;

public class BillDB extends DatabaseHandler{

    private static final String TABLE_BILL = "BILL";
    private static final String KEY_ID = "id";
    private static final String KEY_CUS_ID = "customerId";
    private static final String KEY_BRANCH_ID = "branchId";
    private static final String KEY_PAYMENT = "payment";
    private static final String KEY_DATE = "date";
    private static final String KEY_VOUCHER_ID = "voucherId";
    private static final String KEY_AMOUNT = "amount";
    private static final String KEY_STATE = "state";

    public BillDB(Context context){
        super(context);
    }

    public void add(Bill bill){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_CUS_ID, bill.getCustomerId());
        values.put(KEY_BRANCH_ID, bill.getBranchId());
        values.put(KEY_PAYMENT, bill.getPayment());
        values.put(KEY_DATE, bill.getDate());
        values.put(KEY_VOUCHER_ID, bill.getVoucherId());
        values.put(KEY_AMOUNT, bill.getAmount());
        values.put(KEY_STATE, bill.getState());

        db.insert(TABLE_BILL, null, values);
        db.close();
    }

    public Bill get(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_BILL, new String[]{ KEY_ID, KEY_CUS_ID, KEY_BRANCH_ID, KEY_PAYMENT, KEY_DATE, KEY_VOUCHER_ID, KEY_AMOUNT, KEY_STATE },
                KEY_ID + "=?", new String[]{ String.valueOf(id) }, null, null, null, null);

        if (cursor != null && cursor.getCount() > 0)
            cursor.moveToFirst();
        else
            return null;
        return new Bill(cursor);
    }

    public List<Bill> get(String customerId){
        List<Bill> bills = new ArrayList<Bill>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_BILL, new String[]{ KEY_ID, KEY_CUS_ID, KEY_BRANCH_ID, KEY_PAYMENT, KEY_DATE, KEY_VOUCHER_ID, KEY_AMOUNT, KEY_STATE },
                KEY_CUS_ID + "=?", new String[]{ customerId }, null, null, KEY_ID + " DESC", null);
        if (!cursor.moveToFirst()) {
            return bills;
        }
        do{
            Bill bill = new Bill(cursor);
            bills.add(bill);
        }while (cursor.moveToNext());
        return bills;
    }

    public List<Bill> get(){
        List<Bill> bills = new ArrayList<Bill>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_BILL, new String[]{ KEY_ID, KEY_CUS_ID, KEY_BRANCH_ID, KEY_PAYMENT, KEY_DATE, KEY_VOUCHER_ID, KEY_AMOUNT, KEY_STATE },
                null, null, null, null,  KEY_ID + " DESC");
        if (!cursor.moveToFirst()) {
            return bills;
        }
        do{
            Bill bill = new Bill(cursor);
            bills.add(bill);
        }while (cursor.moveToNext());
        return bills;
    }

    public double totalOfDate(String date, String customerId){
        double amount = 0;
        String startDate =  date + " 00:00";
        String endDate = date + " 23:59";
        String query = "SELECT sum(" + KEY_AMOUNT + ") FROM " + TABLE_BILL + " WHERE " + KEY_CUS_ID + " =? AND " +
                "strftime('%s', " + KEY_DATE + ") >= strftime('%s', '" + startDate + "') AND  strftime('%s', " + KEY_DATE + ") <= strftime('%s', '" + endDate + "')";
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(query, new String[]{customerId});
            if (cursor != null)
                if (cursor.moveToFirst()) {
                    amount = cursor.getDouble(0);
                }
            cursor.close();
            return amount;
        }catch (Exception ex){
            return -1;
        }
    }

    public double totalOfMonth(String date, String customerId){
        double amount = 0;
        String[] days = date.split("-");

        String startDate =  days[0] + "-" + days[1] + "-01" + " 00:00";
        String endDate = maxDay(date) + " 23:59";
        String query = "SELECT sum(" + KEY_AMOUNT + ") FROM " + TABLE_BILL + " WHERE " + KEY_CUS_ID + " =? AND " +
                "strftime('%s', " + KEY_DATE + ") >= strftime('%s', '" + startDate + "') AND  strftime('%s', " + KEY_DATE + ") <= strftime('%s', '" + endDate + "')";
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(query, new String[]{customerId});
            if (cursor != null)
                if (cursor.moveToFirst()) {
                    amount = cursor.getDouble(0);
                }
            cursor.close();
            return amount;
        }catch (Exception ex){
            return -1;
        }
    }

    public String maxDay(String date){
        String[] days = date.split("-");
        int month = Integer.parseInt(days[1]);
        int day = 0;
        int year = Integer.parseInt(days[0]);
        switch (month){
            case 1: case 3: case 5: case 7: case 8: case 10: case 12:
                day = 31;
                break;
            case 4: case 6: case 9: case 11:
                day = 30;
                break;
            case 2:
                if (year % 400 == 0 || (year % 4 == 0 && year % 100 != 0))
                    day = 29;
                else
                    day = 28;
                break;
        }
        return year + "-" + days[1] + "-" + day;
    }

    public int update(Bill bill){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_CUS_ID, bill.getCustomerId());
        values.put(KEY_BRANCH_ID, bill.getBranchId());
        values.put(KEY_PAYMENT, bill.getPayment());
        values.put(KEY_DATE, bill.getDate());
        values.put(KEY_VOUCHER_ID, bill.getVoucherId());
        values.put(KEY_AMOUNT, bill.getAmount());
        values.put(KEY_STATE, bill.getState());
        return db.update(TABLE_BILL, values, KEY_ID + "=?", new String[]{String.valueOf(bill.getId())});
    }

    public void delete(Bill bill){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_BILL, KEY_ID + "=?", new String[]{String.valueOf(bill.getId())});
        db.close();
    }

    public void delete(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_BILL, KEY_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
    }

    public int getCount(){
        String query = "SELECT * FROM " + TABLE_BILL;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        cursor.close();
        return cursor.getCount();
    }

    public int getMaxID(){
        int mx=-1;
        try{
            SQLiteDatabase db=this.getReadableDatabase();
            Cursor cursor=db.rawQuery("SELECT max(id) from " + TABLE_BILL + " ",new String [] {});
            if (cursor != null)
                if(cursor.moveToFirst())
                {
                    mx= cursor.getInt(0);
                }
              cursor.close();
            return mx;
        }
        catch(Exception e){
            return -1;
        }
    }

}
