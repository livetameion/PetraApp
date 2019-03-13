package com.yagna.petra.app.AdvncedCashier.Search;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yagna.petra.app.Admin.Transaction.TransactionFragment;
import com.yagna.petra.app.AdvncedCashier.SubCategory.SubCategoryActivity;
import com.yagna.petra.app.Model.Product.Categories;
import com.yagna.petra.app.R;

import org.json.JSONException;

import java.util.ArrayList;


/**
 * Fills the result line with the passed cursor
 */
public class SearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private final boolean mTwoPane;
    SearchActivity act;

    ArrayList<Categories.Category> categories;

    // Constructors ________________________________________________________________________________


    public SearchAdapter(SearchActivity searchActivity, ArrayList<Categories.Category> categories, boolean mTwoPane) {
        this.categories = categories;
        this.act = searchActivity;
        this.mTwoPane = mTwoPane;

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
        viewHolder.cv_add.setVisibility(View.GONE);
        viewHolder.header.setText(categories.get(position).title);
        viewHolder.header.setTextColor(act.getResources().getColor(R.color.black));
        viewHolder.arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (categories.get(position).sub_cat_view) {
                    categories.get(position).sub_cat_view = false;
                } else
                    categories.get(position).sub_cat_view = true;
                if(mTwoPane)
                    act.ProdsAndSubCategories(position);
                else
                     act.fillSubCategories(position);

            }
        });
        viewHolder.rd_wrapper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mTwoPane){
                    if (categories.get(position).sub_cat_view) {
                        categories.get(position).sub_cat_view = false;
                    } else
                        categories.get(position).sub_cat_view = true;
                }
                 notifyDataSetChanged();
                 act.ProdsAndSubCategories(position);
                act.txt_all.setTextColor(act.getResources().getColor(R.color.black));
            }
        });

        if (categories.get(position).sub_cat_view) {
            viewHolder.rv_sub_list.setVisibility(View.VISIBLE);
            viewHolder.arrow.setImageResource(R.drawable.ic_down_shift);
        } else {
            viewHolder.rv_sub_list.setVisibility(View.GONE);
            viewHolder.arrow.setImageResource(R.drawable.ic_right_shift);

        }


        if (categories.get(position).subCategories.size() != 0) {

            RecyclerView.LayoutManager storiesLayoutManager = createLayoutManager(act.getResources());
            viewHolder.rv_sub_list.setLayoutManager(storiesLayoutManager);
            SearchSubAdapter adpt = new SearchSubAdapter(act, categories.get(position).subCategories, mTwoPane, position);
            viewHolder.rv_sub_list.setAdapter(adpt);
        }


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
        public CardView cv_add;
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
