package com.pos.petra.app.Model.Product;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SubCategories {

    private JSONArray json=new JSONArray();
    public ArrayList<SubCategory> sub_categories = new ArrayList<>();


    public SubCategories(JSONArray json) {
        this.json = json;
        setValues();

    }

    private void setValues() {
        for(int i =0 ;i< json.length();i++){
            try {
                sub_categories.add(new SubCategory(json.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    public class SubCategory {
        public boolean isSelected=false;
        JSONObject json = new JSONObject();
        public String title;
        public String id;
        public String description;
        public String parent;
        public String merchant_location_id;
        public String admin_id;
        public String code;
        public SubCategory(JSONObject json) {
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
                parent = json.getString("parent");
            } catch (JSONException e) {
                e.printStackTrace();
            }


            try {
                merchant_location_id = json.getString("merchant_location_id");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                admin_id = json.getString("admin_id");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                code = json.getString("code");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
}

