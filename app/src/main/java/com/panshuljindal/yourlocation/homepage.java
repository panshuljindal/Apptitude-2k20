package com.panshuljindal.yourlocation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class homepage extends AppCompatActivity {
    TextView textView1,textView2,textView3,textView4,textView5;
    Button LButton,showMap;
    FusedLocationProviderClient fusedLocationProviderClient;
    FirebaseAuth mFireBaseAuth;
    List<Address> addresses;
    String count;
    int countint;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        LButton=findViewById(R.id.getlocation);
        showMap=findViewById(R.id.showMap);
        textView1=findViewById(R.id.textview1);
        textView2=findViewById(R.id.textview2);
        textView3=findViewById(R.id.textview3);
        textView4=findViewById(R.id.textview4);
        textView5=findViewById(R.id.textview5);

        mFireBaseAuth=FirebaseAuth.getInstance();
        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this);
        clickListeners();
    }
    public void clickListeners(){
        LButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animAlpha = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.anim_alpha);
                v.startAnimation(animAlpha);
                if(ActivityCompat.checkSelfPermission(homepage.this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                    fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                        @Override
                        public void onComplete(@NonNull Task<Location> task) {
                            Location location = task.getResult();
                            if(location !=null){
                                showMap.setVisibility(View.VISIBLE);
                                try{
                                    Geocoder geocoder=new Geocoder(homepage.this,Locale.getDefault());
                                    addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                                    textView1.setText(Html.fromHtml(
                                            "<font color='#6200EE'><b>Latitude :</b><br></font>"
                                            + addresses.get(0).getLatitude()));
                                    textView2.setText(Html.fromHtml(
                                            "<font color='#6200EE'><b>Longitude :</b><br></font>"
                                                    + addresses.get(0).getLongitude()));
                                    textView3.setText(Html.fromHtml(
                                            "<font color='#6200EE'><b>Country Name :</b><br></font>"
                                                    + addresses.get(0).getCountryName()));
                                    textView5.setText(Html.fromHtml(
                                            "<font color='#6200EE'><b>Address :</b><br></font>"
                                                    + addresses.get(0).getAddressLine(0)));
                                    textView4.setText(Html.fromHtml(
                                            "<font color='#6200EE'><b>Locality :</b><br></font>"
                                                    + addresses.get(0).getLocality()));
                                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                                    Intent intents =getIntent();
                                    String email;
                                    email = intents.getStringExtra("Email");
                                    DatabaseReference myref = database.getReference("Users").child(email.replace(".","_"));
                                    myref.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            count=dataSnapshot.child("Count").getValue().toString();
                                            Log.i("count",count);
                                            countint = Integer.parseInt(count)+1;
                                        }
                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                    myref.child("Count").setValue(countint);
                                    myref.child("Location").child("Latitude").setValue(addresses.get(0).getLatitude());
                                    myref.child("Location").child("Longitude").setValue(addresses.get(0).getLongitude());
                                    myref.child("Location").child("Country Name").setValue(addresses.get(0).getCountryName());
                                    myref.child("Location").child("Address").setValue(addresses.get(0).getLocality());
                                    myref.child("Location").child("Address").setValue(addresses.get(0).getAddressLine(0));

                                }
                                catch(IOException e){
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                }
                else{
                    ActivityCompat.requestPermissions(homepage.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},44);
                }
            }
        });
        showMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(homepage.this,MapsActivity.class);
                intent.putExtra("Latitude",addresses.get(0).getLatitude());
                intent.putExtra("Longitude",addresses.get(0).getLongitude());
                startActivity(intent);
            }
        });
    }
}