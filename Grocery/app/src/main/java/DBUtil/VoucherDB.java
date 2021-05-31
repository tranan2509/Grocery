package DBUtil;

import android.content.Context;

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
}
