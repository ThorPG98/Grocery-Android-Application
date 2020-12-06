package com.example.bacon.retailsystem.Activity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bacon.retailsystem.Fragment.ProductFragment;
import com.example.bacon.retailsystem.Fragment.ProfileFragment;
import com.example.bacon.retailsystem.Prevalent.Prevalent;
import com.example.bacon.retailsystem.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends AppCompatActivity {

    private DatabaseReference editProfileReference;
    StorageReference storageReference;

    private TextView tvCancelUpdateProfile;
    private EditText etEditProfileName;
    private Button btnUpdateProfile, btnUploadProfileImage;
    private CircleImageView civProfileImage;
    private Uri filePath;
    private String originalProfile;

    private final int PICK_IMAGE_REQUEST = 20;

    private String userId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        userId = Prevalent.currentOnlineUser.getId();

        tvCancelUpdateProfile = findViewById(R.id.tv_cancel_update_profile);
        etEditProfileName = findViewById(R.id.et_edit_profile_name);
        btnUpdateProfile = findViewById(R.id.btn_update_profile);

        btnUploadProfileImage = findViewById(R.id.btn_upload_image);
        civProfileImage = findViewById(R.id.civ_edit_profile_image);

        originalProfile = Prevalent.currentOnlineUser.getProfileImage();
        Picasso.get().load(originalProfile).noFade().into(civProfileImage);

        storageReference = FirebaseStorage.getInstance().getReference("images");

        etEditProfileName.setText("" + Prevalent.currentOnlineUser.getUsername());

        editProfileReference = FirebaseDatabase.getInstance().getReference()
                .child("users")
                .child(userId);

        btnUploadProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectImage();
            }
        });

        btnUpdateProfile.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String updatedName = etEditProfileName.getText().toString();

                uploadProfile();
                updateProfileDetails(updatedName);

                finish();
                startActivity(new Intent(EditProfileActivity.this, HomePageNav.class));
            }
        });

        tvCancelUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(EditProfileActivity.this, HomePageNav.class));
            }
        });
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadProfile() {
        if (filePath != null) {
            final StorageReference fileReference = storageReference.child(System.currentTimeMillis()
                    + "." + getFileExtension(filePath));

            fileReference.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    editProfileReference.child("profileImage").setValue(uri.toString());
                                    Prevalent.currentOnlineUser.setProfileImage(uri.toString());

                                }
                            });
                            Toast.makeText(EditProfileActivity.this, "Upload Successful", Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(EditProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        else {
            Picasso.get().load(Prevalent.currentOnlineUser.getProfileImage()).noFade().into(civProfileImage);
        }
    }

    private void SelectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult( Intent.createChooser(intent, "Select Image from here..."),
                PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {
            filePath = data.getData();

            Picasso.get().load(filePath).noFade().into(civProfileImage);
        }
    }

    private void updateProfileDetails(String updatedName) {

        updatedName = etEditProfileName.getText().toString();
        String updatedProfile = Prevalent.currentOnlineUser.getProfileImage();

        String originalName = Prevalent.currentOnlineUser.getUsername();

        editProfileReference.child("username").setValue(updatedName);
        editProfileReference.child("profileImage").setValue(updatedProfile);

        Prevalent.currentOnlineUser.setUsername(updatedName);
        Prevalent.currentOnlineUser.setProfileImage(updatedProfile);

    }
}
