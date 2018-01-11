package com.hcmus.yennhi0105.usedadvance.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hcmus.yennhi0105.usedadvance.CClass.CCategory;
import com.hcmus.yennhi0105.usedadvance.R;

public class EditCategoryActivity extends AppCompatActivity {

    private static String TAG = "EditCategory247";

    EditText edtName;
    ImageButton ibtnEditCategory;
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_category);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        AddControl();

        MobileAds.initialize(getApplicationContext(), "ca-app-pub-6317808399413254~2123658523");
        AdView mAdView = (AdView) findViewById(R.id.adViewEditCateAdmin);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        Bundle myBundle = getIntent().getBundleExtra("dataCategory");
        final CCategory category = (CCategory) myBundle.getSerializable("Category");

        ibtnEditCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String _name = edtName.getText().toString().trim();

                if (_name.equals("")) {
                    Toast.makeText(EditCategoryActivity.this, "Input Information", Toast.LENGTH_SHORT).show();
                } else {
                    // Update Name Category

                    category.setName(_name);

                    mDatabase.child("Category").child(category.getKey()).setValue(category, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            if (databaseError == null) {
                                Toast.makeText(EditCategoryActivity.this, "Update Success!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), UserActivity.class));
                            } else {
                                Toast.makeText(EditCategoryActivity.this, "Fail!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
        });

    }

    private void AddControl() {

        ibtnEditCategory = (ImageButton) findViewById(R.id.imageButtonOkEditCate);
        edtName = (EditText) findViewById(R.id.editTextEditNameCategory);
    }
}
