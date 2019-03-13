package com.yagna.petra.app.Model;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class LoadCashiers {
    JSONObject json = new JSONObject();
    public ArrayList<Admin> admins =new ArrayList<>();
    public ArrayList<Cashiers> cashiers =new ArrayList<>();

    public LoadCashiers(JSONObject json){
        this.json=json;
        try {
            setValues();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setValues() throws JSONException {
           for (int i = 0 ;i<json.getJSONArray("admins").length();i++){
            admins.add(new Admin(json.getJSONArray("admins").getJSONObject(i)));
           }

            for (int i = 0 ;i<json.getJSONArray("cashiers").length();i++){
                cashiers.add(new Cashiers(json.getJSONArray("cashiers").getJSONObject(i)));
            }

    }
}
