package ravnik.org.meetatsport;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SingleSportActivity extends AppCompatActivity {
    private static final String JSON_URL = "http://demo2800873.mockable.io/locations";
    private String currentSport;
    private String currentEmail;
    List<Item> items = new ArrayList<>();
    ExpandableAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singlesport);
        Intent intent = getIntent();
        currentSport= intent.getExtras().getString("sport");
        currentEmail= intent.getExtras().getString("loginmail");

        VolleySingleton requestQueue= VolleySingleton.getInstance(getApplicationContext());
        requestQueue.addToRequestQueue(makeJsonObjectRequest(JSON_URL));

        ListView lvItems = (ListView) findViewById(R.id.lv_items);
        adapter = getAdapter();
        lvItems.setAdapter(adapter);
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ExpandableAdapter adapter = (ExpandableAdapter) parent.getAdapter();

                Item item = (Item) adapter.getItem(position);
                if(item != null){
                    if(item.isExpanded){
                        item.isExpanded = false;

                    }else{
                        item.isExpanded = true;
                    }
                }

                adapter.notifyDataSetChanged();
            }
        });

        Log.d("DEBELI,","FEBELi");

    }

    public JsonObjectRequest makeJsonObjectRequest(String jsonURL){
        Log.d("cund", "brund");
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, jsonURL,

                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            JSONArray jsonArrayFootball = response.getJSONArray(currentSport);
                            for(int i=0; i<jsonArrayFootball.length();i++){
                                Item item= new Item();
                                JSONObject currentObject= jsonArrayFootball.getJSONObject(i);

                                Object currentCity =currentObject.get("city");
                                Object currentPlayground =currentObject.get("playground");
                                Object currentCurrentPlayers =currentObject.get("current_players");
                                Object currentNeededPlayers =currentObject.get("needed_players");
                                Object currentGoogleMapsX =currentObject.get("googlemapsx");
                                Object currentGoogleMapxY =currentObject.get("googlemapsy");
                                Object currentInfo =currentObject.get("info");

                                item.title=currentCity.toString()+"-"+ currentPlayground.toString();
                                item.current_players="Trenutno število igralcev:"+currentCurrentPlayers.toString();
                                item.needed_players= "Število potrebnih igralcev: "+currentNeededPlayers.toString();
                                item.location="Navodila za pot";
                                item.contact_info="Kontakt: "+currentInfo.toString();
                                items.add(item);


                            }

                            adapter.notifyDataSetChanged();
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


    private ExpandableAdapter getAdapter(){

        for (Item i : items){
            Log.d("TAG", i.title);
        }


        return new ExpandableAdapter(this, items);
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

