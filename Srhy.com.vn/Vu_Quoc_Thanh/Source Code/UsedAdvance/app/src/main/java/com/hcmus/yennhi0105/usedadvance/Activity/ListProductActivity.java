package com.hcmus.yennhi0105.usedadvance.Activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hcmus.yennhi0105.usedadvance.Adapter.ProductAdapter;
import com.hcmus.yennhi0105.usedadvance.CClass.CProduct;
import com.hcmus.yennhi0105.usedadvance.R;

import java.util.ArrayList;
import java.util.Arrays;

// Của Member
public class ListProductActivity extends BaseMemberActivity {


    EditText edtSearch;
    AutoCompleteTextView completeTextViewCity, completeTextViewDistrict;
    ArrayList<String> arrayCity;
    ArrayList<String> arrayDistrict;
    ArrayAdapter adapterCity = null;
    ArrayAdapter adapterDistrict = null;

    ListView lvProduct;
    ArrayList<CProduct> arrayProduct;
    ProductAdapter adapterProduct = null;

    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_product);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        AddControl();

        //TODO: Đổ danh sách Product ra ListView
        mDatabase.child("Product").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                CProduct product = dataSnapshot.getValue(CProduct.class);

                CProduct cProduct = new CProduct(dataSnapshot.getKey(),
                        product.getTitle(),
                        product.getCategory(),
                        product.getPrice(),
                        product.getDescritption(),
                        product.getZone(),
                        product.getType(),
                        product.getStatus(),
                        product.getStatusSell(),
                        product.getArrayIcon(),
                        product.getUser()); // Lấy ID User bán sản phẩm khi đăng nhập luôn.

                //Log.d("LOLL", product.getArrayIcon().get(0));
                if (cProduct.getStatusSell() == 1) {
                    arrayProduct.add(0, cProduct);
                }

                // Đưa lên ListView
                adapterProduct = new ProductAdapter(ListProductActivity.this, R.layout.line_product, arrayProduct);
                lvProduct.setAdapter(adapterProduct);
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

        // Bắt sự kiện Click
        lvProduct.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ListProductActivity.this, SellProActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("product", arrayProduct.get(position));
                intent.putExtra("dataproduct", bundle);
                startActivity(intent);
            }
        });

        // Xử lý AutocompeleTextView
        String[] city = getResources().getStringArray(R.array.City);
        arrayCity.addAll(Arrays.asList(city));
        adapterCity.notifyDataSetChanged();

        // Tìm kiếm Pro trên Editext
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapterProduct.filter(String.valueOf(s));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/VNF-Sofia Regular.ttf");
        completeTextViewCity.setTypeface(typeface);
        completeTextViewDistrict.setTypeface(typeface);
        edtSearch.setTypeface(typeface);
        // Tìm kiếm Theo tỉnh thành
        completeTextViewCity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                adapterProduct.filterCity(String.valueOf(s));
            }
        });

        // Timf kiếm theo  Quận
        // TODO: Chua lam xong
    }


    private void AddControl() {

        edtSearch = (EditText) findViewById(R.id.editTextSearchListPro);

        completeTextViewCity = (AutoCompleteTextView) findViewById(R.id.autoCity);
        arrayCity = new ArrayList<String>();
        adapterCity = new ArrayAdapter(ListProductActivity.this, android.R.layout.simple_list_item_1, arrayCity);
        completeTextViewCity.setAdapter(adapterCity);

        completeTextViewDistrict = (AutoCompleteTextView) findViewById(R.id.autoDistrict);
        arrayDistrict = new ArrayList<String>();
        adapterDistrict = new ArrayAdapter(ListProductActivity.this, android.R.layout.simple_list_item_1, arrayDistrict);
        completeTextViewDistrict.setAdapter(adapterDistrict);

        lvProduct = (ListView) findViewById(R.id.lvProduct);
        arrayProduct = new ArrayList<CProduct>();
        adapterProduct = new ProductAdapter(ListProductActivity.this, R.layout.line_product, arrayProduct);
    }
}
