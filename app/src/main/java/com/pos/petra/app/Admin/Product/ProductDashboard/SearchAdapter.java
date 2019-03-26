package com.pos.petra.app.Admin.Product.ProductDashboard;

import android.content.res.Resources;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.pos.petra.app.AdvancedCashier.DataKeeper;
import com.pos.petra.app.Model.Product.Categories;
import com.pos.petra.app.Model.Product.Products;
import com.pos.petra.app.R;
import com.pos.petra.app.Util.CustomDialog;

import java.util.ArrayList;


/**
 * Fills the result line with the passed cursor
 */
public class SearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private final boolean mTwoPane;
    ProductDashboardActivity act;
    CustomDialog cd;
    ArrayList<Categories.Category> categories;

    // Constructors ________________________________________________________________________________


    public SearchAdapter(ProductDashboardActivity searchActivity, ArrayList<Categories.Category> categories, boolean mTwoPane) {
        this.categories = categories;
        this.act = searchActivity;
        this.mTwoPane = mTwoPane;
        cd=new CustomDialog(act);

    }


    // Callbacks ___________________________________________________________________________________
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.custom_searchable_row_details, parent, false);
        ViewHolder holder = new ViewHolder(view);
        // UI configuration
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.subHeader.setVisibility(View.GONE);
        viewHolder.cb_chcek.setVisibility(View.GONE);
        // viewHolder.cv_add.setVisibility(View.GONE);
        viewHolder.header.setText(categories.get(position).title);
        viewHolder.header.setTextColor(act.getResources().getColor(R.color.black));
       /* viewHolder.arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (categories.get(position).sub_cat_view) {
                    categories.get(position).sub_cat_view = false;
                } else
                    categories.get(position).sub_cat_view = true;

                for(int i=0;i<categories.size();i++){
                    if(position==i){
                        categories.get(i).isSelected=true;
                    }
                    else {
                        categories.get(i).isSelected=false;
                    }
                }
                if(mTwoPane)
                    act.ProdsAndSubCategories(position);
                else
                     act.fillSubCategories(position);

            }
        });*/
        viewHolder.rd_wrapper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if(mTwoPane)
                {
                    if (categories.get(position).sub_cat_view) {
                        categories.get(position).sub_cat_view = false;
                    } else
                        categories.get(position).sub_cat_view = true;
                }

                for(int i=0;i<categories.size();i++){
                    if(position==i){
                        if(categories.get(position).isSelected) {
                            categories.get(i).isSelected = false;
                            DataKeeper.search_product_array.clear();
                            for(int k =0 ;k<DataKeeper.all_products_array.size();k++){
                                try {
                                    DataKeeper.search_product_array.add((Products.Product)DataKeeper.all_products_array.get(k).clone());
                                } catch (CloneNotSupportedException e) {
                                    e.printStackTrace();
                                }
                            }
                            act.fillProducts("",true);
                            act.txt_all.setTextColor(act.getResources().getColor(R.color.ink_blue));
                            act.selected_cate_id="";
                            act.selected_posit= "";

                        }
                        else{
                            categories.get(i).isSelected = true;
                            act.ProdsAndSubCategories(position);
                            act.txt_all.setTextColor(act.getResources().getColor(R.color.black));
                            act.selected_cate_id=categories.get(position).id;
                            act.selected_posit= ""+position;
                        }
                    }
                    else {
                        categories.get(i).isSelected =false;
                    }
                }


                notifyDataSetChanged();
            }
        });
        if(categories.get(position).isSelected){
            viewHolder.header.setTextColor(act.getResources().getColor(R.color.ink_blue));
            viewHolder.rv_sub_list.setVisibility(View.VISIBLE);
            viewHolder.arrow.setImageResource(R.mipmap.up_ink_blue);
        }
        else {
            viewHolder.header.setTextColor(act.getResources().getColor(R.color.black));
            viewHolder.rv_sub_list.setVisibility(View.GONE);
            viewHolder.arrow.setImageResource(R.mipmap.down_black);
        }


       /* if (categories.get(position).subCategories.size() != 0) */{

            RecyclerView.LayoutManager storiesLayoutManager = createLayoutManager(act.getResources());
            viewHolder.rv_sub_list.setLayoutManager(storiesLayoutManager);
            SearchSubAdapter adpt = new SearchSubAdapter(act, categories.get(position).subCategories, mTwoPane, position);
            viewHolder.rv_sub_list.setAdapter(adpt);
        }

        viewHolder.cv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //creating a popup menu
                PopupMenu popup = new PopupMenu(act, viewHolder.cv_add);
                //inflating menu from xml resource
                popup.inflate(R.menu.crud_cat_menu);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.add:
                                //handle menu1 click
                                act.showAddSubCategNewDialog(false,position,0);
                                break;
                            case R.id.edit:
                                //handle menu2 click
                                act.showAddCategNewDialog(true,position);
                                break;
                            case R.id.delete:
                                //handle menu3 click
                                act.position=position;
                                cd.showAlertmulti(false,"Do you want to delete "+viewHolder.header.getText().toString()+" category?");
                                act.categ_id =categories.get(position).id;
                                notifyDataSetChanged();
                                break;
                        }
                        return false;
                    }
                });
                //displaying the popup
                popup.show();


            }
        });

    }

    private RecyclerView.LayoutManager createLayoutManager(Resources resources) {
        int spans = 1;/*resources.getInteger(R.integer.feed_columns);*/
        return new StaggeredGridLayoutManager(spans, RecyclerView.VERTICAL);
    }

    // Getters and Setters _________________________________________________________________________
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return (categories.size());
    }

    public Categories.Category getItem(Integer position) {
        return categories.get(position);

    }

    // View Holder _________________________________________________________________________________
    private class ViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout rd_wrapper;
        public TextView header;
        public TextView subHeader;
        public ImageView img_btn_edit;
        public ImageView img_btn_delete;
        public ImageView img_product, arrow;
        public AppCompatCheckBox cb_chcek;
        public ImageView cv_add;
        public RecyclerView rv_sub_list;

        public ViewHolder(View v) {
            super(v);
            this.img_product = (ImageView) v.findViewById(R.id.img_product);
            this.header = (TextView) v.findViewById(R.id.rd_header_text);
            this.subHeader = (TextView) v.findViewById(R.id.rd_sub_header_text);
            this.img_btn_edit = (ImageView) v.findViewById(R.id.img_btn_edit);
            this.img_btn_delete = (ImageView) v.findViewById(R.id.img_btn_add);
            this.rd_wrapper = (LinearLayout) v.findViewById(R.id.ly_main);
            this.cb_chcek = v.findViewById(R.id.cb_chcek);
            this.cv_add = v.findViewById(R.id.cv_add);
            this.arrow = v.findViewById(R.id.arrow);
            this.rv_sub_list = v.findViewById(R.id.rv_sub_list);
        }
    }
}
