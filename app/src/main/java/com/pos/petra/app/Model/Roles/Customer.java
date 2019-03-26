package com.pos.petra.app.Model.Roles;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Customer {
    JSONObject json = new JSONObject();
    public String customer_id;
    public String name;
    public String address;
    public String city;
    public String state;
    public String zipcode;
    public String email;
    public String phone;
    public String points;
    public String nest_eligible;
    public String verify_code;
    public String verify_code_used;


    public Customer(JSONObject json) {
        this.json = json;
        setValues();
      //  setWallets();
    }





    private void setValues() {

            try {
                customer_id = json.getString("customer_id");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                name = json.getString("name");
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
            email = json.getString("email");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            phone = json.getString("phone");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            points = json.getString("points");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            nest_eligible = json.getString("nest_eligible");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            verify_code = json.getString("verify_code");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            verify_code_used = json.getString("verify_code_used");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

