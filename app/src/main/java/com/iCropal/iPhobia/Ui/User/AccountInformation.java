package com.iCropal.iPhobia.Ui.User;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.iCropal.iPhobia.DataModel.AppUser;
import com.iCropal.iPhobia.R;
import com.iCropal.iPhobia.Ui.Home.Home;
import com.iCropal.iPhobia.Utility.Dialogs.SelectImage;
import com.iCropal.iPhobia.Utility.Resources.Animations;
import com.iCropal.iPhobia.Utility.Resources.Constants;
import com.iCropal.iPhobia.Utility.Resources.Time;
import com.iCropal.iPhobia.Utility.Transmittors.RuntimeData;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Random;

public class AccountInformation extends AppCompatActivity {
    boolean saveDate = false, savePic = false;
    private EditText userNameText;
    private String userGender;
    private String phoneNumber;
    private String tnDp, acDp;
    private ImageView imageView;
    private View layoutOne, layoutTwo;
    private static final int CAMERA = 0, GALLERY = 1;
    private Snackbar savingInfo;
    private Animations animations;

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_information);
        boolean changeDate = getIntent().getBooleanExtra(Constants.changeDate, false);
        animations = new Animations(this);
        layoutOne = findViewById(R.id.AAI_layoutOne);
        layoutTwo = findViewById(R.id.AAI_changeDate);
        userNameText = findViewById(R.id.AAI_userName);
        phoneNumber = getIntent().getExtras().getString(Constants.UserPhoneNumber);
        savingInfo = Snackbar.make(userNameText, "Saving User Info", Snackbar.LENGTH_INDEFINITE);
        if (changeDate) {
            changeUserDate();
        }
        imageView = findViewById(R.id.AAI_userDp);
        if (RuntimeData.appUser != null) {
            Glide.with(imageView).load(RuntimeData.appUser.getTnDp()).into(imageView);
            userNameText.setText(RuntimeData.appUser.getUserName());
        }
        findViewById(R.id.AAI_btnSave).setOnClickListener(v -> {
            if (userNameText.getText().length() == 0) {
                ((TextInputLayout) findViewById(R.id.AAI_nameLayout)).setError("You have to tell us your name 😊😊");
            } else if (saveDate && layoutTwo.getVisibility() != View.VISIBLE) {
                layoutOne.setVisibility(View.GONE);
                layoutTwo.setVisibility(View.VISIBLE);
                layoutTwo.startAnimation(animations.fadeIn);
            } else {
                if (savePic) {
                    saveUserPic();
                } else {
                    saveAccountInfo();
                }
            }
        });
        setupGender();
        requestMultiplePermissions();
        findViewById(R.id.AAI_btnAddPic).setOnClickListener(v -> {
            showPictureDialog();
        });
    }

    private void saveUserPic() {
        savingInfo.show();
        disableViews();
        findViewById(R.id.AAI_btnSave).setEnabled(false);
        final StorageReference image = FirebaseStorage.getInstance().getReference().child(phoneNumber).child("acDp");
        final UploadTask uploadTask = image.putFile(Uri.parse(acDp));
        uploadTask.continueWithTask(task -> {
            if (!task.isSuccessful()) {
                throw task.getException();
            }

            return image.getDownloadUrl();
        }).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Uri taskResult = task.getResult();
                acDp = taskResult.toString();
                final StorageReference image1 = FirebaseStorage.getInstance().getReference().child(phoneNumber).child("tnDp");
                final UploadTask uploadTask1 = image1.putFile(Uri.parse(tnDp));
                uploadTask1.continueWithTask(task1 -> {
                    if (!task1.isSuccessful()) {
                        throw task1.getException();
                    }

                    return image1.getDownloadUrl();
                }).addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {
                        Uri taskResult1 = task.getResult();
                        tnDp = taskResult1.toString();
                        saveAccountInfo();
                    }
                });
            }
        }).addOnFailureListener(command -> {
            enableViews();
        });
    }

    private void enableViews() {
        savingInfo.dismiss();
        View m = findViewById(R.id.AAI_gdM), f = findViewById(R.id.AAI_gdF), b = findViewById(R.id.AAI_gdB);
        m.setEnabled(true);
        f.setEnabled(true);
        b.setEnabled(true);
        DatePicker datePicker = findViewById(R.id.AAI_datePicker);
        datePicker.setEnabled(true);
        findViewById(R.id.AAI_btnAddPic).setEnabled(true);
    }

    private void disableViews() {
        View m = findViewById(R.id.AAI_gdM), f = findViewById(R.id.AAI_gdF), b = findViewById(R.id.AAI_gdB);
        m.setEnabled(false);
        f.setEnabled(false);
        b.setEnabled(false);
        DatePicker datePicker = findViewById(R.id.AAI_datePicker);
        datePicker.setEnabled(false);
        findViewById(R.id.AAI_btnAddPic).setEnabled(false);
    }

    private void setupGender() {
        View m = findViewById(R.id.AAI_gdM), f = findViewById(R.id.AAI_gdF), b = findViewById(R.id.AAI_gdB);
        View.OnClickListener listener = v -> {
            switch (v.getId()) {
                case R.id.AAI_gdB:
                    updateChanges(b);
                    break;
                case R.id.AAI_gdM:
                    updateChanges(m);
                    break;
                case R.id.AAI_gdF:
                    updateChanges(f);
                    break;
            }
        };
        m.setOnClickListener(listener);
        f.setOnClickListener(listener);
        b.setOnClickListener(listener);
    }

    private void updateChanges(View x) {
        TextView v = (TextView) x;
        TextView m = findViewById(R.id.AAI_gdM), f = findViewById(R.id.AAI_gdF), b = findViewById(R.id.AAI_gdB);
        boolean isM = m == v, isF = f == v, isB = v == b;
        if (isM) {
            m.setBackgroundColor(ContextCompat.getColor(this, android.R.color.darker_gray));
            userGender = "Male";
            m.setTextColor(Color.WHITE);
            f.setTextColor(Color.BLACK);
            f.setBackgroundColor(Color.WHITE);
            b.setTextColor(Color.BLACK);
            b.setBackgroundColor(Color.WHITE);
        }
        if (isF) {
            f.setBackgroundColor(ContextCompat.getColor(this, android.R.color.darker_gray));
            userGender = "Female";
            f.setTextColor(Color.WHITE);
            m.setTextColor(Color.BLACK);
            m.setBackgroundColor(Color.WHITE);
            b.setTextColor(Color.BLACK);
            b.setBackgroundColor(Color.WHITE);
        }
        if (isB) {
            b.setBackgroundColor(ContextCompat.getColor(this, android.R.color.darker_gray));
            userGender = "Non-Binary";
            b.setTextColor(Color.WHITE);
            f.setTextColor(Color.BLACK);
            f.setBackgroundColor(Color.WHITE);
            m.setTextColor(Color.BLACK);
            m.setBackgroundColor(Color.WHITE);
        }

    }

    private void saveAccountInfo() {
        DatePicker datePicker = findViewById(R.id.AAI_datePicker);
        String dateOfBirth = Time.getYear(datePicker);
        String userName = userNameText.getText().toString();
        AppUser appUser;
        if (RuntimeData.appUser == null) {
            appUser = new AppUser(phoneNumber);
        } else {
            appUser = RuntimeData.appUser;
        }
        if (savePic) {
            appUser.setTnDp(tnDp);
            appUser.setAcDp(acDp);
        }
        if (saveDate) {
            appUser.setDateOfBirth(dateOfBirth);
            appUser.setUserGender(userGender);
        }
        appUser.setUserName(userName);
        DatabaseReference userReference = FirebaseDatabase.getInstance().getReference().child(phoneNumber);
        appUser.setUserId(userReference.getKey());
        userReference.setValue(appUser).addOnCompleteListener(task -> {
            if (savingInfo.isShown()) {
                savingInfo.dismiss();
                finish();
                if (RuntimeData.home == null) {
                    startActivity(new Intent(AccountInformation.this, Home.class));
                }
            }
        });
    }

    private void changeUserDate() {
        saveDate = true;
    }

    private void showPictureDialog() {
        new SelectImage(this, R.style.DialogBox, c -> {
            if (c) {
                takePhotoFromCamera();
            } else {
                choosePhotoFromGallery();
            }

        });
    }

    private void requestMultiplePermissions() {
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            Toast.makeText(AccountInformation.this, "Enable app permissions on the settings menu", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(error -> Toast.makeText(getApplicationContext(), "Some Error! ", Toast.LENGTH_SHORT).show())
                .onSameThread()
                .check();
    }

    private void choosePhotoFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, GALLERY);
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), contentURI);
                    bitmap.setHasAlpha(true);
                    savePic = true;
                    tnDp = String.valueOf(getImageUri(bitmap, 200));
                    acDp = String.valueOf(getImageUri(bitmap, 900));
                    Glide.with(imageView).load(tnDp).into(imageView);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } else if (requestCode == CAMERA) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            bitmap.setHasAlpha(true);
            savePic = true;
            tnDp = String.valueOf(getImageUri(bitmap, 200));
            acDp = String.valueOf(getImageUri(bitmap, 900));
            Glide.with(imageView).load(tnDp).into(imageView);
        } else {

            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private Uri getImageUri(Bitmap inImage, int size) {
        Uri uri = null;
        try {
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = calculateInSampleSize(options, size, size);
            options.inJustDecodeBounds = false;
            Bitmap newBitmap = Bitmap.createScaledBitmap(inImage, 700, 700, true);
            File file = new File(getFilesDir(), "iPhoby" + new Random().nextInt() + ".png");
            FileOutputStream out = openFileOutput(file.getName(), Context.MODE_PRIVATE);
            newBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
            String realPath = file.getAbsolutePath();
            File f = new File(realPath);
            uri = Uri.fromFile(f);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
        }
        return uri;

    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth,
                                             int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            boolean c = ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth);
            while (c) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
}
