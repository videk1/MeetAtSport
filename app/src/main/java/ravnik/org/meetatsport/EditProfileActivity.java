package ravnik.org.meetatsport;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;

public class EditProfileActivity extends AppCompatActivity {
    String currentEmail;
    long id;
    String fbId;
    String password;
    String newName;
    String newGender;
    String newAge;
    DatabaseConnector database = new DatabaseConnector(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        setTitle("Uredi profil");
        Intent intent = getIntent();
        currentEmail = intent.getExtras().getString("loginmail");
        TextView tw = (TextView) findViewById(R.id.textView2);
        tw.setText("Email: " + currentEmail);
        database.open();
        String previousName = database.getCurrentName(currentEmail);
        EditText etName = (EditText) findViewById(R.id.etNameEditProfile);
        etName.setText(previousName);
        String previousAge = database.getCurrentAge(currentEmail);
        EditText etAge = (EditText) findViewById(R.id.etAgeEditProfile);
        etAge.setText(previousAge);
        String previousGener = database.getCurrentGender(currentEmail);
        Spinner s = (Spinner) findViewById(R.id.sGenderEditProfile);
        s.setSelection(previousGener == "Moški" ? 1 : 0);
        id = database.getCurrentID(currentEmail);
        fbId = database.getCurrentfbProfileID(currentEmail);
        password = database.getSHAHashIfExist(currentEmail);
        database.close();
    }
    public void onClickUrediProfil(View view){

        EditText etName = (EditText) findViewById(R.id.etNameEditProfile);
        newName = etName.getText().toString();

        Spinner spinnerGender = (Spinner) findViewById(R.id.sGenderEditProfile);
        newGender = spinnerGender.getSelectedItem().toString();

        EditText etAge = (EditText) findViewById(R.id.etAgeEditProfile);
        newAge = etAge.getText().toString();

        if(newName != "" && newAge != ""){
            new InsertToDatabaseTask().execute(this);
        }
        else{
            Toast toast = Toast.makeText(getApplicationContext(), "      Polja ne smejo biti prazna!       ", Toast.LENGTH_LONG);
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

            database.updateUser(id, newName, currentEmail, newAge, newGender, password, fbId);
            return null;
        }

        @Override
        protected void onPostExecute(Cursor result) {

            database.close();
            Toast toast = Toast.makeText(getApplicationContext(), "      USPEŠNO POSODOBLJEN PROFIL        ", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            toast.setDuration(Toast.LENGTH_LONG);
            toast.show();
            Intent intent = new Intent(getApplicationContext(),SportsActivity.class);
            intent.putExtra("loginmail",currentEmail);
            startActivity(intent);
        }


    }
}
