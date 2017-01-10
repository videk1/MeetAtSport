package ravnik.org.meetatsport;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;




public class SportsActivity extends AppCompatActivity {

    private static final String TAG = "SportsActivity";
    String currentName;
    String currentEmail;
    private TextView FootballDescriptionTextView;
    private TextView BasketBallDescriptionTextView;
    private TextView IceHockeyDescriptionTextView;
    private TextView RunningDescriptionTextView;
    private TextView FittnesDescriptionTextView;
    DatabaseConnector database= new DatabaseConnector(this);
    private static final String JSON_URL = "http://demo2800873.mockable.io/locations";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sports);

        FootballDescriptionTextView=(TextView)findViewById(R.id.info_text_footbal_description);
        BasketBallDescriptionTextView=(TextView)findViewById(R.id.info_text_basketball_description);
        IceHockeyDescriptionTextView=(TextView)findViewById(R.id.info_text_ice_hockey_description);
        RunningDescriptionTextView=(TextView)findViewById(R.id.info_text_running_description);
        FittnesDescriptionTextView=(TextView)findViewById(R.id.info_text_fittnes_description);


        Intent intent = getIntent();
        currentEmail= intent.getExtras().getString("loginmail");
        database.open();
        currentName= database.getCurrentName(currentEmail);
        database.close();
        setTitle("Dobrodošli "+currentName);


        VolleySingleton requestQueue= VolleySingleton.getInstance(getApplicationContext());
        requestQueue.addToRequestQueue(makeJsonObjectRequest(JSON_URL));


    }

    public void onFootballCardClick(View view){
        Intent intent = new Intent(this,SingleSportActivity.class);
        intent.putExtra("sport","football");
        intent.putExtra("loginmail",currentEmail);
        startActivity(intent);
    }

    public void onBasketballCardClick(View view){
        Intent intent = new Intent(this,SingleSportActivity.class);
        intent.putExtra("sport","basketball");
        intent.putExtra("loginmail",currentEmail);
        startActivity(intent);
    }
    public void onIceHockeyCardClick(View view){
        Intent intent = new Intent(this,SingleSportActivity.class);
        intent.putExtra("sport","icehockey");
        intent.putExtra("loginmail",currentEmail);
        startActivity(intent);
    }
    public void onRunningCardClick(View view){
        Intent intent = new Intent(this,SingleSportActivity.class);
        intent.putExtra("sport","running");
        intent.putExtra("loginmail",currentEmail);
        startActivity(intent);
    }
    public void onFittnesCardClick(View view){
        Intent intent = new Intent(this,SingleSportActivity.class);
        intent.putExtra("sport","fittness");
        intent.putExtra("loginemail",currentEmail);
        startActivity(intent);
    }

    public JsonObjectRequest makeJsonObjectRequest(String jsonURL){

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, jsonURL,

                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            JSONArray jsonArrayFootball = response.getJSONArray("football");
                            FootballDescriptionTextView.setText("Išče se "+ getAllAvailablePlayers(jsonArrayFootball)+ " igralcev");

                            JSONArray jsonArrayBasketball = response.getJSONArray("basketball");
                            BasketBallDescriptionTextView.setText("Išče se "+ getAllAvailablePlayers(jsonArrayBasketball)+ " igralcev");

                            JSONArray jsonArrayIceHockey = response.getJSONArray("icehockey");
                            IceHockeyDescriptionTextView.setText("Išče se "+ getAllAvailablePlayers(jsonArrayIceHockey)+ " igralcev");

                            JSONArray jsonArrayRunning = response.getJSONArray("running");
                            RunningDescriptionTextView.setText("Išče se "+ getAllAvailablePlayers(jsonArrayRunning)+ " tekačev");

                            JSONArray jsonArrayFittnes = response.getJSONArray("fittness");
                            FittnesDescriptionTextView.setText("Išče se " + getAllAvailablePlayers(jsonArrayFittnes)+ " oseb");




                        }

                        catch (JSONException e) {

                            e.printStackTrace();
                        }
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),"Napaka pri povezavi s strežnikom",Toast.LENGTH_LONG);
                        error.printStackTrace();
                        Log.e("Volley", error.toString());
                    }
                }
        );

        return jsonObjectRequest;
    }


    public long getAllAvailablePlayers(JSONArray array){
        long allPlayers=0;

        for(int i=0;i< array.length(); i++){
            try{
                JSONObject currentObject= array.getJSONObject(i);
                Object current =currentObject.get("needed_players");
                long curentLong= Long.parseLong(current.toString());
                allPlayers= allPlayers+curentLong;
            }
            catch (JSONException jex){
                jex.printStackTrace();
            }

        }



        return allPlayers;
    }

    public void OnFabClick(View view) {
        Intent intent= new Intent(this, AddSportActivity.class);
        intent.putExtra("loginmail",currentEmail);
        startActivity(intent);

    }



    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }

    public boolean  onLogoutOptionClick(MenuItem item){
        AccessToken currentAccesToken= AccessToken.getCurrentAccessToken();
        if(currentAccesToken!=null){
            LoginManager.getInstance().logOut();

        }

        Intent intent= new Intent(this, LoginActivity.class);

        startActivity(intent);
        return true;

    }
    public boolean onAddSportClick(MenuItem item){
        Intent intent= new Intent(this, AddSportActivity.class);
        intent.putExtra("loginmail",currentEmail);
        startActivity(intent);
        return true;
    }


    public boolean  onEditOptionClick(MenuItem item){
        Intent intent= new Intent(this, ChangePasswordActivity.class);
        intent.putExtra("loginmail",currentEmail);
        startActivity(intent);
        return true;

    }
    public boolean onEditProfileOptionClick(MenuItem item){
        Intent intent = new Intent(this, EditProfileActivity.class);
        intent.putExtra("loginmail", currentEmail);
        startActivity(intent);
        return true;
    }


}
