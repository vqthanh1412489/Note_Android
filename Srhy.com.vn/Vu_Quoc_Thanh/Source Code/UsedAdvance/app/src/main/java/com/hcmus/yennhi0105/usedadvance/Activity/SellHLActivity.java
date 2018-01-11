package com.hcmus.yennhi0105.usedadvance.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hcmus.yennhi0105.usedadvance.CClass.CHighLightProduct;
import com.hcmus.yennhi0105.usedadvance.R;

public class SellHLActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

    CHighLightProduct hl = new CHighLightProduct();
    private static String API_KEY = "AIzaSyADVNDMwC6qVhJp4WLMaq0yERVEaNfuSx0";
    YouTubePlayerView youTubePlayerView;

    TextView txtName, txtPrice, txtDate, txtDescr, txtAddress, txtStatus, txtCompany;
    ImageButton ibtnGmail, ibtnSMS, ibtnCall;

    int REQUEST_CODE_VIDEO = 2403;

    private static String TAG = "SellHL247";

    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_hl);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        AddControl();

        Bundle myBundle = getIntent().getBundleExtra("dataProHlUser");
        if (myBundle != null) {
             hl = (CHighLightProduct) myBundle.getSerializable("ProHlUser");
            // Khởi tạo video để chạy
            youTubePlayerView.initialize(API_KEY, SellHLActivity.this);

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
    }

    private void AddControl() {

        youTubePlayerView = (YouTubePlayerView) findViewById(R.id.youtubePlayerViewHLUser);
        txtName = (TextView) findViewById(R.id.textViewNameViewHlUser);
        txtDate = (TextView) findViewById(R.id.textViewDateViewHlUser);
        txtPrice = (TextView) findViewById(R.id.textViewPriceViewHLUser);
        txtDescr = (TextView) findViewById(R.id.textViewDescrViewHLUser);
        txtAddress = (TextView) findViewById(R.id.textViewAddressViewHlUser);
        txtCompany = (TextView) findViewById(R.id.textViewCompanyViewHlUser);
        txtStatus = (TextView) findViewById(R.id.textViewStatusViewHLUser);

        ibtnCall = (ImageButton) findViewById(R.id.imageButtonCallHLUser);
        ibtnSMS = (ImageButton) findViewById(R.id.imageButtonSMSHLUser);
        ibtnGmail = (ImageButton) findViewById(R.id.imageButtonGmailHLUser);

    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        youTubePlayer.cueVideo(hl.getLinkVideo());
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        if (youTubeInitializationResult.isUserRecoverableError()) { // Trả về true nếu là lỗi người dùng
            // Thông báo lỗi
            youTubeInitializationResult.getErrorDialog(SellHLActivity.this, REQUEST_CODE_VIDEO);
        } else { // Do video bị lỗi
            Toast.makeText(this, "Video Error!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_VIDEO) {
            // -> Thử khởi tạo lại video!
            youTubePlayerView.initialize(API_KEY, SellHLActivity.this); // Key API, Do đã truyền ở trên rồi
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
