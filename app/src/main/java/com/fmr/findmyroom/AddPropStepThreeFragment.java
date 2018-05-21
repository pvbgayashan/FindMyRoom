package com.fmr.findmyroom;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class AddPropStepThreeFragment extends Fragment implements View.OnClickListener {

    private DatabaseReference mDatabaseRef;
    private StorageReference mStorageRef;

    private ImageView selectedImageView;
    private ProgressBar progressBar;

    private Bundle addPropDataBundle;
    private Uri file;

    private final int PICK_IMAGE_REQUEST = 100;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_prop_step_three, container, false);

        // set database reference
        final String DATA_REF = "property_data";
        mDatabaseRef = FirebaseDatabase.getInstance().getReference(DATA_REF);

        // set storage reference
        mStorageRef = FirebaseStorage.getInstance().getReference();

        // get data from fragment
        addPropDataBundle = getArguments();

        // init input items and progressbar
        selectedImageView = view.findViewById(R.id.addPropMainImage);
        progressBar = view.findViewById(R.id.addPropProgressbar);

        // handle button clicks
        Button savePropDataBtn = view.findViewById(R.id.savePropDataBtn);
        Button imageSelectorBtn = view.findViewById(R.id.addPropImgSelectorBtn);

        savePropDataBtn.setOnClickListener(this);
        imageSelectorBtn.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.savePropDataBtn:
                savePropertyDataWithImage();
                break;
            case R.id.addPropImgSelectorBtn:
                showFileChooser();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK
                && data != null && data.getData() != null) {
            file = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), file);
                selectedImageView.setImageBitmap(bitmap);
            } catch (Exception ex) {
                ex.printStackTrace();
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

    // save all property data with image
    private void savePropertyDataWithImage() {
        // upload image
        StorageReference imageUploadRef = mStorageRef.child("images/" + System.currentTimeMillis() + ".jpg");
        if (file != null) {
            // show progressbar
            progressBar.setVisibility(View.VISIBLE);

            imageUploadRef.putFile(file).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // hide progressbar
                    progressBar.setVisibility(View.GONE);

                    if (taskSnapshot.getDownloadUrl() != null) {
                        // save property data
                        String imgDownloadUrl = taskSnapshot.getDownloadUrl().toString();
                        savePropertyData(imgDownloadUrl);
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception ex) {
                    // hide progressbar
                    progressBar.setVisibility(View.GONE);

                    Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    // save property data
    public void savePropertyData(String imageDownloadUrl) {
        // get input data from bundle
        if (addPropDataBundle != null) {
            String propName = addPropDataBundle.getString("prop_name");
            String propPrice = addPropDataBundle.getString("prop_price");
            String country = addPropDataBundle.getString("country");
            String city = addPropDataBundle.getString("city");

            // generate an unique id
            String id = mDatabaseRef.push().getKey();

            // create data model
            Property property = new Property(propName, country, city, propPrice, imageDownloadUrl);

            // save data
            mDatabaseRef.child(id).setValue(property).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(getContext(), "Data has been saved", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception ex) {
                    Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
