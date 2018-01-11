package com.hcmus.yennhi0105.usedadvance.Activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hcmus.yennhi0105.usedadvance.CClass.CUser;
import com.hcmus.yennhi0105.usedadvance.R;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;

import static com.hcmus.yennhi0105.usedadvance.R.string.view;

public class UpdateInforActivity extends BaseActivity {

    private static String TAG = "Update247";

    EditText edtName, edtAddress, edtPhone, edtFacebook, edtEmail;
    ImageView imgIcon;
    ImageButton ibtnUpdate, ibtnCamera, ibtnFolder;

    private DatabaseReference mDatabase;
    final int REQUEST_CODE_CAMERA = 111;
    final int REQUEST_CODE_FOLDER = 222;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_infor);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        AddControl();

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/VNF-Sofia Regular.ttf");
        edtName.setTypeface(typeface);
        edtPhone.setTypeface(typeface);
        edtFacebook.setTypeface(typeface);
        edtEmail.setTypeface(typeface);
        edtAddress.setTypeface(typeface);

        Bundle myBundle = getIntent().getBundleExtra("dataUpdateUser");

        final CUser userNow = (CUser) myBundle.getSerializable("updateUser");

        edtName.setText(userNow.getName());
        edtAddress.setText(userNow.getAddress());
        edtPhone.setText(userNow.getPhone());
        edtFacebook.setText(userNow.getFacebook());
        edtEmail.setText(userNow.getEmail());
        //edtTypeAccount.setText(String.valueOf(userNow.getTypeAccount()));
        Picasso.with(UpdateInforActivity.this).load(userNow.getAvartar()).into(imgIcon);

        final Animation animation = AnimationUtils.loadAnimation(this, R.anim.amin_button_login);
        ibtnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(animation);
                ActivityCompat.requestPermissions(
                        UpdateInforActivity.this,
                        new String[]{android.Manifest.permission.CAMERA},
                        REQUEST_CODE_CAMERA );
            }
        });

        ibtnFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(animation);
                ActivityCompat.requestPermissions(
                        UpdateInforActivity.this,
                        new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_FOLDER );
            }
        });

        ibtnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animation);
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser(); // User ben Authentication

                final String _name = edtName.getText().toString().trim();
                final String _address = edtAddress.getText().toString().trim();
                final String _phone = edtPhone.getText().toString().trim();
                final String _face = edtFacebook.getText().toString();
                final String _email = edtEmail.getText().toString();

                //final int _type = Integer.parseInt(edtTypeAccount.getText().toString()); // Chỉ cho phép đăng ký Member. Quản lý sẽ được cấp tài khoản trên server

                if (_name.equals("") || _address.equals("") || _phone.equals("") || _face.equals("") || _email.equals("")) {
                    Toast.makeText(UpdateInforActivity.this, "Chưa nhập đủ thông tin!", Toast.LENGTH_SHORT).show();
                } else {
                    // Them vào Database
                    FirebaseStorage storage = FirebaseStorage.getInstance("gs://useradvance-a262e.appspot.com");
                    Calendar calendar = Calendar.getInstance();
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
                            Toast.makeText(UpdateInforActivity.this, "Upload Error!", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                            Uri downloadUrl = taskSnapshot.getDownloadUrl();

                            CUser cUser = new CUser();
                            cUser.setID(userNow.getID());
                            cUser.setName(_name);
                            cUser.setAddress(_address);
                            cUser.setPhone(_phone);
                            cUser.setFacebook(_face);
                            cUser.setEmail(_email);
                            cUser.setTypeAccount(1);
                            cUser.setAvartar(String.valueOf(downloadUrl));
                            mDatabase.child("User").child(cUser.getID()).setValue(cUser, new DatabaseReference.CompletionListener() { // Chỉ Update ID hiện tại
                                @Override
                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                    if (databaseError == null) {
                                        Toast.makeText(UpdateInforActivity.this, R.string.update_success, Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                    } else {
                                        Toast.makeText(UpdateInforActivity.this, R.string.update_fail, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                            // Đặt DisplayName và PhotoUri
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(_name)
                                    .setPhotoUri(Uri.parse(String.valueOf(downloadUrl)))
                                    .build();

                            user.updateProfile(profileUpdates)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d(TAG, "User profile updated.");
                                            } else {
                                                Log.d(TAG, "User update Error");
                                            }
                                        }
                                    });
                            user.updateEmail(_email)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d(TAG, "User email address updated.");
                                            }
                                        }
                                    });
                        }
                    });
                }

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
                    Toast.makeText(this, R.string.can_not_access_camera, Toast.LENGTH_SHORT).show();
                }
                return;
            }
            case REQUEST_CODE_FOLDER:{
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Intent intentFolder = new Intent(Intent.ACTION_PICK);  // lấy media ra
                    intentFolder.setType("image/*"); //Chọn kiểu hình ảnh
                    startActivityForResult(intentFolder, REQUEST_CODE_FOLDER);
                } else {
                    Toast.makeText(this, R.string.can_not_access_sdcard, Toast.LENGTH_SHORT).show();
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

        ibtnUpdate = (ImageButton) findViewById(R.id.imageButtonOkUpdateInfor);
        ibtnCamera = (ImageButton) findViewById(R.id.imageButtonCameraUpdateInfor);
        ibtnFolder = (ImageButton) findViewById(R.id.imageButtonFolderUpdateInfor);

        imgIcon = (ImageView) findViewById(R.id.imageViewIcon);

        edtName = (EditText) findViewById(R.id.editTextName);
        edtAddress = (EditText) findViewById(R.id.editTextAddress);
        edtPhone = (EditText) findViewById(R.id.editTextPhone);
        edtFacebook = (EditText) findViewById(R.id.editTextFacebook);
        edtEmail = (EditText) findViewById(R.id.editTextEmail);

    }
}
