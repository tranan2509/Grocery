package hcmute.edu.vn.mssv18110249;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Properties;
import java.util.Random;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import Provider.Validator;

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
                Boolean isEmail = Validator.validateEmail(email);
                if (email.equals("")){
                    Toast.makeText(getApplicationContext(), "Email cannot be left blank", Toast.LENGTH_LONG).show();
                    return;
                }
                if (isEmail){
//                    Toast.makeText(getApplicationContext(), "Network: " + isOnline().toString(), Toast.LENGTH_LONG).show();
                    //Check email exists
                    //
                    //
                    if (true){
                        try {
                            String code = createCode();
                            String fromEmail = "antran2509@gmail.com";
                            String fromPassword = "01692889894";
                            String toEmail = email;

                            Properties props = new Properties();
                            props.put("mail.transport.protocol", "smtps");
                            props.put("mail.smtps.host", "smtp.gmail.com");
                            props.put("mail.smtps.port", 465);
                            props.put("mail.smtps.auth", "true");
                            props.put("mail.smtps.quitwait", "false");
                            Session session = Session.getDefaultInstance(props);
                            session.setDebug(true);

                            Message message = new MimeMessage(session);
                            message.setSubject("subject");
                            message.setText("text");

                            Address fromAddress = new InternetAddress(fromEmail);
                            Address toAddress = new InternetAddress(toEmail);
                            message.setFrom(fromAddress);
                            message.setRecipient(Message.RecipientType.TO, toAddress);

                            Transport transport = session.getTransport();
                            transport.connect(fromEmail, fromPassword);
                            transport.sendMessage(message, message.getAllRecipients());
                            transport.close();

//                            GMailSender sender = new GMailSender(fromEmail, fromPassword);
//                            sender.sendMail("This is Subject", "This is Body", fromEmail, toEmail);
                            Toast.makeText(getApplicationContext(), "Email success", Toast.LENGTH_LONG).show();
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
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    public String createCode(){
        Random random = new Random();
        int value = random.nextInt((999999 - 111111) + 1) + 111111;
        return String.valueOf(value);
    }

    private Boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if(ni != null && ni.isConnected()) {
            return true;
        }
        return false;
    }
}