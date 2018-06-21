package inc.co.quiz.quiz;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class RegistrationActivity extends AppCompatActivity {

    EditText Username;
    EditText Email;
    EditText Age;
    EditText Password;
    Button register;
    ImageView userProfilePic;
    FirebaseAuth firebaseAuth;
    String user,password,email;
    String age;
    FirebaseStorage firebaseStorage;
    int pick_image = 12;
    Uri imagePath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        if(getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        setUpUIViews();
        firebaseAuth = FirebaseAuth.getInstance();
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = Username.getText().toString();
                password = Password.getText().toString();
                email = Email.getText().toString();
                 age = Age.getText().toString();
                final ProgressDialog progressDialog;
                progressDialog= new ProgressDialog(RegistrationActivity.this);
                progressDialog.setTitle("Registration in progress ..");
                progressDialog.setMessage("We appreciate your patience");
                progressDialog.show();
                 if( validate()){
                firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            sendUserData();
                            progressDialog.dismiss();
                            sendEmailVerification();
                            startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
                            finish();
                        } else
                        {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(),"Registeration Failed",Toast.LENGTH_LONG).show();
                        }
                    }
                });
                }
            }
        });
        userProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Profile Image :"),pick_image);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == pick_image && resultCode== RESULT_OK && data.getData() != null)
        {
            imagePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imagePath);
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(userProfilePic.getContext().getResources(),bitmap);
                circularBitmapDrawable.setCircular(true);
                userProfilePic.setImageDrawable(circularBitmapDrawable);
              userProfilePic.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private boolean validate(){
        Boolean result = false;
        if(user.isEmpty() ||user.length()<5){
           Username.setError("Please enter 5-10 charecters");
           result=true;
        }
        if( password.isEmpty() || Password.length() < 5 ){
            Password.setError("Please enter 5-10 charecters");
            result=true;
        }
        if( email.isEmpty()){
            Email.setError("Please enter valid email !");
            result=true;
        } if(age.isEmpty()){
           Age.setError("I guess you are not immortal");
            result=true;
        }
        if (result== true)
        {
            Toast.makeText(getApplicationContext(),"Wrong Credentials",Toast.LENGTH_LONG).show();
        }
        return !result;
    }


    private void setUpUIViews()
    {
        Username = (EditText)findViewById(R.id.etUserName);
        Password = (EditText)findViewById(R.id.etUserPassword);
        Email = (EditText)findViewById(R.id.etUserEmail);
        register = (Button)findViewById(R.id.btnRegister);
        Age = (EditText)findViewById(R.id.etAge);
        userProfilePic = (ImageView)findViewById(R.id.ivProfile);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
        {
           startActivity(new Intent(RegistrationActivity.this,signupActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }


    private void sendUserData(){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = firebaseDatabase.getReference(firebaseAuth.getUid());
        firebaseStorage = FirebaseStorage.getInstance();
        UserProfile userProfile = new UserProfile(age, email, user);
        myRef.setValue(userProfile);
    }


    private void sendEmailVerification(){
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser!=null){
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        sendUserData();
                        Toast.makeText(RegistrationActivity.this, "Successfully Registered, Verification mail sent!", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(RegistrationActivity.this, "Error in Connection!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
