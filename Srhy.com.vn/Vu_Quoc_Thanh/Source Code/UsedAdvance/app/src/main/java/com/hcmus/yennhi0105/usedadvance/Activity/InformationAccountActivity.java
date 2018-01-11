package com.hcmus.yennhi0105.usedadvance.Activity;

import android.app.Dialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hcmus.yennhi0105.usedadvance.CClass.CUser;
import com.hcmus.yennhi0105.usedadvance.R;
import com.hcmus.yennhi0105.usedadvance.RoundedTransformation;
import com.squareup.picasso.Picasso;

public class InformationAccountActivity extends AppCompatActivity {

    ImageView imgViewIcon;
    TextView txtName, txtPhone, txtEmail, txtAddress;
    ImageButton ibtnPhone, ibtnEmail, ibtnAddress;


    private DatabaseReference mDatabase;
    CUser cUserInfor = new CUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_account);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        AddControl();

        MobileAds.initialize(getApplicationContext(), "ca-app-pub-6317808399413254~2123658523");
        AdView mAdView = (AdView) findViewById(R.id.adViewInformationAcc);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        Bundle myBundle_Admin = getIntent().getBundleExtra("datacUserAdmin");
        Bundle myBundle_Member = getIntent().getBundleExtra("datacUserMember");
        if (myBundle_Admin != null) {
            cUserInfor = (CUser) myBundle_Admin.getSerializable("cUserAdmin");
        }
        if (myBundle_Member != null) {
            cUserInfor = (CUser) myBundle_Member.getSerializable("cUserMember");
        }

        txtName.setText(cUserInfor.getName());
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/FiolexGirlVH.ttf");
        txtName.setTypeface(typeface);
        txtPhone.setText(cUserInfor.getPhone());
        txtAddress.setText(cUserInfor.getAddress());
        txtEmail.setText(cUserInfor.getEmail());

        //Picasso.with(getApplicationContext()).load(user.getAvartar()).into(imgViewIcon);

        Picasso.with(getApplicationContext())
                .load(cUserInfor.getAvartar())
                .transform(new RoundedTransformation(100, 4)) // Redius imageView nhe!
                .into(imgViewIcon);



        // Thay đổi só điện thoại
        ibtnPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogChangegePhone();
            }
        });

        ibtnEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogChangegeEmail();
            }
        });

        ibtnAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogChangegeAddress();
            }
        });

    }


    // Dialog Đổi Address
    private void DialogChangegeAddress() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_change_address);
        dialog.setTitle(R.string.change_address);
        dialog.show();
        final EditText edtInput = (EditText) dialog.findViewById(R.id.editTextChangeAddress);
        Button btnSubmit = (Button) dialog.findViewById(R.id.buttonChangeAddress);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String newValue = edtInput.getText().toString().trim();
                if (newValue.equals("")) {
                    Toast.makeText(getApplicationContext(), R.string.input_empty, Toast.LENGTH_SHORT).show();
                }else if (newValue.length() < 15){
                    Toast.makeText(InformationAccountActivity.this, R.string.sort_address, Toast.LENGTH_SHORT).show();
                }else {
                    cUserInfor.setAddress(newValue);
                    mDatabase.child("User").child(cUserInfor.getID()).setValue(cUserInfor, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            if (databaseError == null) {
                                Toast.makeText(InformationAccountActivity.this, R.string.change_address_success, Toast.LENGTH_SHORT).show();
                                txtAddress.setText(newValue);
                                dialog.dismiss();
                            } else {
                                Toast.makeText(InformationAccountActivity.this, R.string.change_address_success, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    // Dialog đổi email
    private void DialogChangegeEmail() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_change_email);
        dialog.setTitle(R.string.change_email);
        dialog.show();
        final EditText edtInput = (EditText) dialog.findViewById(R.id.editTextChangeEmail);
        Button btnSubmit = (Button) dialog.findViewById(R.id.buttonChangeEmail);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String newValue = edtInput.getText().toString().trim();
                if (newValue.equals("")) {
                    Toast.makeText(getApplicationContext(), R.string.input_empty, Toast.LENGTH_SHORT).show();
                }else {
                    cUserInfor.setEmail(newValue);
                    mDatabase.child("User").child(cUserInfor.getID()).setValue(cUserInfor, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            if (databaseError == null) {
                                Toast.makeText(InformationAccountActivity.this, R.string.change_email_success, Toast.LENGTH_SHORT).show();
                                txtEmail.setText(newValue);
                                dialog.dismiss();
                            } else {
                                Toast.makeText(InformationAccountActivity.this, R.string.change_email_success, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

    }

    // Hiện Dialog Đổi Phone
    private void DialogChangegePhone() {

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_change_phone);
        dialog.setTitle(R.string.change_phone);
        dialog.show();
        final EditText edtInput = (EditText) dialog.findViewById(R.id.editTextChangePhone);
        Button btnSubmit = (Button) dialog.findViewById(R.id.buttonChangePhone);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String newValue = edtInput.getText().toString().trim();
                if (newValue.equals("")) {
                    Toast.makeText(getApplicationContext(), R.string.input_empty, Toast.LENGTH_SHORT).show();
                } else if (newValue.length() < 9) {
                    Toast.makeText(InformationAccountActivity.this, R.string.sort_phone, Toast.LENGTH_SHORT).show();
                } else {
                    cUserInfor.setPhone(newValue);
                    mDatabase.child("User").child(cUserInfor.getID()).setValue(cUserInfor, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            if (databaseError == null) {
                                Toast.makeText(InformationAccountActivity.this, R.string.change_phone_success, Toast.LENGTH_SHORT).show();
                                txtPhone.setText(newValue);
                                dialog.dismiss();
                            } else {
                                Toast.makeText(InformationAccountActivity.this, R.string.change_phone_success, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

    }


    private void AddControl() {

        imgViewIcon = (ImageView) findViewById(R.id.imageViewIconInfor);
        txtName = (TextView) findViewById(R.id.textViewNameInfor);
        txtEmail = (TextView) findViewById(R.id.textViewEmailInfor);
        txtAddress = (TextView) findViewById(R.id.textViewAddressInfor);
        txtPhone = (TextView) findViewById(R.id.textViewPhoneInfor);

        ibtnPhone = (ImageButton) findViewById(R.id.imageButtonEditPhone);
        ibtnEmail = (ImageButton) findViewById(R.id.imageButtonEditEmail);
        ibtnAddress = (ImageButton) findViewById(R.id.imageButtonEditAddress);
    }
}
