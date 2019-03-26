package com.pos.petra.app.Model.Wallet;

import org.json.JSONException;
import org.json.JSONObject;

public class Wallet {
    JSONObject json = new JSONObject();
    public String wallet_id;
    public String customer_id;
    public String merchant_id;
    public String dba;
    public String address;
    public String city;
    public String state;
    public String zipcode;
    public String phone;
    public String industry;
    public String feathers;
    public String nest_feathers;
    public String nest_price;



    public Wallet(JSONObject jsonObject) {
        this.json = jsonObject;
        setValues();
    }




    public void setValues() {

        try {
            wallet_id = json.getString("wallet_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            customer_id = json.getString("customer_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        try {
            merchant_id = json.getString("merchant_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        try {
            dba = json.getString("dba");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        try {
            address = json.getString("address");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            city = json.getString("city");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            state = json.getString("state");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            zipcode = json.getString("zipcode");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            phone = json.getString("phone");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            industry = json.getString("industry");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        try {
            feathers = json.getString("feathers");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            nest_feathers = json.getString("nest_feathers");
        } catch (JSONException e) {
            e.printStackTrace();
        }



        try {
            nest_price = json.getString("nest_price");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        try {
            merchant_id = json.getString("merchant_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            customer_id = json.getString("customer_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }





    }
}
