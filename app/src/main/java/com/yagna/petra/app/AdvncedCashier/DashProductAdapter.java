package com.yagna.petra.app.AdvncedCashier;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yagna.petra.app.AdvncedCashier.Discount.DiscountActivity;
import com.yagna.petra.app.AdvncedCashier.Modifer.ModifierActivity;
import com.yagna.petra.app.Model.Product.Products;
import com.yagna.petra.app.R;
import com.yagna.petra.app.Util.CustomDialog;
import com.yagna.petra.app.Util.TouchImageView;
import com.yagna.petra.app.Util.Utils;
import com.yagna.petra.app.views.TypefacedTextView;

import java.util.ArrayList;

public class DashProductAdapter extends RecyclerView.Adapter<DashProductAdapter.ViewHolder> {

    private final SharedPreferences common_mypref;
    final CustomDialog cd;
    private final Utils util;
    private final ArrayList<Products.Product> arrayData;
    AdvCashierActivity context;
    private static LayoutInflater inflater = null;

    public DashProductAdapter(AdvCashierActivity productActivity, ArrayList<Products.Product> arrayData) {
        context = productActivity;
        util = new Utils(context.getApplicationContext());
        this.arrayData = arrayData;

        common_mypref = context.getSharedPreferences(
                "user", 0);
        cd = new CustomDialog(context);
        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.d_product_item, parent, false);
        DashProductAdapter.ViewHolder vh = new DashProductAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

