package com.yagna.petra.app.Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Merchants {
    JSONObject jsonArray = new JSONObject();

    public ArrayList<Merchant> array;


    public Merchants(JSONArray jsonArray) {
        this.jsonArray = this.jsonArray;
        array= new ArrayList<>();
        for(int i =0 ;i<jsonArray.length();i++){
            try {
                array.add(new Merchant(jsonArray.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }


}
