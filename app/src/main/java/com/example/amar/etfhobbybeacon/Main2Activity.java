package com.example.amar.etfhobbybeacon;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.async.http.body.StreamBody;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;

public class Main2Activity extends AppCompatActivity {

    private ListView mListView;
    private ArrayList<NearbyMResponse> users;
    private String username;
    MojAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        mListView = (ListView) findViewById(R.id.mobile_list);

        users=new ArrayList<>();
        username=getIntent().getStringExtra("username");
        final Main2Activity m2a=this;


        String url=getString(R.string.server_url)+"/nearbymobile/get?username="+username;
        Log.w("TAG",url);
        Ion.with(Main2Activity.this).load(url)
                .setHeader("token","Bearer "+getIntent().getStringExtra("token"))
                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>(){
                    @Override
                    public void onCompleted(Exception e, JsonArray result) {
                        Log.w("TAG","RADI ION");
                        Log.w("TAG",Integer.toString(result.size()));

                        if(e==null){
                           for(int i=0;i<result.size();i++){
                               NearbyMResponse nmr=new NearbyMResponse();
                               nmr.username=result.get(i).getAsJsonObject().get("username").getAsString();
                               nmr.percentage=result.get(i).getAsJsonObject().get("percentage").getAsDouble();
                               users.add(nmr);
                           }
                            adapter=new MojAdapter(m2a,users);
                            mListView.setAdapter(adapter);

                        }
                    }
                });




    }


}
