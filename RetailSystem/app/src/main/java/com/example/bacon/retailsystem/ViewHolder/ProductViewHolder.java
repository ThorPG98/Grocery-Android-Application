package com.example.bacon.retailsystem.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.bacon.retailsystem.Interface.ItemClickListener;
import com.example.bacon.retailsystem.R;

import androidx.recyclerview.widget.RecyclerView;

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView tvProductName, tvProductPrice;
    public ImageView ivProductImage;
    public ItemClickListener listener;

    public ProductViewHolder(View itemView) {
        super(itemView);

        tvProductName = itemView.findViewById(R.id.tv_product_name);
        tvProductPrice = itemView.findViewById(R.id.tv_product_price);
        ivProductImage = itemView.findViewById(R.id.iv_product_row_image);
    }

    public void setItemClickListener(ItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        listener.onClick(v, getAdapterPosition(), false);
    }
}
