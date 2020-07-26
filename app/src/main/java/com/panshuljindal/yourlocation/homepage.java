package com.panshuljindal.yourlocation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class homepage extends AppCompatActivity {
    TextView textView1,textView2,textView3,textView4,textView5;
    Button LButton,showMap;
    FusedLocationProviderClient fusedLocationProviderClient;
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

        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this);
        clickListerners();
    }

    private void clickListerners() {
        LButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animAlpha = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.anim_alpha);
                v.startAnimation(animAlpha);
                if(ActivityCompat.checkSelfPermission(homepage.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                    fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                        @Override
                        public void onComplete(@NonNull Task<Location> task) {
                            Location location=task.getResult();
                            if(location!=null){
                                try {
                                    Geocoder geocoder=new Geocoder(homepage.this, Locale.getDefault());
                                    List<Address> addresses=geocoder.getFromLocation(
                                            location.getLatitude(),location.getLongitude(),1
                                    );
                                    //Latitude textview
                                    textView1.setText(Html.fromHtml(
                                            "<font color='#6200EE'><b>Latitude:</b><br></font>"
                                                    + addresses.get(0).getLatitude()
                                    ));
                                    //Longitude textview

                                    textView2.setText(Html.fromHtml(
                                            "<font color='#6200EE'><b>Longitude:</b><br></font>"
                                                    + addresses.get(0).getLongitude()
                                    ));
                                    //Country name

                                    textView3.setText(Html.fromHtml(
                                            "<font color='#6200EE'><b>Country:</b><br></font>"
                                                    + addresses.get(0).getCountryName()
                                    ));
                                    //Locality

                                    textView4.setText(Html.fromHtml(
                                            "<font color='#BBDEFB'><b>Locality:</b><br></font>"
                                                    + addresses.get(0).getLocality()
                                    ));
                                    //address
                                    textView5.setText(Html.fromHtml(
                                            "<font color='#6200EE'><b>Address:</b><br></font>"
                                                    + addresses.get(0).getAddressLine(0)
                                    ));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            else{
                                Toast.makeText(homepage.this, "Please try again!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else {
                    ActivityCompat.requestPermissions(homepage.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},44);
                }
            }
        });
    }
}