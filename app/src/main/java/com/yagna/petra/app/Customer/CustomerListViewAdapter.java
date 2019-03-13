package com.yagna.petra.app.Customer;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.yagna.petra.app.R;
import com.yagna.petra.app.Util.Constants;
import com.yagna.petra.app.Util.CustomDialog;
import com.yagna.petra.app.Util.Utils;
import com.yagna.petra.app.views.TypefacedTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Sejal on 14-06-2017.
 */
public class CustomerListViewAdapter extends BaseAdapter {
    private final SharedPreferences common_mypref;
    private final CustomerActivity frag;
    LayoutInflater inflater;
    Activity context;
    JSONArray jsonarray;
    JSONArray jsonarrayOne;
    public JSONObject jsonObject= null;
    private TypefacedTextView txt_refundType;
    private TypefacedTextView txt_cancel;
    private LinearLayout ly_printDelete;
    private TypefacedTextView txt_voucherNo;
    private CheckBox cb_consolidate;
    private  String id="";


    private JSONObject user;
    private EditText edt_name;
    private TextView edt_phone;
    private EditText edt_email;
    private TextView edt_pin;
    private Spinner spn_isactive;
    private Button btn_update;
    private Utils util;
    private CustomDialog cd;
    private String isactive;
    private Dialog dialogUpdateCashier;

    public CustomerListViewAdapter(Activity mainActivity, JSONArray jarry, CustomerActivity usersFragment) {
        context = mainActivity;
        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        jsonarray = jarry;
        jsonarrayOne = jarry;
        common_mypref = context.getSharedPreferences("user", 0);
        frag = usersFragment;
    }

    @Override
    public int getCount() {
        return jsonarray.length();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }



    public class Holder
    {
       TypefacedTextView list_name,txt_date, list_e_id, list_email,list_isactive;
        public TypefacedTextView list_phone;
        LinearLayout ly_item;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        Holder holder=new Holder();
        View rowView;
        rowView=view;
        if(rowView==null)
        {
            rowView = inflater.inflate(R.layout.item_customer_list, null);

        }
       /* holder.txt_date=(TypefacedTextView) rowView.findViewById(R.id.list_date);
        try {
            holder.txt_date.setText(Utils.getConvetredDate(jsonarray.getJSONObject(position).getString("transaction_complete")));
        } catch (JSONException e) {
            e.printStackTrace();
        }
*/
        holder.list_name =(TypefacedTextView) rowView.findViewById(R.id.list_name);
        try {
            holder.list_name.setText(jsonarray.getJSONObject(position).getString("name"));
            // (jsonarray.getJSONObject(position).isNull("name")?"":"(Merchant)"));
        } catch (JSONException e) {
            e.printStackTrace();
        }



        holder.list_phone = (TypefacedTextView) rowView.findViewById(R.id.list_phone);
        try {
            holder.list_phone.setText(jsonarray.getJSONObject(position).getString("phone"));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        holder.list_email = (TypefacedTextView) rowView.findViewById(R.id.list_email);
        try {
            holder.list_email.setText(jsonarray.getJSONObject(position).getString("email"));
            LinearLayout la = (LinearLayout) rowView.findViewById(R.id.linearLayoutProdottoGenerale);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        holder.list_isactive = (TypefacedTextView) rowView.findViewById(R.id.list_isactive);
        try {
            holder.list_isactive.setText((jsonarray.getJSONObject(position).getString("points")));
            LinearLayout la = (LinearLayout) rowView.findViewById(R.id.linearLayoutProdottoGenerale);
            //la.setBackgroundResource( ((position % 2) == 0) ? R.color.grey : R.color.white);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        holder.ly_item = (LinearLayout) rowView.findViewById(R.id.ly_item);
        holder.ly_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    dialogUpdateCashier(jsonarray.getJSONObject(position));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        return rowView;
    }

    //Dialog for shoiwng final total of transaction
    public void dialogUpdateCashier(final JSONObject user) {
        dialogUpdateCashier = new Dialog(context, android.R.style.Theme_Black_NoTitleBar);
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = (View) inflater.inflate(R.layout.activity_customer_detail, null);
        Rect displayRectangle = new Rect();
        Window window = context.getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        dialogUpdateCashier.getWindow().setBackgroundDrawableResource(R.color.translucent_black);

        LinearLayout ly_pay = rowView.findViewById(R.id.ly_pay);
        ly_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogUpdateCashier.dismiss();
            }
        });

