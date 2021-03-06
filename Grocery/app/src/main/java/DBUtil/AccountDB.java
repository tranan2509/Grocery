package DBUtil;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import Model.Account;

public class AccountDB extends DatabaseHandler{

    private static final String TABLE_ACCOUNT = "ACCOUNT";
    private static final String KEY_ID = "id";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_IS_EMAIL = "isEmail";
    private static final String KEY_ROLE = "role";
    private static final String KEY_STATE = "state";

    public AccountDB(Context context) {
        super(context);
    }

    public void add(Account account){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, account.getId());
        values.put(KEY_EMAIL, account.getEmail());
        values.put(KEY_PASSWORD, account.getPassword());
        values.put(KEY_IS_EMAIL, account.isEmail());
        values.put(KEY_ROLE, account.getRole());
        values.put(KEY_STATE, account.isState());
        db.insert(TABLE_ACCOUNT, null, values);
        db.close();
    }

    public Account getAccount(String id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_ACCOUNT, new String[]{ KEY_ID, KEY_EMAIL, KEY_PASSWORD, KEY_IS_EMAIL, KEY_ROLE, KEY_STATE },
                KEY_ID + "=?", new String[]{ id }, null, null, null, null);

        if (cursor != null && cursor.getCount() > 0)
            cursor.moveToFirst();
        else
            return null;
        Account account = new Account(cursor);
        return account;
    }

    public Account getAccountByEmail(String email){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_ACCOUNT, new String[]{ KEY_ID, KEY_EMAIL, KEY_PASSWORD, KEY_IS_EMAIL, KEY_ROLE, KEY_STATE },
                KEY_EMAIL + "=?", new String[]{ email }, null, null, null, null);

        if (cursor != null && cursor.getCount() > 0)
            cursor.moveToFirst();
        else
            return null;
        Account account = new Account(cursor);
        return account;
    }

    public List<Account> getAccounts(){
        List<Account> accounts = new ArrayList<Account>();
        String query = "SELECT * FROM " + TABLE_ACCOUNT;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()){
            do{
                Account account = new Account(cursor);
                accounts.add(account);
            }while (cursor.moveToNext());
        }
        return accounts;
    }

    public int update(Account account){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, account.getId());
        values.put(KEY_EMAIL, account.getEmail());
        values.put(KEY_PASSWORD, account.getPassword());
        values.put(KEY_IS_EMAIL, account.isEmail());
        values.put(KEY_ROLE, account.getRole());
        values.put(KEY_STATE, account.isState());
        return db.update(TABLE_ACCOUNT, values, KEY_ID + "=?", new String[]{account.getId()});
    }

    public void delete(Account account){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ACCOUNT, KEY_ID + "=?", new String[]{account.getId()});
        db.close();
    }

    public void delete(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ACCOUNT, KEY_ID + "=?", new String[]{id});
        db.close();
    }

    public int getCount(){
        String query = "SELECT * FROM " + TABLE_ACCOUNT;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        cursor.close();
        return cursor.getCount();
    }

    public Account accountLogged(String email, String password){
        Account account = getAccountByEmail(email);
        if (account == null)
            return null;
        if (account.getPassword().equals(password) && !password.equals(""))
            return account;
        return null;
    }

}
