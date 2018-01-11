package com.hcmus.yennhi0105.usedadvance.Activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hcmus.yennhi0105.usedadvance.Adapter.CategoryAdapter;
import com.hcmus.yennhi0105.usedadvance.CClass.CCategory;
import com.hcmus.yennhi0105.usedadvance.CClass.CProduct;
import com.hcmus.yennhi0105.usedadvance.Dialog.DialogCityActivity;
import com.hcmus.yennhi0105.usedadvance.R;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

public class EditProductActivity extends BaseMemberActivity {

    private static String TAG = "ProEdit";

    ImageView img1, img2, img3, img4;
    EditText edtName, edtPrice, edtDescription;
    TextView txtZone;
    Spinner spCate, spType, spStatus, spStatusSell;

    Button btnEdit;
    final int REQUEST_CODE_CITY = 555;

    private DatabaseReference mDatabase;
    CProduct product = new CProduct();

    ArrayList<CCategory> arrayCate;
    CategoryAdapter adapterCate = null;
    ArrayList<String> arrayPicture;
    Toolbar toolbar;

    final int REQUEST_CODE_CAMERA1 = 111;
    final int REQUEST_CODE_CAMERA2 = 222;
    final int REQUEST_CODE_CAMERA3 = 333;
    final int REQUEST_CODE_CAMERA4 = 444;

    LinearLayout layoutConfirmEdit;
    ProgressBar progressBar;
    ImageButton imgConfirmEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        AddControl();

