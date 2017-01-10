package ravnik.org.meetatsport;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class AddSportActivity extends AppCompatActivity {
    String loginEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sport);
        Intent intent= getIntent();
        loginEmail= intent.getExtras().getString("loginmail");
        TextView twEmail= (TextView)findViewById(R.id.tw_current_mail);
        twEmail.setText(loginEmail);

    }
    public void OnAddSportButtonClick(View view){
        Intent intent= new Intent(this,SportsActivity.class);
        intent.putExtra("loginmail",loginEmail);
        startActivity(intent);

    }
}
