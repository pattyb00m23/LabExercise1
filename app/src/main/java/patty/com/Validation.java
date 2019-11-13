package patty.com;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

public class Validation extends AppCompatActivity {

    TextView tvCreate;


       EditText etUser, etpass, etfullname;
    String etUsername, etpassword, fullname;
    int formsuccess, userid;
     DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validation);


      db = new DBHelper(this);

      etUser = findViewById(R.id.etUsername);
       etpass = findViewById(R.id.etPassword);
      etfullname = findViewById(R.id.etFullname);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menubar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {


        switch (item.getItemId())
        {
            case R.id.btnSave:
                etUsername = etUser.getText().toString();
                etpassword = etpass.getText().toString();
                fullname = etfullname.getText().toString();
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
                    map_user.put(db.TBL_USERNAME, etUsername);
                    map_user.put(db.TBL_PASSWORD, etpassword);
                    map_user.put(db.TBL_FULLNAME, fullname);

                    userid = db.createUser(map_user);

                    if (userid < 1)
                    {
                        Toast.makeText(this, "USER SUCCESSFULLY CREATED", Toast.LENGTH_SHORT).show();
                    }
                   else
                    {
                        etUser.setError("Username already existed");
                    }
                }
                break;
            case R.id.btnCancel:
                this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
