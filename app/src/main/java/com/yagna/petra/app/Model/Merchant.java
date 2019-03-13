package com.yagna.petra.app.Model;

import org.json.JSONException;
import org.json.JSONObject;

public class Merchant {
    JSONObject json;
    public String merchant_id;
    public String location_id;
    public String address;
    public String city;
    public String state;
    public String zipcode;
    public String token_price;
    public String vp_discount;
    public String sales;
    public String advertising;
    public String processing;
    public String paper;
    public String loyalty;
    public String rewards;
    public String website;
    public String monthly_transactions;
    public String avg_transaction;
    public String device_id;
    public String merchant_location_id;
    public String id_token;
    public String app_profile_id;
    public String profile_id;
    public String agent_id;
    public String bank_acct_num;
    public String bank_routing_num;
    public String bank_acct_holder;
    public String irs_filing_name;
    public String logo;
    public String verify_code_used;
    public String verify_code;
    public String enroll_status;
    public String ein;
    public String owner_id;
    public String name;
    public String dba;
    public String email;
    public String password;
    public String merchant_phone;
    public String fax;
    public String industry;
    public String years_in_business;
    public String points;
    public String store_phone;
    public String tip_status;
    public String charity_id;


    public Merchant(JSONObject json) {
        this.json = json;
        setLocation();
        setExpense();
        setDevice();
        setMerchant();
    }

    private void setMerchant() {
        try {
            ein = json.getJSONObject("merchant").getString("ein");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            owner_id = json.getJSONObject("merchant").getString("owner_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            name = json.getJSONObject("merchant").getString("name");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            dba = json.getJSONObject("merchant").getString("dba");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            email = json.getJSONObject("merchant").getString("email");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            password = json.getJSONObject("merchant").getString("password");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            merchant_phone = json.getJSONObject("merchant").getString("phone");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            fax = json.getJSONObject("merchant").getString("fax");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            industry = json.getJSONObject("merchant").getString("industry");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            years_in_business = json.getJSONObject("merchant").getString("years_in_business");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            points = json.getJSONObject("merchant").getString("points");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            enroll_status = json.getJSONObject("merchant").getString("enroll_status");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            verify_code = json.getJSONObject("merchant").getString("verify_code");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            verify_code_used = json.getJSONObject("merchant").getString("verify_code_used");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            logo = json.getJSONObject("merchant").getString("logo");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            irs_filing_name = json.getJSONObject("merchant").getString("irs_filing_name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            bank_acct_holder = json.getJSONObject("merchant").getString("bank_acct_holder");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            bank_routing_num = json.getJSONObject("merchant").getString("bank_routing_num");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            bank_acct_num = json.getJSONObject("merchant").getString("bank_acct_num");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            agent_id = json.getJSONObject("merchant").getString("agent_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            profile_id = json.getJSONObject("merchant").getString("profile_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            app_profile_id = json.getJSONObject("merchant").getString("app_profile_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            id_token = json.getJSONObject("merchant").getString("id_token");
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void setDevice() {
        try {
            device_id = json.getJSONObject("device").getString("device_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            merchant_location_id = json.getJSONObject("device").getString("merchant_location_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void setExpense() {
        try {
            sales = json.getJSONObject("location").getJSONObject("expenses").getString("sales");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            advertising = json.getJSONObject("location").getJSONObject("expenses").getString("advertising");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            processing = json.getJSONObject("location").getJSONObject("expenses").getString("processing");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        try {
            paper = json.getJSONObject("location").getJSONObject("expenses").getString("paper");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        try {
            loyalty = json.getJSONObject("location").getJSONObject("expenses").getString("loyalty");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            rewards = json.getJSONObject("location").getJSONObject("expenses").getString("rewards");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            website = json.getJSONObject("location").getJSONObject("expenses").getString("website");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            monthly_transactions = json.getJSONObject("location").getJSONObject("expenses").getString("monthly_transactions");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            avg_transaction = json.getJSONObject("location").getJSONObject("expenses").getString("avg_transaction");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setLocation() {
        try {
            merchant_id = json.getJSONObject("location").getString("merchant_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            location_id = json.getJSONObject("location").getString("location_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            charity_id = json.getJSONObject("location").getString("charity_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            address = json.getJSONObject("location").getString("address");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        try {
            store_phone = json.getJSONObject("location").getString("phone");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        try {
            city = json.getJSONObject("location").getString("city");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            state = json.getJSONObject("location").getString("state");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            zipcode = json.getJSONObject("location").getString("zipcode");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            token_price = json.getJSONObject("location").getString("token_price");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            vp_discount = json.getJSONObject("location").getString("vp_discount");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            tip_status = json.getJSONObject("location").getString("tip_status");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

