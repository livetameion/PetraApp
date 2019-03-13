package com.yagna.petra.app.Product;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.yagna.petra.app.GlobalActivity;
import com.yagna.petra.app.R;
import com.yagna.petra.app.Util.CustomDialog;
import com.yagna.petra.app.Util.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ActionListActivity extends GlobalActivity {
    public ArrayList<Bitmap> bitmapArray;
    private EditText categ_name, p_description;
    private ImageView p_image,camera,gallery;
    private static final int CAMERA_REQUEST = 1888;
    private static int REQUEST_PICTURE = 1;
    private static int REQUEST_CROP_PICTURE = 2;
    private File lastCaptured;
    private Uri imageUri;
    public ListView listview;
    int SELECT_FILE=2,REQUEST_CAMERA=3;
    public ArrayList<String> arrayData;
    public TextView tv_no_record;
    private ActionListAdpter adapter;
    private EditText b_address,b_city,b_state,b_oprator,b_ofice_no,b_email,b_name,b_pincode;
    private SharedPreferences common_mypref;
    private JSONObject returnedData;
    private Utils util;
    private CustomDialog cd;
    private TextView title,texttitle_product;
    private ActionListActivity context;
    public int position;
    private JSONArray returnedImageData;
    public ArrayList<String[]> imageData;
    Uri myuri;
    private Uri imageToUploadUri;
    String imageid=null,productid=null;
    SimpleDateFormat sdf;
    String currentDateandTime;
    boolean imageupdate=false,flag=false;
    JSONArray j_arrayupdatearray;
    String productimagid[]=null;

    TextInputLayout txtxtitlep_discrption,txtxtitlep_name,txtxtitlep_code,txtxtitlep_price;
    TextView txt_producttitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.b_activity_action_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setVisibility(View.VISIBLE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       // fillBitmapArrayList();
        //fillDataArraylist();
        context= ActionListActivity.this;
        common_mypref = getApplicationContext().getSharedPreferences(
                "user", 0);
        util=new Utils(context);
        tv_no_record = (TextView) findViewById(R.id.tv_no_record_);
        util=new Utils(context);
        cd=new CustomDialog(context);
        txt_producttitle=(TextView)findViewById(R.id.txt_branchtitle);
        txt_producttitle.setText("Products");
        tv_no_record = (TextView) findViewById(R.id.tv_no_record_);
        bitmapArray=new ArrayList<>();
        arrayData = new ArrayList<>();
        imageData = new ArrayList<>();
        listview=(ListView)findViewById(R.id.listview);
        fillData(1);
        sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        listview.setAdapter(adapter = new ActionListAdpter(ActionListActivity.this,arrayData));
        listview.setVisibility(View.VISIBLE);
        tv_no_record.setVisibility(View.GONE);
    }


    private void fillData(int i){
        arrayData.add("Categories");
        arrayData.add("SubCategories");
        arrayData.add("Products");
        //arrayData.add("Modifiers");
        //arrayData.add("Discounts");
    }



    public void myClickMethod(final View v) {
        // /Function purpose to Perform ClickEvent of Element Based on view
        // /PARAM 1.v=view of the clicked Element....
        switch (v.getId()) {



            case R.id.btn_ok:
                util.ButtonClickEffect(v);
                cd.hide();
                if(adapter!=null)
                    adapter.cd.hide();
                cd.hide();
                break;


            case R.id.btn_cancel:
                util.ButtonClickEffect(v);
                cd.hide();
                if(adapter!=null)
                    adapter.cd.hide();
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
