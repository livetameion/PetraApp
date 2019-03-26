package com.pos.petra.app.Admin.Product.Discount;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.pos.petra.app.AdvancedCashier.DataKeeper;
import com.pos.petra.app.GlobalActivity;
import com.pos.petra.app.Model.Product.Categories;
import com.pos.petra.app.Model.Product.Discount;
import com.pos.petra.app.R;
import com.pos.petra.app.Util.Constants;
import com.pos.petra.app.Util.CustomDialog;
import com.pos.petra.app.Util.PrefUtil;
import com.pos.petra.app.Util.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DiscountActivity extends GlobalActivity {
    public ArrayList<Bitmap> bitmapArray;
    private EditText product_name, p_rate, p_description;
    private Switch active_switch;
    private static final int CAMERA_REQUEST = 1888;
    private static int REQUEST_PICTURE = 1;
    private static int REQUEST_CROP_PICTURE = 2;
    private File lastCaptured;
    private Uri imageUri;
    public RecyclerView listview;
    int SELECT_FILE=2,REQUEST_CAMERA=3;
    public TextView tv_no_record;
    private DiscountAdapter adapter;
    private SharedPreferences common_mypref;
    private JSONObject returnedData;
    private Utils util;
    private CustomDialog cd;
    private TextView title,texttitle_product;
    private DiscountActivity context;
    public int position;
    private JSONArray returnedImageData;
    Uri myuri;
    private Uri imageToUploadUri;
    String imageid=null,productid=null;
    SimpleDateFormat sdf;
    String currentDateandTime;
    boolean imageupdate=false,flag=false;
    JSONArray j_arrayupdatearray;
    String productimagid[]=null;

    TextInputLayout txtxtitlep_discrption,txtxtitlep_name,txtxtitlep_code,txtxtitlep_price;
    TextView txt_producttitle,product_nam,product_sku;
    private LinearLayout ly_prod_details;
    private String admin_id;
    private String merchant_location_id;
    private ArrayList<Categories.Category> categories;
    private ArrayList<String> cate_array;
    private CardView cv_subcategory;
    private RecyclerView.LayoutManager storiesLayoutManager;
    private EditText p_terms;
    private ArrayList<Discount> disc_array;
    private CardView cv_sku;
    private TextInputLayout type;
    private boolean p_active;
    private ImageView btn_add;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setVisibility(View.VISIBLE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       // fillBitmapArrayList();
        //fillDataArraylist();
        context= DiscountActivity.this;
        common_mypref = getApplicationContext().getSharedPreferences(
                "user", 0);
        util=new Utils(context);
        tv_no_record = (TextView) findViewById(R.id.tv_no_record_);
        util=new Utils(context);
        cd=new CustomDialog(context);
        txt_producttitle=(TextView)findViewById(R.id.txt_branchtitle);
        txt_producttitle.setText("Discount");
        tv_no_record = (TextView) findViewById(R.id.tv_no_record_);
        ly_prod_details = (LinearLayout) findViewById(R.id.ly_prod_details);
        ly_prod_details.setVisibility(View.VISIBLE);
        product_sku = (TextView) findViewById(R.id.product_sku);
        product_nam = (TextView) findViewById(R.id.product_nam);
        try {
            JSONObject jobj = new JSONObject(PrefUtil.getLoginData(common_mypref));
            admin_id=jobj.getJSONObject("admin").getString("admin_id");
            merchant_location_id =jobj.getJSONObject("admin").getString("merchant_location_id");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        bitmapArray=new ArrayList<>();
        disc_array = new ArrayList<>();
        listview=(RecyclerView) findViewById(R.id.listview);
        storiesLayoutManager = createLayoutManager(getResources());
        listview.setLayoutManager(storiesLayoutManager);
        btn_add =findViewById(R.id.btn_add);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addData(false,0);
            }
        });
        fillData(1);
        sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
         /*  listview.setAdapter(adapter = new ActionListAdpter(DiscountActivity.this,disc_array,imageData));
             listview.setVisibility(View.VISIBLE);
             tv_no_record.setVisibility(View.GONE);*/
    }
    private RecyclerView.LayoutManager createLayoutManager(Resources resources) {
        int spans = resources.getInteger(R.integer.feed_columns);
        return new StaggeredGridLayoutManager(spans, RecyclerView.VERTICAL);
    }

    private void fillData(int i){
        Log.e("called by",""+i);
         String title = DataKeeper.detail_product.title;
         product_nam.setText(title);
         product_sku.setText(DataKeeper.detail_product.sku);
         disc_array = DataKeeper.detail_product.discount;
         listview.setAdapter(adapter = new DiscountAdapter(DiscountActivity.this,  DataKeeper.detail_product.discount));

        if(adapter==null) {
            if(disc_array.size()!=0) {
                txt_producttitle.setText("Discounts" + " (" + disc_array.size() + ")");
                listview.setVisibility(View.VISIBLE);
                tv_no_record.setVisibility(View.GONE);
            }
            else
            {
                txt_producttitle.setText("Discounts" + "(0)");
                listview.setVisibility(View.GONE);
                tv_no_record.setVisibility(View.VISIBLE);
            }
        }
        else{
            adapter.notifyDataSetChanged();
        }


    }
    public void addData(final boolean flag, final int position){

        final Dialog alertDialog = new Dialog(DiscountActivity.this, android.R.style.Theme_Black_NoTitleBar);
        LayoutInflater inflater = getLayoutInflater();
        final View rowView = (View) inflater.inflate(R.layout.dialog_add_discount, null);
        Rect displayRectangle = new Rect();
        Window window = getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawableResource(R.color.translucent_black);
        product_name=(EditText) rowView.findViewById(R.id.p_name);
        p_rate =(EditText) rowView.findViewById(R.id.p_rate);
        active_switch =(Switch) rowView.findViewById(R.id.active_switch);
        p_description =(EditText) rowView.findViewById(R.id.p_discrption);
        p_terms =(EditText) rowView.findViewById(R.id.p_terms);
        active_switch.setChecked(false);

        if(flag) {
            this.flag=true;

            if(disc_array.get(position).isActive.equalsIgnoreCase("1"))
            {
                p_active = true;
                active_switch.setChecked(true);
            }
            else
            {
                p_active = false;

                active_switch.setChecked(false);
            }

            active_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    if(isChecked) {
                        //do stuff when Switch is ON
                        p_active = true;
                    } else {
                        p_active = false;
                    }
                }
            });


            p_terms.setText(disc_array.get(position).term_conditon);

            product_name.setText(disc_array.get(position).title);
            product_name.setSelection(product_name.getText().length());
            p_rate.setText(disc_array.get(position).subtitle);
            p_rate.setSelection(p_rate.getText().length());
            p_description.setText(disc_array.get(position).description);
            p_description.setSelection(p_description.getText().length());
            productid= disc_array.get(position).id;

            try {
                JSONObject j_obj = new JSONObject();
                j_obj.put("ProductImageId", imageid);
                 j_arrayupdatearray=new JSONArray();
                j_arrayupdatearray.put(imageid);
            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        Button done = (Button) rowView.findViewById(R.id.done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
/////////////////////////////////////////////////////
                Log.e("calling","validation");
                if(validation())
                {
                    Log.e("callied","validation");
                    if(flag){
                        setUrlPayload(Constants.UPDATEITEM,DataKeeper.detail_product.id);

                    }
                    else
                    {
                        setUrlPayload(Constants.ADDITEM,DataKeeper.detail_product.id);
                    }
                    myuri=null;
                    alertDialog.dismiss();

                }

            }
        });
        Button back = (Button) rowView.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        p_description.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if(validation())
                    {
                        Log.e("callied","validation");
                        if(flag){
                            setUrlPayload(Constants.UPDATEITEM,DataKeeper.detail_product.id);

                        }
                        else
                        {
                            setUrlPayload(Constants.ADDITEM,DataKeeper.detail_product.id);
                        }
                        myuri=null;
                        alertDialog.dismiss();
                    }
                }
                return false;
            }
        });
                alertDialog.setContentView(rowView);
                alertDialog.setCancelable(true);
                alertDialog.setCanceledOnTouchOutside(true);
                alertDialog.show();

    }


    /**Return image path of given URI*/

    private boolean validation(){
        boolean flag = true;
        /*if(myuri==null)
        {
            util.customToast("Please add your product's Image.");
            flag = false;
        }
        else*/ /*if(active_switch.getText().toString().trim().length()==0){
            // product_name.setError("Product Name should not be null!");
            util.customToast(getResources().getString(R.string.productcodenull));
            active_switch.requestFocus();
            flag = false;
        }
       else*/ if(product_name.getText().toString().trim().length()==0){
           // product_name.setError("Product Name should not be null!");
            util.customToast(getResources().getString(R.string.productnamenull));
            product_name.requestFocus();
            flag = false;
        }
       else if (!(p_rate.getText().toString().trim().length()>0)&& TextUtils.isDigitsOnly(p_rate.getText().toString().trim())){
           // p_rate.setError("Digit Only!");
            util.customToast(getResources().getString(R.string.productprice));
            p_rate.requestFocus();

        }
        else if(p_description.getText().toString().trim().length()==0){
            util.customToast(getResources().getString(R.string.descriptionnull));
            p_description.requestFocus();
            flag = false;
        }
        return flag;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        addData(false, 0);
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }

      /*  int id = item.getItemId();
        if (id == R.id.add) {
            if (disc_array.size()==10){
                util.customToast("Sorry!!You can add Only 10 Products");
            }
            else {
                addData(false, 0);
            }
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
     //   getMenuInflater().inflate( R.menu.menu_add, menu );
       // MenuItem addOption = menu.findItem(R.id.add);
      //  addOption.setVisible(true);
        return true;
    }
    public void setUrlPayload(int category,String extra) {
        switch (category){

            case Constants.GETLIST:
                JSONObject j_obj = new JSONObject();
                try {
                    j_obj.put("product_id", extra);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                jsonwebrequest(Constants.DsicountAPI,j_obj,Constants.GETLIST,Constants.VIEW);
                break;
            case  Constants.ADDITEM:
                Log.e("calliang","Add Product");
                //sendDataToServer(Constants.b_Add_Product);
                j_obj = new JSONObject();
                try {
                    JSONObject data = new JSONObject();
                    data.put("merchant_location_id", merchant_location_id);
                    data.put("admin_id", admin_id);
                    JSONObject categ = new JSONObject();
                    categ.put("title", product_name.getText().toString().trim());
                    categ.put("product_id", extra);
                    categ.put("term_Conditon", p_terms.getText().toString().trim());
                    categ.put("description", p_description.getText().toString().trim());
                    categ.put("isActive", ""+p_active);
                    categ.put("subtitle", p_rate.getText().toString().trim());
                   //categ.put("img", util.getBase64(p_image));
                    JSONArray categories =new JSONArray();
                    categories.put(categ);
                    data.put("discounts",categories);
                    j_obj.put("data",data);
                    jsonwebrequest(Constants.DsicountAPI,j_obj,Constants.ADDITEM,Constants.CREATE);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;

            case  Constants.UPDATEITEM:
                Log.e("calliang","Add Product");
                //sendDataToServer(Constants.b_Add_Product);
                j_obj = new JSONObject();
                try {
                    JSONObject data = new JSONObject();
                    data.put("merchant_location_id", merchant_location_id);
                    data.put("admin_id", admin_id);
                    JSONObject categ = new JSONObject();
                    categ.put("id", productid);
                    categ.put("product_id",extra);
                    categ.put("title", product_name.getText().toString().trim());
                    categ.put("term_Conditon", p_terms.getText().toString().trim());
                    categ.put("description", p_description.getText().toString().trim());
                    categ.put("isActive", ""+p_active);
                    categ.put("subtitle", p_rate.getText().toString().trim());
                  // categ.put("img", util.getBase64(p_image));
                    JSONArray categories =new JSONArray();
                    categories.put(categ);
                    data.put("discounts",categories);
                    j_obj.put("data",data);
                    jsonwebrequest(Constants.DsicountAPI,j_obj,Constants.UPDATEITEM,Constants.UPDATE);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


                break;

            case  Constants.DELETEITEM:

                j_obj = new JSONObject();
                try {
                    JSONObject data = new JSONObject();
                    data.put("merchant_location_id", merchant_location_id);
                    data.put("admin_id", admin_id);
                    JSONObject categ = new JSONObject();
                    categ.put("id", productid);
                    JSONArray categories =new JSONArray();
                    categories.put(categ);
                    data.put("discount_id",categories);
                    j_obj.put("data",data);
                    jsonwebrequest(Constants.DsicountAPI,j_obj,Constants.DELETEITEM,Constants.DELETE);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }



    }

    public void setReturnedData(int category, JSONObject j_obj) {
        switch (category){

            case Constants.GETLIST:
                Log.e("Apitransect",""+j_obj);
                JSONArray re_j_obj;
                try {
                    returnedData =j_obj;
                    re_j_obj = returnedData.getJSONArray("data");

                    disc_array.clear();
                    for(int i =0 ;i<re_j_obj.length();i++){
                        try {
                            disc_array.add(new Discount(re_j_obj.getJSONObject(i)));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                    fillData(0);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            case Constants.ADDITEM:
                Log.e("Apitransect",""+j_obj);
                setUrlPayload(Constants.GETLIST, DataKeeper.detail_product.id);

                break;
            case Constants.UPDATEITEM:
                Log.e("Apitransect",""+j_obj);
                setUrlPayload(Constants.GETLIST, DataKeeper.detail_product.id);

                break;
            case Constants.DELETEITEM:
                Log.e("Apitransect",""+j_obj);
                setUrlPayload(Constants.GETLIST, DataKeeper.detail_product.id);
                break;
        }
    }



    public void jsonwebrequest(String url ,final JSONObject j_obj, final int action, final String action_text) {
        if (util.checkInternetConnection()) {
            cd.show("");

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        public JSONObject data;

                        @Override
                        public void onResponse(String response) {
                            cd.hide();
                            Log.e("loadTrans", ""+response);
                            try {
                                if((new JSONObject(response)).getString("status").equalsIgnoreCase("1")) {

                                    JSONObject returnJSON =new JSONObject(response);

                                    if(!(new JSONObject(response)).isNull("message"))
                                        util.customToast((new JSONObject(response)).getString("message"));


                                    setReturnedData(action, returnJSON);

                                }
                                else{
                                    cd.hide();
                                    util.customToast("Failed");
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
                            Toast.makeText(DiscountActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }) {

                @Override
                protected Map<String, String> getParams() {

                    Map<String, String> params = new HashMap<String, String>();
                    params.put("data",j_obj.toString());
                    params.put("action",action_text);
                    Log.e("getParams", String.valueOf(params));
                    return params;
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
            RequestQueue requestQueue = Volley.newRequestQueue(DiscountActivity.this);
            requestQueue.add(stringRequest);
        }
        else
        {
            util.customToast(getResources().getString(R.string.nointernet));

        }
    }

    public void myClickMethod(final View v) {
        // /Function purpose to Perform ClickEvent of Element Based on view
        // /PARAM 1.v=view of the clicked Element....
        switch (v.getId()) {

/*
            case R.id.btnOkPopup:
                util.ButtonClickEffect(v);
                progressbar.hide();
                if(adapter!=null)
                    adapter.progressbar.hide();
                break;
*/
            case R.id.btn_ok:
                util.ButtonClickEffect(v);
                cd.hide();
                setUrlPayload(Constants.DELETEITEM,productid);
                if(adapter!=null)
                    adapter.cd.hide();
                cd.hide();
                break;              /*

            case R.id.btnwifi:
                util.ButtonClickEffect(v);
                boolean result=util.checkPermissionwifi(DiscountActivity.this);
                if(result) {
                    WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
                    if (!wifi.isWifiEnabled()) {
                        wifi.setWifiEnabled(true);
                        progressbar.hide();
                    }
                }
                if(adapter!=null)
                    adapter.progressbar.hide();
                break;
              */

         /*   case R.id.btnreconnect:
                util.ButtonClickEffect(v);
                if(adapter!=null)
                    adapter.progressbar.hide();
                progressbar.hide();
                setUrlPayload(Constants.DELETEITEM, Integer.parseInt(disc_array.get(position)[1]));
                break*/

            case R.id.btn_cancel:
                util.ButtonClickEffect(v);
                cd.hide();
                if(adapter!=null)
                    adapter.cd.hide();
                break;
        }
    }


}
