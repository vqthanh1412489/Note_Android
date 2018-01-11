package com.hcmus.yennhi0105.usedadvance.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hcmus.yennhi0105.usedadvance.Adapter.HLRecylerAdapter;
import com.hcmus.yennhi0105.usedadvance.Adapter.NewsAdapter;
import com.hcmus.yennhi0105.usedadvance.Adapter.ViewPagerUserAdapter;
import com.hcmus.yennhi0105.usedadvance.CClass.CHighLightProduct;
import com.hcmus.yennhi0105.usedadvance.CClass.CNews;
import com.hcmus.yennhi0105.usedadvance.CClass.CUser;
import com.hcmus.yennhi0105.usedadvance.Fragment.fragment_1_user;
import com.hcmus.yennhi0105.usedadvance.Fragment.fragment_2_user;
import com.hcmus.yennhi0105.usedadvance.Fragment.fragment_3_user;
import com.hcmus.yennhi0105.usedadvance.R;
import com.hcmus.yennhi0105.usedadvance.RecyclerItemClickListener;
import com.hcmus.yennhi0105.usedadvance.XMLDOMParser;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserActivity extends AppCompatActivity {

    private static String TAG = "User247";

    private DatabaseReference mDatabase;

    DrawerLayout drawerLayout;
    NavigationView navigationViewAdmin, navigationViewMember;
    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;

    TextView txtHL;
    public static CUser cUserAdmin = new CUser();
    public static CUser cUserMember = new CUser();

    private static int REQUEST_CODE_USER = 141;

    ArrayList<CUser> arraycUser;

    RecyclerView recyclerViewHL;
    ArrayList<CHighLightProduct> arrayHL;
    HLRecylerAdapter adapterHL = null;

    RecyclerView recyclerViewNews;
    ArrayList<CNews> arrayNews;
    NewsAdapter adapterNews = null;

    int currentPage = 0;
    int NUM_PAGES = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        AddControl();

        //Khi nào cần thi mang quảng cáo ra
//        MobileAds.initialize(getApplicationContext(), "ca-app-pub-6317808399413254~2123658523");
//        AdView mAdView = (AdView) findViewById(R.id.adViewUser);
//        AdRequest adRequest = new AdRequest.Builder().build();
//        mAdView.loadAd(adRequest);


        Typeface typefaceHLUser = Typeface.createFromAsset(getAssets(), "fonts/UVF HeraBig-Black.ttf");
        txtHL.setTypeface(typefaceHLUser);
        //Đổ Sản phẩm HL xuống mảng
        mDatabase.child("ProHL").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                CHighLightProduct hl = dataSnapshot.getValue(CHighLightProduct.class);

                arrayHL.add(0, new CHighLightProduct(dataSnapshot.getKey(),
                        hl.getName(),
                        hl.getPrice(),
                        hl.getDescription(),
                        hl.getNameCompany(),
                        hl.getPhone(),
                        hl.getEmail(),
                        hl.getAddress(),
                        hl.getDataPost(),
                        hl.getLinkVideo(),
                        hl.getStatusSell())
                );

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
        // Khi đổ xong HL rồi
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Xét cho arrayHL
                if (arrayHL.size() >= NUM_PAGES) {
                    ViewPagerUserAdapter adapter = new ViewPagerUserAdapter(getSupportFragmentManager());
                    fragment_1_user fragment1 = new fragment_1_user();
                    fragment_2_user fragment2 = new fragment_2_user();
                    fragment_3_user fragment3 = new fragment_3_user();

                    adapter.AddTab(fragment1, "TOP 1");
                    adapter.AddTab(fragment2, "TOP 2");
                    adapter.AddTab(fragment3, "TOP 3");

                    // Truyền  qua bên fragment để hiển thị (cả arrayObject luôn nha)
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList("arayHLUser", arrayHL);
                    fragment1.setArguments(bundle);
                    fragment2.setArguments(bundle);
                    fragment3.setArguments(bundle);

                    viewPager.setAdapter(adapter);
                    tabLayout.setupWithViewPager(viewPager);

                    // Auto Chuyển Fragment
                    final Handler handler = new Handler();
                    final Runnable update = new Runnable() {
                        public void run() {
                            if (currentPage == NUM_PAGES) {
                                currentPage = 0;
                            }
                            viewPager.setCurrentItem(currentPage++, true);
                        }
                    };


                    new Timer().schedule(new TimerTask() {

                        @Override
                        public void run() {
                            handler.post(update);
                        }
                    }, 500, 2000); // Thời gian chuyển là 2s nhé!

                    // Đưa Recyler View lên nhé!
                    initView();


                }

                // Bắt sự kiện trên recyclerViewHL
                recyclerViewHL.addOnItemTouchListener(
                        new RecyclerItemClickListener(getApplicationContext(), recyclerViewHL ,new RecyclerItemClickListener.OnItemClickListener() {
                            @Override public void onItemClick(View view, int position) {
                                // TODO: Chuyển qua màn hình chi tiết sản phẩm HL
                                // Chuyển sản phẩm Hl được chọn qua SellHLActivity
                                Intent intent = new Intent(getApplicationContext(), SellHLActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("ProHlUser", arrayHL.get(position));
                                intent.putExtra("dataProHlUser", bundle);
                                startActivity(intent);

                            }

                            @Override public void onLongItemClick(View view, int position) {
                                //TODO : Sử dụng nếu cần cập nhật gì đó :V Giờ thì chưa cần nhé!
                            }
                        })
                );
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        //user trên Authentication
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Log.d(TAG, "User Authen:" + user.getEmail());
        // Bắt xác nhận tài khoản Email
        if (user != null) {
            if (!user.isEmailVerified()) {
                ShowDialogConfirmEmail();
            }
        }

        //Log.d(TAG, "user:" + user.getEmail());
        mDatabase.child("User").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                CUser cUser = dataSnapshot.getValue(CUser.class);
                cUser.setID(dataSnapshot.getKey()); // Gán ID cho User. Tại vì lúc up lên chưa có ID
//                arraycUser.add(new CUser(dataSnapshot.getKey(),
//                        cUser.getUsername(),
//                        cUser.getName(),
//                        cUser.getAddress(),
//                        cUser.getPhone(),
//                        cUser.getFacebook(),
//                        cUser.getEmail(),
//                        cUser.getTypeAccount(),
//                        cUser.getAvartar()));

//                if (arraycUser.size() > 0) {
//                    Log.d(TAG, "size:" + arraycUser.size());
//                    for (int i = 0 ; i < arraycUser.size(); i++) {
//                        if (arraycUser.get(i).getUsername().equals(user.getEmail()) && arraycUser.get(i).getTypeAccount() == 1) {
//                            Log.d(TAG, "Tai khoan ad: " + arraycUser.get(i).getUsername());
//                            cUserMember = arraycUser.get(i);
//                        }
//                        if (arraycUser.get(i).getUsername().equals(user.getEmail()) && arraycUser.get(i).getTypeAccount() == 2) {
//                            Log.d(TAG, "Tai khoan mem: " + arraycUser.get(i).getUsername());
//                            cUserAdmin = arraycUser.get(i);
//                        }
//                    }
//                }
                // Nếu là tài khoản Admin
                if (cUser.getUsername().equals(user.getEmail()) && cUser.getTypeAccount() == 1) {
                    //Log.d(TAG, "Tai khoan ad: " + cUser.getUsername());
                    cUserAdmin = cUser;
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED); // Không cho Navigation chạy 2 bên. Khi nhấn vào Home mới chạy ra
                    //Toast.makeText(UserActivity.this, "Admin", Toast.LENGTH_SHORT).show();
                    setSupportActionBar(toolbar);
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    toolbar.setNavigationIcon(R.drawable.menu);
                    navigationViewAdmin.setItemIconTintList(null);
                    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            drawerLayout.openDrawer(Gravity.START);
                        }
                    });

                    // Disible UpdateInformation -> Cập nhật ở phiên bản sau
                    Menu menuNavi = navigationViewAdmin.getMenu();
                    MenuItem menuItem = menuNavi.findItem(R.id.menu_admin_updateinfor);
                    menuItem.setEnabled(false);

                    navigationViewAdmin.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                        @Override
                        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.menu_admin_category:
                                {
                                    drawerLayout.closeDrawer(Gravity.START);
                                    startActivity(new Intent(UserActivity.this, AdminListCategoryActivity.class));
                                    break;
                                }

                                case R.id.menu_admin_product:
                                {
                                    startActivity(new Intent(UserActivity.this, ListProductAdminActivity.class));
                                    drawerLayout.closeDrawer(Gravity.START);
                                    break;
                                }

                                case R.id.menu_admin_updateinfor:
                                {
                                    Intent intent = new Intent(getApplicationContext(), UpdateInforActivity.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("updateUser", cUserAdmin);
                                    intent.putExtra("dataUpdateUser", bundle);
                                    startActivity(intent);
                                    drawerLayout.closeDrawer(Gravity.START);
                                    break;
                                }
                                case R.id.menu_admin_higtlight_product:
                                {
                                    drawerLayout.closeDrawer(Gravity.START);
                                    Intent intent = new Intent(getApplicationContext(), HighLightProductActivity.class);
                                    startActivity(intent);
                                    break;
                                }
                                case R.id.menu_admin_infor_account:
                                {
                                    drawerLayout.closeDrawer(Gravity.START);
                                    Intent intent = new Intent(getApplicationContext(), InformationAccountActivity.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("cUserAdmin",cUserAdmin);
                                    intent.putExtra("datacUserAdmin", bundle);
                                    startActivity(intent);
                                    break;
                                }

                                case R.id.menu_admin_change_pass:
                                {
                                    drawerLayout.closeDrawer(Gravity.START);
                                    startActivity(new Intent(UserActivity.this, ChangePassActivity.class));
                                    break;
                                }

                                case R.id.menu_admin_delete_account:
                                {
                                    drawerLayout.closeDrawer(Gravity.START);
                                    ShowDialogDelete(cUserAdmin);
                                    break;
                                }

                                case R.id.menu_admin_logout:
                                {
                                    drawerLayout.closeDrawer(Gravity.START);
                                    ShowDialogLogout();
                                    break;
                                }
                            }
                            return false;
                        }
                    });
                }

                // Nếu là tài khoản Member
                if (cUser.getUsername().equals(user.getEmail()) && cUser.getTypeAccount() == 2) {
                    //Log.d(TAG, "Tai khoan mem: " + cUser.getUsername());
                    cUserMember = cUser;

                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED); // Lock navigation
                    //Toast.makeText(UserActivity.this, "Member", Toast.LENGTH_SHORT).show();

                    setSupportActionBar(toolbar);
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    toolbar.setNavigationIcon(R.drawable.menu);
                    navigationViewMember.setItemIconTintList(null);

                    // Ẩn cái xóa tài khoản đi. Ahihi.. Khi nào cần thì cho xóa.
                    // (nếu cho người dùng xóa tự do thì sẽ mất thông tin sản phẩm của họ đang bán)
                    Menu menuNavi = navigationViewMember.getMenu();
                    MenuItem menuItem = menuNavi.findItem(R.id.menu_menber_delete_account);
                    menuItem.setEnabled(false);

                    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            drawerLayout.openDrawer(Gravity.END);
                        }
                    });

                    navigationViewMember.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                        @Override
                        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.menu_member_addpro:
                                {
                                    drawerLayout.closeDrawer(Gravity.END);
                                    startActivity(new Intent(UserActivity.this, AddProductActivity.class));
                                    break;
                                }
                                case R.id.menu_menber_allProduct:
                                {
                                    drawerLayout.closeDrawer(Gravity.END);
                                    startActivity(new Intent(UserActivity.this, ListProductActivity.class));
                                    break;
                                }
                                case R.id.menu_menber_sellProduct:
                                {
                                    drawerLayout.closeDrawer(Gravity.END);
                                    startActivity(new Intent(UserActivity.this, SellingMemberActivity.class));
                                    break;
                                }
                                case R.id.menu_member_category:
                                {
                                    drawerLayout.closeDrawer(Gravity.END);
                                    startActivity(new Intent(UserActivity.this, ListCategoryActivity.class));
                                    break;
                                }

                                case R.id.menu_menber_infor:
                                {
                                    drawerLayout.closeDrawer(Gravity.END);
                                    Intent intent = new Intent(getApplicationContext(), InformationAccountActivity.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("cUserMember", cUserMember);
                                    intent.putExtra("datacUserMember", bundle);
                                    startActivity(intent);
                                    break;
                                }

                                case R.id.menu_menber_changePass:
                                {
                                    drawerLayout.closeDrawer(Gravity.END);
                                    startActivity(new Intent(UserActivity.this, ChangePassActivity.class));
                                    break;
                                }

                                case R.id.menu_menber_delete_account:
                                {
                                    drawerLayout.closeDrawer(Gravity.END);
                                    ShowDialogDelete(cUserMember);
                                    break;
                                }

                                case R.id.menu_menber_logout:
                                {
                                    drawerLayout.closeDrawer(Gravity.END);
                                    ShowDialogLogout();
                                    break;
                                }
                            }
                            return false;
                        }
                    });
                }
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



        // Đọc tin tức VnExpress
        initViewNew();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new ReadURL().execute("http://vnexpress.net/rss/the-thao.rss");
            }
        });

        //Bắt sự kiện recy News

        recyclerViewNews.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), recyclerViewNews ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        // TODO: Chuyển qua màn hình Webview
                        // Chuyển link qua Webview
                        Intent intent = new Intent(getApplicationContext(), NewsActivity.class);
                        intent.putExtra("linkNews", arrayNews.get(position).getLink());
                        startActivity(intent);
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        //TODO : Sử dụng nếu cần cập nhật gì đó :V Giờ thì chưa cần nhé!
                    }
                })
        );

    }

    //Recy
    private void initView() {
        //recyclerViewHL.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewHL.setLayoutManager(linearLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerViewHL.getContext(), DividerItemDecoration.HORIZONTAL);
        Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ct_divider); // Custom Divider
        dividerItemDecoration.setDrawable(drawable);

        recyclerViewHL.addItemDecoration(dividerItemDecoration); // Add effect default remove, Add , Update......
        recyclerViewHL.setItemAnimator(new DefaultItemAnimator()); //

        adapterHL = new HLRecylerAdapter(getApplicationContext(), arrayHL); // Theo thông sô bên kia nhé! Tùy vào hàm constructor
        recyclerViewHL.setAdapter(adapterHL);
    }

    // RecylerView New

    private void initViewNew() {
        //recyclerViewHL.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewNews.setLayoutManager(linearLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerViewNews.getContext(), DividerItemDecoration.HORIZONTAL);
        Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ct_divider); // Custom Divider
        dividerItemDecoration.setDrawable(drawable);

        recyclerViewNews.addItemDecoration(dividerItemDecoration); // Add effect default remove, Add , Update......
        recyclerViewNews.setItemAnimator(new DefaultItemAnimator()); //

        adapterNews = new NewsAdapter(getApplicationContext(), arrayNews); // Theo thông sô bên kia nhé! Tùy vào hàm constructor
        recyclerViewNews.setAdapter(adapterNews);
    }

    // Hàm hỗ trợ load tin từ VNEXpress
    private class ReadURL extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            return docNoiDung_Tu_URL(strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            XMLDOMParser parser = new XMLDOMParser();
            Document document = parser.getDocument(s);
            NodeList nodeList = document.getElementsByTagName("item");
            NodeList nodeListDescription = document.getElementsByTagName("description");


            for (int i = 0; i < nodeList.getLength(); i++) {

                CNews news = new CNews();
                String cData = nodeListDescription.item(i + 1).getTextContent();
                Pattern pattern = Pattern.compile("<img[^>]+src\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>");
                Matcher matcher = pattern.matcher(cData);
                String picture = "";
                if (matcher.find()) {
                    picture = matcher.group(1);
                    //Log.d("image", picture);
                }

                Element element = (Element) nodeList.item(i);

                news.setTitle(parser.getValue(element, "title"));
                news.setLink(parser.getValue(element, "link"));
                news.setPicture(picture);

                arrayNews.add(news);
            }

            adapterNews.notifyDataSetChanged();
        }
    }

    private String docNoiDung_Tu_URL(String theURL) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            URL url = new URL(theURL);
            URLConnection urlConnection = url.openConnection();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line + "\n");
            }
            bufferedReader.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
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
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
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
                finish();
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
                            Toast.makeText(UserActivity.this, R.string.delete_account_success, Toast.LENGTH_SHORT).show();
                            FirebaseAuth.getInstance().signOut();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        } else {
                            Toast.makeText(UserActivity.this, R.string.delete_account_fail, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    // Hiển thị Dialog bắt xác nhận Email
    public void ShowDialogConfirmEmail() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.not_conform_account);
        builder.setTitle(R.string.conform_account);
        builder.setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                user.sendEmailVerification()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(UserActivity.this, R.string.check_email, Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(UserActivity.this, R.string.erorr, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        });
        builder.create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_USER && resultCode == RESULT_OK && data != null) {

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            String _name = data.getStringExtra("userDisplayName");
            String _photoUrl = data.getStringExtra("userPhotoUri");

            // setDisplayName và setPhotoUri
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(_name)
                    .setPhotoUri(Uri.parse(_photoUrl))
                    .build();
            user.updateProfile(profileUpdates)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "User profile updated.");
                            } else {
                                Log.d(TAG, "User update Error");
                            }
                        }
                    });
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void AddControl() {
        txtHL = (TextView) findViewById(R.id.textViewHLProUser);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        toolbar = (Toolbar) findViewById(R.id.toolBarUser);
        navigationViewAdmin = (NavigationView) findViewById(R.id.navigationViewAdmin);
        navigationViewMember = (NavigationView) findViewById(R.id.navigationView_menber);

        tabLayout = (TabLayout) findViewById(R.id.tabLayoutUser);
        viewPager = (ViewPager) findViewById(R.id.viewPagerUser);

        recyclerViewHL = (RecyclerView) findViewById(R.id.recylerViewUser);
        arraycUser = new ArrayList<CUser>();
        arrayHL = new ArrayList<CHighLightProduct>();

        recyclerViewNews = (RecyclerView) findViewById(R.id.recylerViewNewsUser);
        arrayNews = new ArrayList<CNews>();
        arrayNews = new ArrayList<CNews>();
    }
}
