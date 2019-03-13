package com.yagna.petra.app.Model.Product;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Products {
    private JSONArray json=new JSONArray();
    public ArrayList<Product> products = new ArrayList<>();

    public Products(JSONArray json) {
        this.json = json;
        setValues();

    }

    private void setValues() {
        for(int i =0 ;i<json.length();i++){
            try {
                products.add(new Product(json.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
                try {
                    products.add(new Product(json.getJSONArray(i).getJSONObject(0)));
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }


    public class Product implements Cloneable {
        public int qty=1;
        public String selected_discount ="";
        public String selected_modifier="";
        public double price_applied =0.0;
        public double new_price =0.0;
        JSONObject json = new JSONObject();
        public String title;
        public String id;
        public String description;
        public String category;
        public String merchant_location_id;
        public String admin_id;
        public String price;
        public String stock;
        public String image;
        public String image_full_path;

        public String sku;
        public String haveModifier;
        public String haveDiscount;
        public ArrayList<Modifier> modifier=new ArrayList<>();
        public ArrayList<Discount> discount=new ArrayList<>();

        public Product(JSONObject json) {
            this.json = json;
            setValues();
            setModifiers();
            setDiscount();
        }

        public Object clone() throws
                CloneNotSupportedException
        {
            return super.clone();
        }

        private void setModifiers() {
            JSONArray modif =new JSONArray();
            try {
                 modif =  json.getJSONArray("modifier");

            } catch (JSONException e) {
                try {
                    modif =  json.getJSONArray("modifier_detail");
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            }
            for(int i =0 ;i<modif.length();i++){
                try {
                    modifier.add(new Modifier(modif.getJSONObject(i)));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        private void setDiscount() {
            JSONArray disct = new JSONArray();
            try {
                 disct =  json.getJSONArray("discounts");

            } catch (JSONException e) {
                try {
                    disct =  json.getJSONArray("discount_detail");
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            }
            for(int i =0 ;i< disct.length();i++){
                try {
                    discount.add(new Discount(disct.getJSONObject(i)));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
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
                category = json.getString("category");
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
                image = json.getString("image");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                haveModifier = json.getString("haveModifier");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                haveDiscount = json.getString("haveModifier");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                sku = json.getString("sku");
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
}

