package com.panshuljindal.yourlocation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class signuppage extends AppCompatActivity {
    public EditText name,mobilenumber,email,password;
    int count;
    TextView login;
    Button signup;
    FirebaseAuth mFireBaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signuppage);

        name=findViewById(R.id.editTextName);
        mobilenumber=findViewById(R.id.editTextMobile);
        email=findViewById(R.id.editTextEmail);
        password=findViewById(R.id.editTextPassword);
        login=findViewById(R.id.textViewSignUp);
        signup=findViewById(R.id.buttonSignup);
        mFireBaseAuth=FirebaseAuth.getInstance();
        clickListeners();
    }
    public void clickListeners(){
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(signuppage.this,MainActivity.class);
                startActivity(i);
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animAlpha = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.anim_alpha);
                v.startAnimation(animAlpha);
                InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
                View view = getCurrentFocus();
                if (view == null) {
                    view = new View(getApplicationContext());
                }
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                final String emailid=email.getText().toString();
                String names=name.getText().toString();
                String mobiles=mobilenumber.getText().toString();
                String passwords=password.getText().toString();
                if(checkempty()) {
                    if (checkemail()) {
                        mFireBaseAuth.createUserWithEmailAndPassword(emailid, passwords).addOnCompleteListener(signuppage.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    try {
                                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                                        DatabaseReference myref = database.getReference("Users").child(email.getText().toString().replace(".","_"));
                                        myref.child("Sign In").child("Name").setValue(name.getText().toString());
                                        myref.child("Sign In").child("Mobile Number").setValue(mobilenumber.getText().toString());
                                        myref.child("Sign In").child("Password").setValue(password.getText().toString());
                                        myref.child("Count").setValue(0);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    Toast.makeText(signuppage.this, "SignUp Successfull", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(signuppage.this, MainActivity.class);
                                    startActivity(i);
                                } else {
                                    Toast.makeText(signuppage.this, "Signup Failed please try again", Toast.LENGTH_SHORT).show();
                                    name.setText("");
                                    mobilenumber.setText("");
                                    email.setText("");
                                    password.setText("");
                                }
                            }
                        });
                    }
                }
            }
        });
    }
    public boolean checkemail(){
        String tempemail=email.getText().toString().trim();
        Pattern emailpattern = Pattern.compile("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+");
        Matcher emailMatcher= emailpattern.matcher(tempemail);
        if(emailMatcher.matches()){
            return true;
        }
        email.setError("Please enter a valid email id");
        email.requestFocus();
        return false;
    }
    public Boolean checkempty(){
        if(name.getText().length()==0){
            name.setError("Please enter your name");
            name.requestFocus();
            return false;
        }
        else if(mobilenumber.getText().length()==0){
            mobilenumber.setError("Please enter your mobile number");
            mobilenumber.requestFocus();
            return false;
        }
        else if(mobilenumber.getText().length()>10){
            mobilenumber.setError("Please enter valid mobile number");
            mobilenumber.requestFocus();
            return false;
        }
        else if(email.getText().length()==0){
            email.setError("Please enter your email");
            email.requestFocus();
            return false;
        }
        else if(password.getText().length()==0){
            password.setError("Please enter your password");
            password.requestFocus();
            return false;
        }
        else if(password.getText().length()<=6){
            password.setError("Enter minimum of 6 characters");
            password.requestFocus();
            return false;
        }
        return true;
    }
}
