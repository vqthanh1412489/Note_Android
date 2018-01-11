package com.hcmus.yennhi0105.usedadvance.Activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
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
import com.hcmus.yennhi0105.usedadvance.Adapter.ProductAdminAdapter;
import com.hcmus.yennhi0105.usedadvance.CClass.CProduct;
import com.hcmus.yennhi0105.usedadvance.R;

import java.util.ArrayList;

public class ListProductAdminActivity extends BaseActivity {

    ListView lvProduct;

    ProductAdminAdapter adapterProduct = null;
    ArrayList<CProduct> arrayProduct;

    DatabaseReference mDatabase;
    int index;
    CProduct cProduct = new CProduct();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_product_admin);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        AddControl();

        //TODO: Đổ danh sách Product ra ListView. Đổ toàn bộ ra. Cả Private lần Public
        LoadData();

        lvProduct.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                index = position;
                showPopupMenu(view);
            }
        });

    }

    private void LoadData() {
        mDatabase.child("Product").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                CProduct product = dataSnapshot.getValue(CProduct.class);

                // Lấy ID User bán sản phẩm khi đăng nhập luôn.
                cProduct = new CProduct(
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

                //Log.d("vqt", "ID la: " + cProduct.getID());
                arrayProduct.add(0, cProduct);
                // Đưa lên ListView
                adapterProduct = new ProductAdminAdapter(ListProductAdminActivity.this, R.layout.line_product_admin, arrayProduct);
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
    }

    private void showPopupMenu(View view) {
        PopupMenu popup = new PopupMenu(this, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_popup_listproduct, popup.getMenu());
        popup.show();
        // Bắt sự kiện
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch(menuItem.getItemId()){
                    case R.id.menu_admin_listpro_delete:
                        showDialogDelete();
                        return true;
                    case R.id.menu_admin_listpro_private:
                        showDialogPrivate();
                        return true;
                    case R.id.menu_admin_listpro_public:
                        showDialogPublic();
                        return true;
                    default: return false;
                }
            }
        });

    }

    private void showDialogPrivate() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.private_pro);
        builder.setTitle(R.string.notification);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //TODO: Chinh thuoc tinh StatusSell thanh Private
                arrayProduct.get(index).setStatusSell(2); // 2 la Private
                // Đưa lên lại server
                mDatabase.child("Product").child(arrayProduct.get(index).getID()).setValue(arrayProduct.get(index), new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if (databaseError == null) {
                            Toast.makeText(ListProductAdminActivity.this, R.string.update_success, Toast.LENGTH_SHORT).show();
                            arrayProduct.clear();
                            LoadData();
                        } else {
                            Toast.makeText(ListProductAdminActivity.this, R.string.update_fail, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create().show();
    }

    private void showDialogPublic() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.public_pro);
        builder.setTitle(R.string.notification);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //TODO: Chinh thuoc tinh StatusSell thanh public
                arrayProduct.get(index).setStatusSell(1); // 1 la Public
                // Đưa lên lại server
                mDatabase.child("Product").child(arrayProduct.get(index).getID()).setValue(arrayProduct.get(index), new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if (databaseError == null) {
                            Toast.makeText(ListProductAdminActivity.this, R.string.update_success, Toast.LENGTH_SHORT).show();
                            arrayProduct.clear();
                            LoadData();
                        } else {
                            Toast.makeText(ListProductAdminActivity.this, R.string.update_fail, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create().show();
    }

    private void showDialogDelete() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete);
        builder.setTitle(R.string.delete);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //TODO: xoa san pham
                mDatabase.child("Product").child(arrayProduct.get(index).getID()).removeValue();
                arrayProduct.remove(index);
                adapterProduct.notifyDataSetChanged();
            }
        });

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create().show();
    }


    private void AddControl() {
        lvProduct = (ListView) findViewById(R.id.lvProductAdmin);
        arrayProduct = new ArrayList<CProduct>();
    }
}
