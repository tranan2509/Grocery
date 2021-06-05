package DBUtil;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import Model.Bill;
import Model.BillDetail;
import Model.Product;

public class BillDetailDB extends DatabaseHandler{

    private static final String TABLE_BILL_DETAIL = "BILL_DETAIL";
    private static final String KEY_ID = "id";
    private static final String KEY_BILL_ID = "billId";
    private static final String KEY_PRODUCT_ID = "productId";
    private static final String KEY_QUANTITY = "quantity";
    private static final String KEY_PRICE = "price";
    private static final String KEY_AMOUNT = "amount";

    private Context context;

    public BillDetailDB(Context context){
        super(context);
        this.context = context;
    }

    public void add(BillDetail billDetail){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_BILL_ID, billDetail.getBillId());
        values.put(KEY_PRODUCT_ID, billDetail.getProductId());
        values.put(KEY_QUANTITY, billDetail.getQuantity());
        values.put(KEY_PRICE, billDetail.getPrice());
        values.put(KEY_AMOUNT, billDetail.getAmount());

        db.insert(TABLE_BILL_DETAIL, null, values);
        db.close();
    }

    public BillDetail get(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_BILL_DETAIL, new String[]{ KEY_ID, KEY_BILL_ID, KEY_PRODUCT_ID, KEY_QUANTITY, KEY_PRICE, KEY_AMOUNT },
                KEY_ID + "=?", new String[]{ String.valueOf(id) }, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0)
            cursor.moveToFirst();
        else
            return null;
        return new BillDetail(cursor);
    }

    public List<BillDetail> get(){
        List<BillDetail> billDetails = new ArrayList<BillDetail>();
        String query = "SELECT * FROM " + TABLE_BILL_DETAIL;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (!cursor.moveToFirst()) {
            return billDetails;
        }
        do{
            BillDetail billDetail = new BillDetail(cursor);
            billDetails.add(billDetail);
        }while (cursor.moveToNext());
        return billDetails;
    }

    public List<BillDetail> getByBillId(int billId){
        List<BillDetail> billDetails = new ArrayList<BillDetail>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_BILL_DETAIL, new String[]{KEY_ID, KEY_BILL_ID, KEY_PRODUCT_ID, KEY_QUANTITY, KEY_PRICE, KEY_AMOUNT},
                KEY_BILL_ID + "=?", new String[]{String.valueOf(billId)}, null, null, null, null);
        if (!cursor.moveToFirst()) {
            return billDetails;
        }
        do{
            BillDetail billDetail = new BillDetail(cursor);
            billDetails.add(billDetail);
        }while (cursor.moveToNext());
        return billDetails;
    }


    public List<Product> getBestSelling(String date) {
        ProductDB productDB = new ProductDB(context);
        List<Product>products = new ArrayList<Product>();
        String query = "SELECT BILL_DETAIL.productId, SUM(BILL_DETAIL.quantity) as quantity FROM " + TABLE_BILL_DETAIL +
                " INNER JOIN BILL on BILL.id = BILL_DETAIL.billId" +
                " WHERE DATE(BILL.date) >= DATE('" + date + "', '-7 days') " +
                " GROUP BY BILL_DETAIL.productId" +
                " HAVING quantity > 0" +
                " ORDER BY quantity DESC";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (!cursor.moveToFirst()) {
            return products;
        }
        do{
            int productId = cursor.getInt(cursor.getColumnIndex("productId"));
            Product product = productDB.get(productId);
            products.add(product);
        }while (cursor.moveToNext());
        return products;
    }

    public List<Product> getBestSelling(String date, String name) {
        ProductDB productDB = new ProductDB(context);
        List<Product>products = new ArrayList<Product>();
        String query = "SELECT BILL_DETAIL.productId, SUM(BILL_DETAIL.quantity) as totalQuantity FROM " + TABLE_BILL_DETAIL +
                " INNER JOIN BILL on BILL.id = BILL_DETAIL.billId" +
                " INNER JOIN PRODUCT on PRODUCT.id = BILL_DETAIL.productId" +
                " WHERE DATE(BILL.date) >= DATE('" + date + "', '-7 days') " +
                " AND PRODUCT.name LIKE '%" + name + "%'" +
                " GROUP BY BILL_DETAIL.productId" +
                " HAVING totalQuantity > 0" +
                " ORDER BY totalQuantity DESC";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (!cursor.moveToFirst()) {
            return products;
        }
        do{
            int productId = cursor.getInt(cursor.getColumnIndex("productId"));
            Product product = productDB.get(productId);
            products.add(product);
        }while (cursor.moveToNext());
        return products;
    }

    public int update(BillDetail billDetail){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_BILL_ID, billDetail.getBillId());
        values.put(KEY_PRODUCT_ID, billDetail.getProductId());
        values.put(KEY_QUANTITY, billDetail.getQuantity());
        values.put(KEY_PRICE, billDetail.getPrice());
        values.put(KEY_AMOUNT, billDetail.getAmount());
        return db.update(TABLE_BILL_DETAIL, values, KEY_ID + "=?", new String[]{String.valueOf(billDetail.getId())});
    }

    public void delete(BillDetail billDetail){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_BILL_DETAIL, KEY_ID + "=?", new String[]{String.valueOf(billDetail.getId())});
        db.close();
    }

    public void delete(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_BILL_DETAIL, KEY_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
    }

    public int getCount(){
        String query = "SELECT * FROM " + TABLE_BILL_DETAIL;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        cursor.close();
        return cursor.getCount();
    }

}
