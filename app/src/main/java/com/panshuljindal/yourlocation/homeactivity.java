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
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    // variables

    TextView textView1,textView2,textView3,textView4,textView5;
    Button LButton;
    FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //variable assignment
        LButton=findViewById(R.id.getlocation);
        textView1=findViewById(R.id.textview1);
        textView2=findViewById(R.id.textview2);
        textView3=findViewById(R.id.textview3);
        textView4=findViewById(R.id.textview4);
        textView5=findViewById(R.id.textview5);

        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this);
        LButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //permission check
                if(ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
                    fetchLocation();

                }else{
                    ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},44);
                }
            }
        });
    }

    private void fetchLocation() {
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location=task.getResult();
                if(location!=null){

                    try {
                        Geocoder geocoder=new Geocoder(MainActivity.this, Locale.getDefault());
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
            }
        });
    }
}