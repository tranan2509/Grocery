package hcmute.edu.vn.mssv18110249;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {

    ImageButton btnBack;
    Button btnManagerLogin, btnSetPassword, btnLanguage;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        getView();
        setOnClick();
    }

    public void getView(){
        btnBack = (ImageButton)findViewById(R.id.btnBack);
        btnManagerLogin = (Button)findViewById(R.id.btnManageLogin);
        btnSetPassword = (Button)findViewById(R.id.btnSetPassword);
        btnLanguage = (Button)findViewById(R.id.btnLanguage);
    }

    public void setOnClick(){
        btnBack.setOnClickListener(this);
        btnManagerLogin.setOnClickListener(this);
        btnSetPassword.setOnClickListener(this);
        btnLanguage.setOnClickListener(this);
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.btnBack:
                intent = new Intent(this, AccountActivity.class);
                startActivity(intent);
                break;
            case R.id.btnManageLogin:
                break;
            case R.id.btnSetPassword:
                intent = new Intent(this, SetPasswordActivity.class);
                startActivity(intent);
                break;
            case R.id.btnLanguage:
                break;
        }
    }
}