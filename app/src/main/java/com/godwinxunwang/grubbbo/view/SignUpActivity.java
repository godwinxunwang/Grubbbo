package com.godwinxunwang.grubbbo.view;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.webkit.WebView;

import com.godwinxunwang.grubbbo.R;


public class SignUpActivity extends Activity {
    private WebView webView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.signUpWebview);

        webView = (WebView) findViewById(R.id.signUpWebview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("http://www.google.com");
    }
}
