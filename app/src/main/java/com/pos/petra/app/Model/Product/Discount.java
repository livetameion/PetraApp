package com.pos.petra.app.Model.Product;

import org.json.JSONException;
import org.json.JSONObject;

public class Discount {

    JSONObject json = new JSONObject();
    public String title;
    public String id;
    public String description;
    public String product_id;
    public String subtitle;
    public String isActive;
    public String term_conditon;
    public String merchant_location_id;
    public String admin_id;




    public Discount(JSONObject json) {
        this.json = json;
        setValues();
    }

    private void setValues() {

        try {
            title = json.getString("title");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            id = json.getString("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        try {
            description = json.getString("description");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        try {
            product_id = json.getString("product_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        try {
            subtitle = json.getString("subtitle");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            isActive = json.getString("isActive");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            term_conditon = json.getString("term_conditon");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            merchant_location_id = json.getString("merchant_location_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            admin_id = json.getString("admin_id ");
        } catch (JSONException e) {
            e.printStackTrace();
        }



    }
}
