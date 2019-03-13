package com.yagna.petra.app.Model.Product;

import org.json.JSONException;
import org.json.JSONObject;

public class Modifier {

    JSONObject json = new JSONObject();
    public String title;
    public String id;
    public String description;
    public String product_id;
    public String type;
    public String price;
    public String stock;
    public String img;
    public String image_full_path;




    public Modifier(JSONObject json) {
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
            type = json.getString("type");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            price = json.getString("price");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            stock = json.getString("stock");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            img = json.getString("img");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            image_full_path = json.getString("image_full_path");
        } catch (JSONException e) {
            e.printStackTrace();
        }

}
}
