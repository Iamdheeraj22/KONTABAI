package com.example.kontabai.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kontabai.Activities.UserSide.UserSideProfileCreation;
import com.example.kontabai.R;
import com.google.firebase.auth.FirebaseAuth;

public class VerificationActivity extends AppCompatActivity {
    TextView verifyButton;
    ImageView backButton;
    EditText verificationCode;
    FirebaseAuth firebaseAuth;
//    AlertDialog alertDialog;
    String mobilenumber,verificationId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        initViews();
        verifyButton.setOnClickListener(view -> {
            String verification=verificationCode.getText().toString();
            if(verification.equals("")){
                verificationCode.setError("Enter verification code!");
            }else if(verification.length()!=6){
                verificationCode.setError("Enter valid code!");
            }else {
//
                setUpTheAlertBox();
            }
        });
        backButton.setOnClickListener(view -> startActivity(new Intent(VerificationActivity.this,RegistrationActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK)));
    }

//    private void verifyCode(String code) {
//        PhoneAuthCredential authCredential= PhoneAuthProvider.getCredential(verificationId,code);
//        signWithPhoneCredentail(authCredential);
//    }
//
//    private void signWithPhoneCredentail(PhoneAuthCredential authCredential)
//    {
//        firebaseAuth.signInWithCredential(authCredential).addOnCompleteListener(task -> {
//            if(task.isSuccessful()){
//                alertDialog=new AlertDialog.Builder(VerificationActivity.this,R.style.verification_done).create();
//                View view=getLayoutInflater().inflate(R.layout.confirmation_dialog,null,false);
//                alertDialog.setView(view);
//                alertDialog.setCancelable(false);
//                alertDialog.show();
//                TextView okButton=view.findViewById(R.id.okButton);
//                okButton.setOnClickListener(view1 -> {
//                    Intent intent=new Intent(VerificationActivity.this,UserSideProfileCreation.class);
//                    intent.putExtra("number",mobilenumber);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(intent);
//                    alertDialog.dismiss();
//                    finish();
//                });
//            }else{
//                alertDialog.dismiss();
//                Toast.makeText(VerificationActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//
//    }


    private void setUpTheAlertBox(){
        AlertDialog alertDialog=new AlertDialog.Builder(VerificationActivity.this,R.style.verification_done).create();
        View view= LayoutInflater.from(VerificationActivity.this).inflate(R.layout.confirmation_dialog,null,false);
        alertDialog.setView(view);
        alertDialog.show();
        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                alertDialog.dismiss();
                startActivity(new Intent(VerificationActivity.this, UserSideProfileCreation.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        },3000);

    }

    private void initViews()
    {
        verifyButton=findViewById(R.id.continueButton);
        firebaseAuth=FirebaseAuth.getInstance();
        backButton=findViewById(R.id.backButton);
        verificationCode=findViewById(R.id.codeEdittext);
        mobilenumber=getIntent().getStringExtra("mobile");
        verificationId=getIntent().getStringExtra("id");
    }
}