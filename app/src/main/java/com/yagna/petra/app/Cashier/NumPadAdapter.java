package com.yagna.petra.app.Cashier;
import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.yagna.petra.app.R;
import com.yagna.petra.app.Util.Utils;

public class NumPadAdapter extends BaseAdapter {
    private CashierActivity mContext;

    // Constructor
    public NumPadAdapter(CashierActivity c) {
        mContext = c;
    }

    public int getCount() {
        return mThumbIds.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewHolder holder;

        if (convertView == null) {
            grid = new View(mContext);
            holder = new ViewHolder();
            grid = inflater.inflate(R.layout.item_numpad, null);
            holder.textView = (Button) grid.findViewById(R.id.btn_signin);
            holder.backspace = (ImageView) grid.findViewById(R.id.img_signin);
            grid.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();

            grid = (View) convertView;
        }
       /* if(position==(mThumbIds.length-3)){
            holder.textView.setBackgroundColor(mContext.getResources().getColor(R.color.light_red));
        }*/
        //   final ImageButton backspace = (ImageButton) grid.findViewById(R.id.backspace);

            if(position==(mThumbIds.length-3)){
                holder.textView.setVisibility(View.GONE);
                holder.backspace.setVisibility(View.VISIBLE);
            }
            else
            {
                holder.backspace.setVisibility(View.GONE);
                holder.textView.setVisibility(View.VISIBLE);
                holder.textView.setText(mThumbIds[position]);
            }
        holder.textView.setText(mThumbIds[position]);
        holder.backspace.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Utils.ButtonClickEffect(view);
                    String str =mContext.edt_Amount.getText().toString();

                    if(str.trim().equalsIgnoreCase("")){
                        mContext.edt_Amount.setText("");
                    }
                    else{
                        mContext.edt_Amount.setText((str.substring(0,(str.length()-1))));
                    }

                    str =mContext.edt_Amount.getText().toString();

                    if(str.trim().equalsIgnoreCase("")){
                        mContext.edt_Amount.setText("");
                    }
                }
           });

        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.ButtonClickEffect(view);

               /* if(position==(mThumbIds.length-3)){


                }
                else*/
               {
                    String str =mContext.edt_Amount.getText().toString();

                    if(position==(mThumbIds.length-1)&&str.contains("."))
                    {
                        mContext.edt_Amount.setText( str);

                    }
                    else if(str.contains(".")&&str.substring(str.indexOf(".")).length()>2)
                    {

                        mContext.edt_Amount.setText( str);

                    }
                    else if(position==(mThumbIds.length-1)&&str.length()==0)
                    {

                        mContext.edt_Amount.setText("0.");

                    }
                    else
                        mContext.edt_Amount.setText( str+mThumbIds[position]);
                }

            }
        });


        return grid;
    }

    // Keep all Images in array
    public String[] mThumbIds = {
            "1","2","3","4","5","6","7","8","9","<-","0","."
    };

    static class ViewHolder {
        private TextView textView;
        private ImageView backspace;

    }
}