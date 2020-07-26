package com.panshuljindal.yourlocation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    EditText emailId,password;
    Button loginButton;
    TextView signup,forgotPassword;
    FirebaseAuth mFirebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emailId=findViewById(R.id.editTextUsername);
        password=findViewById(R.id.editTextMobile);
        loginButton=findViewById(R.id.buttonLogin);
        signup=findViewById(R.id.textViewSignUp);
        forgotPassword=findViewById(R.id.textViewForget);
        mFirebaseAuth=FirebaseAuth.getInstance();
        clickListerners();
    }
    public void clickListerners(){
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,forgotpassword.class));
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,signuppage.class);
                startActivity(intent);
            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animAlpha = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.anim_alpha);
                v.startAnimation(animAlpha);
                if (checkempty()) {
                    if (checkemail()) {
                        String email = emailId.getText().toString();
                        String passwordd = password.getText().toString();
                        mFirebaseAuth.signInWithEmailAndPassword(email, passwordd).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(MainActivity.this,homepage.class));

                                } else if (!task.isSuccessful()) {
                                    Toast.makeText(MainActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                                    emailId.setText("");
                                    password.setText("");
                                }
                            }
                        });
                    }
                }
            }
        });
    }
    public boolean checkempty(){
        if(emailId.getText().length()==0){
            emailId.setError("Please enter email id");
            emailId.requestFocus();
            return false;
        }
        else if(password.getText().length()==0){
            password.setError("Please enter the password");
            password.requestFocus();
            return false;
        }
        else if(password.length()<=6){
            password.setError("Please enter minimum of 6 characters");
            password.requestFocus();
            return false;
        }
        return true;
    }
    public boolean checkemail(){
        String tempemail=emailId.getText().toString().trim();
        Pattern emailpattern = Pattern.compile("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+");
        Matcher emailMatcher= emailpattern.matcher(tempemail);
        if(emailMatcher.matches()){
            return true;
        }
        emailId.setError("Please enter a valid email id");
        emailId.requestFocus();
        return false;
    }
}
