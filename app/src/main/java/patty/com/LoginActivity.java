package patty.com;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    TextView tvcreate;
    EditText etUser,etPass;
    Button btnLogin;
    String username,password;
    int formsuccess, userid;

    DBHelper db;

    SharedPreferences shared;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DBHelper(this);

        etUser =  findViewById(R.id.etUsername);
        etPass =  findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvcreate = findViewById(R.id.tvCreateAccount);

        btnLogin.setOnClickListener(this);
        tvcreate.setOnClickListener(this);

        shared = getSharedPreferences("VBFC", Context.MODE_PRIVATE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnLogin:


                username = etUser.getText().toString().trim();
                password = etUser.getText().toString().trim();
                formsuccess = 2;

                if(username.equals ("")){
                    etUser.setError("This field is Required");
                    formsuccess--;
                }

                if(password.equals(""))
                {
                    etPass.setError("This field is Required");
                    formsuccess--;
                }
                if(formsuccess == 2)
                {

                    userid = db.checkUser(username,password);

                    if(userid >=1 )
                    {
                        SharedPreferences.Editor editor = shared.edit();
                        editor.putInt(db.TBL_USERS, userid).commit();
                        this.finish();

                       startActivity(new Intent(this,HomeActivity.class));
                    }
                    else
                    {
                        etUser.setError("Invalid Login Credential");
                        etPass.setText("");
                    }
                }
                break;
            case R.id.tvCreateAccount:
                startActivity(new Intent(this,Validation.class));
                break;
        }
    }

    protected void onResume() {
        if (shared.contains(db.TBL_ID)){
            this.finish();
            startActivity(new Intent(this,HomeActivity.class));
        }
        super.onResume();
    }
}
