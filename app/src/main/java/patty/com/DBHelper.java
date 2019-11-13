package patty.com;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class DBHelper extends SQLiteOpenHelper {

    public static final String TBL_USERS = "users",
            TBL_ID = "id",
            TBL_USERNAME = "username",
            TBL_PASSWORD = "password",
           TBL_FULLNAME = "fullname";

   SQLiteDatabase dbReadable = getReadableDatabase();
   SQLiteDatabase dbWritable = getWritableDatabase();

    public DBHelper(Context context) {
        super(context, "STID", null, 1);


    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql_create_users_table = String.format("CREATE TABLE %s(%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s TEXT, %s TEXT)",
               TBL_USERS, TBL_ID, TBL_USERNAME, TBL_PASSWORD, TBL_FULLNAME);
       db.execSQL(sql_create_users_table);

   }

   @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

   }

    public int createUser(HashMap<String, String> map_user)
    {

        int userid = 0;

       String sql_check_username = String.format("SELECT * FROM %s WHERE %s = '%s'",
                TBL_USERS, TBL_USERNAME, map_user.get(TBL_USERNAME));

        Cursor cur = dbReadable.rawQuery(sql_check_username, null);

        if (cur.moveToNext()) {
            userid = cur.getInt(cur.getColumnIndex(TBL_ID));
        }
        else
           {
           // Insertion of data to database
           ContentValues val = new ContentValues();
            val.put(TBL_USERNAME, map_user.get(TBL_USERNAME));
           val.put(TBL_PASSWORD, map_user.get(TBL_PASSWORD));
            val.put(TBL_FULLNAME, map_user.get(TBL_FULLNAME));
           dbWritable.insert(TBL_USERS, null, val);
        }
        return userid;

    }

    public int checkUser(String username, String password){
        int userid = 0;

        String sql_check_user = String.format("SELECT * FROM %s WHERE %s = '%s' and %s = '%s'",
                TBL_USERS, TBL_USERNAME,username,TBL_PASSWORD,password);
        Cursor cursor = dbReadable.rawQuery(sql_check_user,null);
        if (cursor.moveToNext())
        {
            userid = cursor.getInt(cursor.getColumnIndex(TBL_ID));
        }
        return userid;
    }

    public ArrayList<HashMap<String, String>> getallUsers() {

        ArrayList<HashMap<String, String>> all_users = new ArrayList();
        String sql_get_all_users = String.format("SELECT * FROM %s ORDER BY %s ASC",
                TBL_USERS,TBL_USERNAME);
        Cursor cursor = dbReadable.rawQuery(sql_get_all_users,null);

        while (cursor.moveToNext())
        {
            HashMap<String,String>map_user = new HashMap();
            map_user.put(TBL_ID, cursor.getString(cursor.getColumnIndex(TBL_ID)));
            map_user.put(TBL_FULLNAME, cursor.getString(cursor.getColumnIndex(TBL_FULLNAME)));
            map_user.put(TBL_USERNAME, cursor.getString(cursor.getColumnIndex(TBL_USERNAME)));
            all_users.add(map_user);
        }
        cursor.close();

        return all_users;
    }

    public void deleteUser(int userid) {
        dbWritable.delete(TBL_USERS,TBL_ID+ "=" + userid, null);
    }

    public ArrayList<HashMap<String, String>> getSelectedUserData(int userid) {

        String sql = "Select * from "+TBL_USERS+" where "+TBL_ID+"="+userid;
        Cursor cursor = dbReadable.rawQuery(sql, null);

        ArrayList<HashMap<String, String>> Selected_user = new ArrayList();

        while (cursor.moveToNext()){
            HashMap<String, String> map_user = new HashMap();
            map_user.put(TBL_USERNAME, cursor.getString(cursor.getColumnIndex(TBL_USERNAME)));
            map_user.put(TBL_PASSWORD, cursor.getString(cursor.getColumnIndex(TBL_PASSWORD)));
            map_user.put(TBL_FULLNAME, cursor.getString(cursor.getColumnIndex(TBL_FULLNAME)));
            Selected_user.add(map_user);
        }
        cursor.close();
        return Selected_user;
    }

    public void updateUser(HashMap<String, String> map_user) {
        ContentValues val = new ContentValues();
        val.put(TBL_USERNAME, map_user.get(TBL_USERNAME));
        val.put(TBL_PASSWORD, map_user.get(TBL_PASSWORD));
        val.put(TBL_FULLNAME, map_user.get(TBL_FULLNAME));
        dbWritable.update(TBL_USERS,val, TBL_ID+ "=" + map_user.get(TBL_ID),null);
    }
}
