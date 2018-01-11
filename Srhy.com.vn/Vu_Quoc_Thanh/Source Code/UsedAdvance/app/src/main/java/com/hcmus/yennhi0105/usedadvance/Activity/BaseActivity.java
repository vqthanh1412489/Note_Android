package com.hcmus.yennhi0105.usedadvance.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hcmus.yennhi0105.usedadvance.CClass.CUser;
import com.hcmus.yennhi0105.usedadvance.R;

import static com.hcmus.yennhi0105.usedadvance.Activity.UserActivity.cUserAdmin;

// Lớp chưa NavigationView và toolBar Cho Admin
public class BaseActivity extends AppCompatActivity {

    SearchView searchView;
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public void setContentView(int layoutResID)
    {

        final DrawerLayout fullView = (DrawerLayout) getLayoutInflater().inflate(R.layout.activity_base, null);
        NavigationView navigationViewAdmin = (NavigationView) fullView.findViewById(R.id.navigationViewForAdmin);
        FrameLayout activityContainer = (FrameLayout) fullView.findViewById(R.id.activity_content_admin);
        getLayoutInflater().inflate(layoutResID, activityContainer, true);
        super.setContentView(fullView);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarAdmin);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_launcher);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fullView.openDrawer(Gravity.START);
            }
        });

        navigationViewAdmin.setItemIconTintList(null);
        toolbar.setNavigationIcon(R.drawable.menu);
        navigationViewAdmin.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_admin_category:
                        startActivity(new Intent(getApplicationContext(), AdminListCategoryActivity.class));
                        break;
                    case R.id.menu_admin_product:
                        startActivity(new Intent(getApplicationContext(), ListProductAdminActivity.class));
                        break;
                    case R.id.menu_admin_updateinfor: {
                        Intent intent = new Intent(getApplicationContext(), UpdateInforActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("updateUser", cUserAdmin);
                        intent.putExtra("dataUpdateUser", bundle);
                        startActivity(intent);
                        break;
                    }
                    case R.id.menu_admin_higtlight_product:
                    {
                        Intent intent = new Intent(getApplicationContext(), HighLightProductActivity.class);
                        startActivity(intent);
                        break;
                    }
                    case R.id.menu_admin_infor_account:
                    {
                        Intent intent = new Intent(getApplicationContext(), InformationAccountActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("cUserAdmin",cUserAdmin);
                        intent.putExtra("datacUserAdmin", bundle);
                        startActivity(intent);
                        break;
                    }

                    case R.id.menu_admin_change_pass:
                        startActivity(new Intent(getApplicationContext(), ChangePassActivity.class));
                        break;
                    case R.id.menu_admin_delete_account:
                        ShowDialogDelete(cUserAdmin);
                        break;
                    case R.id.menu_admin_logout:
                        ShowDialogLogout();
                        break;
                }
                return false;
            }
        });
    }

    // Xác nhận Đăng xuất
    private void ShowDialogLogout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.logout);
        builder.setMessage(R.string.logout_conform);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), MainActivity.class)); //Cho ra đảo
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        builder.create().show();

    }


    // Xác nhận Delete Account
    private void ShowDialogDelete(final CUser cUser1) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.delete_account);
        builder.setMessage(R.string.danger);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DeleteAccount(cUser1);
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.create().show();
    }

    // Xóa tài khoản
    private void DeleteAccount(CUser cUser1) {
        // Xóa trên Firebase
        mDatabase.child("User").child(cUser1.getID()).removeValue();
        // Xóa trên Authentication
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        user.delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), R.string.delete_account_success, Toast.LENGTH_SHORT).show();
                            FirebaseAuth.getInstance().signOut();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class)); // Cho ra đảo luôn
                        } else {
                            Toast.makeText(getApplicationContext(), R.string.delete_account_fail, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}
