package com.aditas.marketplacemerch.Activity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.aditas.marketplacemerch.Entity.Category;
import com.aditas.marketplacemerch.R;
import java.util.ArrayList;


public class Adapt extends BaseAdapter {
    ArrayList<Category> cat = new ArrayList<>();

    @Override
    public int getCount(){
        return cat != null ? cat.size() : 0;
    }

    @Override
    public Category getItem(int pos){
        return cat.get(pos);
    }

    @Override
    public long getItemId(int pos){
        return cat.get(pos).getId();
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent){
        if(convertView == null){
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.drop_down, parent, false);
        }
        Category catg = (Category)  getItem(pos);
        Log.d("Market", catg.getName());
        TextView tvTxt = convertView.findViewById(R.id.tv_drop);
        tvTxt.setText(catg.getName());

        return convertView;
    }

    public void addData(ArrayList<Category> cat){
        this.cat = cat;
    }
}
