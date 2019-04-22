package com.sparrowpaul.location;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class MainActivity extends AppCompatActivity implements LocationListener {


    Button buttonEnable, buttonGet, mapsBtn ;
    TextView textViewLongitude, textViewLatitude ;
    Context context;
    Intent intent1 ;
    Location location;
    LocationManager locationManager ;
    boolean GpsStatus = false ;
    Criteria criteria ;
    String Holder;
    static Double lati , longi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MainActivityPermissionsDispatcher.voidLocationWithPermissionCheck(this);

        buttonEnable = (Button)findViewById(R.id.button);
        buttonGet = (Button)findViewById(R.id.button2);
        mapsBtn = findViewById(R.id.button3);

        textViewLongitude = (TextView)findViewById(R.id.textView);
        textViewLatitude = (TextView)findViewById(R.id.textView2);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        criteria = new Criteria();

        Holder = locationManager.getBestProvider(criteria, false);

        context = getApplicationContext();

        CheckGpsStatus();

        buttonEnable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                intent1 = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent1);


            }
        });

        buttonGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CheckGpsStatus();

                if(GpsStatus == true) {
                    if (Holder != null) {
                        if (ActivityCompat.checkSelfPermission(
                                MainActivity.this,
                                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                                &&
                                ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)
                                        != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        location = locationManager.getLastKnownLocation(Holder);
                        locationManager.requestLocationUpdates(Holder, 12000, 7, MainActivity.this);
                    }
                }else {

                    Toast.makeText(MainActivity.this, "Please Enable GPS First", Toast.LENGTH_LONG).show();

                }
            }
        });

        mapsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uri = String.format(Locale.ENGLISH, "geo:%f,%f", longi, lati);
                String geoUrl = "http://maps.google.com/maps?q=loc:"+lati+","+longi+"("+"vehicle location"+")";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(geoUrl));
                context.startActivity(intent);
            }
        });
    }


    @NeedsPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    void voidLocation() {
    }

    @Override
    public void onLocationChanged(Location location) {

        textViewLongitude.setText("Longitude:" + location.getLongitude());
        textViewLatitude.setText("Latitude:" + location.getLatitude());

        Toast.makeText(this, "location changed"+location.getLongitude(), Toast.LENGTH_SHORT).show();

        lati = location.getLatitude();
        longi = location.getLongitude();

    }

    public void CheckGpsStatus(){

        locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);

        GpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
