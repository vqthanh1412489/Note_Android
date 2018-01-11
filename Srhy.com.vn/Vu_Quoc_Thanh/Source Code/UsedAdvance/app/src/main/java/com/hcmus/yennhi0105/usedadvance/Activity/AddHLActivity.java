package com.hcmus.yennhi0105.usedadvance.Activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hcmus.yennhi0105.usedadvance.CClass.CHighLightProduct;
import com.hcmus.yennhi0105.usedadvance.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

public class AddHLActivity extends BaseActivity {

    ImageButton ibtnAdd;
    EditText edtName, edtPrice, edtDescr, edtCompany, edtAddress, edtLinkVideo, edtPhone, edtEmail;
    Spinner spinnerStatus;

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy - hh:mm:ss");
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_hl);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        AddControl();

        final Calendar calendar = Calendar.getInstance();

        SpinnerStatusSellPro();

        ibtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CHighLightProduct hlPro = new CHighLightProduct();
                String _name = edtName.getText().toString().trim();
                String _price = edtPrice.getText().toString().trim();
                String _descr = edtDescr.getText().toString().trim();
                String _company = edtCompany.getText().toString().trim();
                String _phone = edtPhone.getText().toString().trim();
                String _email = edtEmail.getText().toString().trim();
                String _address = edtAddress.getText().toString().trim();
                String _linkVideo = edtLinkVideo.getText().toString().trim();

                hlPro.setName(_name);
                hlPro.setPrice(Integer.parseInt(_price));
                hlPro.setDescription(_descr);
                hlPro.setNameCompany(_company);
                hlPro.setPhone(_phone);
                hlPro.setEmail(_email);
                hlPro.setAddress(_address);
                hlPro.setLinkVideo(_linkVideo);
                hlPro.setDataPost(String.valueOf(simpleDateFormat.format(calendar.getTime())));
                hlPro.setStatusSell(spinnerStatus.getSelectedItemPosition());

                mDatabase.child("ProHL").push().setValue(hlPro, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if (databaseError == null) {
                            Toast.makeText(AddHLActivity.this, R.string.success, Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(AddHLActivity.this, R.string.fail, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
    }

    private void SpinnerStatusSellPro() {

        String[] statusSellHL = getResources().getStringArray(R.array.StatusSellPro);
        ArrayList<String> arrayStatusSellHL = new ArrayList<String>(Arrays.asList(statusSellHL));

        ArrayAdapter<String> adapterStatusSellPro = new ArrayAdapter<String>(AddHLActivity.this,
                android.R.layout.simple_spinner_item,
                arrayStatusSellHL){
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
        spinnerStatus.setAdapter(adapterStatusSellPro);
    }

    private void AddControl() {

        ibtnAdd = (ImageButton) findViewById(R.id.imageButtonAddHL);
        edtName = (EditText) findViewById(R.id.editTextNameHL);
        edtPrice = (EditText) findViewById(R.id.editTextPriceHL);
        edtDescr = (EditText) findViewById(R.id.editTextDescrHL);
        edtCompany = (EditText) findViewById(R.id.editTextNameCompanyHL);
        edtEmail = (EditText) findViewById(R.id.editTextEmailHl);
        edtPhone = (EditText) findViewById(R.id.editTextPhoneHL);
        edtAddress = (EditText) findViewById(R.id.editTextAddressHL);
        edtLinkVideo = (EditText) findViewById(R.id.editTextLinkVideoHL);

        spinnerStatus = (Spinner) findViewById(R.id.spinnerStatusSellHL);
    }
}
