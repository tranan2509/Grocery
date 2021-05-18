package DBUtil;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import Model.Account;
import Model.Category;

public class CategoryDB extends DatabaseHandler{
    private static final String TABLE_CATEGORY = "CATEGORY";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";

    public CategoryDB(Context context) {
        super(context);
    }

    public void add(Category category){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, category.getName());
        db.insert(TABLE_CATEGORY, null, values);
        db.close();
    }

    public Category get(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_CATEGORY, new String[]{ KEY_ID, KEY_NAME },
                KEY_ID + "=?", new String[]{ String.valueOf(id) }, null, null, null, null);

        if (cursor != null && cursor.getCount() > 0)
            cursor.moveToFirst();
        else
            return null;
        return new Category(cursor);
    }

    public List<Category> get(){
        List<Category> categories = new ArrayList<Category>();
        String query = "SELECT * FROM " + TABLE_CATEGORY;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()){
            do{
                Category category = new Category(cursor);
                categories.add(category);
            }while (cursor.moveToNext());
        }
        return categories;
    }

    public int update(Category category){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, category.getId());
        values.put(KEY_NAME, category.getName());
        return db.update(TABLE_CATEGORY, values, KEY_ID + "=?", new String[]{String.valueOf(category.getId())});
    }

    public void delete(Category category){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CATEGORY, KEY_ID + "=?", new String[]{String.valueOf(category.getId())});
        db.close();
    }

    public void delete(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CATEGORY, KEY_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
    }

    public int getCount(){
        String query = "SELECT * FROM " + TABLE_CATEGORY;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        cursor.close();
        return cursor.getCount();
    }

}
