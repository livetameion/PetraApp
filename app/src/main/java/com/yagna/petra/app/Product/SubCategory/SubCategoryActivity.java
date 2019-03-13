package com.yagna.petra.app.Product.SubCategory;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import com.yagna.petra.app.GlobalActivity;
import com.yagna.petra.app.Model.Product.Categories;
import com.yagna.petra.app.Model.Product.SubCategories;
import com.yagna.petra.app.Product.Category.CategoryActivity;
import com.yagna.petra.app.Product.Category.CategoryReAdapter;
import com.yagna.petra.app.R;
import com.yagna.petra.app.Util.Constants;
import com.yagna.petra.app.Util.CustomDialog;
import com.yagna.petra.app.Util.PrefUtil;
import com.yagna.petra.app.Util.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SubCategoryActivity extends GlobalActivity {
    public ArrayList<Bitmap> bitmapArray;
    private EditText  description;

    public RecyclerView listview;
    public ArrayList<SubCategories.SubCategory> arrayData=new ArrayList<>();
    public TextView tv_no_record;
    private SubCategoryReAdapter adapter;
    private SharedPreferences common_mypref;
    private JSONObject returnedData;
    private Utils util;
    private CustomDialog cd;
    private SubCategoryActivity context;
    public int position;
    public ArrayList<String[]> imageData;
    Uri myuri;
    String subcateg_id =null;
    SimpleDateFormat sdf;
    boolean flag=false;
    TextView txt_producttitle;
    private String merchant_location_id;
    private String admin_id;
    private LinearLayout ly_spinner;
    private Spinner spn_category;
    private ArrayList<String> cate_array;
    private ArrayList<Categories.Category> categories;
    private EditText subcateg_name;
    private Categories.Category parent;
    private EditText code;
    private RecyclerView.LayoutManager storiesLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.b_activity_category);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setVisibility(View.VISIBLE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        context= SubCategoryActivity.this;
        common_mypref = getApplicationContext().getSharedPreferences(
                "user", 0);
        util=new Utils(context);
        tv_no_record = (TextView) findViewById(R.id.tv_no_record_);
        util=new Utils(context);
        cd=new CustomDialog(context);
        txt_producttitle=(TextView)findViewById(R.id.txt_branchtitle);
        txt_producttitle.setText("Categories");
        tv_no_record = (TextView) findViewById(R.id.tv_no_record_);
        bitmapArray=new ArrayList<>();
        arrayData = new ArrayList<>();
        imageData = new ArrayList<>();
        listview=(RecyclerView)findViewById(R.id.listview);
        storiesLayoutManager = createLayoutManager(getResources());
        listview.setLayoutManager(storiesLayoutManager);
        ly_spinner = (LinearLayout) findViewById(R.id.ly_spinner);
        ly_spinner.setVisibility(View.VISIBLE);
        spn_category = (Spinner) findViewById(R.id.spn_category);


        try {
            JSONObject jobj = new JSONObject(PrefUtil.getLoginData(common_mypref));
            admin_id=jobj.getJSONObject("admin").getString("admin_id");
            merchant_location_id =jobj.getJSONObject("admin").getString("merchant_location_id");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        fillData(1);
        listview.setAdapter(adapter = new SubCategoryReAdapter(SubCategoryActivity.this,arrayData));

        sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
      /*  listview.setAdapter(adapter = new ActionListAdpter(ProductActivity.this,product_array,imageData));
        listview.setVisibility(View.VISIBLE);
        tv_no_record.setVisibility(View.GONE);*/
    }
    private RecyclerView.LayoutManager createLayoutManager(Resources resources) {
        int spans = resources.getInteger(R.integer.feed_columns);
        return new StaggeredGridLayoutManager(spans, RecyclerView.VERTICAL);
    }

    private void fillData(int i){
        Log.e("called by",""+i);
        setUrlPayload(Constants.GETLIST_1,"");
    }
    public void addData(final boolean flag, final int position){

        final Dialog alertDialog = new Dialog(SubCategoryActivity.this, android.R.style.Theme_Black_NoTitleBar);
        LayoutInflater inflater = getLayoutInflater();
        final View rowView = (View) inflater.inflate(R.layout.dialog_add_category, null);
        Rect displayRectangle = new Rect();
        Window window = getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawableResource(R.color.translucent_black);
        subcateg_name =(EditText) rowView.findViewById(R.id.s_name);
        code =(EditText) rowView.findViewById(R.id.p_code);
        description =(EditText) rowView.findViewById(R.id.p_discrption);
        CardView ly_subcateg = (CardView) rowView.findViewById(R.id.ly_subcateg);
        ly_subcateg.setVisibility(View.VISIBLE);
        CardView ly_categ = (CardView) rowView.findViewById(R.id.ly_categ);
        ly_categ.setVisibility(View.GONE);

        ((TextView)rowView.findViewById(R.id.texttitle)).setText("Add/Edit SubCategory");
        ((TextInputLayout)rowView.findViewById(R.id.txtxtitlep_code)).setHint("SubCategory Code");

        if(flag) {
            this.flag=true;
            subcateg_name.setText(arrayData.get(position).title);
            subcateg_name.setSelection(subcateg_name.getText().length());
            description.setText(arrayData.get(position).description);
            code.setText(arrayData.get(position).code);
            description.setSelection(description.getText().length());
            subcateg_id =arrayData.get(position).id;
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

                        setUrlPayload(Constants.UPDATEITEM,arrayData.get(position).id);
                    }
                    else
                    {
                        setUrlPayload(Constants.ADDITEM,"");
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

        description.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if(validation())
                    {
                        Log.e("callied","validation");
                        if(flag){

                            setUrlPayload(Constants.UPDATEITEM,arrayData.get(position).id);
                        }
                        else
                        {
                            setUrlPayload(Constants.ADDITEM,"");
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

       if(subcateg_name.getText().toString().trim().length()==0){
           // categ_name.setError("Product Name should not be null!");
            util.customToast(getResources().getString(R.string.productnamenull));
           subcateg_name.requestFocus();
            flag = false;
        }

        else if(description.getText().toString().trim().length()==0){
            util.customToast(getResources().getString(R.string.descriptionnull));
            description.requestFocus();
            flag = false;
        }
        return flag;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.add) {
            addData(false, 0);
/*
            if (product_array.size()==10){
                util.customToast("Sorry!!You can add Only 10 Products");
            }
            else {
                addData(false, 0);
            }*/
            return true;
        }

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }


        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate( R.menu.menu_add, menu );
        MenuItem addOption = menu.findItem(R.id.add);
        addOption.setVisible(true);
        return true;
    }
    public void setUrlPayload(int action,String extra) {
        switch (action){
            case Constants.GETLIST_1:
                JSONObject j_obj = new JSONObject();
                try {
                    j_obj.put("merchant_location_id", merchant_location_id);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                jsonwebrequest(j_obj,Constants.GETLIST_1,Constants.VIEW);
                break;
            case Constants.GETLIST:
                 j_obj = new JSONObject();
                try {
                    j_obj.put("category_id", extra );

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                jsonwebrequest(j_obj,Constants.GETLIST,Constants.VIEW_SUB);
                break;
            case  Constants.ADDITEM:

                        //sendDataToServer(Constants.b_Add_Product);
                j_obj = new JSONObject();
                try {
                    JSONObject data = new JSONObject();
                    data.put("merchant_location_id", merchant_location_id);
                    data.put("admin_id", admin_id);
                    JSONObject categ = new JSONObject();
                    categ.put("title", subcateg_name.getText().toString().trim());
                    categ.put("parent_id", parent.id);
                    categ.put("code",  code.getText().toString().trim());

                    categ.put("description", description.getText().toString().trim());
                    JSONArray categories =new JSONArray();
                    categories.put(categ);
                    data.put("sub_categories",categories);
                    j_obj.put("data",data);
                    jsonwebrequest(j_obj,Constants.ADDITEM,Constants.CREATE);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;

            case  Constants.UPDATEITEM:

                   //  sendDataToServer(Constants.b_Update_Product);
                 j_obj = new JSONObject();
                try {
                    JSONObject data = new JSONObject();
                    data.put("merchant_location_id", merchant_location_id);
                    data.put("admin_id", admin_id);
                    JSONObject categ = new JSONObject();
                    categ.put("id", extra);
                    categ.put("code",  code.getText().toString().trim());
                    categ.put("title", subcateg_name.getText().toString().trim());
                    categ.put("description", description.getText().toString().trim());
                    JSONArray categories =new JSONArray();
                    categories.put(categ);
                    data.put("sub_categories",categories);
                    j_obj.put("data",data);
                    jsonwebrequest(j_obj,Constants.UPDATEITEM,Constants.UPDATE);

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
                    categ.put("id", subcateg_id);
                    JSONArray categories =new JSONArray();
                    categories.put(categ);
                    data.put("categories",categories);
                    j_obj.put("data",data);
                    jsonwebrequest(j_obj,Constants.DELETEITEM,Constants.DELETE);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }



    }

    public void setReturnedData(int category, JSONObject j_obj) {
        switch (category){


            case Constants.GETLIST_1:
                Log.e("Apitransect",""+j_obj);
                cd.hide();
                try {
                    JSONObject categData =j_obj;
                    JSONArray re_j_obj = categData.getJSONArray("categories");
                    categories=(new Categories(re_j_obj)).categories;
                    cate_array = new ArrayList<>();
                    for (Categories.Category cate : categories){
                        cate_array.add(cate.title);
                    }
                    ArrayAdapter<String> uTyperArrayAdapter = new ArrayAdapter<String>(this, R.layout.item_spn_selected, cate_array);
                    uTyperArrayAdapter.setDropDownViewResource(R.layout.item_spn);
                    spn_category.setAdapter(uTyperArrayAdapter);
                    spn_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            parent = categories.get(i);
                            setUrlPayload(Constants.GETLIST, categories.get(i).id );
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case Constants.GETLIST:
                Log.e("Apitransect",""+j_obj);
                cd.hide();
                JSONArray re_j_obj;
                try {
                    returnedData =j_obj;
                    re_j_obj = returnedData.getJSONArray("sub_categories");
                    ArrayList<SubCategories.SubCategory> mContentItemss=(new SubCategories(re_j_obj)).sub_categories;

                    if(arrayData.size()==0){
                        arrayData.clear();
                        for(int i =0 ;i<mContentItemss.size();i++){
                            arrayData.add(mContentItemss.get(i));
                        }
                        adapter.notifyDataSetChanged();

                    }
                    else {
                        arrayData.clear();
                        ArrayList<SubCategories.SubCategory> json = (new SubCategories(re_j_obj)).sub_categories;
                        for(int i =0 ;i<json.size();i++){
                            arrayData.add(json.get(i));
                        }
                        adapter.notifyDataSetChanged();
                    }
                    if(arrayData.size()!=0) {
                        txt_producttitle.setText("SubCategories" + " (" + re_j_obj.length() + ")");
                        listview.setVisibility(View.VISIBLE);
                        tv_no_record.setVisibility(View.GONE);
                    }
                    else
                    {
                        listview.setVisibility(View.GONE);
                        tv_no_record.setVisibility(View.VISIBLE);
                        txt_producttitle.setText("SubCategories" + "(0)");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;


            case Constants.ADDITEM:
                Log.e("Apitransect",""+j_obj);
                //fillData(2);
                setUrlPayload(Constants.GETLIST, categories.get(spn_category.getSelectedItemPosition()).id );
                break;
            case Constants.UPDATEITEM:
                Log.e("Apitransect",""+j_obj);
                //fillData(3);
                setUrlPayload(Constants.GETLIST, categories.get(spn_category.getSelectedItemPosition()).id );

                break;
            case Constants.DELETEITEM:
                Log.e("Apitransect",""+j_obj);
                //fillData(4);
                setUrlPayload(Constants.GETLIST, categories.get(spn_category.getSelectedItemPosition()).id );

                break;
        }
    }


    public void jsonwebrequest(final JSONObject j_obj, final int action, final String action_text) {
        if (util.checkInternetConnection()) {
            cd.show("");

            StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.CategoryAPI,
                    new Response.Listener<String>() {
                        public JSONObject data;

                        @Override
                        public void onResponse(String response) {
                            cd.hide();
                            Log.e("loadTrans", ""+response);
                            try {
                                if((new JSONObject(response)).getString("status").equalsIgnoreCase("1")) {

                                    JSONObject returnJSON =new JSONObject();
                                    if(!(new JSONObject(response)).isNull("data"))
                                        returnJSON = (new JSONObject(response)).getJSONObject("data");

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
                            Toast.makeText(SubCategoryActivity.this, error.toString(), Toast.LENGTH_LONG).show();
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
            RequestQueue requestQueue = Volley.newRequestQueue(SubCategoryActivity.this);
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


            case R.id.btn_ok:
                util.ButtonClickEffect(v);
                cd.hide();
                setUrlPayload(Constants.DELETEITEM,subcateg_id);
                if(adapter!=null)
                    adapter.cd.hide();
                cd.hide();
                break;
              /*

            case R.id.btnwifi:
                util.ButtonClickEffect(v);
                boolean result=util.checkPermissionwifi(ProductActivity.this);
                if(result) {
                    WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
                    if (!wifi.isWifiEnabled()) {
                        wifi.setWifiEnabled(true);
                        cd.hide();
                    }
                }
                if(adapter!=null)
                    adapter.cd.hide();
                break;
              */

         /*   case R.id.btnreconnect:
                util.ButtonClickEffect(v);
                if(adapter!=null)
                    adapter.cd.hide();
                cd.hide();
                setUrlPayload(Constants.DELETEITEM, Integer.parseInt(product_array.get(position)[1]));
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
