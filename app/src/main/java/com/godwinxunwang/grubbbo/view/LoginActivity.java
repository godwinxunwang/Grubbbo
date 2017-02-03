package com.godwinxunwang.grubbbo.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.JsonSyntaxException;
import com.godwinxunwang.grubbbo.R;
import com.godwinxunwang.grubbbo.dribbble.Dribbble;
import com.godwinxunwang.grubbbo.dribbble.auth.Auth;
import com.godwinxunwang.grubbbo.dribbble.auth.AuthActivity;
import com.godwinxunwang.grubbbo.view.SignUpActivity;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {
    // @BindView(R.id.activity_login_btn) TextView loginBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        TextView loginBtn = (TextView) findViewById(R.id.login_btn);
        TextView signUpBtn = (TextView) findViewById(R.id.sign_up_btn);
        // load access token from shared preference
        Dribbble.init(this);

        if (!Dribbble.isLoggedIn()) {
            loginBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Auth.openAuthActivity(LoginActivity.this);
                }
            });

            signUpBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://dribbble.com/signup"));
                    startActivity(intent);
                }
            });

        } else {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Auth.REQ_CODE && resultCode == RESULT_OK) {
            final String authCode = data.getStringExtra(AuthActivity.KEY_CODE);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        // this is a network call and it's time consuming
                        // that's why we're doing this in a non-UI thread
                        String token = Auth.fetchAccessToken(authCode);

                        // store access token in SharedPreferences
                        Dribbble.login(LoginActivity.this, token);

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } catch (IOException | JsonSyntaxException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
}
