package com.iCropal.iPhobia.Ui.User;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.iCropal.iPhobia.DataModel.AppUser;
import com.iCropal.iPhobia.R;
import com.iCropal.iPhobia.Ui.Home.Home;
import com.iCropal.iPhobia.Utility.Resources.Constants;
import com.iCropal.iPhobia.Utility.Transmittors.RuntimeData;

import java.util.concurrent.TimeUnit;

public class UserAuthorization extends AppCompatActivity {
    private TextInputEditText phoneNumberText, verificationCode;
    private String phoneNumber;
    private String verificationId;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_verification);
        addPhoneNumber();
    }

    private void addPhoneNumber() {
        phoneNumberText = findViewById(R.id.AUR_phoneNumber);
        verificationCode = findViewById(R.id.AUR_verificationCode);
        findViewById(R.id.AUR_btnChangeNo).setOnClickListener(v -> {
            phoneNumberText.setText("");
            phoneNumber = null;
            if (!phoneNumberText.isEnabled()) {
                phoneNumberText.setEnabled(true);
            }
            if (!findViewById(R.id.AUR_btnVerify).isEnabled()) {
                findViewById(R.id.AUR_btnVerify).setEnabled(true);
            }
        });
        findViewById(R.id.AUR_btnVerify).setOnClickListener(v -> {
            if (phoneNumber == null || !phoneNumber.equals(getPhoneNumber(phoneNumberText.getText().toString()))) {
                if (phoneNumberText.getText().length() == 0) {
                    phoneNumberText.setError("Fill value.");
                } else {
                    phoneNumber = getPhoneNumber(phoneNumberText.getText().toString());
                    startPhoneNumberVerification(phoneNumber);
                    phoneNumberText.setEnabled(false);
                    verificationCode.setEnabled(true);
                    Snackbar.make(phoneNumberText, "Verification code has been sent.", Snackbar.LENGTH_LONG).show();
                }
            } else {
                if (verificationCode.getText().length() != 0) {
                    verifyPhoneNumberWithCode(verificationId, verificationCode.getText().toString(), phoneNumber);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                phoneNumberText.setEnabled(true);
                signInWithPhoneAuthCredential(phoneAuthCredential, phoneNumber);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Log.i("TAG", "onVerificationFailed: " + e.getLocalizedMessage());
                Snackbar.make(phoneNumberText, "Phone verification failed", Snackbar.LENGTH_SHORT).show();
            }


            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                verificationId = s;
            }
        };
    }

    private String getPhoneNumber(String phoneNumber) {
        if (phoneNumber.length() != 0) {
            if (phoneNumber.charAt(0) == '0') {
                phoneNumber = "+254" + phoneNumber.substring(1);
            }
        }
        return phoneNumber;
    }

    private void verifyPhoneNumberWithCode(String verificationId, String code, String phoneNumber) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithPhoneAuthCredential(credential, phoneNumber);
    }

    private void startPhoneNumberVerification(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                60,
                TimeUnit.SECONDS,
                this,
                mCallbacks);

    }

    private void signInWithPhoneAuthCredential(final PhoneAuthCredential credential, final String phoneNumber) {
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        initUser(phoneNumber);
                        task.getResult().getUser().delete();
                    } else {
                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            verificationCode.setError("Invalid code.");
                        }

                    }
                });
    }

    private void initUser(String phoneNumber) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (RuntimeData.pDetails.hasChild(phoneNumber)) {
            RuntimeData.referenceManger.saveUserIds(phoneNumber);
            startActivity(new Intent(this, Home.class));
        } else {
            getUserDetails(phoneNumber);
        }
        finish();
    }

    private void getUserDetails(String phoneNumber) {
        if (RuntimeData.appUser == null) {
            RuntimeData.appUser = new AppUser(phoneNumber);
        } else {
            RuntimeData.appUser.setPhoneNumber(phoneNumber);
        }
        startActivity(new Intent(this, AccountInformation.class).putExtra(Constants.changeDate, true).putExtra(Constants.UserPhoneNumber, phoneNumber));
    }
}
