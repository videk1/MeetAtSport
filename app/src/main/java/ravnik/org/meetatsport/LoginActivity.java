package ravnik.org.meetatsport;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Calendar;

import com.facebook.*;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.internal.CallbackManagerImpl;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.common.hash.Hashing;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    public static CallbackManager mCallbackManager;

    DatabaseConnector database = new DatabaseConnector(this);
    private static final String TAG = "LoginActivity";
    private Logger logger = new Logger();
    String titleName;

    public void onCreateAccount(View view){
        Intent i = new Intent(this,  CreateAcountActivity.class);
        startActivityForResult(i, 1);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {




        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);



        AppEventsLogger.activateApp(this);


        }

    public void fbBtnClick(View view){

        AccessToken currentAccesToken= AccessToken.getCurrentAccessToken();
        final LoginButton loginFacebookButton = (LoginButton) findViewById(R.id.login_button);

        if(currentAccesToken!=null){

            logger.appendLog(getApplicationContext(),TAG,currentAccesToken.getUserId()+ " preteče " +currentAccesToken.getExpires());
            String fbProfileID= currentAccesToken.getUserId().toString();
            Intent intent = new Intent(getApplicationContext(),SportsActivity.class);
            database.open();
            String email=database.getEmailFromAccesToken(fbProfileID);
            String name= database.getCurrentName(email);
            database.close();
            intent.putExtra("loginmail",email);
            intent.putExtra("loginname",name);
            startActivity(intent);


        }
        else{

            mCallbackManager = CallbackManager.Factory.create();
            loginFacebookButton.setReadPermissions(Arrays.asList("public_profile","email","user_birthday"));
            loginFacebookButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {

                @Override
                public void onSuccess(final LoginResult loginResult) {

                    logger.appendLog(getApplicationContext(),TAG,"succesfuly generated Callback");
                    GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {

                        @Override
                        public void onCompleted(JSONObject object, GraphResponse response) {
                            try {

                                String id = object.getString("id");
                                String name = object.getString("name");
                                String email = object.getString("email");
                                String[] birthdaydata = object.getString("birthday").split("/");
                                String age = getAge(birthdaydata[2], birthdaydata[0], birthdaydata[1]);
                                String gender = object.getString("gender");
                                String fbProfileId= loginResult.getAccessToken().getUserId().toString();
                                database.open();
                                boolean existinguser= database.checkEmailExistence(email);


                                if(existinguser){
                                    database.updateUser(database.getCurrentID(email),name,email,age,gender,database.getSHAHashIfExist(email),fbProfileId);
                                }
                                else {
                                    database.insertUser(name,email,age,gender,null,fbProfileId);
                                }
                                database.close();


                                logger.appendLog(getApplicationContext(),TAG,"Got fb user"+name.toString());

                                Intent intent = new Intent(getApplicationContext(),SportsActivity.class);
                                intent.putExtra("loginmail",email);
                                intent.putExtra("loginname",name);
                                startActivity(intent);


                            } catch (JSONException e) {
                                e.printStackTrace();
                                logger.appendLog(getApplicationContext(),TAG,e.toString());
                            }

                        }
                    });

                    Bundle parameters = new Bundle();
                    parameters.putString("fields", "id,name,gender,email,birthday");
                    request.setParameters(parameters);
                    request.executeAsync();
                }

                @Override
                public void onCancel() {

                }

                @Override
                public void onError(FacebookException error) {

                }
            });


        }





    }









    public void onLoginBtnClick(View view){

        EditText loginEmail= (EditText)findViewById(R.id.etLoginUserEmail);
        EditText loginPassword= (EditText) findViewById(R.id.etLoginPassword);

        String email= loginEmail.getText().toString();
        String password =loginPassword.getText().toString();
        final String enteredpassword = Hashing.sha256().hashString(password, StandardCharsets.UTF_8).toString();
        if(email.length()>0 && (password.length()>0 )){
            database.open();
            boolean checkMail= database.checkEmailExistence(email);
            String correctpasswordHash= database.getSHAHashIfExist(email);
            if(checkMail){
                String name= database.getCurrentName(email);
            }

            database.close();




            if(checkMail && (enteredpassword.equals(correctpasswordHash))){
                Intent intent = new Intent(this,SportsActivity.class);
                intent.putExtra("loginmail",email);
                intent.putExtra("loginname",titleName);
                startActivity(intent);
                logger.appendLog(this,TAG,"USPEŠNA STANDARNA PRIJAVA");

            }
            else {

                Toast toast = Toast.makeText(getApplicationContext(),"NAPAČNO UPORABNIŠKO IME ALI GESLO",Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                toast.setDuration(Toast.LENGTH_LONG);
                toast.show();

            }



        }






    }



    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK){
                String valid_email=data.getStringExtra("valid_email");
                Toast toast = Toast.makeText(getApplicationContext(),"Sedaj se lahko prijavite s "+ valid_email,Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                toast.setDuration(Toast.LENGTH_LONG);
                toast.show();
            }
        }
        if (requestCode==CallbackManagerImpl.RequestCodeOffset.Login.toRequestCode()){
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
        }



    }


    private String getAge(String syear, String smonth, String sday){
        int year=Integer.parseInt(syear);
        int month=Integer.parseInt(smonth);
        int day=Integer.parseInt(sday);

        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(year, month, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)){
            age--;
        }

        Integer ageInt = new Integer(age);
        String ageS = ageInt.toString();

        return ageS;
    }



}
