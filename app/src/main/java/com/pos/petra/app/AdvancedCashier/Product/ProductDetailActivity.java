package com.pos.petra.app.AdvancedCashier.Product;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cunoraz.tagview.TagView;
import com.squareup.picasso.Picasso;
import com.pos.petra.app.AdvancedCashier.DataKeeper;
import com.pos.petra.app.AdvancedCashier.BrowseAndSearch.SearchActivity;
import com.pos.petra.app.Model.Product.Categories;
import com.pos.petra.app.Model.Product.Products;
import com.pos.petra.app.Model.Product.SubCategories;
import com.pos.petra.app.R;
import com.pos.petra.app.Util.CustomDialog;
import com.pos.petra.app.Util.PrefUtil;
import com.pos.petra.app.Util.TouchImageView;
import com.pos.petra.app.Util.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ProductDetailActivity extends AppCompatActivity {

    public RecyclerView listview;
    public static ArrayList<Products.Product> product_array = new ArrayList<>();
    private Utils util;
    private CustomDialog cd;
    private ProductDetailActivity context;
    public int position;
    private LinearLayout  modifier, discount;
    private TextView txt_discount_count, rd_header_text, product_nondisc_price, rd_header_brand, product_desc, txt_modif_name, txt_modif_stock, txt_modif_type, txt_modif_count, txt_stock_count,  txt_price;
    private ImageView img_modif,txt_remove_disc,plus,minus;
    private TextView edt_qty;
    private ImageView img_product;
    public Dialog dialogModifer;
    private ImageView txt_remove_modif;
    public Dialog dialogDiscount;
    public CardView cv_add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        context = ProductDetailActivity.this;
        util = new Utils(context);
        util = new Utils(context);
        cd = new CustomDialog(context);

        if (DataKeeper.detail_product.discount.size() > 0)
            DataKeeper.detail_product.selected_discount = "0";
        img_product = findViewById(R.id.img_product);
        product_desc = findViewById(R.id.product_desc);
        img_modif = findViewById(R.id.img_modif);
        txt_modif_type = findViewById(R.id.txt_modif_type);
        modifier = findViewById(R.id.modifier);
        txt_modif_count = findViewById(R.id.txt_modif_count);
        txt_stock_count = findViewById(R.id.txt_stock_count);
        txt_discount_count = findViewById(R.id.txt_discount_count);
        txt_remove_disc = findViewById(R.id.txt_remove_disc);
        discount = findViewById(R.id.discount);
        rd_header_text = findViewById(R.id.rd_header_text);
        txt_price = findViewById(R.id.txt_price);
        product_nondisc_price = findViewById(R.id.product_nondisc_price);
        rd_header_brand = findViewById(R.id.rd_header_brand);
        txt_modif_stock = findViewById(R.id.txt_modif_stock);
        txt_modif_name = findViewById(R.id.txt_modif_name);
        txt_remove_modif = findViewById(R.id.txt_remove_modif);
        minus = findViewById(R.id.minus);
        edt_qty = findViewById(R.id.edt_qty);
        plus = findViewById(R.id.plus);
        cv_add = findViewById(R.id.cv_add);
        fillDetail();

    }


    public void fillDetail() {


        if (DataKeeper.detail_product.selected_modifier.trim().length() == 0) {
            try {
                Picasso.get().load(DataKeeper.detail_product.image_full_path)
                        // .placeholder(R.mipmap.petra)
                        .error(R.mipmap.petra)
                        .into(img_product);
            } catch (Exception e) {
                Picasso.get().load(R.mipmap.petra)
                        // .placeholder(R.mipmap.petra)
                        .error(R.mipmap.petra)
                        .into(img_product);

            }

            product_desc.setText(DataKeeper.detail_product.description);
            try {
                Picasso.get()
                        .load(DataKeeper.detail_product.image_full_path)
                        // .placeholder(R.mipmap.petra)
                        .error(R.mipmap.petra)
                        .into(img_modif);
            } catch (Exception e) {
                Picasso.get().load(R.mipmap.petra)
                        // .placeholder(R.mipmap.petra)
                        .error(R.mipmap.petra)
                        .into(img_product);

            }

            txt_stock_count.setText("(" + DataKeeper.detail_product.stock + ")");
            txt_price.setText("$" + String.format("%.2f", Double.parseDouble(DataKeeper.detail_product.price)));
            rd_header_text.setText(DataKeeper.detail_product.title);
            rd_header_brand.setVisibility(View.GONE);
            if (DataKeeper.detail_product.discount.size() != 0) {
                txt_discount_count.setText("(" + DataKeeper.detail_product.discount.size() + ")");
                discount.setVisibility(View.VISIBLE);
                product_nondisc_price.setVisibility(View.VISIBLE);
                price_caluclator();
            } else {
                discount.setVisibility(View.GONE);
                product_nondisc_price.setVisibility(View.GONE);
            }
            txt_remove_modif.setVisibility(View.GONE);
        } else {
            txt_remove_modif.setVisibility(View.VISIBLE);
            int modif_posit = Integer.parseInt(DataKeeper.detail_product.selected_modifier);
            try {
                Picasso.get().load(DataKeeper.detail_product.modifier.get(modif_posit).image_full_path)
                        // .placeholder(R.mipmap.petra)
                        .error(R.mipmap.petra)
                        .into(img_product);
            }catch (Exception e){

            }

            product_desc.setText(DataKeeper.detail_product.modifier.get(modif_posit).description);

            try {
                Picasso.get()
                        .load(DataKeeper.detail_product.image_full_path)
                        // .placeholder(R.mipmap.petra)
                        .error(R.mipmap.petra)
                        .into(img_modif);
            }catch (Exception e){

            }

            txt_stock_count.setText("(" + DataKeeper.detail_product.modifier.get(modif_posit).stock + ")");
            txt_price.setText("$" + String.format("%.2f", Double.parseDouble(DataKeeper.detail_product.modifier.get(modif_posit).price)));
            rd_header_text.setText(DataKeeper.detail_product.modifier.get(modif_posit).title);
            rd_header_brand.setVisibility(View.GONE);
            if (DataKeeper.detail_product.discount.size() != 0) {
                txt_discount_count.setText("(" + DataKeeper.detail_product.discount.size() + ")");
                discount.setVisibility(View.VISIBLE);
                product_nondisc_price.setVisibility(View.VISIBLE);
                price_caluclator();
            } else {
                discount.setVisibility(View.GONE);
                product_nondisc_price.setVisibility(View.GONE);
            }

        }

        if (DataKeeper.detail_product.modifier.size() != 0) {
            modifier.setVisibility(View.VISIBLE);
            txt_modif_stock.setText("(" + DataKeeper.detail_product.modifier.get(0).stock + ")");
            txt_modif_name.setText(DataKeeper.detail_product.modifier.get(0).title);
            txt_modif_type.setText(DataKeeper.detail_product.modifier.get(0).type);
            txt_modif_count.setText("(" + DataKeeper.detail_product.modifier.size() + ")");
        } else {
            modifier.setVisibility(View.GONE);
        }
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataKeeper.detail_product.qty++;
                edt_qty.setText("" + DataKeeper.detail_product.qty);
                //price_caluclator();
            }
        });
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (DataKeeper.detail_product.qty > 1)
                    DataKeeper.detail_product.qty--;
                edt_qty.setText("" + DataKeeper.detail_product.qty);
                // price_caluclator();
            }
        });

        modifier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showModifDialog();
            }
        });

        discount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDiscDialog();
            }
        });

        txt_remove_modif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataKeeper.detail_product.selected_modifier = "";
                fillDetail();
            }
        });
        txt_remove_disc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataKeeper.detail_product.selected_discount = "";
                fillDetail();
            }
        });

        cv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchActivity.addTag(DataKeeper.detail_product);
                DataKeeper.detail_product = null;
                finish();
            }
        });


    }


    private void showModifDialog() {
        dialogModifer = new Dialog(ProductDetailActivity.this, android.R.style.Theme_Black_NoTitleBar);
        LayoutInflater inflater = getLayoutInflater();
        View rowView = (View) inflater.inflate(R.layout.dialog_recyleview, null);
        Rect displayRectangle = new Rect();
        Window window = getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        dialogModifer.getWindow().setBackgroundDrawableResource(R.color.translucent_black);
        final RecyclerView rv_list = rowView.findViewById(R.id.rv_list);

        StaggeredGridLayoutManager lm = new StaggeredGridLayoutManager(1, RecyclerView.VERTICAL);
        rv_list.setLayoutManager(lm);
        ModifierAdapter modif_adapter = new ModifierAdapter(context, DataKeeper.detail_product.modifier);
        rv_list.setAdapter(modif_adapter);
        LinearLayout ly_main = rowView.findViewById(R.id.ly_main);
        ly_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogModifer.dismiss();
            }
        });

        dialogModifer.setContentView(rowView);
        dialogModifer.setCancelable(true);
        dialogModifer.setCanceledOnTouchOutside(true);
        dialogModifer.show();
    }

    private void showDiscDialog() {
        dialogDiscount = new Dialog(ProductDetailActivity.this, android.R.style.Theme_Black_NoTitleBar);
        LayoutInflater inflater = getLayoutInflater();
        View rowView = (View) inflater.inflate(R.layout.dialog_recyleview, null);
        Rect displayRectangle = new Rect();
        Window window = getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        dialogDiscount.getWindow().setBackgroundDrawableResource(R.color.translucent_black);
        final RecyclerView rv_list = rowView.findViewById(R.id.rv_list);

        StaggeredGridLayoutManager lm = new StaggeredGridLayoutManager(1, RecyclerView.VERTICAL);
        rv_list.setLayoutManager(lm);
        DiscountAdapter disc = new DiscountAdapter(context, DataKeeper.detail_product.discount);
        rv_list.setAdapter(disc);
        LinearLayout ly_main = rowView.findViewById(R.id.ly_main);
        ly_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogDiscount.dismiss();
            }
        });
        dialogDiscount.setContentView(rowView);
        dialogDiscount.setCancelable(true);
        dialogDiscount.setCanceledOnTouchOutside(true);
        dialogDiscount.show();


    }

    private RecyclerView.LayoutManager createLayoutManager(Resources resources) {
        int spans = resources.getInteger(R.integer.feed_columns);
        return new StaggeredGridLayoutManager(spans, RecyclerView.VERTICAL);
    }

    public void price_caluclator() {
        if (!DataKeeper.detail_product.selected_discount.equalsIgnoreCase("")) {
            product_nondisc_price.setVisibility(View.VISIBLE);
            product_nondisc_price.setText(txt_price.getText().toString() + "  (" + DataKeeper.detail_product.discount.get(Integer.parseInt(DataKeeper.detail_product.selected_discount)).subtitle + "% Off)");
            String rate = DataKeeper.detail_product.discount.get(Integer.parseInt(DataKeeper.detail_product.selected_discount)).subtitle;
            double org_price = Double.parseDouble(txt_price.getText().toString().trim().replace("$", ""));
            rate.replace("%", "");
            float r = Float.parseFloat(rate);
            double new_price = org_price * r / 100;
            txt_price.setText("$" + String.format("%.2f", new_price));
            txt_remove_disc.setVisibility(View.VISIBLE);
            txt_remove_disc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DataKeeper.detail_product.selected_discount = "";
                    price_caluclator();
                }
            });
            DataKeeper.detail_product.price_applied = Double.parseDouble(DataKeeper.detail_product.price) * DataKeeper.detail_product.qty;
        } else {
            product_nondisc_price.setVisibility(View.GONE);
            txt_remove_disc.setVisibility(View.GONE);
            DataKeeper.detail_product.price_applied = Double.parseDouble(DataKeeper.detail_product.price) * DataKeeper.detail_product.qty;
            txt_price.setText("$" + String.format("%.2f", DataKeeper.detail_product.price_applied));
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add, menu);

        return true;
    }


    public void showimage(int p) {
        final Dialog Dialog = new Dialog(ProductDetailActivity.this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        LayoutInflater inflater = getLayoutInflater();
        View rowView = (View) inflater.inflate(R.layout.banner_full_image, null);
        Rect displayRectangle = new Rect();
        Window window = getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        rowView.setMinimumWidth((int) (displayRectangle.width() * 1f));
        rowView.setMinimumHeight((int) (displayRectangle.height() * 0.5f));
        TouchImageView imgview = (TouchImageView) rowView.findViewById(R.id.image_detail);
        if (product_array.get(position).image_full_path.trim().length() != 0) {
            Picasso.get()
                    .load(product_array.get(p).image_full_path.replace(" ", "%20"))
                    .placeholder(R.mipmap.petra)
                    .error(R.mipmap.petra)
                    .into(imgview);
        } else {
            Picasso.get()
                    .load(R.mipmap.petra)
                    .placeholder(R.mipmap.petra)
                    .error(R.mipmap.petra)
                    .into(imgview);
        }


        imgview.setMaxZoom(4f);

        Dialog.setContentView(rowView);
        Dialog.setCancelable(true);
        Dialog.setCanceledOnTouchOutside(true);
        Dialog.show();
    }


    public void myClickMethod(final View v) {

        switch (v.getId()) {

            case R.id.btn_ok:
                util.ButtonClickEffect(v);
                cd.hide();

                cd.hide();
                break;
            case R.id.btn_cancel:
                util.ButtonClickEffect(v);
                cd.hide();

                break;
        }
    }


}
