package com.example.kontabai.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kontabai.R;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class RegistrationActivity extends AppCompatActivity {
    private static final int PERMISSION_CAMERA_CODE = 121;
    private static final int PERMISSION_COARSE_LOCATION=111;
    private static final int PERMISSION_FINE_LOCATION=222;
    TextView textView;
    EditText editText;
    AlertDialog alertDialog;
    FirebaseAuth firebaseAuth;
    String[] manifest={Manifest.permission.CAMERA,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION};
    String verificationId;
    @SuppressLint("ObsoleteSdkInt")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (Build.VERSION.SDK_INT > 16) {
//            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        }
        setContentView(R.layout.activity_registration);
        firebaseAuth = FirebaseAuth.getInstance();
        editText=findViewById(R.id.phoneNumberEdit);
        textView=findViewById(R.id.nextButton);
//        AlertDialog alertDialog=new AlertDialog.Builder(RegistrationActivity.this,R.style.verification_done).create();
//        View view= LayoutInflater.from(RegistrationActivity.this).inflate(R.layout.progress_dialog,null,false);
//        alertDialog.setView(view);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number=editText.getText().toString();
                if(number.equals("") || number.length()!=10){
                    editText.setError("Please enter the valid number!");
                }else{
//                    registrationUserMobile(number);
////                    alertDialog.show();
                    Intent intent=new Intent(RegistrationActivity.this,VerificationActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

//    private void registrationUserMobile(String number) {
//        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(firebaseAuth).
//                setPhoneNumber("+91" + number)
//                .setTimeout(60L, TimeUnit.SECONDS)
//                .setActivity(this)
//                .setCallbacks(mCallBacks)
//                .build();
//        PhoneAuthProvider.verifyPhoneNumber(options);
//    }
//
//    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//        @Override
//        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
//        }
//        @Override
//        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
//            super.onCodeSent(s, forceResendingToken);
//            verificationId=s;
//            Intent intent=new Intent(RegistrationActivity.this,VerificationActivity.class);
//            intent.putExtra("number", editText.getText().toString());
//            intent.putExtra("id",verificationId);
////            alertDialog.dismiss();
//            startActivity(intent);
//        }
//        @Override
//        public void onVerificationFailed(@NonNull FirebaseException e) {
////            alertDialog.dismiss();
//            Toast.makeText(RegistrationActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//        }
//    };

    private void checkPermission()
    {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this,manifest, RegistrationActivity.PERMISSION_CAMERA_CODE);
        }else if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this,manifest, RegistrationActivity.PERMISSION_CAMERA_CODE);
        }else if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this,manifest, RegistrationActivity.PERMISSION_CAMERA_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==PERMISSION_CAMERA_CODE && grantResults[0]==PackageManager.PERMISSION_DENIED ||
                requestCode==PERMISSION_COARSE_LOCATION && grantResults[1]==PackageManager.PERMISSION_DENIED
                || requestCode==PERMISSION_FINE_LOCATION && grantResults[2]==PackageManager.PERMISSION_DENIED ){
            Toast.makeText(RegistrationActivity.this, "Please allow the all permissions!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkPermission();
//        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
//            startActivity(new Intent(RegistrationActivity.this,UserSideProfileCreation.class)
//                 .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
//        }
    }
}