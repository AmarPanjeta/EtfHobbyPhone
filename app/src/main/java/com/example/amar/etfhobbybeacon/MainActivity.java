package com.example.amar.etfhobbybeacon;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

public class MainActivity extends AppCompatActivity {

    private LocationListener locationListener;
    private LocationManager locationManager;
    private String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        locationListener = new MyLocationListener();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        username=getIntent().getStringExtra("username");
        if(locationManager==null) Log.w("TAG","Poruka: nemamo locationManagera!");
    }

    @Override
    public void onBackPressed(){
        moveTaskToBack(true);
    }

    public void aktiviraj(View view){
        Log.w("TAG","Radi dugme!");
        Toast.makeText(MainActivity.this,"Beacon aktiviran",Toast.LENGTH_SHORT).show();
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, locationListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(locationListener);
    }

    public void logout(View view){
        Intent i = new Intent(this,LoginActivity.class);
        startActivity(i);
    }

    public void koJeBlizu(View view){
        Intent i = new Intent(this,Main2Activity.class);
        Log.w("TAG","Ovo je username: "+getIntent().getStringExtra("username"));
        i.putExtra("username",getIntent().getStringExtra("username"));
        i.putExtra("token",getIntent().getStringExtra("token"));
        startActivity(i);
    }

    public class MyLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(Location loc){
            Log.w("TAG","Desilo se!");
            Toast.makeText(MainActivity.this,"Location changed: Lat: " + loc.getLatitude() + " Lng: "
                    + loc.getLongitude(), Toast.LENGTH_SHORT).show();
            Log.w("TAG","Location changed: Lat: " + loc.getLatitude() + " Lng: "
                    + loc.getLongitude());
            final double lat=loc.getLatitude();
            final double lng=loc.getLongitude();
            Log.w("TAG","Imal ovaj username "+username);
            Ion.with(MainActivity.this).load(getString(R.string.server_url)+"/users/search/getIdbyUsername?username="+username)
                    .setHeader("token","Bearer "+getIntent().getStringExtra("token"))
                    .asString()
                    .setCallback(new FutureCallback<String>(){
                        @Override
                        public void onCompleted(Exception e, String result) {
                            Log.w("TAG","RADI ION OPEEEE  "+result);
                            if(e==null){
                                JsonObject json = new JsonObject();
                                json.addProperty("height",lat);
                                json.addProperty("width",lng);
                                json.addProperty("userId",result);
                                Log.w("TAG","ID je :");
                                Ion.with(MainActivity.this).load("POST",getString(R.string.server_url)+"/location/update")
                                        .setJsonObjectBody(json).asString();
                            }
                        }
                    });


        }

        @Override
        public void onProviderDisabled(String provider) {}

        @Override
        public void onProviderEnabled(String provider) {
            Log.w("TAG","Imamo providera");
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.w("TAG","GDJE SI?");
        }
    }
}
