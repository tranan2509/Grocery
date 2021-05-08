package hcmute.edu.vn.mssv18110249;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import Provider.CircleImage;

public class SignUpActivity extends AppCompatActivity {

    TextView txtViewSignIn;
    Button btnSignUp;
    EditText txtName, txtPhone, txtEmail, txtPassword, txtConfirmPassword, txtDob, txtAddress;
    RadioButton rdoMale;
    CheckBox ckbPolicy;
    CircleImage imgAvatar;

    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        btnSignUp = (Button)findViewById(R.id.btnSignUp);
        txtViewSignIn = (TextView)findViewById(R.id.btnSignIn);
        txtName = (EditText)findViewById(R.id.txtName);
        txtEmail = (EditText)findViewById(R.id.txtEmail);
        txtPhone = (EditText)findViewById(R.id.txtPhone);
        txtDob = (EditText)findViewById(R.id.txtDob);
        txtAddress = (EditText)findViewById(R.id.txtAddress);
        txtPassword = (EditText)findViewById(R.id.txtPassword);
        txtConfirmPassword = (EditText)findViewById(R.id.txtConfirmPassword);
        rdoMale = (RadioButton)findViewById(R.id.rdoMale);
        ckbPolicy = (CheckBox)findViewById(R.id.ckbPolicy);
        imgAvatar = (CircleImage)findViewById(R.id.imgAvatar);

        imgAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                }
                else
                {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }
            }
        });

        txtViewSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                imgAvatar.setScaleType(ImageView.ScaleType.CENTER_CROP);
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
            else
            {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            int width = photo.getWidth() < photo.getHeight() ? photo.getWidth() : photo.getHeight();
            int x = Integer.valueOf((photo.getWidth() - width)/2 );
            int y = Integer.valueOf((photo.getHeight() - width)/2 );
            Bitmap avatar = Bitmap.createBitmap(photo, x, y, width, width);
            imgAvatar.setImageBitmap(avatar);
        }
    }

    public boolean isInvalid(){
        if (txtName.getText().toString() != "")
            if (txtPhone.getText().toString() != ""){
                if (txtEmail.getText().toString() != ""){
                    if (txtPassword.getText().toString()!= ""){
                        if (txtConfirmPassword.getText().toString() != ""){
                            if (ckbPolicy.isChecked()){
                                return true;
                            }else{
                                Toast.makeText(getApplicationContext(), "Vui lòng chấp nhận chính sách", Toast.LENGTH_LONG).show();
                            }
                        }else{
                            Toast.makeText(getApplicationContext(), "Vui lòng xác nhận mật khẩu", Toast.LENGTH_LONG).show();
                        }
                    }else{
                        Toast.makeText(getApplicationContext(), "Vui lòng nhập mật khẩu", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "Vui lòng nhập địa chỉ email", Toast.LENGTH_LONG).show();
                }
            }else{
                Toast.makeText(getApplicationContext(), "Vui lòng nhập số điện thoại", Toast.LENGTH_LONG).show();
            }else{
            Toast.makeText(getApplicationContext(), "Vui lòng nhập tên", Toast.LENGTH_LONG).show();
        }
        return false;
    }

}