package com.pos.petra.app.Model.Roles;

import org.json.JSONException;
import org.json.JSONObject;

public class Admin {
    JSONObject json = new JSONObject();
    public String admin_id;
    public String merchant_location_id;
    public String merchant_id;
    public String admin_name;
    public String admin_phone;



    public Admin(JSONObject json) {
        this.json = json;
        setValues();
    }

    private void setValues() {

            try {
                admin_id = json.getString("admin_id");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                merchant_location_id = json.getString("merchant_location_id");
            } catch (JSONException e) {
                e.printStackTrace();
            }


            try {
                merchant_id = json.getString("merchant_id");
            } catch (JSONException e) {
                e.printStackTrace();
            }


            try {
                admin_name = json.getString("admin_name");
            } catch (JSONException e) {
                e.printStackTrace();
            }


            try {
                admin_phone = json.getString("admin_phone");
            } catch (JSONException e) {
                e.printStackTrace();
            }
    }
}

