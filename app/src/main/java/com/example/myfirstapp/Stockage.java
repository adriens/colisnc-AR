package com.example.myfirstapp;


import android.os.StrictMode;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;



public class Stockage {

    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Stockage(String code) {
        this.code = code;
    }

    public String getInfo() throws  UnirestException {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        HttpResponse<String> response = Unirest.get("https://colis-nc.p.rapidapi.com/colis/"+this.code)
                .header("x-rapidapi-host", "colis-nc.p.rapidapi.com")
                .header("x-rapidapi-key", "f587dc6483msh84e28a7c9a650bcp11b260jsn7c8b7ea489f5")

                .asString()
                ;
        return response.getBody().toString();
    }
}
