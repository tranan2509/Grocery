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
                "isEmail NUMERIC," +
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

        String CREATE_CATEGORY_TABLE = "CREATE TABLE CATEGORY (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT )";
        db.execSQL(CREATE_CATEGORY_TABLE);

        String CREATE_PRODUCT_TABLE = "CREATE TABLE PRODUCT(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "categoryId INTEGER, " +
                "name TEXT, " +
                "image BLOB, " +
                "importDate TEXT, " +
                "importPrice REAL, " +
                "price REAL, " +
                "unit TEXT, " +
                "discount INTEGER, " +
                "quantity INTEGER, " +
                "description TEXT, " +
                "rate REAL," +
                "reviewers INTEGER )";
        db.execSQL(CREATE_PRODUCT_TABLE);

        String CREATE_CART_TABLE = "CREATE TABLE CART(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "customerId TEXT, " +
                "productId INTEGER, " +
                "quantity INTEGER, " +
                "amount REAL, " +
                "state NUMERIC)";
        db.execSQL(CREATE_CART_TABLE);

        String CREATE_VOUCHER_TABLE = "CREATE TABLE VOUCHER(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, " +
                "discount INTEGER, " +
                "condition INTEGER, " +
                "startDate NUMERIC, " +
                "endDate NUMERIC, " +
                "state NUMERIC)";
        db.execSQL(CREATE_VOUCHER_TABLE);

        String CREATE_BILL_TABLE = "CREATE TABLE BILL(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "customerId TEXT, " +
                "payment INTEGER, " +
                "date TEXT, " +
                "voucherId INTEGER, " +
                "amount REAL, " +
                "state TEXT)";
        db.execSQL(CREATE_BILL_TABLE);

        String CREATE_BILL_DETAIL_TABLE = "CREATE TABLE BILL_DETAIL(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "billId INTEGER, " +
                "productId INTEGER, " +
                "quantity INTEGER, " +
                "price REAL, " +
                "amount REAL)";
        db.execSQL(CREATE_BILL_DETAIL_TABLE);
    }

    @Override
    public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS ACCOUNT");
        db.execSQL("DROP TABLE IF EXISTS CUSTOMER");
        db.execSQL("DROP TABLE IF EXISTS CATEGORY");
        db.execSQL("DROP TABLE IF EXISTS PRODUCT");
        db.execSQL("DROP TABLE IF EXISTS CART");
        db.execSQL("DROP TABLE IF EXISTS VOUCHER");
        db.execSQL("DROP TABLE IF EXISTS BILL");
        db.execSQL("DROP TABLE IF EXISTS BILL_DETAIL");
        onCreate(db);
    }
}
