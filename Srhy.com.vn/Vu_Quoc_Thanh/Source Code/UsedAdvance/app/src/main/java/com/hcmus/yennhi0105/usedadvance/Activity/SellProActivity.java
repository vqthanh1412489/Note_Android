package com.hcmus.yennhi0105.usedadvance.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hcmus.yennhi0105.usedadvance.Adapter.ViewPagerAdapter;
import com.hcmus.yennhi0105.usedadvance.CClass.CProduct;
import com.hcmus.yennhi0105.usedadvance.CClass.CUser;
import com.hcmus.yennhi0105.usedadvance.Fragment.fragment_1;
import com.hcmus.yennhi0105.usedadvance.Fragment.fragment_2;
import com.hcmus.yennhi0105.usedadvance.Fragment.fragment_3;
import com.hcmus.yennhi0105.usedadvance.Fragment.fragment_4;
import com.hcmus.yennhi0105.usedadvance.R;
import com.squareup.picasso.Picasso;

public class SellProActivity extends BaseMemberActivity implements View.OnClickListener{

    private static String TAG = "SellPro111";
    TabLayout tabLayout;
    ViewPager viewPager;

    TextView txtTitle, txtPrice, txtType, txtZone, txtCate, txtStatus, txtAddress, txtNameUser;
    ImageView imgIcon;

    String linkFB = "";
    String phone = "";
    String email = "";
    String address = "";
    String iconUser = "";
    CProduct product;

    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_pro);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        AddControl();


        // Nhân tùy theo màn hình. Nếu là từ màn hình sản phẩm hoặc từ màn hình Loại sản phẩm
        Bundle myBundlePro = getIntent().getBundleExtra("dataproduct");
        if (myBundlePro != null) {
            product = (CProduct) myBundlePro.getSerializable("product");
        }
        Bundle myBundleProCate = getIntent().getBundleExtra("dataproduct_cate");
        if (myBundleProCate != null) {
            product = (CProduct) myBundleProCate.getSerializable("product_cate");
        }


        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        fragment_1 fragment1 = new fragment_1();
        fragment_2 fragment2 = new fragment_2();
        fragment_3 fragment3 = new fragment_3();
        fragment_4 fragment4 = new fragment_4();
        switch (product.getArrayIcon().size()) {
            case 1:
                viewPagerAdapter.addFragment(fragment1);
                break;
            case 2:
                viewPagerAdapter.addFragment(fragment1);
                viewPagerAdapter.addFragment(fragment2);
                break;
            case 3:
                viewPagerAdapter.addFragment(fragment1);
                viewPagerAdapter.addFragment(fragment2);
                viewPagerAdapter.addFragment(fragment3);
                break;
            case 4: case 5: case 6:case 7:case 8:case 9:case 10:
                viewPagerAdapter.addFragment(fragment1);
                viewPagerAdapter.addFragment(fragment2);
                viewPagerAdapter.addFragment(fragment3);
                viewPagerAdapter.addFragment(fragment4);
                break;

        }
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("imageArray", product.getArrayIcon());
        fragment1.setArguments(bundle);
        fragment2.setArguments(bundle);
        fragment3.setArguments(bundle);
        fragment4.setArguments(bundle);

        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager); // Hiện tabLayout lên cho biết chọn tới cái nào nhé!

        // Lấy dữ liệu của người bán

        mDatabase.child("User").child(product.getUser()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                CUser cUser = dataSnapshot.getValue(CUser.class);

                txtNameUser.setText(cUser.getName());
                Picasso.with(SellProActivity.this).load(cUser.getAvartar()).into(imgIcon);
                linkFB = cUser.getFacebook();
                email = cUser.getEmail();
                phone = cUser.getPhone();
                txtAddress.setText(cUser.getAddress());
                iconUser = cUser.getAvartar();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        txtTitle.setText(product.getTitle());
        txtPrice.setText(String.valueOf(product.getPrice()) + " VND");
        switch (product.getType()) {
            case 1:
                txtType.setText("Cần bán"); break;
            case 2:
                txtType.setText("Cần mua"); break;
            case 3:
                txtType.setText("Cần cho"); break;
        }

        Log.d(TAG, "address" + address);
        txtZone.setText(product.getZone());
        txtCate.setText(product.getCategory());
        txtAddress.setText(address);

        switch (product.getStatus()) {
            case 1: txtStatus.setText("Mới");break;
            case 2: txtStatus.setText("Đã sử dụng");break;
        }

    }

    private void AddControl() {
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);

        txtAddress = (TextView) findViewById(R.id.textViewAddressSEll);
        txtCate = (TextView) findViewById(R.id.textViewCateSell);
        txtZone = (TextView) findViewById(R.id.textViewZoneSell);
        txtType = (TextView) findViewById(R.id.textViewTypeSell);
        txtNameUser = (TextView) findViewById(R.id.textViewNameUserSell);
        txtStatus = (TextView) findViewById(R.id.textViewStatusSell);
        txtPrice = (TextView) findViewById(R.id.textViewPriceSell);
        txtTitle = (TextView) findViewById(R.id.textViewTitleSell);

        imgIcon = (ImageView) findViewById(R.id.imageViewIconUserSell);
        findViewById(R.id.imageButtonFacebook).setOnClickListener(this);
        findViewById(R.id.imageButtonGmail).setOnClickListener(this);
        findViewById(R.id.imageButtonSMS).setOnClickListener(this);
        findViewById(R.id.imageButtonCall).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageButtonFacebook: {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(linkFB));
                    startActivity(intent);
                    break;
                }

            case R.id.imageButtonGmail: {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_EMAIL, "vqthanh1412489@gmail.com"); // Đại chỉ người gửi
                    intent.putExtra(Intent.EXTRA_SUBJECT, "[Mua đồ trên Srhy.com.vn]"); // Tiêu đề
                    intent.putExtra(Intent.EXTRA_TEXT, "Tôi muốn mua " + txtTitle.getText().toString() + " của bạn trên Srhy.com.vn");  // Nội dung
                    startActivity(Intent.createChooser(intent, "Send Email"));
                    break;
                }

            case R.id.imageButtonSMS:{
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SENDTO);
                intent.putExtra("sms_body","Tôi muốn mua " + txtTitle.getText().toString() + " của bạn trên Srhy.com.vn");
                intent.setData(Uri.parse("sms:'"+phone+"'"));
                startActivity(intent);
                break;
            }


            case R.id.imageButtonCall:{
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:'"+phone+"'"));
                startActivity(intent);
                break;
            }
        }
    }
}
