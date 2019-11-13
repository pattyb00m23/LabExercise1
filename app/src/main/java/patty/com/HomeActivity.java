package patty.com;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.HashMap;

public class HomeActivity extends AppCompatActivity {

    ListView lvUsers;
    DBHelper db;
    ArrayList<HashMap<String, String>> all_users;
    ListViewAdapter adapter;

    SharedPreferences shared;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        db = new DBHelper(this);
        lvUsers = findViewById(R.id.lvUsers);
        fetch_users();

        shared = getSharedPreferences("VBFC", Context.MODE_PRIVATE);

    }

    private void fetch_users()
    {
        all_users = db.getallUsers();
        adapter = new ListViewAdapter(this,R.layout.adapter_user, all_users);
        lvUsers.setAdapter(adapter);
        registerForContextMenu(lvUsers);
    }

    private class ListViewAdapter extends ArrayAdapter
    {
        LayoutInflater inflater;
        ArrayList<HashMap<String, String>> all_users;

        TextView tvUsername, tvFullnames;
        ImageView ivEdit,ivDelete;




        public ListViewAdapter(Context context, int resource, ArrayList<HashMap<String,String>> all_users) {
            super(context, resource,all_users);
            inflater = LayoutInflater.from(context);
            this.all_users = all_users;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            convertView = inflater.inflate(R.layout.adapter_user,parent,false);
            tvUsername = convertView.findViewById(R.id.tvusername);
            tvFullnames = convertView.findViewById(R.id.tvfullname);
            ivEdit = convertView.findViewById(R.id.ivEdit);
            ivDelete = convertView.findViewById(R.id.ivdelete);

            tvUsername.setText(all_users.get(position).get(db.TBL_USERNAME));
            tvFullnames.setText(all_users.get(position).get(db.TBL_FULLNAME));



            ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int userid = Integer.parseInt(all_users.get(position).get(db.TBL_ID));
                    db.deleteUser(userid);
                    Toast.makeText(HomeActivity.this,"USER SUCCESSFULLY DELETED",Toast.LENGTH_SHORT).show();
                    fetch_users();
                }
            });

            ivEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int userid = Integer.parseInt(all_users.get(position).get(db.TBL_ID));
                    Intent intent = new Intent(getBaseContext(), EditUserActivity.class);
                    intent.putExtra(db.TBL_ID, userid);
                    startActivity(intent);
                }
            });

            return convertView;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.logout,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.btnlogout:
                new AlertDialog.Builder(this)
                        .setTitle("Logout")
                        .setMessage("Are you sure you want to logout?")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                shared.edit().remove(db.TBL_ID).commit();
                                Toast.makeText(HomeActivity.this,"You've been Successfully logout", Toast.LENGTH_SHORT).show();
                               startActivity(new Intent(getBaseContext(),LoginActivity.class));
                                HomeActivity.this.finish();
                            }
                        })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        fetch_users();
        if (shared.contains(db.TBL_ID))
        {
            this.finish();
            startActivity(new Intent(this,LoginActivity.class));
        }
        super.onResume();
    }
}
