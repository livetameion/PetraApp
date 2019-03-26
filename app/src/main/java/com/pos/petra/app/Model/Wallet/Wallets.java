package com.pos.petra.app.Model.Wallet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Wallets {
    JSONObject jsonArray = new JSONObject();

    public ArrayList<Wallet> array;


    public Wallets(JSONArray jsonArray) {
        this.jsonArray = this.jsonArray;
        array= new ArrayList<>();
        for(int i =0 ;i<jsonArray.length();i++){
            try {
                array.add(new Wallet(jsonArray.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }


}
