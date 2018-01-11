package com.hcmus.yennhi0105.usedadvance.Activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hcmus.yennhi0105.usedadvance.Adapter.HLAdapter;
import com.hcmus.yennhi0105.usedadvance.CClass.CHighLightProduct;
import com.hcmus.yennhi0105.usedadvance.R;

import java.util.ArrayList;

public class HighLightProductActivity extends AppCompatActivity {


    private DatabaseReference mDatabase;

    Toolbar toolbar;
    DrawerLayout drawerLayout;

    ListView lvHighLight;
    ArrayList<CHighLightProduct> arrayHL;
    HLAdapter adapterHL = null;
    private int index;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_light_product);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        AddControl();

        //Show toolBar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Đổ HL về
        mDatabase.child("ProHL").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                CHighLightProduct hl = dataSnapshot.getValue(CHighLightProduct.class);
                CHighLightProduct highLightProduct = new CHighLightProduct();

                highLightProduct.setID(dataSnapshot.getKey());
                highLightProduct.setName(hl.getName());
                highLightProduct.setPrice(hl.getPrice());
                highLightProduct.setDescription(hl.getDescription());
                highLightProduct.setStatusSell(hl.getStatusSell());
                highLightProduct.setAddress(hl.getAddress());
                highLightProduct.setDataPost(hl.getDataPost());
                highLightProduct.setNameCompany(hl.getNameCompany());
                highLightProduct.setLinkVideo(hl.getLinkVideo());

                arrayHL.add(0, highLightProduct);
                adapterHL = new HLAdapter(getApplicationContext(), R.layout.line_hl, arrayHL);
                lvHighLight.setAdapter(adapterHL);
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

        lvHighLight.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                index = i;
                showPopupMenu(view);

            }
        });

    }

    // Show Popup menu
    private void showPopupMenu(View view) {
        PopupMenu popup = new PopupMenu(this, view);
        final MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_popup_hl, popup.getMenu());
        popup.show();
        // Bắt sự kiện
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch(menuItem.getItemId()){
                    case R.id.menu_hl_view:
                    {
                        Intent intent = new Intent(getApplicationContext(), ViewUpdateHLActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("highLightProduct", arrayHL.get(index));
                        intent.putExtra("datahighLightProduct", bundle);
                        startActivity(intent);
                        return true;
                    }
                    case R.id.menu_hl_delete:
                    {
                        mDatabase.child("ProHL").child(arrayHL.get(index).getID()).removeValue();
                        arrayHL.remove(index);
                        adapterHL.notifyDataSetChanged();
                        return true;
                    }

                    default: return false;
                }
            }
        });
    }

    private void AddControl() {

        lvHighLight = (ListView) findViewById(R.id.lvHighLight);
        toolbar = (Toolbar) findViewById(R.id.toolBarHL);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayoutHL);

        arrayHL = new ArrayList<CHighLightProduct>();
    }

    // Show menu trên toolBar nhé!
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_highlight, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    Toast.makeText(getApplicationContext(), query.toString(), Toast.LENGTH_SHORT).show();
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    adapterHL.filter(newText);
                    return false;
                }
            });
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_add_hl) {
            Intent intent = new Intent(getApplicationContext(), AddHLActivity.class);
            startActivity(intent);
        }
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


}
