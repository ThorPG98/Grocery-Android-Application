package com.example.bacon.retailsystem.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bacon.retailsystem.Interface.ItemClickListener;
import com.example.bacon.retailsystem.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView tvCartProductName, tvCartProductId, tvCartProductQuantity, tvCartProductPrice, tvCartProductTotalPrice;
    public ImageView ivCartProductImage;
    private ItemClickListener itemClickListener;

    public CartViewHolder(@NonNull View itemView) {
        super(itemView);

        tvCartProductId = itemView.findViewById(R.id.tv_cart_product_id);
        tvCartProductName = itemView.findViewById(R.id.tv_cart_product_name);
        tvCartProductQuantity = itemView.findViewById(R.id.tv_cart_product_quantity);
        tvCartProductPrice = itemView.findViewById(R.id.tv_cart_product_price);
        tvCartProductTotalPrice = itemView.findViewById(R.id.tv_cart_product_total_price);
        ivCartProductImage = itemView.findViewById(R.id.iv_cart_img);
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition(), false);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}
