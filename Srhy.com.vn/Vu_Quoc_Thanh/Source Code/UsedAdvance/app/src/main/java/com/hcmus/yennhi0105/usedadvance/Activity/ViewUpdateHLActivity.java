package com.hcmus.yennhi0105.usedadvance.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hcmus.yennhi0105.usedadvance.CClass.CHighLightProduct;
import com.hcmus.yennhi0105.usedadvance.R;

public class ViewUpdateHLActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener,
        View.OnClickListener{

    CHighLightProduct hl = new CHighLightProduct();
    private static String API_KEY = "AIzaSyADVNDMwC6qVhJp4WLMaq0yERVEaNfuSx0";
    YouTubePlayerView youTubePlayerView;

    TextView txtName, txtPrice, txtDate, txtDescr, txtAddress, txtStatus, txtCompany;
    ImageButton ibtnGmail, ibtnSMS, ibtnCall;

    int REQUEST_CODE_VIDEO = 2303;

    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_update_hl);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        AddControl();
        Bundle myBundle = getIntent().getBundleExtra("datahighLightProduct");

        if (myBundle != null) {
             hl = (CHighLightProduct) myBundle.getSerializable("highLightProduct");
        }

        // Khởi tạo video để chạy
        youTubePlayerView.initialize(API_KEY, ViewUpdateHLActivity.this);

        // Nhận data
        txtName.setText(hl.getName());
        txtDate.setText(hl.getDataPost());
        txtPrice.setText(String.valueOf(hl.getPrice()));
        txtDescr.setText(hl.getDescription());
        txtDescr.setSelected(true);
        txtCompany.setText(hl.getNameCompany());
        txtAddress.setText(hl.getAddress());
        txtAddress.setSelected(true);

        if (hl.getStatusSell() == 1) {
            txtStatus.setText(R.string.private_1);
        } else {
            txtStatus.setText(R.string.public_1);
        }

        ibtnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + hl.getPhone()));
                startActivity(intent);
            }
        });

        ibtnGmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, "Mua hàng trên Srhy"); // Tiêu đề
                intent.putExtra(Intent.EXTRA_TEXT, "Tôi muốn mua " + hl.getName() + " trên Srhy của bạn!");  // Nội dung
                startActivity(Intent.createChooser(intent, "Send Email"));
            }
        });

        ibtnSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SENDTO);
                intent.putExtra("sms_body", "Tôi muốn mua " + hl.getName() + " trên Srhy của bạn!"); // Nội dung
                intent.setData(Uri.parse("sms:" + hl.getPhone())); // Số điện thoại nhận
                startActivity(intent);
            }
        });
    }

    private void AddControl() {

        youTubePlayerView = (YouTubePlayerView) findViewById(R.id.youtubePlayerViewHL);
        txtName = (TextView) findViewById(R.id.textViewNameViewHl);
        txtDate = (TextView) findViewById(R.id.textViewDateViewHl);
        txtPrice = (TextView) findViewById(R.id.textViewPriceViewHL);
        txtDescr = (TextView) findViewById(R.id.textViewDescrViewHL);
        txtAddress = (TextView) findViewById(R.id.textViewAddressViewHl);
        txtCompany = (TextView) findViewById(R.id.textViewCompanyViewHl);
        txtStatus = (TextView) findViewById(R.id.textViewStatusViewHL);

        ibtnCall = (ImageButton) findViewById(R.id.imageButtonCallHL);
        ibtnSMS = (ImageButton) findViewById(R.id.imageButtonSMSHL);
        ibtnGmail = (ImageButton) findViewById(R.id.imageButtonGmailHL);

        findViewById(R.id.imageButtonEditLinkHL).setOnClickListener(this);
        findViewById(R.id.imageButtonEditNameHL).setOnClickListener(this);
        findViewById(R.id.imageButtonEditPriceHL).setOnClickListener(this);
        findViewById(R.id.imageButtonEditAddressHL).setOnClickListener(this);
        findViewById(R.id.imageButtonEditStatusHL).setOnClickListener(this);


    }

    //Kết nối thành công
    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        youTubePlayer.cueVideo(hl.getLinkVideo());
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        if (youTubeInitializationResult.isUserRecoverableError()) { // Trả về true nếu là lỗi người dùng
            // Thông báo lỗi
            youTubeInitializationResult.getErrorDialog(ViewUpdateHLActivity.this, REQUEST_CODE_VIDEO);
        } else { // Do video bị lỗi
            Toast.makeText(this, "Video Error!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_VIDEO) {
            // -> Thử khởi tạo lại video!
            youTubePlayerView.initialize(API_KEY, ViewUpdateHLActivity.this); // Key API, Do đã truyền ở trên rồi
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageButtonEditLinkHL:
            {
                ShowDialogChangeLinkHL();
                break;
            }
            case R.id.imageButtonEditNameHL:
            {
                ShowDialogChangeNameHL();
                break;
            }
            case R.id.imageButtonEditPriceHL:
            {
                ShowDialogChangePriceHL();
                break;
            }
            case R.id.imageButtonEditStatusHL:
            {
                //ShowDialogChangeStatusHL();
                break;
            }
            case R.id.imageButtonEditAddressHL:
            {
                ShowDialogChangeAddressHL();
                break;
            }
        }
    }

    private void ShowDialogChangeAddressHL() {

        final Dialog dialog = new Dialog(ViewUpdateHLActivity.this);
        dialog.setTitle(R.string.change_address_hl);
        dialog.setContentView(R.layout.dialog_newvalue);
        dialog.show();

        final EditText edtInput = (EditText) dialog.findViewById(R.id.editTextNewValuesHL);
        ImageButton ibtnOk = (ImageButton) dialog.findViewById(R.id.imageButtonChangeNewValueHL);

        ibtnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String newValue = edtInput.getText().toString().trim();
                if (newValue.equals("")) {
                    Toast.makeText(getApplicationContext(), R.string.input_empty, Toast.LENGTH_SHORT).show();
                }else if (newValue.length() < 15){
                    Toast.makeText(ViewUpdateHLActivity.this, R.string.sort_data, Toast.LENGTH_SHORT).show();
                }else {
                    hl.setAddress(newValue); // Đặt giá trị mới cho sản phẩm hl
                    mDatabase.child("ProHL").child(hl.getID()).setValue(hl, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            if (databaseError == null) {
                                Toast.makeText(ViewUpdateHLActivity.this, R.string.success, Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            } else {
                                Toast.makeText(ViewUpdateHLActivity.this, R.string.fail, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    private void ShowDialogChangePriceHL() {

        final Dialog dialog = new Dialog(ViewUpdateHLActivity.this);
        dialog.setTitle(R.string.change_price_hl);
        dialog.setContentView(R.layout.dialog_newvalue);
        dialog.show();

        final EditText edtInput = (EditText) dialog.findViewById(R.id.editTextNewValuesHL);
        ImageButton ibtnOk = (ImageButton) dialog.findViewById(R.id.imageButtonChangeNewValueHL);

        ibtnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String newValue = edtInput.getText().toString().trim();
                if (newValue.equals("")) {
                    Toast.makeText(getApplicationContext(), R.string.input_empty, Toast.LENGTH_SHORT).show();
                }else {
                    hl.setPrice(Integer.parseInt(newValue)); // Đặt giá trị mới cho sản phẩm hl
                    mDatabase.child("ProHL").child(hl.getID()).setValue(hl, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            if (databaseError == null) {
                                Toast.makeText(ViewUpdateHLActivity.this, R.string.success, Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            } else {
                                Toast.makeText(ViewUpdateHLActivity.this, R.string.fail, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    private void ShowDialogChangeNameHL() {
        final Dialog dialog = new Dialog(ViewUpdateHLActivity.this);
        dialog.setTitle(R.string.change_name_hl);
        dialog.setContentView(R.layout.dialog_newvalue);
        dialog.show();

        final EditText edtInput = (EditText) dialog.findViewById(R.id.editTextNewValuesHL);
        ImageButton ibtnOk = (ImageButton) dialog.findViewById(R.id.imageButtonChangeNewValueHL);

        ibtnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String newValue = edtInput.getText().toString().trim();
                if (newValue.equals("")) {
                    Toast.makeText(getApplicationContext(), R.string.input_empty, Toast.LENGTH_SHORT).show();
                }else if (newValue.length() < 5){
                    Toast.makeText(ViewUpdateHLActivity.this, R.string.sort_data, Toast.LENGTH_SHORT).show();
                }else {
                    hl.setName(newValue); // Đặt giá trị mới cho sản phẩm hl
                    mDatabase.child("ProHL").child(hl.getID()).setValue(hl, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            if (databaseError == null) {
                                Toast.makeText(ViewUpdateHLActivity.this, R.string.success, Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            } else {
                                Toast.makeText(ViewUpdateHLActivity.this, R.string.fail, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    private void ShowDialogChangeLinkHL() {

        final Dialog dialog = new Dialog(ViewUpdateHLActivity.this);
        dialog.setTitle(R.string.change_link_hl);
        dialog.setContentView(R.layout.dialog_newvalue);
        dialog.show();

        final EditText edtInput = (EditText) dialog.findViewById(R.id.editTextNewValuesHL);
        ImageButton ibtnOk = (ImageButton) dialog.findViewById(R.id.imageButtonChangeNewValueHL);

        ibtnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String newValue = edtInput.getText().toString().trim();
                if (newValue.equals("")) {
                    Toast.makeText(getApplicationContext(), R.string.input_empty, Toast.LENGTH_SHORT).show();
                }else if (newValue.length() < 10){
                    Toast.makeText(ViewUpdateHLActivity.this, R.string.sort_data, Toast.LENGTH_SHORT).show();
                }else {
                    hl.setAddress(newValue); // Đặt giá trị mới cho sản phẩm hl
                    mDatabase.child("ProHL").child(hl.getID()).setValue(hl, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            if (databaseError == null) {
                                Toast.makeText(ViewUpdateHLActivity.this, R.string.success, Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            } else {
                                Toast.makeText(ViewUpdateHLActivity.this, R.string.fail, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}
