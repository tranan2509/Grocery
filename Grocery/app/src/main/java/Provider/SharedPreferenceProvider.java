package Provider;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import Model.Account;
import Model.Branch;
import Model.Customer;

public class SharedPreferenceProvider {

    private static final Object lock = new Object();
    private static SharedPreferenceProvider instance;
    private Context context;

    private static SharedPreferences sharedPreferences;


    public static SharedPreferenceProvider getInstance(Context context){
        synchronized (lock){
            if (instance == null){
                instance = new SharedPreferenceProvider(context);
            }
            return instance;
        }
    }
    private SharedPreferenceProvider(Context context){
        this.context = context;
    }

    public Object get(String name){
        sharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        String json = sharedPreferences.getString(name, "");
        return new Gson().fromJson(json, Customer.class);
    }

    public Object getBranch(String name){
        sharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        String json = sharedPreferences.getString(name, "");
        return new Gson().fromJson(json, Branch.class);
    }

    public void set(String name, Object object){
        sharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String json = new Gson().toJson(object);
        editor.putString(name, json);
        editor.commit();
    }

}
