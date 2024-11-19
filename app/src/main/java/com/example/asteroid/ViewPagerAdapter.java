package com.example.asteroid;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends RecyclerView.Adapter<ViewPagerAdapter.SliderViewHolder> {

    private ArrayList<ProductsModel> productsModels;
    private List<String> imageAccessTokens;
    private Context context;
    private ProductsModel product;

    public ViewPagerAdapter(Context context, List<String> imageAccessTokens) {
        this.context = context;
        this.imageAccessTokens = imageAccessTokens;
    }

    @NonNull
    @Override
    public SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.view_pager_layout, parent, false);
        return new SliderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SliderViewHolder holder, int position) {

        Glide.with(context).load(imageAccessTokens.get(position))
                .placeholder(R.drawable.asteroid_icon_small).error(R.drawable.asteroid_icon_small)
                .into(holder.vpImage);

    }

    @Override
    public int getItemCount() {
        return imageAccessTokens.size();
    }

    public static class SliderViewHolder extends RecyclerView.ViewHolder {
        ImageView vpImage;

        public SliderViewHolder(@NonNull View itemView) {
            super(itemView);
            vpImage = itemView.findViewById(R.id.vpImage);
        }
    }
}
