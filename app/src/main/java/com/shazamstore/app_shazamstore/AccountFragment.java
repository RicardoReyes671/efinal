package com.shazamstore.app_shazamstore;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;

public class AccountFragment extends Fragment {
    ImageView imageProfile;
    ProgressDialog progressDialog;
    Bitmap bitmap;
    Uri imageUri;

    private static final int CAMERA_REQUEST = 100;
    private static final int IMAGE_PICK_CAMERA_REQUEST = 400;

    private static final int READ_STORAGE_EXTERNAL_REQUEST = 10;

    String cameraPermission[];

    FirebaseAuth mAuth;
    DatabaseReference mDatabase;

    @Override
    public void onCreate(Bundle savedInstanceState){ super.onCreate(savedInstanceState); }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.account_fragment,container,false);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        FirebaseUser user = mAuth.getCurrentUser();

        if(user!=null){

        }

        MaterialButton logoutButton = view.findViewById(R.id.button_logout);
        FloatingActionButton editImage = view.findViewById(R.id.button_photo);
        imageProfile = view.findViewById(R.id.image_profile);
        progressDialog = new ProgressDialog(this.getContext());
        progressDialog.setCanceledOnTouchOutside(false);
        cameraPermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};

        editImage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                progressDialog.setMessage("Updating Profile Picture");
                showImagePicDialog();
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(getContext(), "Sesión cerrada correctamente. Hasta luego.", Toast.LENGTH_LONG).show();
                goToLogin();
            }
        });

        return view;
    }

    private void goToLogin(){
        ((com.shazamstore.app_shazamstore.NavigationHost) getActivity()).navigateTo(new com.shazamstore.app_shazamstore.LoginFragment(),false);
    }

    public void showImagePicDialog(){
        String options[] = {"Camera","Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        builder.setTitle("Pick image from");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which == 0){
                    if(!checkCameraPermission()){
                        requestCameraPermission();
                    }
                    else{
                        takePhoto();
                    }
                }
                else {
                    if(!checkReadStoragePermission()){
                        requestReadStoragePermission();
                    }
                    else{
                        loadGallery();
                    }
                }
            }
        });
        builder.create().show();
    }

    //Camera
    private boolean checkCameraPermission(){
        boolean result = ContextCompat.checkSelfPermission(this.getContext(),Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(this.getContext(),Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);

        return (result&&result1);
    }

    private void requestCameraPermission(){
        requestPermissions(cameraPermission,CAMERA_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == IMAGE_PICK_CAMERA_REQUEST){
            if(resultCode == Activity.RESULT_OK && data != null){
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getActivity().getContentResolver(), imageUri);
                    imageProfile.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if(requestCode == READ_STORAGE_EXTERNAL_REQUEST){
            if(resultCode == Activity.RESULT_OK && data != null){
                Uri path = data.getData();
                imageProfile.setImageURI(path);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(ContextCompat.checkSelfPermission(this.getContext(),Manifest.permission.CAMERA) != (PackageManager.PERMISSION_GRANTED)
        && ContextCompat.checkSelfPermission(this.getContext(),Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED)){
            if(requestCode == CAMERA_REQUEST){
                if(permissions.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    takePhoto();
                }
            }
        }
       super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void takePhoto(){
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE,"Temp_pic");
        contentValues.put(MediaStore.Images.Media.TITLE,"Temp_description");
        contentValues.put(MediaStore.Images.Media.RELATIVE_PATH,"Pictures/Shazam_Store");
        imageUri = this.getContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,contentValues);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
        startActivityForResult(cameraIntent,IMAGE_PICK_CAMERA_REQUEST);
    }

    //Gallery
    private boolean checkReadStoragePermission(){
        boolean result = ContextCompat.checkSelfPermission(this.getContext(),Manifest.permission.READ_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);

        return (result);
    }

    private void requestReadStoragePermission(){
        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},READ_STORAGE_EXTERNAL_REQUEST);
    }

    private void loadGallery(){
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent.createChooser(galleryIntent,"Select an app"),READ_STORAGE_EXTERNAL_REQUEST);
    }
}
