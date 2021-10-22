package com.example.kontabai.Activities.DriverSide;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kontabai.Activities.UserSide.UserSideProfileCreation;
import com.example.kontabai.Activities.UserSideActivity;
import com.example.kontabai.Classes.ImportantMethods;
import com.example.kontabai.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class DriverSideProfileCreation extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST =121 ;
    private static final int PERMISSION_CAMERA_CODE = 111;
    EditText fullname,carnumber,mobilenumber;
    TextView submit;
    ImageView driverImage,carimage;
    CircleImageView circleImageView;
    FloatingActionButton addCarImageButton;
    ProgressDialog progressDialog;
    Uri imageUri,imageUriCar,imageUriDriver;
    StorageTask<UploadTask.TaskSnapshot> uploadTask;
    DatabaseReference driverInfo;
    String driverDownloadUri;
    StorageReference storageReference;
    String whichImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_side_profile_creation);
        initViews();

        addCarImageButton.setOnClickListener(view -> {
            whichImage="car";
            popMenu(addCarImageButton);
        });
        driverImage.setOnClickListener(view -> {
            whichImage="driver";
            popMenu(driverImage);
        });
        circleImageView.setOnClickListener(v -> {
            whichImage="driver";
            popMenu(driverImage);
        });

        submit.setOnClickListener(v->{
            String name=fullname.getText().toString();
            String carNumber=carnumber.getText().toString();
            String mobileNumber=mobilenumber.getText().toString();

            if(name.equals(""))
                fullname.setError("Enter your name!");
            else if(carNumber.equals(""))
                carnumber.setError("Car number must be required!");
            else if(mobileNumber.equals(""))
                mobilenumber.setError("Type your mobile number!");
            else{
                submit.setBackgroundResource(R.drawable.screen_background);
                saveInformationOnFirebase(name,carNumber,mobileNumber);
            }
        });
    }

    private void initViews() {
        fullname=findViewById(R.id.driverFullName);
        carnumber=findViewById(R.id.carNumber);
        mobilenumber=findViewById(R.id.driverPhoneNumber);
        submit=findViewById(R.id.createDriverProfileButton);
        addCarImageButton=findViewById(R.id.carimageButton);
        carimage=findViewById(R.id.carImage);
        driverImage=findViewById(R.id.driverSideImageview);
        circleImageView=findViewById(R.id.driverSideImageview2);
        driverInfo= FirebaseDatabase.getInstance().getReference().child("users").child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser().getUid()));
        storageReference = FirebaseStorage.getInstance().getReference(Objects.requireNonNull(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()));
    }

    private void popMenu(ImageView imageView) {
        PopupMenu menu=new PopupMenu(DriverSideProfileCreation.this,imageView);
        menu.getMenuInflater().inflate(R.menu.popmenu_imageview,menu.getMenu());
        menu.setOnMenuItemClickListener(menuItem -> {
            if(menuItem.getItemId()==R.id.fromCamera){
                openCamera();
                return true;
            }else if(menuItem.getItemId()==R.id.fromGallery){
                SelectImage();
                return true;
            }
            return false;
        });
        menu.show();
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
    private void openCamera() {
        Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent1, PERMISSION_CAMERA_CODE);
        intent1.setType("image/*");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PERMISSION_CAMERA_CODE && resultCode== Activity.RESULT_OK ){
            if(whichImage.equals("driver")){
                Bitmap bitmap=(Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes=new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG,100,bytes);
                String path=MediaStore.Images.Media.insertImage(getApplicationContext().getContentResolver(),bitmap,"val",null);
                Uri uri=Uri.parse(path);
                driverImage.setImageURI(uri);
                imageUriDriver=uri;
                whichImage="";
            }
            if(whichImage.equals("car")){
                Bitmap bitmap=(Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes=new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG,100,bytes);
                String path=MediaStore.Images.Media.insertImage(getApplicationContext().getContentResolver(),bitmap,"val",null);
                Uri uri=Uri.parse(path);
                carimage.setImageURI(uri);
                addCarImageButton.setVisibility(View.GONE);
                carimage.setVisibility(View.VISIBLE);
                imageUriCar=uri;
                whichImage="";
            }
        }
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK){
            imageUri = data.getData();
            // Setting image on image view using Bitmap
            if(imageUri==null){
                Toast.makeText(DriverSideProfileCreation.this, "Please Select the image", Toast.LENGTH_SHORT).show();
            }else {
                if(whichImage.equals("driver")){
                    imageUriDriver=imageUri;
                    driverImage.setImageURI(imageUriDriver);
                    whichImage="";}
                if(whichImage.equals("car")){
                    imageUriCar=imageUri;
                    addCarImageButton.setVisibility(View.GONE);
                    carimage.setVisibility(View.VISIBLE);
                    carimage.setImageURI(imageUriCar);
                    whichImage="";
                }else{
                    addCarImageButton.setVisibility(View.VISIBLE);
                    carimage.setVisibility(View.GONE);
                }
            }
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        String number= FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
        mobilenumber.setText(number);
        mobilenumber.setEnabled(false);
        if(mobilenumber.getText().toString().equals("")){
            mobilenumber.setEnabled(true);
        }
        submit.setBackgroundResource(R.drawable.black_corners);
    }

    @SuppressLint("SetTextI18n")
    private void saveInformationOnFirebase(String name, String carNumber, String mobileNumber)
    {
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Please wait for few seconds, we are setting up your Profile");
        progressDialog.show();
        if(imageUriDriver!=null){
            final StorageReference file=storageReference.child(System.currentTimeMillis()+"."+ ImportantMethods.getExtension(DriverSideProfileCreation.this,imageUriDriver));
            uploadTask=file.putFile(imageUriDriver);
            uploadTask.continueWithTask(task -> {
                if(!task.isSuccessful()) {
                    throw Objects.requireNonNull(task.getException());
                }
                return file.getDownloadUrl();
            }).addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    Uri downloaduri= task.getResult();
                    driverDownloadUri=downloaduri.toString();
                    driverInfo.child("image").setValue(driverDownloadUri);
                    if(imageUriCar!=null){
                        final StorageReference file1=storageReference.child(System.currentTimeMillis()+"."+ ImportantMethods.getExtension(DriverSideProfileCreation.this,imageUriCar));
                        uploadTask=file1.putFile(imageUriCar);
                        uploadTask.continueWithTask(task1 -> {
                            if(!task1.isSuccessful())
                            {
                                throw Objects.requireNonNull(task1.getException());
                            }
                            return file1.getDownloadUrl();
                        }).addOnCompleteListener(task1 -> {
                            if(task1.isSuccessful()){
                                Uri downloaduri1= task1.getResult();
                                String carImageDownloadUri=downloaduri1.toString();
                                String referanceToken= FirebaseInstanceId.getInstance().getToken();
                                driverInfo.child("carimage").setValue(carImageDownloadUri);
                                HashMap<String,Object> hashMap=new HashMap<>();
                                hashMap.put("fullName",name);
                                hashMap.put("carNumber",carNumber);
                                hashMap.put("driverImage",driverDownloadUri);
                                hashMap.put("userRole",2);
                                hashMap.put("latitude","");
                                hashMap.put("longitude","");
                                hashMap.put("isVerified",true);
                                hashMap.put("fcmToken",referanceToken);
                                hashMap.put("id",FirebaseAuth.getInstance().getCurrentUser().getUid());
                                hashMap.put("carImage",carImageDownloadUri);
                                hashMap.put("mobileNumber",mobileNumber);
                                driverInfo.setValue(hashMap);
                                progressDialog.dismiss();
                                    startActivity(new Intent(DriverSideProfileCreation.this, DriverDashBoard.class)
                                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                                    finish();
                            }else{
                                Toast.makeText(DriverSideProfileCreation.this,"Failed",Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        }).addOnFailureListener(e -> {
                            Toast.makeText(DriverSideProfileCreation.this, "Failed"+e.getMessage(), Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        });
                    }
                }else{
                    Toast.makeText(DriverSideProfileCreation.this,"Failed",Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }).addOnFailureListener(e -> {
                Toast.makeText(DriverSideProfileCreation.this, "Failed"+e.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            });
        }

    }
}