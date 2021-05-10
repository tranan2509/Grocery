package DBUtil;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "GROCERY";

    public DatabaseHandler(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(final SQLiteDatabase db) {
        String CREATE_ACCOUNT_TABLE = "CREATE TABLE ACCOUNT (" +
                "id TEXT PRIMARY KEY," +
                "email TEXT, " +
                "password TEXT, " +
                "role INT," +
                "state NUMERIC)";
        db.execSQL(CREATE_ACCOUNT_TABLE);

        String CREATE_CUSTOMER_TABLE = "CREATE TABLE CUSTOMER (" +
                "id TEXT PRIMARY KEY, " +
                "accountId TEXT, " +
                "name TEXT, " +
                "avatar TEXT, " +
                "phone TEXT, " +
                "address TEXT, " +
                "dob NUMERIC, " +
                "gender NUMERIC )";
        db.execSQL(CREATE_CUSTOMER_TABLE);
    }

    @Override
    public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS ACCOUNT");
        db.execSQL("DROP TABLE IF EXISTS CUSTOMER");
        onCreate(db);
    }
}
