package com.hcmus.yennhi0105.usedadvance.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
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

public class SellingMemberActivity extends BaseMemberActivity {

    ListView lvSelling;
    ProductAdapter adapterProduct = null;
    ArrayList<CProduct> arrayProduct;
    private DatabaseReference mDatabase;

    int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selling_member);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        AddControl();

        MobileAds.initialize(getApplicationContext(), "ca-app-pub-6317808399413254~2123658523");
        AdView mAdView = (AdView) findViewById(R.id.adViewSellingMember);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        final String IDUser = UserActivity.cUserMember.getID();

        mDatabase.child("Product").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                CProduct product = dataSnapshot.getValue(CProduct.class);

                // Lấy ra những sản phẩm có ID của người đang đăng nhập
                if (product.getUser().contains(IDUser)) {
                    CProduct cProduct = new CProduct(
                            dataSnapshot.getKey(),
                            product.getTitle(),
                            product.getCategory(),
                            product.getPrice(),
                            product.getDescritption(),
                            product.getZone(),
                            product.getType(),
                            product.getStatus(),
                            product.getStatusSell(),
                            product.getArrayIcon(),
                            product.getUser());

                    arrayProduct.add(0, cProduct);
                }
                adapterProduct = new ProductAdapter(SellingMemberActivity.this, R.layout.line_product, arrayProduct);
                lvSelling.setAdapter(adapterProduct);
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

        lvSelling.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                index = position;
                return false;
            }
        });

        registerForContextMenu(lvSelling);

    }

    private void AddControl() {

        lvSelling = (ListView) findViewById(R.id.lvSellingPro);
        arrayProduct = new ArrayList<CProduct>();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.menu_selling, menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_member_selling) {
            Update();
        }
        return super.onContextItemSelected(item);
    }

    private void Update() {

        Intent intent = new Intent(SellingMemberActivity.this, EditProductActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("sellingProduct", arrayProduct.get(index));
        intent.putExtra("Data_sellingProduct", bundle);
        startActivity(intent);
    }
}
