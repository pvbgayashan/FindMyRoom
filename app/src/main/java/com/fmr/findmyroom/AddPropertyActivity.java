package com.fmr.findmyroom;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class AddPropertyActivity extends AppCompatActivity implements View.OnClickListener {

    // input elements
    private EditText propNameTxt, cityTxt, priceTxt;
    private Spinner countrySpinner;
    private Button selectImgBtn, savePropBtn;
    private ImageView selectedImageView;

    // selected file holder
    private Uri file;

    // constant
    private static final String DATA_SAVING_STATUS = "Data Saving Status";
    private static final int PICK_IMAGE_REQUEST = 100;

    // database reference
    private static final String DATA_REF = "property_data";
    private DatabaseReference mDatabaseRef;

    // storage reference
    private StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_property);

        // init elements
        propNameTxt = findViewById(R.id.propNameTxt);
        cityTxt = findViewById(R.id.cityTxt);
        priceTxt = findViewById(R.id.priceTxt);

        countrySpinner = findViewById(R.id.countrySpinner);
        selectedImageView = findViewById(R.id.selectedImgView);

        selectImgBtn = findViewById(R.id.selectImgBtn);
        savePropBtn = findViewById(R.id.savePropBtn);

        // set database reference
        mDatabaseRef = FirebaseDatabase.getInstance().getReference(DATA_REF);

        // set storage reference
        mStorageRef = FirebaseStorage.getInstance().getReference();

        selectImgBtn.setOnClickListener(this);
        savePropBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == selectImgBtn) {
            showFileChooser();
        } else if (view == savePropBtn) {
            saveData();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            file = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), file);
                selectedImageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // show image selector
    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select an Image"), PICK_IMAGE_REQUEST);
    }

    // save data
    private void saveData() {
        // validate property name
        String propName = propNameTxt.getText().toString();

        if (propName.isEmpty()) {
            return;
        }

        // upload image first and save the data next
        uploadImageAndSaveData();
    }

    // upload image to fire base
    private void uploadImageAndSaveData() {

        StorageReference imageUploadRef = mStorageRef.child("images/" + System.currentTimeMillis() + ".jpg");

        if (file != null) {
            // show uploading progress
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Saving Data");
            progressDialog.show();

            imageUploadRef.putFile(file).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    if (taskSnapshot.getDownloadUrl() != null) {
                        // get download url
                        String imageDownloadUrl = taskSnapshot.getDownloadUrl().toString();

                        // save property data with image download url
                        savePropData(imageDownloadUrl);
                    }

                    // after data saved
                    Toast.makeText(AddPropertyActivity.this, "Data saved.", Toast.LENGTH_SHORT).show();

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                        }
                    }, 1000);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception ex) {
                    progressDialog.dismiss();
                    Log.w("Image Uploading Status", "Error on uploading an image!", ex);
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    progressDialog.setMessage(((int)progress) + "%");
                }
            });
        }
    }

    // save property data to database
    private void savePropData(String imageDownloadUrl) {
        // get input data
        String propName = propNameTxt.getText().toString();
        String country = countrySpinner.getSelectedItem().toString();
        String city = cityTxt.getText().toString();
        String price = priceTxt.getText().toString();

        // generate an unique id
        String id = mDatabaseRef.push().getKey();
        Property property = new Property(propName, country, city, price, imageDownloadUrl);
        mDatabaseRef.child(id).setValue(property).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(DATA_SAVING_STATUS, "Data has been saved!");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception ex) {
                Log.w(DATA_SAVING_STATUS, "Error while saving data!", ex);
            }
        });
    }
}