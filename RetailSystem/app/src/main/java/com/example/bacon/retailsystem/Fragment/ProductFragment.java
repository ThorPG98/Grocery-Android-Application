package com.example.bacon.retailsystem.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bacon.retailsystem.Activity.HomePageNav;
import com.example.bacon.retailsystem.Model.Cart;
import com.example.bacon.retailsystem.Model.Product;
import com.example.bacon.retailsystem.Prevalent.Prevalent;
import com.example.bacon.retailsystem.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class ProductFragment extends Fragment {

    ImageView productImage, backImage, cartImage;
    TextView productName, productDescription, productPrice;
    Button addToCartButton, btnAddQuantity, btnMinusQuantity, btnQuantity;
    String productID = "", imageUrl = "";

    BottomNavigationView bottomNav;

    Bundle bundle;

    private DatabaseReference productReference;
    private DecimalFormat precision;
    private Boolean existCartProduct = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_product, container, false);

        ((HomePageNav)getActivity()).setNavigationVisibility(false);

        precision = new DecimalFormat("0.00");

        bundle = this.getArguments();

        productID = bundle.getString("id");
        imageUrl = bundle.getString("imageUrl");

        productImage = v.findViewById(R.id.iv_product_detail_image);
        productName = v.findViewById(R.id.tv_product_detail_name);
        productDescription = v.findViewById(R.id.tv_product_detail_description);
        productPrice = v.findViewById(R.id.tv_product_detail_price);
        addToCartButton = v.findViewById(R.id.btn_add_to_cart);
        backImage = v.findViewById(R.id.iv_product_back);
        bottomNav = v.findViewById(R.id.bottom_navigation);
        btnAddQuantity = v.findViewById(R.id.btn_add_quantity);
        btnMinusQuantity = v.findViewById(R.id.btn_minus_quantity);
        btnQuantity = v.findViewById(R.id.btn_quantity);
        cartImage = v.findViewById(R.id.iv_product_cart);

        getProductDetails(productID);

        onClickCartButton();
        onClickBackButton();
        onClickPlusButton();
        onClickMinusButton();
        onClickAddToCartBtn();

        return v;
    }

    private void onClickCartButton() {
        cartImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Cart Frag
                Bundle bundle = new Bundle();
                bundle.putString("fragment", "product");
                CartFragment cartFragment = new CartFragment();
                cartFragment.setArguments(bundle);
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.fragment_container, cartFragment);
                fr.addToBackStack(null);
                fr.commit();
            }
        });
    }

    public void onClickBackButton() {
        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HomePageNav)getActivity()).setNavigationVisibility(true);
                (getActivity()).onBackPressed();
            }
        });
    }

    private void getProductDetails(final String productID) {
        productReference = FirebaseDatabase.getInstance().getReference().child("products");

        productReference.child(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    Product product = dataSnapshot.getValue(Product.class);

                    productName.setText(product.getName());
                    productDescription.setText(product.getDescription());
                    productPrice.setText(precision.format(product.getPrice()));
                    Picasso.get().load(product.getImageUrl()).into(productImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Order List").child(Prevalent.currentOnlineUser.getUsername());

        // Compare duplicated product in cart
        cartListRef.child("Add to Cart Item").orderByChild("id").equalTo(productID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Cart cart = dataSnapshot.child(productID).getValue(Cart.class);

                if(cart!=null) {
                    int previousQty = cart.getQuantity();
                    btnQuantity.setText(""+ previousQty);
                    addToCartButton.setText("Update Cart");
                    existCartProduct = true;
                }else{
                    btnQuantity.setText("0");
                    addToCartButton.setText("Add to Cart");
                    existCartProduct = false;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void onClickPlusButton() {
        btnAddQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentQuantity = Integer.parseInt(btnQuantity.getText().toString());
                currentQuantity++;
                btnQuantity.setText(currentQuantity + "");
            }
        });
    }

    public void onClickMinusButton() {
        btnMinusQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentQuantity = Integer.parseInt(btnQuantity.getText().toString());
                currentQuantity--;

                if (currentQuantity < 0) {
                    currentQuantity = 0;
                    btnQuantity.setText(currentQuantity + "");
                }else{
                    btnQuantity.setText(currentQuantity + "");
                }
            }
        });
    }

    public void onClickAddToCartBtn() {

        addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Order List").child(Prevalent.currentOnlineUser.getUsername());
                int currentQuantity = Integer.parseInt(btnQuantity.getText().toString());
                if(currentQuantity == 0) {
                    if (!existCartProduct) {
                        Toast.makeText(getActivity(), "You have not yet added anything into the cart. Please try again!", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        cartListRef.child("Add to Cart Item")
                                .child(productID)
                                .removeValue()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(getContext(), "Item removed successfully", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                        // Cart Frag
                        Bundle bundle = new Bundle();
                        bundle.putString("fragment", "product");
                        CartFragment cartFragment = new CartFragment();
                        cartFragment.setArguments(bundle);
                        FragmentTransaction fr = getFragmentManager().beginTransaction();
                        fr.replace(R.id.fragment_container, cartFragment);
                        fr.addToBackStack(null);
                        fr.commit();
                    }
                }
                else {
                    addingToCartList();

                    // Cart Frag
                    Bundle bundle = new Bundle();
                    bundle.putString("fragment", "product");
                    CartFragment cartFragment = new CartFragment();
                    cartFragment.setArguments(bundle);
                    FragmentTransaction fr = getFragmentManager().beginTransaction();
                    fr.replace(R.id.fragment_container, cartFragment);
                    fr.addToBackStack(null);
                    fr.commit();
                }
            }
        });
    }

    private void addingToCartList() {
        final String saveCurrentTime, saveCurrentDate;

        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calForDate.getTime());

        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference()
                .child("Order List").child(Prevalent.currentOnlineUser.getUsername());

        // Compare duplicated product in cart
        cartListRef.child("Add to Cart Item").orderByChild("id").equalTo(productID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int currentQuantity = Integer.parseInt(btnQuantity.getText().toString());

                Cart newItem = new Cart(currentQuantity,
                        Double.parseDouble(productPrice.getText().toString()),
                        productID,
                        productName.getText().toString(),
                        saveCurrentDate, saveCurrentTime, imageUrl);

                cartListRef.child("Add to Cart Item").child(productID).setValue(newItem);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
