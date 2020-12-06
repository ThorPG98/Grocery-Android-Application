package com.example.bacon.retailsystem.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bacon.retailsystem.Activity.HomePageNav;
import com.example.bacon.retailsystem.Model.Cart;
import com.example.bacon.retailsystem.Prevalent.Prevalent;
import com.example.bacon.retailsystem.R;
import com.example.bacon.retailsystem.ViewHolder.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CartFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Button placeOrderButton, shoppingButton;
    private TextView totalPrice, totalQuantity, overallTotal;
    private ImageView ivBack, ivDelete;
    private RelativeLayout rlCart, rlEmptyCart;

    private double overallTotalPrice;
    private int overallQuantity;

    private ArrayList<Cart> finalCartOrder = new ArrayList<>();

    DecimalFormat precision;

    ImageView cartBackImg;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        precision = new DecimalFormat("0.00");

        View v = inflater.inflate(R.layout.fragment_cart, container, false);

        recyclerView = v.findViewById(R.id.rv_product_cart);
        cartBackImg = v.findViewById(R.id.iv_cart_back);
        ivBack = v.findViewById(R.id.iv_cart_back);
        rlCart = v.findViewById(R.id.rl_cart);
        ivDelete = v.findViewById(R.id.iv_cart_delete);
        rlEmptyCart = v.findViewById(R.id.rl_empty_cart);
        placeOrderButton = v.findViewById(R.id.btn_place_order);
        shoppingButton = v.findViewById(R.id.btn_shopping);
        totalPrice = v.findViewById(R.id.tv_overall_total);
        totalQuantity = v.findViewById(R.id.tv_total_item);

        Bundle bundle = this.getArguments();
        if(bundle != null) {
            ((HomePageNav) getActivity()).setNavigationVisibility(false);
            ivBack.setVisibility(View.VISIBLE);
        }else {
            ((HomePageNav) getActivity()).setNavigationVisibility(true);
            ivBack.setVisibility(View.GONE);
        }

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        onClickBackButton();
        onClickShoppingButton();
        onClickPlaceOrderButton();
        onClickDeleteCartButton();

        return v;
    }

    private void listenRecyclerAdapter(){
        finalCartOrder = new ArrayList<>();
        overallTotalPrice = 0.0;
        overallQuantity = 0;

        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference()
                .child("Order List").child(Prevalent.currentOnlineUser.getUsername());

        FirebaseRecyclerOptions<Cart> options =
                new FirebaseRecyclerOptions.Builder<Cart>()
                        .setQuery(cartListRef.child("Add to Cart Item"), Cart.class)
                        .build();

        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter
                = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {

            @Override
            public void onDataChanged() {
                super.onDataChanged();

                if(getItemCount() == 0){
                    rlEmptyCart.setVisibility(View.VISIBLE);
                    rlCart.setVisibility(View.GONE);
                    ivDelete.setVisibility(View.GONE);
                }else{
                    rlEmptyCart.setVisibility(View.GONE);
                    rlCart.setVisibility(View.VISIBLE);
                    ivDelete.setVisibility(View.VISIBLE);
                }
            }

            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull final Cart model) {

                holder.tvCartProductName.setText(model.getName());
                holder.tvCartProductId.setText(model.getId());
                holder.tvCartProductQuantity.setText("Quantity : " + (Integer.toString(model.getQuantity())));
                holder.tvCartProductPrice.setText("RM " + (precision.format(model.getPrice())));
                Picasso.get().load(model.getImageUrl()).into(holder.ivCartProductImage);

                double oneProductTotalPrice = (model.getPrice()) * Integer.valueOf(model.getQuantity());

                holder.tvCartProductTotalPrice.setText("RM " + (precision.format(oneProductTotalPrice)));

                overallTotalPrice = overallTotalPrice + oneProductTotalPrice;

                overallQuantity = overallQuantity + ((model.getQuantity()));

                totalPrice.setText("RM " + (precision.format(overallTotalPrice)));
                totalQuantity.setText(Integer.toString(overallQuantity));

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CharSequence options[] = new CharSequence[] {
                                "Edit",
                                "Remove"
                        };
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("Cart Options:");

                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                if (i == 0) {
                                    Bundle bundle = new Bundle();
                                    bundle.putString("imageUrl", model.getImageUrl());
                                    bundle.putString("id", model.getId());
                                    ProductFragment productFragment = new ProductFragment();
                                    FragmentTransaction fr = getFragmentManager().beginTransaction();
                                    productFragment.setArguments(bundle);
                                    fr.replace(R.id.fragment_container, productFragment);
                                    fr.addToBackStack(null);
                                    fr.commit();
                                }
                                if (i == 1) {
                                    cartListRef.child("Add to Cart Item")
                                            .child(model.getId())
                                            .removeValue()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(getContext(), "Item removed successfully", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                    listenRecyclerAdapter();
                                }
                            }
                        });
                        builder.show();
                    }
                });

                finalCartOrder.add(model);
            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_row_items, parent, false);
                CartViewHolder holder = new CartViewHolder(view);
                return holder;
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    public void onStart() {
        super.onStart();

        listenRecyclerAdapter();
    }

    private void onClickBackButton() {
        cartBackImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HomePageNav)getActivity()).setNavigationVisibility(true);
                (getActivity()).onBackPressed();
            }
        });
    }

    private void onClickShoppingButton(){
        shoppingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HomeFragment homeFragment = new HomeFragment();
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.fragment_container, homeFragment);
                fr.addToBackStack(null);
                fr.commit();

                ((HomePageNav)getActivity()).setBottomNavHome();
            }
        });
    }

    private void onClickPlaceOrderButton(){
        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Order List").child(Prevalent.currentOnlineUser.getUsername());

        placeOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = "Are you sure to confirm this order?\n\n";
                float total = 0;

                for(int i = 0; i <finalCartOrder.size(); i++) {
                    Cart cart = finalCartOrder.get(i);

                    if(cart.getName().equals("Apple") || cart.getName().equals("Garlic")) {
                        msg += "\n" +
                                cart.getName() + "\t\t\t\t" +
                                "(x " + cart.getQuantity() + ")\t\t\t\t\t\t\t\t\t\t\t\t" +
                                "RM" + precision.format(cart.getPrice()) + "\n";

                        total += (cart.getPrice()) * Integer.valueOf(cart.getQuantity());

                    }else{
                        msg += "\n" +
                                cart.getName() + "\t" +
                                "(x " + cart.getQuantity() + ")\t\t\t\t\t\t\t\t\t\t\t\t" +
                                "RM" + precision.format(cart.getPrice()) + "\n";

                        total += (cart.getPrice()) * Integer.valueOf(cart.getQuantity());
                    }
                }

                msg+= "\n-------------------------------------------------------------------\n\n" +
                        "Total (To be Paid)\t\t\t\t\t\t\t\t\t" +
                        "RM" + precision.format(total);

                new AlertDialog.Builder(getActivity())
                        .setTitle("Order Confirmation")
                        .setIcon(R.drawable.money_icon)
                        .setMessage(msg)
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                cartListRef.removeValue();
                                Toast.makeText(getActivity(), "Your order has been made!", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        });
    }

    private void onClickDeleteCartButton(){
        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Order List").child(Prevalent.currentOnlineUser.getUsername());

        ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(getActivity())
                        .setTitle("Empty Cart")
                        .setMessage("Confirm to Empty Cart ?")
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                cartListRef.removeValue();
                                Toast.makeText(getActivity(), "You have cleared your cart!", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        });
    }
}