        layoutConfirmEdit.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.INVISIBLE);

        Bundle myBundleSellingPro = getIntent().getBundleExtra("Data_sellingProduct");
        if (myBundleSellingPro != null) {
            product = (CProduct) myBundleSellingPro.getSerializable("sellingProduct");
        }

        //Log.d(TAG, "id:" + product.getID());

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.update_pro);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Đưa hình chụp vào imageview
        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Camera(REQUEST_CODE_CAMERA1);
            }
        });
        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Camera(REQUEST_CODE_CAMERA2);
            }
        });
        img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Camera(REQUEST_CODE_CAMERA3);
            }
        });
        img4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Camera(REQUEST_CODE_CAMERA4);
            }
        });

        SpinnerTypePro();
        SpinnerStatusPro();
        SpinnerStatusSellPro();

        txtZone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditProductActivity.this, DialogCityActivity.class);
                startActivityForResult(intent, REQUEST_CODE_CITY);
            }
        });

        // Đổ Tên Category từ Firebase về spCate
        mDatabase.child("Category").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                CCategory category = dataSnapshot.getValue(CCategory.class);
                arrayCate.add(new CCategory(category.getKey(),
                        category.getID(),
                        category.getName(),
                        category.getIcon()));
                adapterCate = new CategoryAdapter(EditProductActivity.this, R.layout.line_category, arrayCate){
                    @Override
                    public boolean isEnabled(int position) { // Không cho chọn Item thứ 0
                        if (position == 0) {
                            return false;
                        } else {
                            return true;
                        }
                    }

                };

                spCate.setAdapter(adapterCate);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        spCate.setSelection(1);

        // Đổ ảnh lên
        int sizeArrayIcon = product.getArrayIcon().size();
        switch (sizeArrayIcon) {
            case 1:
                Picasso.with(getApplicationContext()).load(product.getArrayIcon().get(0)).into(img1); break;
            case 2:
                Picasso.with(getApplicationContext()).load(product.getArrayIcon().get(0)).into(img1);
                Picasso.with(getApplicationContext()).load(product.getArrayIcon().get(1)).into(img2);break;
            case 3:
                Picasso.with(getApplicationContext()).load(product.getArrayIcon().get(0)).into(img1);
                Picasso.with(getApplicationContext()).load(product.getArrayIcon().get(1)).into(img2);
                Picasso.with(getApplicationContext()).load(product.getArrayIcon().get(2)).into(img3);break;
            case 4:
                Picasso.with(getApplicationContext()).load(product.getArrayIcon().get(0)).into(img1);
                Picasso.with(getApplicationContext()).load(product.getArrayIcon().get(1)).into(img2);
                Picasso.with(getApplicationContext()).load(product.getArrayIcon().get(2)).into(img3);
                Picasso.with(getApplicationContext()).load(product.getArrayIcon().get(3)).into(img4);break;
        }

        edtName.setText(product.getTitle());
        edtPrice.setText(String.valueOf(product.getPrice()));
        edtDescription.setText(product.getDescritption());
        txtZone.setText(product.getZone());

        spType.setSelection(product.getType());
        spStatus.setSelection(product.getStatus());
        spStatusSell.setSelection(product.getStatusSell());

    }

    private void SpinnerStatusSellPro() {
        String[] statusSellPro = getResources().getStringArray(R.array.StatusSellPro);
        ArrayList<String> arrayStatusSellPro = new ArrayList<String>(Arrays.asList(statusSellPro));

        ArrayAdapter<String> adapterStatusSellPro = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_spinner_item,
                arrayStatusSellPro){
            @Override
            public boolean isEnabled(int position) { // Ẩn item thứ 0
                if (position == 0) {
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view =  super.getDropDownView(position, convertView, parent);

                TextView txt = (TextView) view;
                // Đổi màu item thứ 0 so với các item khác trong spinner
                if (position == 0) {
                    txt.setTextColor(Color.GRAY);
                } else {
                    txt.setTextColor(Color.BLACK);
                }
                return view;
            }
        };

        adapterStatusSellPro.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spStatusSell.setAdapter(adapterStatusSellPro);
    }

    private void SpinnerStatusPro() {
        String[] statusPro = getResources().getStringArray(R.array.StatusPro);
        ArrayList<String> arrayStatusPro = new ArrayList<String>(Arrays.asList(statusPro));

        ArrayAdapter<String> adapterStatusPro = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_spinner_item,
                arrayStatusPro){
            @Override
            public boolean isEnabled(int position) { // Ẩn item thứ 0
                if (position == 0) {
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view =  super.getDropDownView(position, convertView, parent);

                TextView txt = (TextView) view;
                // Đổi màu item thứ 0 so với các item khác trong spinner
                if (position == 0) {
                    txt.setTextColor(Color.GRAY);
                } else {
                    txt.setTextColor(Color.BLACK);
                }
                return view;
            }
        };

        adapterStatusPro.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spStatus.setAdapter(adapterStatusPro);
    }

    private void SpinnerTypePro() {
        String[] typePro = getResources().getStringArray(R.array.TypePro);
        ArrayList<String> arraytypePro = new ArrayList<String>(Arrays.asList(typePro));

        ArrayAdapter<String> adaptertypePro = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_spinner_item,
                arraytypePro){
            @Override
            public boolean isEnabled(int position) { // Ẩn item thứ 0
                if (position == 0) {
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view =  super.getDropDownView(position, convertView, parent);

                TextView txt = (TextView) view;
                // Đổi màu item thứ 0 so với các item khác trong spinner
                if (position == 0) {
                    txt.setTextColor(Color.GRAY);
                } else {
                    txt.setTextColor(Color.BLACK);
                }
                return view;
            }
        };

        adaptertypePro.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spType.setAdapter(adaptertypePro);

    }

    // Upload Picture
    private void UploadPicture(ImageView img, String key) {
        FirebaseStorage storage = FirebaseStorage.getInstance("gs://useradvance-a262e.appspot.com");
        StorageReference storageRef = storage.getReference();
        Calendar calendar = Calendar.getInstance();
        StorageReference mountainsRef = storageRef.child("image" + key + calendar.getTimeInMillis() + ".png");
        BitmapDrawable drawable = (BitmapDrawable) img.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = mountainsRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(EditProductActivity.this, "Fail!", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                arrayPicture.add(String.valueOf(downloadUrl));
            }
        });
    }

    // Hàm xin quyền mở camera
    private void Camera(int REQUEST) {
        ActivityCompat.requestPermissions(
                EditProductActivity.this,
                new String[]{android.Manifest.permission.CAMERA},
                REQUEST);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_CAMERA1:
                PermissionsCamera(grantResults, REQUEST_CODE_CAMERA1);
                return;
            case REQUEST_CODE_CAMERA2:
                PermissionsCamera(grantResults, REQUEST_CODE_CAMERA2);
                return;
            case REQUEST_CODE_CAMERA3:
                PermissionsCamera(grantResults, REQUEST_CODE_CAMERA3);
                return;
            case REQUEST_CODE_CAMERA4:
                PermissionsCamera(grantResults, REQUEST_CODE_CAMERA4);
                return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    // Hàm xác định lựa chọn của người dùng khi cho hoặc không cho mở camera
    private void PermissionsCamera(@NonNull int[] grantResults, int REQUEST) {
        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intentCamera, REQUEST);
        } else {
            Toast.makeText(this, "Không cho phép truy cập Camera", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Nhận ảnh trả về
        if(requestCode == REQUEST_CODE_CAMERA1 && resultCode == RESULT_OK && data != null){
            Bitmap bitmapCamera = (Bitmap) data.getExtras().get("data");
            img1.setImageBitmap(bitmapCamera);
        }
        if(requestCode == REQUEST_CODE_CAMERA2 && resultCode == RESULT_OK && data != null){
            Bitmap bitmapCamera = (Bitmap) data.getExtras().get("data");
            img2.setImageBitmap(bitmapCamera);
        }
        if(requestCode == REQUEST_CODE_CAMERA3 && resultCode == RESULT_OK && data != null){
            Bitmap bitmapCamera = (Bitmap) data.getExtras().get("data");
            img3.setImageBitmap(bitmapCamera);
        }
        if(requestCode == REQUEST_CODE_CAMERA4 && resultCode == RESULT_OK && data != null){
            Bitmap bitmapCamera = (Bitmap) data.getExtras().get("data");
            img4.setImageBitmap(bitmapCamera);
        }
        // Nhận City trả về
        if (requestCode == REQUEST_CODE_CITY && resultCode == RESULT_OK && data != null) {
            String _city = data.getStringExtra("zone");
            txtZone.setText(_city);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_member_editpro, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Khi nhấn Back
            case android.R.id.home:
            {
                finish();
                break;
            }
            // Khi nhấn vào icon edit trên toolbar
            case R.id.menu_member_editpro:
            {

                progressBar.setVisibility(View.VISIBLE);
                if (img1.getDrawable() != null) {
                    UploadPicture(img1, "1");
                }
                if (img2.getDrawable() != null) {
                    UploadPicture(img2, "2");
                }
                if (img3.getDrawable() != null) {
                    UploadPicture(img3, "3");
                }
                if (img4.getDrawable() != null) {
                    UploadPicture(img4, "4");
                }

                String _title = edtName.getText().toString().trim();
                int _price = Integer.parseInt(edtPrice.getText().toString().trim());
                String _descrip = edtDescription.getText().toString().trim();

                RelativeLayout relativeLayout = (RelativeLayout) spCate.getSelectedView(); // Khi get từ Spinner ra là 1 View chứ không phải là 1 text nên cần lấy ra cái View
                String _category = "";
                // Lấy Text ra từ Relativelayout Category
                final int childcount = relativeLayout.getChildCount();
                for (int i = 0; i < childcount; i++) {
                    View v = relativeLayout.getChildAt(i);
                    TextView txt = (TextView) v.findViewById(R.id.textViewTenDM); // Chọn tới TextView

                    _category = txt.getText().toString();
                }

                String _zone = txtZone.getText().toString().trim();
                int _type = spType.getSelectedItemPosition();
                int _status = spStatus.getSelectedItemPosition();
                int _statusSell = spStatusSell.getSelectedItemPosition();

                //Log.d(TAG, arrayPicture.size() + "");

                final CProduct productNew = new CProduct();
                productNew.setID(product.getID());
                productNew.setTitle(_title);
                productNew.setPrice(_price);
                productNew.setDescritption(_descrip);
                productNew.setCategory(_category);
                productNew.setZone(_zone);
                productNew.setType(_type);
                productNew.setStatus(_status);
                productNew.setStatusSell(_statusSell);
                productNew.setArrayIcon(arrayPicture);
                productNew.setUser(UserActivity.cUserMember.getID().toString());

                if (productNew.getArrayIcon().size() <= 0) {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setVisibility(View.INVISIBLE);
                            layoutConfirmEdit.setVisibility(View.VISIBLE);
                            imgConfirmEdit.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mDatabase.child("Product").child(productNew.getID()).setValue(productNew, new DatabaseReference.CompletionListener() {
                                        @Override
                                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                            if (databaseError == null) {
                                                arrayPicture.clear();
                                                Toast.makeText(getApplicationContext(), "Add Product Success!", Toast.LENGTH_SHORT).show();
                                                progressBar.setVisibility(View.INVISIBLE);
                                            } else {
                                                Toast.makeText(getApplicationContext(), "Fail!", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                    layoutConfirmEdit.setVisibility(View.INVISIBLE);
                                }
                            });
                        }
                    }, 5000); // Cần 7s để tải 4 ảnh lên
                } else {
                    Toast.makeText(this, "Thích thì Toast thôi :V...Ahihi... Xong rồi", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void AddControl() {
        imgConfirmEdit = (ImageButton) findViewById(R.id.imageButtonEdit247);
        progressBar = (ProgressBar) findViewById(R.id.progressBarEditProMember);
        layoutConfirmEdit = (LinearLayout) findViewById(R.id.linearConfirmEdit);

        toolbar = (Toolbar) findViewById(R.id.toolEditPro);
        img1 = (ImageView) findViewById(R.id.imageView1ProEdit);
        img2 = (ImageView) findViewById(R.id.imageView2ProEdit);
        img3 = (ImageView) findViewById(R.id.imageView3ProEdit);
        img4 = (ImageView) findViewById(R.id.imageView4ProEdit);


        edtName = (EditText) findViewById(R.id.editTextNameProEdit);
        edtDescription = (EditText) findViewById(R.id.editTextDecrProEdit);
        edtPrice = (EditText) findViewById(R.id.editTextPriceProEdit);

        txtZone = (TextView) findViewById(R.id.textViewZoneProEdit);

        spCate = (Spinner) findViewById(R.id.spinnerCateProEdit);
        spType = (Spinner) findViewById(R.id.spinner);
        spStatus = (Spinner) findViewById(R.id.spinnerStatusProEdit);
        spStatusSell = (Spinner) findViewById(R.id.spinnerStatusSellProEdit);


        arrayCate = new ArrayList<CCategory>();
        arrayPicture = new ArrayList<String>();
        arrayCate.add(new CCategory(null, null, "Chọn Danh mục", null));
    }
}
