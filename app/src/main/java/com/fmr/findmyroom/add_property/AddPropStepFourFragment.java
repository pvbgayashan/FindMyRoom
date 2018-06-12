package com.fmr.findmyroom.add_property;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
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

import com.fmr.findmyroom.main.MainActivity;
import com.fmr.findmyroom.common.Property;
import com.fmr.findmyroom.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class AddPropStepFourFragment extends Fragment implements View.OnClickListener {

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
        View view = inflater.inflate(R.layout.fragment_add_prop_step_four, container, false);

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
        Button savePropDataBtn, imageSelectorBtn, addPropCancelBtn;

        savePropDataBtn = view.findViewById(R.id.savePropDataBtn);
        imageSelectorBtn = view.findViewById(R.id.addPropImgSelectorBtn);
        addPropCancelBtn = view.findViewById(R.id.addPropCancelBtn);

        savePropDataBtn.setOnClickListener(this);
        imageSelectorBtn.setOnClickListener(this);
        addPropCancelBtn.setOnClickListener(this);

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
            case R.id.addPropCancelBtn:
                // finish activity
                getActivity().finish();

                // navigate to home
                Intent homeIntent = new Intent(getActivity(), MainActivity.class);
                startActivity(homeIntent);

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
            String name = addPropDataBundle.getString("prop_name");
            String price = addPropDataBundle.getString("prop_price");
            String country = addPropDataBundle.getString("prop_country");
            String city = addPropDataBundle.getString("prop_city");
            String address = addPropDataBundle.getString("prop_address");
            String postalCode = addPropDataBundle.getString("prop_postal_code");
            String phone = addPropDataBundle.getString("prop_phone");
            String userName = addPropDataBundle.getString("prop_added_user_name");
            String ratingString = addPropDataBundle.getString("prop_rating_value");
            float ratingValue = Float.parseFloat(ratingString);
            String rateCountString = addPropDataBundle.getString("prop_rating_count");
            int rateCount = Integer.parseInt(rateCountString);
            int pax = Integer.parseInt(addPropDataBundle.getString("prop_pax"));

            // get property preferences and put it on map
            Map<String, Boolean> propPrefMapper = new HashMap<>();
            propPrefMapper.put("apartment", addPropDataBundle.getBoolean("apartment"));
            propPrefMapper.put("room", addPropDataBundle.getBoolean("room"));
            propPrefMapper.put("employee", addPropDataBundle.getBoolean("employee"));
            propPrefMapper.put("student", addPropDataBundle.getBoolean("student"));
            propPrefMapper.put("other", addPropDataBundle.getBoolean("other"));
            propPrefMapper.put("male", addPropDataBundle.getBoolean("male"));
            propPrefMapper.put("female", addPropDataBundle.getBoolean("female"));
            propPrefMapper.put("urban", addPropDataBundle.getBoolean("urban"));
            propPrefMapper.put("village", addPropDataBundle.getBoolean("village"));
            propPrefMapper.put("sea_side", addPropDataBundle.getBoolean("sea_side"));

            propPrefMapper.put("parking", addPropDataBundle.getBoolean("parking"));
            propPrefMapper.put("attached_bathroom", addPropDataBundle.getBoolean("attached_bathroom"));
            propPrefMapper.put("ac", addPropDataBundle.getBoolean("ac"));
            propPrefMapper.put("hot_water", addPropDataBundle.getBoolean("hot_water"));
            propPrefMapper.put("up_stair", addPropDataBundle.getBoolean("up_stair"));
            propPrefMapper.put("private_balcony", addPropDataBundle.getBoolean("private_balcony"));
            propPrefMapper.put("super_market", addPropDataBundle.getBoolean("super_market"));
            propPrefMapper.put("gym", addPropDataBundle.getBoolean("gym"));
            propPrefMapper.put("hospital", addPropDataBundle.getBoolean("hospital"));
            propPrefMapper.put("train_station", addPropDataBundle.getBoolean("train_station"));
            propPrefMapper.put("bus_station", addPropDataBundle.getBoolean("bus_station"));
            propPrefMapper.put("pets", addPropDataBundle.getBoolean("pets"));
            propPrefMapper.put("smoking", addPropDataBundle.getBoolean("smoking"));
            propPrefMapper.put("child_care", addPropDataBundle.getBoolean("child_care"));

            // generate an unique id
            String id = mDatabaseRef.push().getKey();

            // create data model
            Property property = new Property(id, name, price, country, city, address, postalCode,
                    phone, imageDownloadUrl, userName, ratingValue, rateCount, propPrefMapper, pax);

            // save data
            mDatabaseRef.child(id).setValue(property).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(getContext(), "Data has been saved", Toast.LENGTH_SHORT).show();

                    // navigate to home window with small delay
                    Handler delayHomeNavigator = new Handler();
                    delayHomeNavigator.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent homeNavigator = new Intent(getActivity(), MainActivity.class);
                            startActivity(homeNavigator);
                        }
                    }, 1000);
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
