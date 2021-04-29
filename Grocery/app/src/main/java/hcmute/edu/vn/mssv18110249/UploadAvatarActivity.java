package hcmute.edu.vn.mssv18110249;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class UploadAvatarActivity extends AppCompatActivity {

    ImageButton imgBack;
    TextView uploadPhoto, takePhoto;
    Button btnFinish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_avatar);

        imgBack = (ImageButton)findViewById(R.id.btnBack);
        uploadPhoto = (TextView)findViewById(R.id.txtViewUpload);
        takePhoto = (TextView)findViewById(R.id.txtViewTakePhoto);
        btnFinish = (Button)findViewById(R.id.btnFinish);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}