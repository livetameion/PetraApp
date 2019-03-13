package com.yagna.petra.app.Model;

import android.content.res.ColorStateList;
import android.support.v4.widget.CompoundButtonCompat;

import com.yagna.petra.app.Model.Product.Discount;
import com.yagna.petra.app.Model.Product.Modifier;
import com.yagna.petra.app.Model.Product.Products;
import com.yagna.petra.app.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class AdvTransactions {
    JSONObject jsonArray = new JSONObject();

    public ArrayList<AdvTransaction> array;


    public AdvTransactions(JSONArray jsonArray) {
        this.jsonArray = this.jsonArray;
        array= new ArrayList<>();
        for(int i =0 ;i<jsonArray.length();i++){
            try {
                array.add(new AdvTransaction(jsonArray.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }




    public class AdvTransaction {
        public ArrayList<Products.Product> products;
        public  ArrayList<Refund> refunds;
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
        public boolean isRefundPossible=true;
        public String refunded_already="0.0";
        public AdvTransaction(JSONObject jsonObject) {
            this.json = jsonObject;
            try {
                setTransaction(json.getJSONArray("trans_payanywhere").getJSONObject(0));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
            setTrasnParties(jsonObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                products = (new Products(json.getJSONArray("products_in_orders"))).products;
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                refunds=new ArrayList<>();
                for(int i = 0 ;i<json.getJSONArray("refund").length();i++)
                {
                    refunds.add(new Refund(json.getJSONArray("refund").getJSONObject(i)));
                    JSONArray jaar=new JSONArray(json.getJSONArray("refund").getJSONObject(i).getString("refunded_products"));
                    if(jaar!=null&&jaar.length()!=0)
                    for(int j = 0 ;j<jaar.length();j++){
                        for(int k = 0 ;k<products.size();k++){
                            try {
                                if (products.get(k).order_id.equalsIgnoreCase(jaar.getJSONObject(j).getString("order_id"))) {
                                    products.get(k).refunded = true;
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            setRefundStatus();



        }

        private void setRefundStatus() {
            double refunded_total=0.0;
            if (refunds != null && refunds.size() != 0) {
                for (int i = 0; i < refunds.size(); i++) {

                    try {
                        if (!refunds.get(i).refund_tokens_bought.equalsIgnoreCase("0")) {

                            refunded_total= Double.parseDouble(refunds.get(i).refund_token_price);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        if (!refunds.get(i).refund_vp_token.equalsIgnoreCase("0")) {

                            refunded_total=refunded_total+ Double.parseDouble(refunds.get(i).refund_vp_fee);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        if (!refunds.get(i).refund_token_used.equalsIgnoreCase("0")) {

                            refunded_total= refunded_total-Double.parseDouble(refunds.get(i).refund_token_used);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
            for(int i=0;i<products.size();i++){
                try{
                if(products.get(i).refunded){
                    refunded_total= refunded_total+Double.parseDouble(products.get(i).sale_price);
                }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            try{

            String ref_totla_d= String.format("%.2f",refunded_total);
            refunded_total=Double.parseDouble(ref_totla_d);
            double refunding =(Double.parseDouble(total_price)-Double.parseDouble(charity_price)-Double.parseDouble(tip));
            refunded_already=String.format("%.2f",refunded_total);
            if(refunded_total==refunding) {
               isRefundPossible=false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        }

        public void setTransaction(JSONObject json) {

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

        public void setTrasnParties(JSONObject json){
            try {
                transaction_id = json.getString("transaction_id");
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
        }

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
                    }
                }
            }


            public class Product implements Cloneable {
                public int qty=1;
                public String selected_discount ="";
                public String selected_modifier="";
                public double price_applied =0.0;
                public boolean refunded=false;
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
                public String purchased_qty;
                public String sale_price;
                public String applied_modifier_id;
                public String applied_discount_id;
                public String charity_id;
                public String order_id="0";

                public ArrayList<Modifier> modifier=new ArrayList<>();
                public ArrayList<Discount> discount=new ArrayList<>();
                public Product(JSONObject json) {
                    try {
                        this.json = json.getJSONArray("product_details").getJSONObject(0);
                        setValues(this.json);
                        setDetails(json);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    setModifiers();
                    setDiscount();
                 }

                private void setDetails(JSONObject json) {
                    try {
                        purchased_qty = json.getString("qty");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                try {
                        sale_price = json.getString("sale_price");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        applied_modifier_id = json.getString("modifier_id");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        applied_discount_id = json.getString("discount_id");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        charity_id = json.getString("charity_id").split("-")[0];
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    try {
                        order_id = json.getString("order_id");
                    } catch (Exception e) {
                        e.printStackTrace();
                        try {
                            order_id = json.getString("charity_id").split("/")[1];
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                    }

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

                private void setValues(JSONObject json) {

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

        public class Refund implements Cloneable {

            JSONObject json = new JSONObject();
            public String refund_total_amount;
            public String refund_id;
            public String description;
            public String refund_token_used;
            public String refund_token_price;
            public String refund_token_rate;
            public String refund_tokens_bought;
            public String refund_vp_fee;
            public String refund_vp_rate;
            public String image_full_path;
            public String refund_vp_token;
            public String refund_charity;
            public String refund_tip;
            public String refund_pay_method;
            public String refund_patransaction_id;
            public String refund_patransaction_status;
            public JSONArray refunded_products=new JSONArray();


            public Refund(JSONObject json) {
                this.json = json;
                setValues(this.json);

            }



            public Object clone() throws
                    CloneNotSupportedException
            {
                return super.clone();
            }




            private void setValues(JSONObject json) {

                try {
                    refund_id = json.getString("refund_id");
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                try {
                    refund_total_amount = json.getString("refund_total_amount");
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                try {
                    refund_token_used = json.getString("refund_token_used");
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                try {
                    refund_token_price = json.getString("refund_token_price");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    refund_token_rate = json.getString("refund_token_rate");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    refund_tokens_bought = json.getString("refund_tokens_bought");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    refund_vp_fee = json.getString("refund_vp_fee");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    refund_vp_rate = json.getString("refund_vp_rate");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    refund_vp_token = json.getString("refund_vp_token");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    refund_tip = json.getString("refund_tip");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    refund_charity = json.getString("refund_charity");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    refund_pay_method = json.getString("refund_pay_method");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    refund_patransaction_id = json.getString("refund_patransaction_id");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    refund_patransaction_status = json.getString("refund_patransaction_status");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    refunded_products =new JSONArray(json.getString("refunded_products"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }


    }




}
