package inc.co.quiz.quiz;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

public class LoginActivity extends AppCompatActivity {

    EditText username;
    EditText password1;
    Button login;
    Button signup;
    String email ="",password = "";
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    TextView forgot;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setupUIViews();
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,signupActivity.class);
                startActivity(intent);
            }
        });
        forgot = findViewById(R.id.forgot);

        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startActivity(new Intent(LoginActivity.this , PasswordActivity.class));
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null)
        {
            finish();
            startActivity(new Intent(LoginActivity.this,SelectionActivity.class));
        }
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    email = username.getText().toString();
                    password = password1.getText().toString();
                    final ProgressDialog progressDialog;
                    progressDialog = new ProgressDialog(LoginActivity.this);
                    progressDialog.setTitle("Loading ..");
                    progressDialog.setMessage("Just a moment ... establishing connection");
                    progressDialog.show();
                    firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), "Game On!!", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(LoginActivity.this, SelectionActivity.class));
                                finish();
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), "There seems to be Something wrong !", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            });
        }
    private void setupUIViews(){
        username =(EditText)findViewById(R.id.username);
        password1=(EditText)findViewById(R.id.password);
        login =(Button)findViewById(R.id.button_login);
        signup=(Button)findViewById(R.id.button_signup);

    }
}
