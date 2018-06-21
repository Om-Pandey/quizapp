package inc.co.quiz.quiz;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class PasswordActivity extends AppCompatActivity {

    Button buttonreset;
    EditText resetemail;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);
         resetemail = findViewById(R.id.reset_email);
        buttonreset = findViewById(R.id.button_reset);
        firebaseAuth =FirebaseAuth.getInstance();
        buttonreset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog progressDialog;
                progressDialog= new ProgressDialog(PasswordActivity.this);
                progressDialog.setTitle("Loading ..");
                progressDialog.setMessage("Just a moment ... establishing connection");
                progressDialog.show();
                String useremail = resetemail.getText().toString();
                firebaseAuth.sendPasswordResetEmail(useremail).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Please check your mail !", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(PasswordActivity.this, LoginActivity.class));
                            finish();
                        } else
                        {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(),"Oops! Something is wrong",Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }
}
