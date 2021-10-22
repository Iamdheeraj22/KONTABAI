package com.example.kontabai.Activities;

import androidx.annotation.NonNull;
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
import android.widget.Toast;

import com.example.kontabai.Activities.DriverSide.DriverDashBoard;
import com.example.kontabai.Activities.UserSide.UserSideProfileCreation;
import com.example.kontabai.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class VerificationActivity extends AppCompatActivity {
    TextView verifyButton;
    ImageView backButton;
    EditText verificationCode;
    FirebaseAuth firebaseAuth;
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
                verifyCode(verification);
                verifyButton.setBackgroundResource(R.drawable.screen_background);
            }
        });
        backButton.setOnClickListener(view -> startActivity(new Intent(VerificationActivity.this,RegistrationActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK)));
    }

    private void verifyCode(String code) {
        PhoneAuthCredential authCredential= PhoneAuthProvider.getCredential(verificationId,code);
        signWithPhoneCredentail(authCredential);
    }

    private void signWithPhoneCredentail(PhoneAuthCredential authCredential)
    {
        AlertDialog alertDialog;
        alertDialog=new AlertDialog.Builder(VerificationActivity.this,R.style.verification_done).create();
        View view=getLayoutInflater().inflate(R.layout.progress_dialog,null,false);
        alertDialog.setView(view);
        alertDialog.setCancelable(false);
        alertDialog.show();
        firebaseAuth.signInWithCredential(authCredential).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            int role=Integer.parseInt(snapshot.child("userRole").getValue().toString());
                            if(role==1){
                                alertDialog.dismiss();
                                startActivity(new Intent(VerificationActivity.this,UserSideActivity.class)
                                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                                finish();
                            }
                            if(role==2){
                                alertDialog.dismiss();
                                startActivity(new Intent(VerificationActivity.this, DriverDashBoard.class)
                                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                                finish();
                            }
                        }else {
                            alertDialog.dismiss();
                            AlertDialog verificationDone= new AlertDialog.Builder(VerificationActivity.this).create();
                            View view1=LayoutInflater.from(VerificationActivity.this).inflate(R.layout.confirmation_dialog,null,false);
                            verificationDone.setView(view1);
                            verificationDone.show();
                            Handler handler=new Handler();
                            handler.postDelayed(() -> {
                                verificationDone.dismiss();
                                startActivity(new Intent(VerificationActivity.this,UserSideProfileCreation.class)
                                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                            },3000);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(VerificationActivity.this, "Failed "+error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }else{
                alertDialog.dismiss();
                Toast.makeText(VerificationActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

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

    @Override
    protected void onStart() {
        super.onStart();
        verifyButton.setBackgroundResource(R.drawable.black_corners);
    }
}