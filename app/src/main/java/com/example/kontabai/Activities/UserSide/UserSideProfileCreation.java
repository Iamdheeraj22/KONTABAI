
package com.example.kontabai.Activities.UserSide;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputFilter;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kontabai.Activities.DriverSide.DriverSideProfileCreation;
import com.example.kontabai.Activities.MainActivity;
import com.example.kontabai.R;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;

public class UserSideProfileCreation extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 111;
    TextView createProfile,createAsDriver;
    EditText fullname,phonenumber;
    ImageView imageView;
    Uri imageUri;

    private static final int PERMISSION_CAMERA_CODE=121;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_side_profile_creation);
        initViews();
        imageView.setOnClickListener(v-> setImageView());
        createAsDriver.setOnClickListener(view -> startActivity(new Intent(UserSideProfileCreation.this, DriverSideProfileCreation.class).addFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK
        )));
        createProfile.setOnClickListener(view -> {
            String fullName=fullname.getText().toString().trim();
            String num=phonenumber.getText().toString().trim();
            if(fullName.equals("")){
                fullname.setError("Type your name");
            }
            else if(num.equals("")){
                phonenumber.setEnabled(true);
                phonenumber.setError("Enter the valid number");
                phonenumber.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
            }
            else{
                Intent intent=new Intent(UserSideProfileCreation.this, MainActivity.class);
                intent.putExtra("type","user");
                intent.putExtra("name",fullName);
                intent.putExtra("phonenumber",num);
                startActivity(intent);
            }
        });
        createAsDriver.setOnClickListener(v -> {
            Intent intent=new Intent(UserSideProfileCreation.this,DriverSideProfileCreation.class);
            intent.putExtra("name",fullname.getText().toString());
            intent.putExtra("number",phonenumber.getText().toString());
            startActivity(intent);
        });
    }

    private void openCamera() {
        Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent1, PERMISSION_CAMERA_CODE);
        intent1.setType("image/*");
//        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//        startActivityForResult(cameraIntent, PERMISSION_CAMERA_CODE);

    }
    private void initViews() {
        createProfile=findViewById(R.id.createProfileButton);
        createAsDriver=findViewById(R.id.moveDriverButton);
        fullname=findViewById(R.id.userFullName);
        phonenumber=findViewById(R.id.userPhoneNumber);
        imageView=findViewById(R.id.userImageview);
//        String phone= FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
//        phonenumber.setText(phone);
//        phonenumber.setEnabled(false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PERMISSION_CAMERA_CODE && resultCode==RESULT_OK){
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(photo);
        }
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            imageUri = data.getData();
            try {// Setting image on image view using Bitmap
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imageUri);
                if(imageUri==null){
                    providePhoto();
                    Toast.makeText(UserSideProfileCreation.this, "Please Select the image", Toast.LENGTH_SHORT).show();
                }else {
                    imageView.setImageBitmap(bitmap);}
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void setImageView(){
        PopupMenu popupMenu=new PopupMenu(UserSideProfileCreation.this,imageView);
        popupMenu.getMenuInflater().inflate(R.menu.popmenu_imageview,popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(menuItem -> {
            if(menuItem.getItemId()==R.id.fromCamera){
                openCamera();
                return true;
            }else if(menuItem.getItemId()==R.id.fromGallery){
                SelectImage();
                return true;
            }
            return false;
        });
        popupMenu.show();
    }
    private void providePhoto()
    {
        AlertDialog alertDialog=new AlertDialog.Builder(this,R.style.verification_done).create();
        View view=getLayoutInflater().inflate(R.layout.photo_alertbox,null,false);
        alertDialog.setView(view);
        alertDialog.show();
        alertDialog.setCancelable(false);
        TextView okButton=view.findViewById(R.id.okPhotoButton);
        okButton.setOnClickListener(view1 -> alertDialog.dismiss());
    }

    private void SelectImage()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(
                        intent,
                        "Select Image from here..."),
                PICK_IMAGE_REQUEST);
    }
}