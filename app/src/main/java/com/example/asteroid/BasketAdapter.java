package com.example.asteroid;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class BasketAdapter extends RecyclerView.Adapter<BasketAdapter.ViewHolder> {

    private Context context;
    private static ArrayList<UserBasket> basketModels;
    private OnItemClickListener listener;

    public BasketAdapter (Context context, ArrayList<UserBasket> basketModels, OnItemClickListener listener) {
        this.context = context;
        this.basketModels = basketModels;
        this.listener = listener;
    }


    //interface implemented in BasketFragment
    public interface OnItemClickListener {
        void onItemClick(UserBasket product);
    }


    @NonNull
    @Override
    public BasketAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //layout inflater section
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_view_basket, parent, false);
        return new BasketAdapter.ViewHolder(view, listener);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UserBasket basketViewModel = basketModels.get(position);
        holder.tvNameBasket.setText(basketViewModel.getProductName());
        holder.tvQuantityBasket.setText(basketViewModel.getProductQuantity());
        holder.tvFinalPriceBasket.setText("£ " + basketViewModel.getFinalPrice());

        Glide.with(context).load(basketModels.get(position).getProductImg1())
                .placeholder(R.drawable.asteroid_icon_small).error(R.drawable.asteroid_icon_small)
                .into(holder.imageBasket);

    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageBasket;
        TextView tvNameBasket, tvFinalPriceBasket, tvQuantityBasket, tvMinusBasket, tvPlusBasket;

        public ViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);

            imageBasket = itemView.findViewById(R.id.imageBasket);
            tvNameBasket = itemView.findViewById(R.id.tvNameBasket);
            tvFinalPriceBasket = itemView.findViewById(R.id.tvFinalPriceBasket);
            tvQuantityBasket = itemView.findViewById(R.id.tvQuantityBasket);
            tvPlusBasket = itemView.findViewById(R.id.tvPlusBasket);
            tvMinusBasket = itemView.findViewById(R.id.tvMinusBasket);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getBindingAdapterPosition();

                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(basketModels.get(position));
                    }
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        return basketModels.size();
    }
}