        try {
            edt_name =(EditText)rowView.findViewById(R.id.edt_name);
            edt_name.setText(user.getString("name"));
            id=(user.getString("customer_id"));


            edt_phone=(TextView)rowView.findViewById(R.id.edt_str_phone);
            edt_phone.setText(user.getString("phone"));

            edt_email=(EditText)rowView.findViewById(R.id.edt_email);
            edt_email.setText(user.getString("email"));

            edt_pin=(TextView) rowView.findViewById(R.id.edt_pin);
            edt_pin.setText(user.getString("points"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        btn_update = (Button) rowView.findViewById(R.id.btn_update);
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUser(user);
            }
        });
        edt_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                validation();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });





        dialogUpdateCashier.setContentView(rowView);
        dialogUpdateCashier.setCancelable(true);
        dialogUpdateCashier.setCanceledOnTouchOutside(true);
        dialogUpdateCashier.show();

    }

    private void updateUser(final JSONObject user) {
        util = new Utils(context);
        cd = new CustomDialog(context);

        if (util.checkInternetConnection()) {
            JSONObject jar = new JSONObject();
            cd.show("");
            Log.e("uploadUser", Constants.signupUrl);
            //  Log.e("loadTrans", ""+jar);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.signupUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            cd.hide();
                            Log.e("uploadUser", ""+response);
                            try {
                                if((new JSONObject(response)).getString("status").equalsIgnoreCase("1")){
                                    dialogUpdateCashier.dismiss();
                                    ((CustomerActivity)context).loadCustomers();
                                }
                                else{
                                    Toast.makeText(context,"Failed",Toast.LENGTH_SHORT).show();
                                }

                            }
                            catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            cd.hide();
                            Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }) {

                @Override
                protected Map<String, String> getParams() {

                    Map<String, String> paramss_main = new HashMap<String, String>();
                    JSONArray params_array = new JSONArray();
                    JSONObject params = new JSONObject();
                    try {
                        params.put("customer_id",id.trim());
                        params.put("name",edt_name.getText().toString().trim());
                        params.put("email",edt_email.getText().toString().trim());
                        params_array.put(params);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    paramss_main.put("action","update");
                    paramss_main.put("type","customers");


                    paramss_main.put("data",params.toString());
                    Log.e("getParams", String.valueOf(paramss_main));
                    return paramss_main;
                }
                @Override
                public Map<String, String> getHeaders(){
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Content-Type","application/x-www-form-urlencoded");
                    return params;
                }
            };

            stringRequest.setRetryPolicy(new RetryPolicy() {
                @Override
                public int getCurrentTimeout() {
                    return 100000;
                }

                @Override
                public int getCurrentRetryCount() {
                    return 100000;
                }

                @Override
                public void retry(VolleyError error) throws VolleyError {

                }
            });
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            requestQueue.add(stringRequest);

        }else {
            Toast.makeText(context,context.getResources().getString(R.string.dataconnection),Toast.LENGTH_LONG).show();
        }
    }

    public boolean validation(){
        if(edt_name.getText().toString().trim().length()==0){
            btn_update.setEnabled(false);
            btn_update.setBackgroundColor(context.getResources().getColor(R.color.dark_grey));
            btn_update.setTextColor(context.getResources().getColor(R.color.white));
            return false;
        }


        else if(edt_email.getText().length()<4){
            btn_update.setEnabled(false);
            btn_update.setBackgroundColor(context.getResources().getColor(R.color.dark_grey));
            btn_update.setTextColor(context.getResources().getColor(R.color.white));
            return false;
         }
        else
        {
            btn_update.setEnabled(true);
            btn_update.setBackgroundColor(context.getResources().getColor(R.color.dark_green));
            btn_update.setTextColor(context.getResources().getColor(R.color.white));

            return true;
        }
    }

}
