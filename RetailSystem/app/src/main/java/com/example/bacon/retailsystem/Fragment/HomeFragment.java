package com.example.bacon.retailsystem.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.bacon.retailsystem.Activity.HomePageNav;
import com.example.bacon.retailsystem.Adapter.CategoryAdapter;
import com.example.bacon.retailsystem.Model.Category;
import com.example.bacon.retailsystem.Model.Product;
import com.example.bacon.retailsystem.R;
import com.example.bacon.retailsystem.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HomeFragment extends Fragment {

    RecyclerView categoryRecycelerView;

    SearchView productSearchView;

    ImageSlider imageSlider;

    CategoryAdapter categoryAdapter;

    List<Category> categoryList;
    List<SlideModel> slideModels;

    DecimalFormat precision;

    private DatabaseReference productReference;
    private RecyclerView productRecyclerView;
    LinearLayoutManager productLayoutManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        precision = new DecimalFormat("0.00");
        ((HomePageNav)getActivity()).setNavigationVisibility(true);

        productReference = FirebaseDatabase.getInstance().getReference().child("products");

        View v = inflater.inflate(R.layout.fragment_home, container, false);

        imageSlider = v.findViewById(R.id.vp_board);
        categoryRecycelerView = v.findViewById(R.id.rv_category_selection);
        productRecyclerView = v.findViewById(R.id.rv_product);
        productSearchView = v.findViewById(R.id.sv_home_search);

        slideModels = new ArrayList<>();
        slideModels.add(new SlideModel(getResources().getIdentifier("board1", "drawable", getActivity().getPackageName())));
        slideModels.add(new SlideModel(getResources().getIdentifier("board2", "drawable", getActivity().getPackageName())));
        slideModels.add(new SlideModel(getResources().getIdentifier("board3", "drawable", getActivity().getPackageName())));
        imageSlider.setImageList(slideModels, true);

        categoryList = new ArrayList<>();
        categoryList.add(new Category("CAT1","Bakery", getResources().getIdentifier("bakery","drawable", getActivity().getPackageName())));
        categoryList.add(new Category("CAT2","Fruit", getResources().getIdentifier("fruit","drawable", getActivity().getPackageName())));
        categoryList.add(new Category("CAT3", "Meat",getResources().getIdentifier("meat","drawable", getActivity().getPackageName())));
        categoryList.add(new Category("CAT4", "Veggie",getResources().getIdentifier("veggie","drawable", getActivity().getPackageName())));

        setCategoryRecycler(categoryList);

        productRecyclerView.setHasFixedSize(false);
        productLayoutManager = new LinearLayoutManager(getContext());
        productRecyclerView.setLayoutManager(productLayoutManager);

        productSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                firebaseSearch(query);
                productSearchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                firebaseSearch(newText);
                return false;
            }
        });

        return v;
    }

    private void setCategoryRecycler(List<Category> categoryDataList) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL,false);
        categoryRecycelerView.setLayoutManager(layoutManager);
        categoryAdapter = new CategoryAdapter(getContext(), categoryDataList);
        categoryRecycelerView.setAdapter(categoryAdapter);
    }

    private void firebaseSearch(String searchText) {
        String query = searchText.toLowerCase();

        Query firebaseQuery = productReference.orderByChild("search").startAt(query).endAt(query + "\uf8ff");

        FirebaseRecyclerOptions<Product> options = new FirebaseRecyclerOptions.Builder<Product>()
                .setQuery(firebaseQuery, Product.class)
                .build();

        FirebaseRecyclerAdapter<Product, ProductViewHolder> productAdapter =
                new FirebaseRecyclerAdapter<Product, ProductViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull final Product model) {

                        holder.tvProductName.setText(model.getName());
                        holder.tvProductPrice.setText("RM " + precision.format( model.getPrice()));
                        Picasso.get().load(model.getImageUrl()).into(holder.ivProductImage);

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Bundle bundle = new Bundle();
                                bundle.putString("imageUrl", model.getImageUrl());
                                bundle.putString("id", model.getId());
                                ProductFragment productFragment = new ProductFragment();
                                productFragment.setArguments(bundle);
                                FragmentTransaction fr = getFragmentManager().beginTransaction();
                                fr.replace(R.id.fragment_container, productFragment);
                                fr.addToBackStack(null);
                                fr.commit();
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_row_items, parent, false);
                        ProductViewHolder holder = new ProductViewHolder(view);

                        return holder;
                    }
                };
        productRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        productRecyclerView.setAdapter(productAdapter);
        productAdapter.startListening();
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Product> options = new FirebaseRecyclerOptions.Builder<Product>()
                .setQuery(productReference, Product.class)
                .build();

        FirebaseRecyclerAdapter<Product, ProductViewHolder> productAdapter =
                new FirebaseRecyclerAdapter<Product, ProductViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull final Product model) {

                        holder.tvProductName.setText(model.getName());
                        holder.tvProductPrice.setText("RM " + precision.format( model.getPrice()));
                        Picasso.get().load(model.getImageUrl()).into(holder.ivProductImage);

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Bundle bundle = new Bundle();
                                bundle.putString("imageUrl", model.getImageUrl());
                                bundle.putString("id", model.getId());
                                ProductFragment productFragment = new ProductFragment();
                                productFragment.setArguments(bundle);
                                FragmentTransaction fr = getFragmentManager().beginTransaction();
                                fr.replace(R.id.fragment_container, productFragment);
                                fr.addToBackStack(null);
                                fr.commit();
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_row_items, parent, false);
                        ProductViewHolder holder = new ProductViewHolder(view);

                        return holder;
                    }
                };
        productRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        productRecyclerView.setAdapter(productAdapter);
        productAdapter.startListening();
    }
}