package ravnik.org.meetatsport;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;

public class ChangePasswordActivity extends AppCompatActivity {

    public String currentEmail;
    DatabaseConnector database = new DatabaseConnector(this);
    String password1;
    String password2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        setTitle("Spremenite geslo");
    }


    public void onChangePasswordBtnClick(View view){
        Intent intent= getIntent();
        currentEmail= intent.getExtras().getString("loginmail");
        EditText EditPassword1= (EditText)findViewById(R.id.etChangePassword1);
        EditText EditPassword2= (EditText)findViewById(R.id.etChangePassword2);

        password1= EditPassword1.getText().toString();
        password2= EditPassword2.getText().toString();

        if( password1.equals(password2)){
            new InsertToDatabaseTask().execute(this);
        }
        else {
            Toast toast = Toast.makeText(getApplicationContext(), "      Gesli se ne ujemata       ", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            toast.setDuration(Toast.LENGTH_LONG);
            toast.show();

        }
    }




    private class InsertToDatabaseTask extends AsyncTask<Object, Object, Cursor> {

        boolean emailExist;

        @Override
        protected Cursor doInBackground(Object... params) {


            database.open();
            final String sha256 = Hashing.sha256().hashString(password1, StandardCharsets.UTF_8).toString();
            database.updateUserPassword(database.getCurrentID(currentEmail),sha256);
            return null;
        }

        @Override
        protected void onPostExecute(Cursor result) {

            database.close();
            Toast toast = Toast.makeText(getApplicationContext(), "      USPEŠNO STE POSODOBILI GESLO        ", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            toast.setDuration(Toast.LENGTH_LONG);
            toast.show();
            Intent intent = new Intent(getApplicationContext(),SportsActivity.class);
            intent.putExtra("loginmail",currentEmail);
            startActivity(intent);
        }


    }
}
