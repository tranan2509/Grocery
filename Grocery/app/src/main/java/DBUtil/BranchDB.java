package DBUtil;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import Model.Branch;
import Model.Product;

public class BranchDB extends DatabaseHandler{

    private static final String TABLE_BRANCH = "BRANCH";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_STATE = "state";

    public BranchDB(Context context){
        super(context);
    }

    public void add(Branch branch){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, branch.getName());
        values.put(KEY_ADDRESS, branch.getAddress());
        values.put(KEY_STATE, branch.isState());

        db.insert(TABLE_BRANCH, null, values);
        db.close();
    }

    public Branch get(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_BRANCH, new String[]{ KEY_ID, KEY_NAME, KEY_ADDRESS, KEY_STATE},
                KEY_ID + "=?", new String[]{ String.valueOf(id) }, null, null, null, null);

        if (cursor != null && cursor.getCount() > 0)
            cursor.moveToFirst();
        else
            return null;
        return new Branch(cursor);
    }

    public List<Branch> get(){
        List<Branch> branches = new ArrayList<Branch>();
        String query = "SELECT * FROM " + TABLE_BRANCH;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (!cursor.moveToFirst()) {
            return branches;
        }
        do{
            Branch branch = new Branch(cursor);
            branches.add(branch);
        }while (cursor.moveToNext());
        return branches;
    }

    public List<Branch> getActives(){
        List<Branch> branches = new ArrayList<Branch>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_BRANCH, new String[]{ KEY_ID, KEY_NAME, KEY_ADDRESS, KEY_STATE},
                KEY_STATE + "=?", new String[]{ String.valueOf(1) }, null, null, null, null);
        if (!cursor.moveToFirst()) {
            return branches;
        }
        do{
            Branch branch = new Branch(cursor);
            branches.add(branch);
        }while (cursor.moveToNext());
        return branches;
    }

    public List<Branch> search(String name){
        List<Branch> branches = new ArrayList<Branch>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_BRANCH, new String[]{KEY_ID, KEY_NAME, KEY_ADDRESS, KEY_STATE},
                KEY_NAME + " LIKE '%" + name + "%' AND " + KEY_STATE + "=?", new String[]{String.valueOf(1)}, null, null, null, null);
        if (cursor.moveToFirst()){
            do {
                Branch branch = new Branch(cursor);
                branches.add(branch);
            }while (cursor.moveToNext());
        }
        return branches;
    }

    public int update(Branch branch){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, branch.getName());
        values.put(KEY_ADDRESS, branch.getAddress());
        values.put(KEY_STATE, branch.isState());

        return db.update(TABLE_BRANCH, values, KEY_ID + "=?", new String[]{String.valueOf(branch.getId())});
    }

    public void delete(Branch branch){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_BRANCH, KEY_ID + "=?", new String[]{String.valueOf(branch.getId())});
        db.close();
    }

    public void delete(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_BRANCH, KEY_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
    }

    public int getCount(){
        String query = "SELECT * FROM " + TABLE_BRANCH;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        cursor.close();
        return cursor.getCount();
    }
}
