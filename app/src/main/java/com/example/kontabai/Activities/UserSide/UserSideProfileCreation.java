
package com.example.kontabai.Activities.UserSide;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.InputFilter;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kontabai.Activities.DriverSide.DriverSideProfileCreation;
import com.example.kontabai.Classes.ImportantMethods;
import com.example.kontabai.R;
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

public class UserSideProfileCreation extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 111;
    TextView createProfile,createAsDriver;
    EditText fullname,phonenumber;
    CircleImageView imageView;
    ProgressDialog progressDialog;
    Uri imageUri;
    StorageTask<UploadTask.TaskSnapshot> uploadTask;
    StorageReference storageReference;
    DatabaseReference userInfo;

    private static final int PERMISSION_CAMERA_CODE=121;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_side_profile_creation);
        initViews();
        imageView.setOnClickListener(v-> setImageView());
        createProfile.setOnClickListener(view -> {
            String fullName=fullname.getText().toString().trim();
            String num=phonenumber.getText().toString().trim();
            if(fullName.equals("")){
                fullname.setError("Type your name");
            }
            else if(num.equals("")){
                phonenumber.setEnabled(true);
                phonenumber.setError("Enter the valid number");
                phonenumber.setFilters(new InputFilter[]{new InputFilter.LengthFilter(13)});
            }
            else{
               createProfile.setBackgroundResource(R.drawable.screen_background);
               SaveUserInformation(fullName,num);
            }
        });
        createAsDriver.setOnClickListener(v -> {
            createAsDriver.setBackgroundResource(R.drawable.screen_background);
            Handler handler=new Handler();
            handler.postDelayed(() -> {
                Intent intent=new Intent(UserSideProfileCreation.this,DriverSideProfileCreation.class);
                intent.putExtra("name",fullname.getText().toString());
                intent.putExtra("number",phonenumber.getText().toString());
                startActivity(intent);
            },2000);
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
        String phone= FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
        phonenumber.setText(phone);
        phonenumber.setEnabled(false);
        userInfo= FirebaseDatabase.getInstance().getReference().child("users").child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser().getUid()));
        storageReference = FirebaseStorage.getInstance().getReference(Objects.requireNonNull(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PERMISSION_CAMERA_CODE && resultCode==RESULT_OK){
            Bitmap bitmap=(Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream bytes=new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,bytes);
            String path=MediaStore.Images.Media.insertImage(getApplicationContext().getContentResolver(),bitmap,"val",null);
            Uri uri=Uri.parse(path);
            imageView.setImageURI(uri);
            imageUri=uri;
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

    @Override
    protected void onStart() {
        super.onStart();
        createProfile.setBackgroundResource(R.drawable.black_corners);
        createAsDriver.setBackgroundResource(R.drawable.black_corners);
    }


    // Save the information in Firebase Database

    @SuppressLint("SetTextI18n")
    private void SaveUserInformation(String name, String number){
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Please wait for few seconds, we are setting up your Profile");
        progressDialog.show();
        if(imageUri!=null)
        {
            final StorageReference file=storageReference.child(System.currentTimeMillis()+"."+ImportantMethods.getExtension(UserSideProfileCreation.this,imageUri));
            uploadTask=file.putFile(imageUri);
            uploadTask.continueWithTask(task -> {
                if(!task.isSuccessful())
                {
                    throw Objects.requireNonNull(task.getException());
                }
                return file.getDownloadUrl();
            }).addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    Uri downloaduri= task.getResult();
                    assert downloaduri != null;
                    String mUri= downloaduri.toString();
                    String referanceToken= FirebaseInstanceId.getInstance().getToken();
                    HashMap<String,Object> map=new HashMap<>();
                    map.put("fullName",name);
                    map.put("number",number);
                    map.put("carImage","");
                    map.put("carNumber","");
                    map.put("image",mUri);
                    map.put("latitude","");
                    map.put("fcmToken",referanceToken);
                    map.put("longitude","");
                    map.put("id",FirebaseAuth.getInstance().getCurrentUser().getUid());
                    map.put("isVerified",true);
                    map.put("userRole",1);
                    userInfo.setValue(map);
//                    AlertDialog alertDialog=new AlertDialog.Builder(UserSideProfileCreation.this,R.style.verification_done).create();
//                    View view= LayoutInflater.from(UserSideProfileCreation.this).inflate(R.layout.confirmation_dialog,null,false);
//                    alertDialog.setView(view);
//                    alertDialog.show();
//                    TextView headingTextView=view.findViewById(R.id.confirmationHeading);
//                    headingTextView.setText("Your profile has been created successfully.");
//                    Handler handler=new Handler();
//                    handler.postDelayed(() -> {
//                        //alertDialog.dismiss();
//                        progressDialog.dismiss();
//                        startActivity(new Intent(UserSideProfileCreation.this, UserSideActivity.class));
//                        finish();
//                    },200);

                    startActivity(new Intent(UserSideProfileCreation.this, UserSideActivity.class));
                    finish();
                } else
                {
                    Toast.makeText(UserSideProfileCreation.this,"Failed",Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
                progressDialog.dismiss();
            }).addOnFailureListener(e -> {
                Toast.makeText(UserSideProfileCreation.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            });
        }else {
            providePhoto();
            progressDialog.dismiss();
        }
    }
}