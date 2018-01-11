package com.hcmus.yennhi0105.usedadvance.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.hcmus.yennhi0105.usedadvance.R;

public class MainActivity extends AppCompatActivity {

    private static String TAG = "Main---";

    private FirebaseAuth mAuth;

    EditText edtUser, edtPass;
    ImageButton ibtnLogin, ibtnSignIn, ibtnLoginGoogle;
    TextView txtForgetPass;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        AddControl();
        sharedPreferences = getSharedPreferences("Data", MODE_PRIVATE);

        edtUser.setText(sharedPreferences.getString("tendangnhap", ""));
        edtPass.setText(sharedPreferences.getString("matkhau", ""));

        Typeface typefaceUsername = Typeface.createFromAsset(getAssets(), "fonts/Lemon-Regular.ttf");
        edtUser.setTypeface(typefaceUsername);
        edtPass.setTypeface(typefaceUsername);

        Typeface typefaceForgetPass = Typeface.createFromAsset(getAssets(), "fonts/VNF-Sofia Regular.ttf");
        txtForgetPass.setTypeface(typefaceForgetPass);

        final Animation animation = AnimationUtils.loadAnimation(this, R.anim.amin_button_login);
        ibtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(animation);
                Login();
            }
        });

        ibtnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(animation);
                SignIn();
            }
        });

        txtForgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDialogResetPassword();
            }
        });
    }

    // Hiện Dialog Reset Password
    private void ShowDialogResetPassword() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_reset_password);
        dialog.setTitle(R.string.reset_pass);
        dialog.show();
        final EditText edtInput = (EditText) dialog.findViewById(R.id.editTextEmailResetPass);
        Button btnSubmit = (Button) dialog.findViewById(R.id.buttonResetPass);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String newValue = edtInput.getText().toString().trim();
                if (newValue.equals("")) {
                    Toast.makeText(getApplicationContext(), R.string.input_empty, Toast.LENGTH_SHORT).show();
                } else {
                    FirebaseAuth auth = FirebaseAuth.getInstance();

                    auth.sendPasswordResetEmail(newValue)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(MainActivity.this, R.string.check_email, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
    }

    // Login
    private void Login() {
        final String email = edtUser.getText().toString().trim();
        final String pass = edtPass.getText().toString();
        if (email.equals("") || pass.equals("")) {
            Toast.makeText(this, R.string.empty_email_pass, Toast.LENGTH_SHORT).show();
        } else {
            mAuth.signInWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Lưu lại để lần sau đăng nhập

                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("tendangnhap", email);
                                editor.putString("matkhau", pass);
                                editor.commit();

                                Toast.makeText(MainActivity.this, "Login Success!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(MainActivity.this, UserActivity.class);
                                intent.putExtra("password", pass);
                                startActivity(intent);
                            } else {
                                Toast.makeText(MainActivity.this, "Login Fail!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    // Sign In
    private void SignIn() {

        startActivity(new Intent(MainActivity.this, SignInActivity.class));
    }

    private void AddControl() {

        ibtnLogin = (ImageButton) findViewById(R.id.imageButtonLogin);
        //ibtnLoginGoogle = (ImageButton) findViewById(R.id.imageButtonLoginGoogle);
        ibtnSignIn = (ImageButton) findViewById(R.id.imageButtonSignIn);

        edtPass = (EditText) findViewById(R.id.editTextPassword);
        edtUser = (EditText) findViewById(R.id.editTextUsername);

        txtForgetPass = (TextView) findViewById(R.id.textViewForgetPass);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
