package com.pos.petra.app.Model.Transaction;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Transaction {
    JSONObject json = new JSONObject();
    public String transaction_id;
    public String reference_code;
    public String tokens_used;
    public String vp_accepted;
    public String vp_rate;
    public String vp_fee;
    public String vp_tokens;
    public String tokens_bought;
    public String token_price;
    public String token_rate;
    public String charity_accepted,charity_price,original_price,subtotal, total_price;
    public String points_gained;
    public String token_reward;
    public String pay_method;
    public String tip;
    public String transaction_started;
    public String transaction_complete;
    public String description;
    public String signature;
    public String pa_transaction_id;
    public String pa_receipt_id;
    public String pa_payment_success;
    public String transaction_status;
    public String merchant_id;
    public String cashier_id;
    public String customer_id;
    public String owner_customer_id;
    public String customer_merchant_id;
    public String token_merchant_id;
    public String merchant_location_id;
    public String cashier_name;


    public Transaction(JSONObject jsonObject) {
        this.json = jsonObject;
        setValues();
    }




    public void setValues() {

        try {
            transaction_id = json.getString("transaction_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            reference_code = json.getString("reference_code");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        try {
            tokens_used = json.getString("tokens_used");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        try {
            vp_accepted = json.getString("vp_accepted");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        try {
            vp_rate = json.getString("vp_rate");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            vp_fee = json.getString("vp_fee");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            vp_tokens = json.getString("vp_tokens");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            tokens_bought = json.getString("tokens_bought");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            token_price = json.getString("token_price");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            token_rate = json.getString("token_rate");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            charity_accepted = json.getString("charity_accepted");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            charity_price = json.getString("charity_price");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            original_price = json.getString("original_price");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            subtotal = json.getString("subtotal");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            total_price = json.getString("total_price");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            points_gained = json.getString("points_gained");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            token_reward = json.getString("token_reward");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            pay_method = json.getString("pay_method");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            transaction_started = json.getString("transaction_started");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            transaction_complete = json.getString("transaction_complete");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            description = json.getString("description");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            signature = json.getString("signature");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            pa_transaction_id = json.getString("pa_transaction_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            pa_receipt_id = json.getString("pa_receipt_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            pa_payment_success = json.getString("pa_payment_success");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            transaction_status = json.getString("transaction_status");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            merchant_id = json.getString("merchant_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            cashier_id = json.getString("cashier_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            customer_id = json.getString("customer_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            owner_customer_id = json.getString("owner_customer_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            customer_merchant_id = json.getString("customer_merchant_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            token_merchant_id = json.getString("token_merchant_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            merchant_location_id = json.getString("merchant_location_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            cashier_name = json.getString("cashier_name");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            tip = json.getString("tip");
        } catch (JSONException e) {
            e.printStackTrace();
        }



    }
}
