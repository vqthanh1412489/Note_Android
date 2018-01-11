package com.hcmus.yennhi0105.usedadvance.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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

import static com.hcmus.yennhi0105.usedadvance.Activity.UserActivity.cUserMember;

// Lớp chưa NavigationView và toolBar Cho member
public class BaseMemberActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_member);

        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public void setContentView(int layoutResID) {

        final DrawerLayout fullView = (DrawerLayout) getLayoutInflater().inflate(R.layout.activity_base_member, null);
        NavigationView navigationViewMember = (NavigationView) fullView.findViewById(R.id.navigationViewForMember);
        FrameLayout activityContainer = (FrameLayout) fullView.findViewById(R.id.activity_content_member);
        getLayoutInflater().inflate(layoutResID, activityContainer, true);
        super.setContentView(fullView);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarMember);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationIcon(R.drawable.menu);
        navigationViewMember.setItemIconTintList(null);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fullView.openDrawer(Gravity.END);
            }
        });

        navigationViewMember.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_member_addpro:
                        startActivity(new Intent(getApplicationContext(), AddProductActivity.class));
                        break;
                    case R.id.menu_menber_allProduct:
                        startActivity(new Intent(getApplicationContext(), ListProductActivity.class));
                        break;
                    case R.id.menu_menber_sellProduct:
                        startActivity(new Intent(getApplicationContext(), SellingMemberActivity.class));
                        break;
                    case R.id.menu_member_category:
                        startActivity(new Intent(getApplicationContext(), ListCategoryActivity.class));
                        break;
                    case R.id.menu_menber_infor:
                    {
                        Intent intent = new Intent(getApplicationContext(), InformationAccountActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("cUserMember", cUserMember);
                        intent.putExtra("datacUserMember", bundle);
                        startActivity(intent);
                        break;
                    }

                    case R.id.menu_menber_changePass:
                        startActivity(new Intent(getApplicationContext(), ChangePassActivity.class));
                        break;
                    case R.id.menu_menber_delete_account:
                        ShowDialogDelete(cUserMember);
                        break;
                    case R.id.menu_menber_logout:
                        ShowDialogLogout();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        break;
                }
                return false;
            }
        });
    }

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
