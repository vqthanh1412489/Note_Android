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
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
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

import java.io.ByteArrayOutputStream;
import java.util.Calendar;

public class SignInActivity extends AppCompatActivity{

    private static String TAG = "SI247";
    final int REQUEST_CODE_CAMERA = 1111;
    final int REQUEST_CODE_FOLDER = 2222;
    private FirebaseAuth mAuth;

    EditText edtName, edtUsername, edtPass, edtRetype, edtPhone, edtAddress, edtFace, edtEmail;
    ImageButton ibtnOk;
    ImageView imgIcon;
    String userName = "";
    String passWord = "";

    CUser cUserSI = new CUser();

    ProgressBar progressBarSI;

    private DatabaseReference mDatabase;
    LinearLayout linearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        AddControl();

        progressBarSI.setVisibility(View.INVISIBLE);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/UVF Chikita.ttf");
        edtName.setTypeface(typeface);
        edtUsername.setTypeface(typeface);
        edtPass.setTypeface(typeface);
        edtRetype.setTypeface(typeface);
        edtPhone.setTypeface(typeface);
        edtEmail.setTypeface(typeface);
        edtAddress.setTypeface(typeface);
        edtFace.setTypeface(typeface);

        imgIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(
                        SignInActivity.this,
                        new String[]{android.Manifest.permission.CAMERA},
                        REQUEST_CODE_CAMERA );
            }
        });

        imgIcon.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ActivityCompat.requestPermissions(
                        SignInActivity.this,
                        new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_FOLDER );
                return false;
            }
        });

        final Animation animationOk = AnimationUtils.loadAnimation(this, R.anim.anim_button_ok);
        ibtnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                v.setAnimation(animationOk);

                linearLayout.setVisibility(View.INVISIBLE);
                progressBarSI.setVisibility(View.VISIBLE);

                final String _name = edtName.getText().toString().trim();
                userName = edtUsername.getText().toString().trim();
                passWord = edtPass.getText().toString();
                final String _repass = edtRetype.getText().toString();
                final String _phone = edtPhone.getText().toString().trim();
                final String _address = edtAddress.getText().toString().trim();
                final String _face = edtFace.getText().toString().trim();
                final String _email = edtEmail.getText().toString().trim();
                //--------

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
                        Toast.makeText(SignInActivity.this, "Fail!", Toast.LENGTH_SHORT).show();
                        progressBarSI.setVisibility(View.INVISIBLE);
                        linearLayout.setVisibility(View.VISIBLE);

                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();

                        if (_name.equals("") || userName.equals("") || passWord.equals("") || _repass.equals("") || _phone.equals("") || _address.equals("") || _face.equals("") || _email.equals("")) {
                            Toast.makeText(SignInActivity.this, R.string.input_empty, Toast.LENGTH_SHORT).show();
                            progressBarSI.setVisibility(View.INVISIBLE);
                            linearLayout.setVisibility(View.VISIBLE);
                        } else if (!passWord.equals(_repass)) {
                            Toast.makeText(SignInActivity.this, "Pass khong trung", Toast.LENGTH_SHORT).show();
                            progressBarSI.setVisibility(View.INVISIBLE);
                            linearLayout.setVisibility(View.VISIBLE);
                        } else if (passWord.length() < 6) {
                            Toast.makeText(SignInActivity.this, R.string.sort_pass, Toast.LENGTH_SHORT).show();
                            progressBarSI.setVisibility(View.INVISIBLE);
                            linearLayout.setVisibility(View.VISIBLE);
                        } else if (!userName.contains("@")){
                            Toast.makeText(SignInActivity.this, R.string.invalue_email, Toast.LENGTH_SHORT).show();
                            progressBarSI.setVisibility(View.INVISIBLE);
                            linearLayout.setVisibility(View.VISIBLE);
                        } else if (_phone.length() < 10){
                            Toast.makeText(SignInActivity.this, R.string.sort_phone, Toast.LENGTH_SHORT).show();
                            progressBarSI.setVisibility(View.INVISIBLE);
                            linearLayout.setVisibility(View.VISIBLE);
                        }
                        else if (!_email.contains("@")){
                            Toast.makeText(SignInActivity.this, R.string.invalue_email_1, Toast.LENGTH_SHORT).show();
                            progressBarSI.setVisibility(View.INVISIBLE);
                            linearLayout.setVisibility(View.VISIBLE);
                        }
                        else if (_address.length() < 12){
                            Toast.makeText(SignInActivity.this, R.string.sort_address, Toast.LENGTH_SHORT).show();
                            progressBarSI.setVisibility(View.INVISIBLE);
                            linearLayout.setVisibility(View.VISIBLE);
                        }
                        else if (!_face.contains("https://www.facebook.com/")){
                            Toast.makeText(SignInActivity.this, R.string.invalue_facebook, Toast.LENGTH_SHORT).show();
                            progressBarSI.setVisibility(View.INVISIBLE);
                            linearLayout.setVisibility(View.VISIBLE);
                        }
                        else{
                            cUserSI.setUsername(userName);
                            cUserSI.setName(_name);
                            cUserSI.setPhone(_phone);
                            cUserSI.setAddress(_address);
                            cUserSI.setFacebook(_face);
                            cUserSI.setEmail(_email);
                            cUserSI.setTypeAccount(2); // Đặt mặc định là Member . Admin thì sửa trên server
                            cUserSI.setAvartar(String.valueOf(downloadUrl));

                            Sign();
                        }
                    }
                });
                //---------


            }
        });

