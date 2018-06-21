package inc.co.quiz.quiz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class signupActivity extends AppCompatActivity {

    Button register_start;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        register_start= (Button)findViewById(R.id.button_reg);
        register_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(signupActivity.this, RegistrationActivity.class);
                startActivity(intent);
                Toast.makeText(getApplicationContext(),"Click on Avatar to add profile pic",Toast.LENGTH_LONG).show();
            }
        });
        if(getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
        {
            startActivity(new Intent(signupActivity.this,LoginActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
}
