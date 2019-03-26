package com.pos.petra.app.Model.Transaction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Transactions {
    JSONObject jsonArray = new JSONObject();

    public ArrayList<Transaction> array;


    public Transactions(JSONArray jsonArray) {
        this.jsonArray = this.jsonArray;
        array= new ArrayList<>();
        for(int i =0 ;i<jsonArray.length();i++){
            try {
                array.add(new Transaction(jsonArray.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }


}
