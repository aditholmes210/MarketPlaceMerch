package com.aditas.marketplacemerch.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.aditas.marketplacemerch.Activity.Detail;
import com.aditas.marketplacemerch.Entity.Product;
import com.aditas.marketplacemerch.R;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;

import static com.aditas.marketplacemerch.Activity.Detail.EXTRA_DATA;


public class Adapt extends RecyclerView.Adapter<Adapt.MainHolder> {
    private List<Product> lp = new ArrayList<>();
    private Context ctx;

    @NonNull
    @Override
    public Adapt.MainHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        ctx = parent.getContext();
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new MainHolder(v);
    }

    public void setProd(List<Product> prdz){
        this.lp = prdz;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull Adapt.MainHolder holder, int pos){
        Product pds = lp.get(pos);
        String url = "http://210.210.154.65:4444/api/merchant/products";
        holder.onBind(pds);
            Picasso.get()
                    .load(url+lp.get(pos).getImage())
                    .error(R.drawable.ic_launcher_background)
                    .fit()
                    .centerCrop()
                    .into(holder.img);
    }

    @Override
    public int getItemCount() {
        return lp != null ? lp.size() : 0;
    }

    public class MainHolder extends RecyclerView.ViewHolder{
        private ImageView img;
        private Context ctx;
        private TextView prodName, merchName, prices;
        private Product prod;

        public MainHolder(@NonNull View itemView){
            super(itemView);
            img = itemView.findViewById(R.id.img_prod);
            prodName = itemView.findViewById(R.id.tv_prod);
            merchName = itemView.findViewById(R.id.tv_merch);
            prices = itemView.findViewById(R.id.tv_price);
            ctx = itemView.getContext();
            itemView.setOnClickListener(list);
        }

        public void onBind(Product prod){
            this.prod = prod;
            prodName.setText(prod.getName());
            merchName.setText(prod.getMerch().getName());
            prices.setText("Rp. "+prod.getPrice());
        }
        View.OnClickListener list = new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Toast.makeText(ctx, prod.getImage(), Toast.LENGTH_SHORT).show();
                Intent i = new Intent (ctx, Detail.class);
                i.putExtra(EXTRA_DATA, prod);
                ctx.startActivity(i);
            }
        };
    }
}
