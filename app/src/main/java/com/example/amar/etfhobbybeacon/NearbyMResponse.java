package com.example.amar.etfhobbybeacon;


public class NearbyMResponse {
    public String username;
    public double percentage;

    public NearbyMResponse(){}

    public NearbyMResponse(String username,double percentage){
        this.percentage=percentage;
        this.username=username;
    }
}