          int stock = 0;
        if (!arrayData.get(position).selected_modifier.equalsIgnoreCase("")) {
            int p = Integer.parseInt(arrayData.get(position).selected_modifier);
            holder.p_name.setText(arrayData.get(position).modifier.get(p).title);
            holder.p_price.setText("$"+String.format("%.2f",Double.parseDouble(arrayData.get(position).modifier.get(p).price)));
            if (arrayData.get(position).modifier.get(p).image_full_path != null && arrayData.get(position).modifier.get(p).image_full_path.trim().length() != 0) {
                Picasso.get()
                        .load(arrayData.get(position).modifier.get(p).image_full_path.replace(" ", "%20"))
                         .placeholder(R.mipmap.petra)
                         .error(R.mipmap.petra)
                        .into(holder.p_img);
            } else {
                Picasso.get()
                        .load(R.mipmap.petra)
                        .placeholder(R.mipmap.petra)
                        .error(R.mipmap.petra)
                        .into(holder.p_img);
            }
            holder.txt_add_modif.setText(" " + (arrayData.get(position).modifier.size() == 0 ? "(0)" : "(Applied)"));
            holder.txt_add_discount.setText(" " + (arrayData.get(position).discount.size() == 0 ? "(0)" : "(" + arrayData.get(position).discount.size() + ")"));

            holder.txt_remove_modif.setVisibility(View.VISIBLE);
            holder.txt_remove_modif.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DataKeeper.products_array.get(position).selected_modifier = "";
                    notifyDataSetChanged();
                }
            });
             stock = Integer.parseInt(DataKeeper.products_array.get(position).modifier.get(p).stock);



        } else {
            if (arrayData.get(position).image_full_path != null && arrayData.get(position).image_full_path.trim().length() != 0) {
                Picasso.get()
                        .load(arrayData.get(position).image_full_path.replace(" ", "%20"))
                        // .placeholder(R.mipmap.petra)
                        //  .error(R.mipmap.petra)
                        .into(holder.p_img);
            } else {
                Picasso.get()
                        .load(R.mipmap.petra)
                        .placeholder(R.mipmap.petra)
                        .error(R.mipmap.petra)
                        .into(holder.p_img);
            }

            holder.p_name.setText(arrayData.get(position).title);
            holder.p_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showimage(position);
                }
            });
            holder.p_price.setText("$ " + String.format("%.2f",Double.parseDouble(arrayData.get(position).price)));
            holder.txt_add_modif.setText(" " + (arrayData.get(position).modifier.size() == 0 ? "(0)" : "(" + arrayData.get(position).modifier.size() + ")"));
            holder.txt_add_discount.setText(" " + (arrayData.get(position).discount.size() == 0 ? "(0)" : "(" + arrayData.get(position).discount.size() + ")"));
            holder.txt_remove_modif.setVisibility(View.GONE);
            stock = Integer.parseInt(DataKeeper.products_array.get(position).stock);

        }
        if (!arrayData.get(position).selected_discount.equalsIgnoreCase("")) {
           holder.txt_remove_disc.setVisibility(View.VISIBLE);
        }else{
            holder.txt_remove_disc.setVisibility(View.GONE);

        }

        if(stock>10){
            holder.stock.setText("In Stock");
            holder.stock.setTextColor(context.getResources().getColor(R.color.dark_green));
        }
        else {
            holder.stock.setText("Only "+ stock+ " left in stock");
            holder.stock.setTextColor(context.getResources().getColor(R.color.red));
        }


        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.position = position;
                cd.showAlertmulti(false, "Do you want to delete \"" + holder.p_name.getText().toString() + "\" Product?");
                notifyDataSetChanged();
                if (arrayData.size() == 0) {
                    context.list_products.setVisibility(View.GONE);
                    context.tv_no_record_.setVisibility(View.VISIBLE);
                } else {
                    context.list_products.setVisibility(View.VISIBLE);
                    context.tv_no_record_.setVisibility(View.GONE);
                }

            }
        });

        holder.discount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, DiscountActivity.class);
                i.putExtra("id", "" + (position));
                context.startActivity(i);
            }
        });
        if (arrayData.get(position).modifier.size() > 0)
            holder.modifier.setVisibility(View.VISIBLE);
        else
            holder.modifier.setVisibility(View.GONE);

        if (arrayData.get(position).discount.size() > 0)
            holder.discount.setVisibility(View.VISIBLE);
        else
            holder.discount.setVisibility(View.GONE);

        holder.modifier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, ModifierActivity.class);
                i.putExtra("id", "" + (position));
                context.startActivity(i);
            }
        });
        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataKeeper.products_array.get(position).qty++;
                notifyDataSetChanged();
            }
        });
        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (DataKeeper.products_array.get(position).qty > 1)
                    DataKeeper.products_array.get(position).qty--;
                notifyDataSetChanged();
            }
        });
        holder.edt_qty.setText("" + DataKeeper.products_array.get(position).qty);
        holder.edt_qty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length()>0&&Integer.parseInt(charSequence.toString()) > 0) {
                    DataKeeper.products_array.get(position).qty = Integer.parseInt(charSequence.toString());
                    DataKeeper.products_array.get(position).price_applied = Double.parseDouble(holder.p_price.getText().toString().replace("$", "")) * DataKeeper.products_array.get(position).qty;

                    context.countTotal();

                } else {
                    DataKeeper.products_array.get(position).qty = 0;
                    DataKeeper.products_array.get(position).price_applied = Double.parseDouble(holder.p_price.getText().toString().replace("$", "")) * DataKeeper.products_array.get(position).qty;

                    context.countTotal();

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //notifyDataSetChanged();
            }
        });

        if (!arrayData.get(position).selected_discount.equalsIgnoreCase("")) {
            holder.product_disc_price.setVisibility(View.VISIBLE);
            holder.product_disc_price.setText("(" + holder.p_price.getText().toString() + ")");
            String rate = arrayData.get(position).discount.get(Integer.parseInt(arrayData.get(position).selected_discount)).subtitle;
            double org_price = Double.parseDouble(holder.p_price.getText().toString().trim().replace("$", ""));
            rate.replace("%", "");
            float r = Float.parseFloat(rate);
            double new_price = org_price * r / 100;
            DataKeeper.products_array.get(position).new_price = new_price;
            holder.p_price.setText("$" + String.format("%.2f", new_price));
            holder.txt_remove_disc.setVisibility(View.VISIBLE);
            holder.txt_remove_disc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DataKeeper.products_array.get(position).selected_discount = "";
                    notifyDataSetChanged();
                }
            });


            DataKeeper.products_array.get(position).price_applied = new_price * DataKeeper.products_array.get(position).qty;

        } else {
            holder.product_disc_price.setVisibility(View.GONE);
            holder.txt_remove_disc.setVisibility(View.GONE);
            DataKeeper.products_array.get(position).new_price = Double.parseDouble(holder.p_price.getText().toString().replace("$", ""));
            DataKeeper.products_array.get(position).price_applied = Double.parseDouble(holder.p_price.getText().toString().replace("$", "")) * DataKeeper.products_array.get(position).qty;
        }
        context.countTotal();

    }

    public void showimage(int p) {
        final Dialog Dialog = new Dialog(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = (View) inflater.inflate(R.layout.banner_full_image, null);
        Rect displayRectangle = new Rect();
        Window window = context.getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        rowView.setMinimumWidth((int) (displayRectangle.width() * 1f));
        rowView.setMinimumHeight((int) (displayRectangle.height() * 0.5f));
        TouchImageView imgview = (TouchImageView) rowView.findViewById(R.id.image_detail);
        if (arrayData.get(p).image_full_path != null & arrayData.get(p).image_full_path.trim().length() != 0) {
            Picasso.get()
                    .load(arrayData.get(p).image_full_path.replace(" ", "%20"))
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
      /*  Button backfullimg = (Button) rowView.findViewById(R.id.back_fullimg);
        backfullimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog.dismiss();
            }
        });*/
        Dialog.setContentView(rowView);
        Dialog.setCancelable(true);
        Dialog.setCanceledOnTouchOutside(true);
        Dialog.show();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return arrayData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final EditText edt_qty;
        private final TextView minus;
        private final TextView plus;
        private final TextView txt_remove_modif;
        private final TextView txt_remove_disc;
        private final TextView product_disc_price;
        TextView delete, p_name, p_price, product_sku, p_description, txt_add_modif, txt_add_discount, stock, txt_titleprice, txt_titlepcode, txt_titlepname;
        ImageView p_img;
        LinearLayout discount, modifier;
        Button edit;


        public ViewHolder(View rowView) {
            super(rowView);
            p_img = (ImageView) rowView.findViewById(R.id.p_img);
            p_name = (TextView) rowView.findViewById(R.id.product_name);
            p_price = (TextView) rowView.findViewById(R.id.product_price);
            delete = (TextView) rowView.findViewById(R.id.delete_);
            stock= (TextView) rowView.findViewById(R.id.stock);
            discount = (LinearLayout) rowView.findViewById(R.id.discount);
            modifier = (LinearLayout) rowView.findViewById(R.id.modifier);
            txt_add_modif = rowView.findViewById(R.id.txt_add_modif);
            txt_add_discount = rowView.findViewById(R.id.txt_add_discount);
            minus = (TextView) rowView.findViewById(R.id.minus);

            edt_qty = (EditText) rowView.findViewById(R.id.edt_qty);
            plus = (TextView) rowView.findViewById(R.id.plus);
            product_disc_price = (TextView) rowView.findViewById(R.id.product_disc_price);
            txt_remove_modif = (TextView) rowView.findViewById(R.id.txt_remove_modif);
            txt_remove_disc = (TextView) rowView.findViewById(R.id.txt_remove_disc);
        }
    }


}
