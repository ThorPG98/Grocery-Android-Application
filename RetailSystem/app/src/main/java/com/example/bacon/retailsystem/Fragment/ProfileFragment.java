package com.example.bacon.retailsystem.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.bacon.retailsystem.Activity.EditProfileActivity;
import com.example.bacon.retailsystem.Activity.LoginActivity;
import com.example.bacon.retailsystem.Adapter.ProfileAdapter;
import com.example.bacon.retailsystem.Model.User;
import com.example.bacon.retailsystem.Prevalent.Prevalent;
import com.example.bacon.retailsystem.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {

    private FirebaseAuth firebaseAuth;
    private DatabaseReference profileReference;

    private TextView tvName, tvEmail;
    private CircleImageView civProfileImg;

    ListView profilelistView;

    String[] data = {"Edit Profile", "Logout"};
    int[] drawableId = {R.drawable.edit_icon, R.drawable.exit_icon};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        firebaseAuth = FirebaseAuth.getInstance();

        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        setHasOptionsMenu(true);

        profilelistView = v.findViewById(R.id.profile_list_view);
        civProfileImg = v.findViewById(R.id.civ_profile_img);
        tvName = v.findViewById(R.id.tv_profile_name);
        tvEmail = v.findViewById(R.id.tv_profile_email);

        Picasso.get().load(Prevalent.currentOnlineUser.getProfileImage()).noFade().into(civProfileImg);
        tvName.setText(Prevalent.currentOnlineUser.getUsername());
        tvEmail.setText(Prevalent.currentOnlineUser.getEmail());

        ProfileAdapter profileAdapter = new ProfileAdapter(getContext() ,data, drawableId);

        profilelistView.setAdapter(profileAdapter);

        profilelistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    getActivity().finish();
                    startActivity(new Intent(getActivity(), EditProfileActivity.class));
                }
                else if (position == 1) {
                    firebaseAuth.signOut();
                    getActivity().finish();
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
            }
        });

        return v;
    }
}
