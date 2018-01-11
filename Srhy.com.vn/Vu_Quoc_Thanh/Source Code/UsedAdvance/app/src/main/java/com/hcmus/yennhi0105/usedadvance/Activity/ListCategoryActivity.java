package com.hcmus.yennhi0105.usedadvance.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hcmus.yennhi0105.usedadvance.Adapter.CategoryAdapter;
import com.hcmus.yennhi0105.usedadvance.CClass.CCategory;
import com.hcmus.yennhi0105.usedadvance.R;

import java.util.ArrayList;

public class ListCategoryActivity extends BaseMemberActivity {

    private static String TAG = "ListCate";
    GridView gvCate;
    ArrayList<CCategory> arrayCate;
    CategoryAdapter adapterCate = null;

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_category);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        AddControl();

        // Load Danh sách Danh mục về đưa lên lvCate
        mDatabase.child("Category").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                CCategory category = dataSnapshot.getValue(CCategory.class);
                arrayCate.add(0, new CCategory(dataSnapshot.getKey(),
                        category.getID(), category.getName(), category.getIcon()));
                adapterCate.notifyDataSetChanged();
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

        gvCate.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ListCategoryActivity.this, ListProductCatetActivity.class);
                intent.putExtra("IDCategory", arrayCate.get(position).getName());
                startActivity(intent);
            }
        });

    }

    private void AddControl() {
        gvCate = (GridView) findViewById(R.id.grCate);
        arrayCate = new ArrayList<CCategory>();
        adapterCate = new CategoryAdapter(ListCategoryActivity.this, R.layout.line_category, arrayCate);
        gvCate.setAdapter(adapterCate);
    }
}
