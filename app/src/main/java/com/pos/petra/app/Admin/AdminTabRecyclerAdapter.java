package com.pos.petra.app.Admin;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.pos.petra.app.Admin.Charity.CharityFragment;
import com.pos.petra.app.Admin.StoreInformation.StoreInformationFragment;
import com.pos.petra.app.Admin.ValueCalculations.ValueCalculationsFragment;
import com.pos.petra.app.Admin.Transaction.TransactionFragment;
import com.pos.petra.app.Admin.CashierUsers.CashiersFragment;
import com.pos.petra.app.Admin.Wallet.WalletFragment;
import com.pos.petra.app.Admin.Product.ProductDashboard.ProductDashboardActivity;
import com.pos.petra.app.R;
import com.pos.petra.app.Views.TypefacedTextView;

import java.util.ArrayList;
import java.util.List;

public class AdminTabRecyclerAdapter
        extends RecyclerView.Adapter<AdminTabRecyclerAdapter.ViewHolder> {

    private final TransactionFragment tr_fragment;
    private final CashiersFragment usr_fragment;
    private final WalletFragment wallet_fragment;
    private final ValueCalculationsFragment preferenceFragment;
    private final StoreInformationFragment storeInformationFragment;
    private final CharityFragment charityFragment;
    private AdminListActivity mParentActivity;
    private List<String> mValues;
    private boolean mTwoPane;
    private List<String> check_status;
    AdminListActivity adminListActivity;
    AdminTabRecyclerAdapter(
            AdminListActivity parent,
            ArrayList<String> items,
            ArrayList<String> check_stat,
            boolean twoPane, AdminListActivity adminListActivity) {
        mValues = items;
        check_status = check_stat;
        mParentActivity = parent;
        mTwoPane = twoPane;
        this.adminListActivity = adminListActivity;
        tr_fragment = new TransactionFragment();
        usr_fragment = new CashiersFragment();
        wallet_fragment = new WalletFragment();
        preferenceFragment = new ValueCalculationsFragment();
        storeInformationFragment = new StoreInformationFragment();
        charityFragment = new CharityFragment();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_content, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mIdView.setText(mValues.get(position));
        holder.mContentView.setText("");

        if (check_status.get(position).equalsIgnoreCase("1")) {
            holder.ly_item.setBackgroundColor(adminListActivity.getResources().getColor(R.color.dark_blue));
            holder.mIdView.setTextColor(adminListActivity.getResources().getColor(R.color.white));
            setWhiteIcon(holder.icon, position);
        } else {
            holder.ly_item.setBackgroundColor(adminListActivity.getResources().getColor(R.color.white));
            holder.mIdView.setTextColor(adminListActivity.getResources().getColor(R.color.black));
            setBlueIcon(holder.icon, position);
        }

        holder.itemView.setTag(mValues.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // DummyContent.DummyItem item = (DummyContent.DummyItem) view.getTag();
                for (int i = 0; i < adminListActivity.click_status.size(); i++) {
                    if(i != 5 ) {
                        if (i == position) {
                            adminListActivity.click_status.set(i, "1");
                        } else {
                            adminListActivity.click_status.set(i, "0");
                        }
                    }
                }
                if(!mTwoPane){
                    mParentActivity.drawer.closeDrawer(Gravity.LEFT);
                }
                notifyDataSetChanged();

                    Bundle arguments = new Bundle();
                    arguments.putString(TransactionFragment.ARG_ITEM_ID, "" + position);

                    switch (position) {
                        case 0:

                            tr_fragment.setArguments(arguments);
                            mParentActivity.getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.item_detail_container, tr_fragment)
                                    .commit();
                            break;
                        case 1:
                            usr_fragment.setArguments(arguments);
                            mParentActivity.getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.item_detail_container, usr_fragment)
                                    .commit();
                            break;
                        case 2:

                            wallet_fragment.setArguments(arguments);
                            mParentActivity.getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.item_detail_container, wallet_fragment)
                                    .commit();
                            break;

                        case 3:

                            preferenceFragment.setArguments(arguments);
                            mParentActivity.getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.item_detail_container, preferenceFragment)
                                    .commit();
                            break;
                        case 4:

                            storeInformationFragment.setArguments(arguments);
                            mParentActivity.getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.item_detail_container, storeInformationFragment)
                                    .commit();
                            break;
                        case 5:

                            Context context = view.getContext();
                            Intent intent = new Intent(context, ProductDashboardActivity.class);
                            context.startActivity(intent);
                            break;
                        case 6:

                            charityFragment.setArguments(arguments);
                            mParentActivity.getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.item_detail_container, charityFragment)
                                    .commit();
                            break;

                    }



                /* else {
                    Context context = view.getContext();
                    Intent intent;
                    if (position == 5) {
                        intent = new Intent(context, SearchActivity.class);
                       *//* context.startActivity(i);
                        intent = new Intent(context, ActionListActivity.class);*//*
                    } else
                        intent = new Intent(context, TransactionSingleViewActivity.class);
                    intent.putExtra(TransactionFragment.ARG_ITEM_ID, "" + position);
                    context.startActivity(intent);
                }*/
            }
        });
    }

    //Change Icon color on deselection
    private void setBlueIcon(ImageView icon, int position) {

        switch (position) {
            case 0:
                icon.setImageResource(R.mipmap.doc_blue);
                break;
            case 1:
                icon.setImageResource(R.mipmap.user_blue);
                break;
            case 2:
                icon.setImageResource(R.mipmap.wallet_blue);
                break;
            case 3:
                icon.setImageResource(R.mipmap.calci_blue);
                break;
            case 4:
                icon.setImageResource(R.mipmap.shop_blue);
                break;
            case 5:
                icon.setImageResource(R.mipmap.barcode_blue);
                break;
            case 6:
                icon.setImageResource(R.mipmap.ribbon_blue);
                break;


        }
    }
    //Change Icon color on selection
    private void setWhiteIcon(ImageView icon, int position) {

        switch (position) {
            case 0:
                icon.setImageResource(R.mipmap.doc);
                break;
            case 1:
                icon.setImageResource(R.mipmap.user);
                break;
            case 2:
                icon.setImageResource(R.mipmap.wallet);
                break;
            case 3:
                icon.setImageResource(R.mipmap.calci);
                break;
            case 4:
                icon.setImageResource(R.mipmap.shop);
                break;
            case 5:
                icon.setImageResource(R.mipmap.barcode_white);
                break;
            case 6:
                icon.setImageResource(R.mipmap.ribbon_white);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final TypefacedTextView mIdView;
        final TypefacedTextView mContentView;
        final LinearLayout ly_item;
        final ImageView icon;

        ViewHolder(View view) {
            super(view);
            icon = (ImageView) view.findViewById(R.id.icon);
            mIdView = (TypefacedTextView) view.findViewById(R.id.id_text);
            mContentView = (TypefacedTextView) view.findViewById(R.id.content);
            ly_item = (LinearLayout) view.findViewById(R.id.ly_item);
        }
    }
}