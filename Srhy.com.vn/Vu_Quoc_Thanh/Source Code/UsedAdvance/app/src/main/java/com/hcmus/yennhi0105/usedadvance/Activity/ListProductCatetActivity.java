package com.hcmus.yennhi0105.usedadvance.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hcmus.yennhi0105.usedadvance.Adapter.ProductAdapter;
import com.hcmus.yennhi0105.usedadvance.CClass.CProduct;
import com.hcmus.yennhi0105.usedadvance.R;

import java.util.ArrayList;

public class ListProductCatetActivity extends BaseMemberActivity {

    ListView lvProCate;
    ArrayList<CProduct> arrayProduct;
    ProductAdapter adapterProduct = null;

    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_product_catet);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        AddControl();

        MobileAds.initialize(getApplicationContext(), "ca-app-pub-6317808399413254~2123658523");
        AdView mAdView = (AdView) findViewById(R.id.adViewListProCate);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        final String IDCategory = getIntent().getStringExtra("IDCategory");

        //TODO: Đổ danh sách Product ra ListView (Có IDCategory giống bên kia)
        mDatabase.child("Product").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                CProduct product = dataSnapshot.getValue(CProduct.class);

                if (product.getCategory().contains(IDCategory)) {
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
                    arrayProduct.add(0, cProduct);
                }



                // Đưa lên ListView
                adapterProduct = new ProductAdapter(ListProductCatetActivity.this, R.layout.line_product, arrayProduct);
                lvProCate.setAdapter(adapterProduct);
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

        lvProCate.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ListProductCatetActivity.this, SellProActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("product_cate", arrayProduct.get(position));
                intent.putExtra("dataproduct_cate", bundle);
                startActivity(intent);
            }
        });

    }

    private void AddControl() {

        lvProCate = (ListView) findViewById(R.id.lvProCate);
        arrayProduct = new ArrayList<>();
    }
}
