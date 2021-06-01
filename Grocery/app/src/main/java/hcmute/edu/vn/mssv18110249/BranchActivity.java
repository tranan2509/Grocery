package hcmute.edu.vn.mssv18110249;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.List;

import DBUtil.BranchDB;
import Model.Branch;
import Provider.BranchListViewAdapter;
import Provider.SharedPreferenceProvider;

public class BranchActivity extends AppCompatActivity implements View.OnClickListener{

    ImageButton btnBack;
    EditText txtBranchAddress;
    ListView lvBranchAddress;
    BranchListViewAdapter branchListViewAdapter;
    BranchDB branchDB;
    List<Branch> branchActives;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branch);
        branchDB = new BranchDB(this);

        getView();
        setOnClick();
        branchActives = branchDB.getActives();
        loadListView(branchActives);
        lvBranchAddress.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Branch branch = branchActives.get(position);
                SharedPreferenceProvider.getInstance(BranchActivity.this).set("branch", branch);
                Intent intent = new Intent(BranchActivity.this, HomePageActivity.class);
                startActivity(intent);
            }
        });

        txtBranchAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String name = txtBranchAddress.getText().toString();
                if (name.equals("")){
                    branchActives = branchDB.getActives();

                }else {
                    branchActives = branchDB.search(name);
                }
                loadListView(branchActives);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void getView(){
        btnBack = findViewById(R.id.btnBack);
        txtBranchAddress = findViewById(R.id.txtBranchAddress);
        lvBranchAddress = findViewById(R.id.lvBranchAddress);
    }

    public void setOnClick(){
        btnBack.setOnClickListener(this);
    }

    public void loadListView(List<Branch> branchActives){
        if (branchActives != null && branchActives.size() > 0) {
            branchListViewAdapter = new BranchListViewAdapter(branchActives);
            lvBranchAddress.setAdapter(branchListViewAdapter);
        }
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.btnBack:
                finish();
        }
    }
}