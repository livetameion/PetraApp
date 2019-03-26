package com.pos.petra.app.Admin.Charity;

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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.pos.petra.app.R;
import com.pos.petra.app.Util.Constants;
import com.pos.petra.app.Util.CustomDialog;
import com.pos.petra.app.Util.PrefUtil;
import com.pos.petra.app.Util.Utils;
import com.pos.petra.app.Views.TypefacedTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Sejal on 14-06-2017.
 */
public class CharityListViewAdapter extends BaseAdapter {
    private final SharedPreferences common_mypref;
    private final CharityFragment frag;
    LayoutInflater inflater;
    Activity context;
    JSONArray jsonarray;
    JSONArray jsonarrayOne;
    public JSONObject jsonObject= null;   
    private  String id="";
    private EditText edt_name,edt_add,edt_ac_number;
    private EditText edt_e_id;
    private EditText edt_phone;
    private EditText edt_email;
    private TextView edt_donated_amountt;    
    private Button btn_update;
    private Utils util;
    private CustomDialog cd;    
    private Dialog dialogUpdateCharity;
    

    // Constructor
    public CharityListViewAdapter(Activity mainActivity, JSONArray jarry, CharityFragment usersFragment) {
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
       TypefacedTextView txt_name, txt_e_id, txt_email, txt_donated_amount;
        private TypefacedTextView txt_phone;
        LinearLayout ly_item;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {


        Holder holder=new Holder();
        View rowView;
        rowView=view;
        if(rowView==null)
        {
            rowView = inflater.inflate(R.layout.item_charity_list, null);

        }
  
        holder.txt_name = rowView.findViewById(R.id.list_name);
        try {
            holder.txt_name.setText(jsonarray.getJSONObject(position).getString("name"));  
        } catch (JSONException e) {
            e.printStackTrace();
        }

        holder.txt_e_id = rowView.findViewById(R.id.list_e_id);
        try {
            holder.txt_e_id.setText(jsonarray.getJSONObject(position).getString("mentor"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        holder.txt_phone =  rowView.findViewById(R.id.list_phone);
        try {
            holder.txt_phone.setText(jsonarray.getJSONObject(position).getString("phone"));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        holder.txt_email =  rowView.findViewById(R.id.list_email);
        try {
            holder.txt_email.setText(jsonarray.getJSONObject(position).getString("email"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        holder.txt_donated_amount =  rowView.findViewById(R.id.list_isactive);
        try {
            holder.txt_donated_amount.setText("$"+(jsonarray.getJSONObject(position).getString("amount")));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        holder.ly_item =  rowView.findViewById(R.id.ly_item);
        holder.ly_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    dialogUpdateCharity(jsonarray.getJSONObject(position));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        return rowView;
    }


    //Dialog for shoiwng final total of transaction
    public void dialogUpdateCharity(final JSONObject user) {
        dialogUpdateCharity = new Dialog(context, android.R.style.Theme_Black_NoTitleBar);
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.dialog_charity_detail, null);
        Rect displayRectangle = new Rect();
        Window window = context.getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        dialogUpdateCharity.getWindow().setBackgroundDrawableResource(R.color.translucent_black);

        LinearLayout ly_pay = rowView.findViewById(R.id.ly_pay);
        ly_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogUpdateCharity.dismiss();
            }
        });

        try {
            edt_name =rowView.findViewById(R.id.edt_name);
            edt_name.setText(user.getString("name"));
            id=(user.getString("id"));

            edt_ac_number =rowView.findViewById(R.id.edt_ac_number);
            edt_ac_number.setText(user.getString("account_no"));

            edt_add=rowView.findViewById(R.id.edt_add);
            edt_add.setText(user.getString("address"));

            edt_e_id=rowView.findViewById(R.id.edt_e_id);
            edt_e_id.setText(user.getString("mentor"));

            edt_phone=rowView.findViewById(R.id.edt_str_phone);
            edt_phone.setText(user.getString("phone"));

            edt_email=rowView.findViewById(R.id.edt_email);
            edt_email.setText(user.getString("email"));

            edt_donated_amountt =(TextView) rowView.findViewById(R.id.edt_donated_amountt);
            edt_donated_amountt.setText("$"+user.getString("amount"));



        } catch (JSONException e) {
            e.printStackTrace();
        }

        btn_update =  rowView.findViewById(R.id.btn_create);
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateCharityDetials();
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


        edt_phone.addTextChangedListener(new TextWatcher() {
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
        edt_ac_number.addTextChangedListener(new TextWatcher() {
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

        edt_add.addTextChangedListener(new TextWatcher() {
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

        dialogUpdateCharity.setContentView(rowView);
        dialogUpdateCharity.setCancelable(true);
        dialogUpdateCharity.setCanceledOnTouchOutside(true);
        dialogUpdateCharity.show();

    }

    private void updateCharityDetials() {
        util = new Utils(context);
        cd = new CustomDialog(context);

        if (util.checkInternetConnection()) {
            JSONObject jar = new JSONObject();
            cd.show("");
            Log.e("uploadUser", Constants.charity);
            //  Log.e("loadTrans", ""+jar);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.charity,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            cd.hide();
                            Log.e("uploadUser", ""+response);
                            try {
                                if((new JSONObject(response)).getString("status").equalsIgnoreCase("1")){
                                    dialogUpdateCharity.dismiss();
                                    frag.loadCharityData();
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
                    //Prepare Payload
                    Map<String, String> paramss_main = new HashMap<String, String>();
                    JSONArray params_array = new JSONArray();
                    JSONObject params = new JSONObject();
                    try {
                        params.put("id",id.trim());
                        params.put("name",edt_name.getText().toString().trim());
                        params.put("address", edt_add.getText().toString().trim());
                        params.put("mentor", edt_e_id.getText().toString().trim());
                        params.put("account_no", edt_ac_number.getText().toString().trim());
                        params.put("phone",edt_phone.getText().toString().trim());
                        params.put("email",edt_email.getText().toString().trim());
                        params.put("amount", edt_donated_amountt.getText().toString().trim());
                        params_array.put(params);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    paramss_main.put("action","update");
                    JSONObject jsonObject_parent = new JSONObject();

                    JSONObject jsonObject = new JSONObject();
                    try {
                        String admin_id="";
                        String merchant_location_id = "";
                        try {
                            JSONObject jobj = new JSONObject(PrefUtil.getLoginData(common_mypref));
                            admin_id=jobj.getJSONObject("admin").getString("admin_id");
                            merchant_location_id =jobj.getJSONObject("admin").getString("merchant_location_id");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        jsonObject.put("merchant_location_id",merchant_location_id);
                        jsonObject.put("admin_id",admin_id);
                        jsonObject.put("charity",params_array);
                        jsonObject_parent.put("data",jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    paramss_main.put("data",jsonObject_parent.toString());
                    Log.e("getParams", String.valueOf(paramss_main));
                    return paramss_main;
                }
                @Override
                public Map<String, String> getHeaders(){
                    Map<String, String> params = new HashMap<>();
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
        else if(edt_ac_number.getText().toString().trim().length()==0){
            btn_update.setEnabled(false);
            btn_update.setBackgroundColor(context.getResources().getColor(R.color.dark_grey));
            btn_update.setTextColor(context.getResources().getColor(R.color.white));
            return false;
        }
        else if(edt_add.getText().toString().trim().length()==0){
            btn_update.setEnabled(false);
            btn_update.setBackgroundColor(context.getResources().getColor(R.color.dark_grey));
            btn_update.setTextColor(context.getResources().getColor(R.color.white));
            return false;
        }
        else if(edt_phone.getText().length()<10){
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