//        btnAccept.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String email = edtUser.getText().toString().trim();
//                String pass = edtPass.getText().toString();
//
//                mAuth.createUserWithEmailAndPassword(email, pass)
//                        .addOnCompleteListener(SignInActivity.this, new OnCompleteListener<AuthResult>() {
//                            @Override
//                            public void onComplete(@NonNull Task<AuthResult> task) {
//
//                                if (task.isSuccessful()) {
//                                    Toast.makeText(SignInActivity.this, R.string.signin_success,
//                                            Toast.LENGTH_SHORT).show();
//                                    finish();
//                                } else {
//                                    Toast.makeText(SignInActivity.this, R.string.account_exists, Toast.LENGTH_SHORT).show();
//                                }
//
//                            }
//                        });
//            }
//        });
//
//        checkBoxShow.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                if (b) {
//                    edtPass.setInputType(InputType.TYPE_CLASS_TEXT);
//                } else {
//                    edtPass.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
//                }
//            }
//        });
    }

    private void Sign() {

        mAuth.createUserWithEmailAndPassword(userName, passWord)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) { // Đăng ký không thành công do tài khoản đã tồn tại rồi!
                            progressBarSI.setVisibility(View.INVISIBLE);
                            Toast.makeText(SignInActivity.this, R.string.account_exists, Toast.LENGTH_SHORT).show();
                        } else {
                            // Database
                            mDatabase.child("User").push().setValue(cUserSI, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                    if (databaseError == null) {
                                        Toast.makeText(SignInActivity.this, R.string.signin_success, Toast.LENGTH_SHORT).show();
                                        progressBarSI.setVisibility(View.INVISIBLE);
                                        linearLayout.setVisibility(View.VISIBLE);
                                        finish();
                                    } else {
                                        Toast.makeText(SignInActivity.this, R.string.signin_fail, Toast.LENGTH_SHORT).show();
                                        progressBarSI.setVisibility(View.INVISIBLE);
                                        linearLayout.setVisibility(View.VISIBLE);
                                    }
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
        linearLayout = (LinearLayout) findViewById(R.id.linearSignIn);
        imgIcon = (ImageView) findViewById(R.id.imageViewIconSI);
        ibtnOk = (ImageButton) findViewById(R.id.imageButtonOkSI);
        edtName = (EditText) findViewById(R.id.editTextNameSI);
        edtPass = (EditText) findViewById(R.id.editTextPassSI);
        edtRetype = (EditText) findViewById(R.id.editTextRetypeSI);
        edtUsername = (EditText) findViewById(R.id.editTextUsernameSI);
        edtPhone = (EditText) findViewById(R.id.editTextPhoneSI);
        edtAddress = (EditText) findViewById(R.id.editTextAddressSI);
        edtFace = (EditText) findViewById(R.id.editTextFaceSI);
        edtEmail = (EditText) findViewById(R.id.editTextEmailSI);

        progressBarSI = (ProgressBar) findViewById(R.id.progressBarSI);
    }

}
