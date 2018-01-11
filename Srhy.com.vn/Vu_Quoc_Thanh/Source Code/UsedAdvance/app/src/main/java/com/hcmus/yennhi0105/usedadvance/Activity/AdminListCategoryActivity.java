package com.hcmus.yennhi0105.usedadvance.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hcmus.yennhi0105.usedadvance.Adapter.CategoryAdapter;
import com.hcmus.yennhi0105.usedadvance.CClass.CCategory;
import com.hcmus.yennhi0105.usedadvance.R;

import java.util.ArrayList;

public class AdminListCategoryActivity extends BaseActivity {

    private static String TAG = "AdminListCate";
    GridView gvCate;
    ArrayList<CCategory> arrayCate;
    CategoryAdapter adapterCate = null;

    ImageButton ibtnAdd;
    private DatabaseReference mDatabase;
    int index;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_list_category);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        AddControl();

        // Load Danh sách Danh mục về đưa lên lvCate
        mDatabase.child("Category").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                CCategory category = dataSnapshot.getValue(CCategory.class);
                arrayCate.add(new CCategory(dataSnapshot.getKey(),
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

        final Animation animation = AnimationUtils.loadAnimation(this, R.anim.anim_button_ok);
        // Chuyển sang màn hình Thêm danh muc
        ibtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animation);
                startActivity(new Intent(AdminListCategoryActivity.this, AddCategoryActivity.class));
            }
        });

        // gán menu cho lvCate
        registerForContextMenu(gvCate);
        // Bắt sự kiên Long Click cho lvCate
        gvCate.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                index = position;
                return false;
            }
        });

    }

    //Khỏi tạo menu Update và Delete
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.menu_caterogy, menu);

        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_Update:
                Update();
                break;
            case R.id.menu_Delete:
                Delete();
                break;
        }
        return super.onContextItemSelected(item);
    }

    private void Delete() {

        mDatabase.child("Category").child(arrayCate.get(index).getKey()).removeValue(); // Delete Server
        arrayCate.remove(index); // Delete Client
        adapterCate.notifyDataSetChanged();

    }

    private void Update() {

        Intent intent = new Intent(AdminListCategoryActivity.this, EditCategoryActivity.class);

        Bundle bundle = new Bundle();
        bundle.putSerializable("Category", arrayCate.get(index));
        intent.putExtra("dataCategory", bundle);
        startActivity(intent);
    }
    private void AddControl() {

        gvCate = (GridView) findViewById(R.id.grAdminCate);

        ibtnAdd = (ImageButton) findViewById(R.id.imageButtonAdminAddCate);

        arrayCate = new ArrayList<CCategory>();
        adapterCate = new CategoryAdapter(AdminListCategoryActivity.this, R.layout.line_category, arrayCate);
        gvCate.setAdapter(adapterCate);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(AdminListCategoryActivity.this, UserActivity.class));
    }
}
