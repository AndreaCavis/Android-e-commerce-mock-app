package com.example.asteroid;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ViewHolder> {

    private Context context;
    private static ArrayList<ProductsModel> productsModels;
    private OnItemClickListener listener;

    public ProductsAdapter (Context context, ArrayList<ProductsModel> productsModels, OnItemClickListener listener) {
        this.context = context;
        this.productsModels = productsModels;
        this.listener = listener;
    }


    //interface implemented in HomeFragment
    public interface OnItemClickListener {
        void onItemClick(ProductsModel product);
    }


    @NonNull
    @Override
    public ProductsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //layout inflater section
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recyler_view_home, parent, false);
        return new ProductsAdapter.ViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductsAdapter.ViewHolder holder, int position) {

        ProductsModel productsViewModel = productsModels.get(position);
        holder.tvNameHome.setText(productsViewModel.getName());
        holder.tvBrandHome.setText(productsViewModel.getBrand());
        holder.tvPriceHome.setText("" + productsViewModel.getPrice());

        Glide.with(context).load(productsModels.get(position).getImg1())
                .placeholder(R.drawable.asteroid_icon_small).error(R.drawable.asteroid_icon_small)
                .into(holder.imageHome);

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageHome;
        TextView tvNameHome, tvPriceHome, tvBrandHome;

        public ViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);

            imageHome = itemView.findViewById(R.id.imageHome);
            tvNameHome = itemView.findViewById(R.id.tvNameHome);
            tvPriceHome = itemView.findViewById(R.id.tvPriceHome);
            tvBrandHome = itemView.findViewById(R.id.tvBrandHome);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getBindingAdapterPosition();

                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(productsModels.get(position));
                    }
                }
            });
        }

    }


    @Override
    public int getItemCount() {
        return productsModels.size();
    }
}
