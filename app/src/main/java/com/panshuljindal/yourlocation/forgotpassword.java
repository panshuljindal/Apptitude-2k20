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
import com.google.firebase.auth.FirebaseAuth;

public class forgotpassword extends AppCompatActivity {
    EditText email;
    Button resetpassword;
    FirebaseAuth mFirebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);

        email=findViewById(R.id.editText);
        resetpassword=findViewById(R.id.button);
        mFirebaseAuth=FirebaseAuth.getInstance();
        clickListeners();
    }
    public void clickListeners(){
        resetpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animAlpha = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.anim_alpha);
                v.startAnimation(animAlpha);
                final String emailid = email.getText().toString();
                if (checkempty()) {
                    mFirebaseAuth.sendPasswordResetEmail(emailid).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(forgotpassword.this, "Check email", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(forgotpassword.this,MainActivity.class));
                            }
                            else{
                                Toast.makeText(forgotpassword.this, "Please Try Again", Toast.LENGTH_SHORT).show();
                                email.setText("");
                                email.requestFocus();
                            }
                        }
                    });
                }
            }
        });
    }
    public boolean checkempty(){
        if(email.getText().length()==0){
            email.setError("Please enter email id");
            email.requestFocus();
            return false;
        }
        return true;
    }
}
