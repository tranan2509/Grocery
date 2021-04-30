package hcmute.edu.vn.mssv18110249;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.creativityapps.gmailbackgroundlibrary.BackgroundMail;

import java.util.Random;

import Provider.EmailValidator;

public class ForgetPasswordActivity extends AppCompatActivity {

    Button btnSend;
    EditText txtEmail;
    TextView txtViewBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        btnSend = (Button)findViewById(R.id.btnSend);
        txtEmail = (EditText)findViewById(R.id.txtEmail);
        txtViewBack = (TextView) findViewById(R.id.txtViewBack);

        txtViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = txtEmail.getText().toString();
                Boolean isEmail = EmailValidator.validate2(email);
                if (email.equals("")){
                    Toast.makeText(getApplicationContext(), "Email cannot be left blank", Toast.LENGTH_LONG).show();
                    return;
                }
                if (isEmail){
                    //Check email exists
                    //
                    //
                    if (true){
                        try {
                            String code = createCode();
                            String fromEmail = "antran2509@gmail.com";
                            String fromPassword = "01692889894";
                            String toEmail = email;
                            BackgroundMail.newBuilder(ForgetPasswordActivity.this)
                                    .withUsername(fromEmail)
                                    .withPassword(fromPassword)
                                    .withMailto(toEmail)
                                    .withType(BackgroundMail.TYPE_PLAIN)
                                    .withSubject("this is the subject")
                                    .withBody("this is the body")
                                    .withOnSuccessCallback(new BackgroundMail.OnSuccessCallback() {
                                        @Override
                                        public void onSuccess() {
                                            //do some magic
                                            Toast.makeText(getApplicationContext(), "Email Success", Toast.LENGTH_LONG).show();
                                            Intent intent = new Intent(getApplicationContext(), ConfirmCodePasswordActivity.class);
                                            intent.putExtra("email", email);
                                            intent.putExtra("code", code);
                                            startActivity(intent);

                                        }
                                    })
                                    .withOnFailCallback(new BackgroundMail.OnFailCallback() {
                                        @Override
                                        public void onFail() {
                                            //do some magic
                                            Toast.makeText(getApplicationContext(), "Email Fail", Toast.LENGTH_LONG).show();
                                        }
                                    })
                                    .send();

                        }catch (Exception x){
                            Toast.makeText(getApplicationContext(), "Email failed", Toast.LENGTH_LONG).show();
                        }
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Email is not registered", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "Email not valid", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public String createCode(){
        Random random = new Random();
        int value = random.nextInt((999999 - 111111) + 1) + 111111;
        return String.valueOf(value);
    }
}