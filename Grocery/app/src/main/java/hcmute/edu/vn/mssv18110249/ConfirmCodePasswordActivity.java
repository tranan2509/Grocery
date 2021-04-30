package hcmute.edu.vn.mssv18110249;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ConfirmCodePasswordActivity extends AppCompatActivity {

    EditText txtCode;
    Button btnNext;
    TextView txtViewBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_code_password);

        txtCode = (EditText)findViewById(R.id.txtCode);
        btnNext = (Button) findViewById(R.id.btnNext);
        txtViewBack = (TextView)findViewById(R.id.txtViewBack);

        txtViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = txtCode.getText().toString();
                if (!code.equals("")){
                    Intent intent = getIntent();
                    String preCode = intent.getStringExtra("code");
                    if (code.equals(preCode)){
                        Intent intentChangePassword = new Intent(getApplicationContext(), ChangePasswordActivity.class);
                        String email = intent.getStringExtra("email");
                        intentChangePassword.putExtra("email", email);
                        startActivity(intentChangePassword);
                    }else{
                        Toast.makeText(getApplicationContext(), "Verification code does not match", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "Code cannot be left blank", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}