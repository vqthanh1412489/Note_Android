package com.hcmus.yennhi0105.usedadvance.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hcmus.yennhi0105.usedadvance.R;

public class ChangePassActivity extends AppCompatActivity {

    ImageButton ibtnChangePass;
    EditText edtNewPass, edtRetype;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass);

        AddControl();

        MobileAds.initialize(getApplicationContext(), "ca-app-pub-6317808399413254~2123658523");
        AdView mAdView = (AdView) findViewById(R.id.adViewChangePass);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        ibtnChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String _pass = edtNewPass.getText().toString();
                String _retype = edtRetype.getText().toString();

                if (!_pass.contains(_retype)) {
                    Toast.makeText(ChangePassActivity.this, R.string.password_not_match, Toast.LENGTH_SHORT).show();
                } else {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    user.updatePassword(_pass)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(ChangePassActivity.this, R.string.change_pass_success, Toast.LENGTH_SHORT).show();
                                        FirebaseAuth.getInstance().signOut();
                                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                    } else {
                                        Toast.makeText(ChangePassActivity.this, R.string.change_pass_fail, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
    }

    private void AddControl() {

        ibtnChangePass = (ImageButton) findViewById(R.id.imageButtonChangePass);
        edtNewPass = (EditText) findViewById(R.id.editTextPassChange);
        edtRetype = (EditText) findViewById(R.id.editTextRetypePassChange);

    }
}
