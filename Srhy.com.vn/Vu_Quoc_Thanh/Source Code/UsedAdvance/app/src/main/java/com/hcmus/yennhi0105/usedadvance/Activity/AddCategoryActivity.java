package com.hcmus.yennhi0105.usedadvance.Activity;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hcmus.yennhi0105.usedadvance.CClass.CCategory;
import com.hcmus.yennhi0105.usedadvance.R;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;

public class AddCategoryActivity extends BaseActivity {

    private static String TAG = "Category247";

    EditText edtID, edtName;
    ImageButton ibtnAddCategory, ibtnCamera, ibtnFolder;
    ImageView imgIcon;
    private DatabaseReference mDatabase;

    final int REQUEST_CODE_CAMERA = 111;
    final int REQUEST_CODE_FOLDER = 222;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        AddControl();

        MobileAds.initialize(getApplicationContext(), "ca-app-pub-6317808399413254~2123658523");

        AdView mAdView = (AdView) findViewById(R.id.adViewAddCateAdmin);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        ibtnAddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String _id = edtID.getText().toString().trim();
                final String _name = edtName.getText().toString().trim();

                if (_id.equals("") || _name.equals("")) {
                    Toast.makeText(AddCategoryActivity.this, "Empty Infor", Toast.LENGTH_SHORT).show();
                } else {
                    // Add Category
                    FirebaseStorage storage = FirebaseStorage.getInstance("gs://useradvance-a262e.appspot.com");
                    final Calendar calendar = Calendar.getInstance();
                    StorageReference storageRef = storage.getReference();
                    StorageReference mountainsRef = storageRef.child("image" + calendar.getTimeInMillis() + ".png");

                    BitmapDrawable drawable = (BitmapDrawable) imgIcon.getDrawable();
                    Bitmap bitmap = drawable.getBitmap();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                    byte[] data = baos.toByteArray();

                    UploadTask uploadTask = mountainsRef.putBytes(data);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Toast.makeText(AddCategoryActivity.this, "Fail!", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                            Uri downloadUrl = taskSnapshot.getDownloadUrl();

                            CCategory category = new CCategory();
                            category.setID(_id);
                            category.setName(_name);
                            category.setIcon(String.valueOf(downloadUrl));

                            mDatabase.child("Category").push().setValue(category, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                    if (databaseError == null) {
                                        Toast.makeText(AddCategoryActivity.this, R.string.success, Toast.LENGTH_SHORT).show();
                                        finish();
                                    } else {
                                        Toast.makeText(AddCategoryActivity.this, R.string.fail, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    });
                }

            }
        });


        ibtnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(
                        AddCategoryActivity.this,
                        new String[]{android.Manifest.permission.CAMERA},
                        REQUEST_CODE_CAMERA );
            }
        });

        ibtnFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(
                        AddCategoryActivity.this,
                        new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_FOLDER );
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case REQUEST_CODE_CAMERA:{
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intentCamera, REQUEST_CODE_CAMERA);
                } else {
                    Toast.makeText(this, "Không cho phép truy cập Camera", Toast.LENGTH_SHORT).show();
                }
                return;
            }
            case REQUEST_CODE_FOLDER:{
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Intent intentFolder = new Intent(Intent.ACTION_PICK);  // lấy media ra
                    intentFolder.setType("image/*"); //Chọn kiểu hình ảnh
                    startActivityForResult(intentFolder, REQUEST_CODE_FOLDER);
                } else {
                    Toast.makeText(this, "Không cho phép truy cập Folder", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == REQUEST_CODE_FOLDER && resultCode == RESULT_OK && data != null){
            Uri uriFolder = data.getData(); // đường dẫn tới thư mục chứa ảnh
            imgIcon.setImageURI(uriFolder); // Gán thẳng đường dẫn hình cho imgHinh
        }
        if(requestCode == REQUEST_CODE_CAMERA && resultCode == RESULT_OK && data != null){
            Bitmap bitmapCamera = (Bitmap) data.getExtras().get("data");
            imgIcon.setImageBitmap(bitmapCamera);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void AddControl() {

        edtID = (EditText) findViewById(R.id.editTextIDCategory);
        edtName = (EditText) findViewById(R.id.editTextNameCategory);

        ibtnAddCategory = (ImageButton) findViewById(R.id.imageButtonOkAddCate);
        imgIcon = (ImageView) findViewById(R.id.imageViewIconCate);

        ibtnCamera = (ImageButton) findViewById(R.id.imageButtonCameraAddCate);
        ibtnFolder = (ImageButton) findViewById(R.id.imageButtonFolderAddCate);


    }
}