package hcmute.edu.vn.mssv18110249;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class HelpCenterActivity extends AppCompatActivity implements View.OnClickListener{

    Button btnSendMail;
    ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_center);

        getView();
        setOnClick();
    }

    public void getView(){
        btnSendMail = findViewById(R.id.btnSendMail);
        btnBack = findViewById(R.id.btnBack);
    }

    public void setOnClick(){
        btnSendMail.setOnClickListener(this);
        btnBack.setOnClickListener(this);
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.btnBack:
                finish();
                break;
            case R.id.btnSendMail:
                sendMail();
                Toast.makeText(getApplicationContext(), "Report submitted successfully, thank you for your feedback", Toast.LENGTH_LONG).show();
                break;
        }
    }

    public void sendMail(){
        Intent si = new Intent(Intent.ACTION_SEND);
        si.setType("message/rfc822");
        si.putExtra(Intent.EXTRA_EMAIL, new String[]{"antran2509@gmail.com"});
        si.putExtra(Intent.EXTRA_SUBJECT, "Report a problem about the Grocery app");
        si.putExtra(Intent.EXTRA_TEXT, "");
        startActivity(Intent.createChooser(si,"Choose Mail App"));
    }
}