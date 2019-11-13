package patty.com;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class EditUserActivity extends AppCompatActivity {

    EditText etUser, etpass, etfullname;
    String etUsername, etpassword, fullname;
    int formsuccess, userid;
    ArrayList<HashMap<String, String>> Selected_user;

    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);


        db = new DBHelper(this);

        etUser = findViewById(R.id.etUsername);
        etpass = findViewById(R.id.etPassword);
        etfullname = findViewById(R.id.etFullname);

        Intent intent = getIntent();
        userid = intent.getIntExtra(db.TBL_ID, 0);

        Selected_user = db.getSelectedUserData(userid);
        etUser.setText(Selected_user.get(0).get(db.TBL_USERNAME));
        etpass.setText(Selected_user.get(0).get(db.TBL_PASSWORD));
        etfullname.setText(Selected_user.get(0).get(db.TBL_FULLNAME));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menubar,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.btnSave:
                etUsername = etUser.getText().toString().trim();
                etpassword = etpass.getText().toString().trim();
                fullname = etfullname.getText().toString().trim();

                formsuccess = 3;

                if (etUsername.equals(""))
                {
                    etUser.setError("This field is Required");
                    formsuccess--;
                }

                if (etpassword.equals(""))
                {
                    etpass.setError("This field is Required");
                    formsuccess--;
                }

                if (fullname.equals(""))
                {
                    etfullname.setError("This field is Required");
                    formsuccess--;
                }

                if (formsuccess == 3)
                {
                    HashMap<String, String> map_user = new HashMap<>();
                    map_user.put(db.TBL_ID, String.valueOf(userid));
                    map_user.put(db.TBL_USERNAME, etUsername);
                    map_user.put(db.TBL_PASSWORD, etpassword);
                    map_user.put(db.TBL_FULLNAME, fullname);

                    db.updateUser(map_user);
                    Toast.makeText(this, "FORM SUCCESSFULLY VALIDATED", Toast.LENGTH_SHORT).show();
                    this.finish();
                }
                break;
            case R.id.btnCancel:
                this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
