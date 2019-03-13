package com.yagna.petra.app.Model;

import org.json.JSONException;
import org.json.JSONObject;

public class Cashiers {
    public JSONObject json;
    public String cashier_id;
    public String employee_id;
    public String name;
    public String phone;
    public String email;
    public String merchant_location_id;
    public String perm;
    public String verify_code_used,verify_code,is_active,merchant_id;
    public String password;


    public Cashiers(JSONObject json) {
        this.json = json;
        setValues();
    }

    private void setValues() {

            try {
                cashier_id = json.getString("cashier_id");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                employee_id = json.getString("employee_id");
            } catch (JSONException e) {
                e.printStackTrace();
            }


            try {
                name = json.getString("name");
            } catch (JSONException e) {
                e.printStackTrace();
            }


            try {
                phone = json.getString("phone");
            } catch (JSONException e) {
                e.printStackTrace();
            }


            try {
                email = json.getString("email");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                merchant_location_id = json.getString("merchant_location_id");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                perm = json.getString("perm");
            } catch (JSONException e) {
                e.printStackTrace();
            }

        try {
            password = json.getString("password");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            merchant_id = json.getString("merchant_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            is_active = json.getString("is_active");
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

