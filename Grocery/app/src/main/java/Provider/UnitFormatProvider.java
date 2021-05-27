package Provider;

import android.content.Context;
import android.content.SharedPreferences;

import java.text.NumberFormat;
import java.util.Currency;

public class UnitFormatProvider {
    private static final Object lock = new Object();
    private static UnitFormatProvider instance;

    public static UnitFormatProvider getInstance(){
        synchronized (lock){
            if (instance == null){
                instance = new UnitFormatProvider();
            }
            return instance;
        }
    }
    private UnitFormatProvider(){

    }

    public String format(double amount){
        NumberFormat format = NumberFormat.getCurrencyInstance();
        format.setMaximumFractionDigits(0);
        format.setCurrency(Currency.getInstance("VND"));
        return format.format(amount);
    }

}
