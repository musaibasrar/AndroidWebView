package org.ideoholic.apnabazar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    private WebView webView;
    ProgressDialog prDialog;
    private SwipeRefreshLayout finalMySwipeRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        String url = "https://apnabazar.online/";

        webView = (WebView) findViewById(R.id.webview);
        webView.setWebViewClient(new MyWebViewClient());
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        //webSettings.setCacheMode(webSettings.LOAD_CACHE_ELSE_NETWORK);
        webSettings.setAppCacheEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSettings.setUseWideViewPort(true);
        webSettings.setSavePassword(true);
        webSettings.setSaveFormData(true);
        webSettings.setDatabaseEnabled(true);
        webView.loadUrl(url);


        //SwipeRefreshLayout

        finalMySwipeRefreshLayout = findViewById(R.id.swiperefresh);
        finalMySwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
               // webView.loadUrl(webView.getUrl());
                webView.reload();
            }
        });
    }

    @Override
    public void onBackPressed() {

        if(webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }


    private class MyWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            view.loadUrl(request.getUrl().toString());
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            prDialog = new ProgressDialog(MainActivity.this);
            prDialog.setMessage("Apna Hai Tho Bharosa Hai");
            prDialog.show();
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if(prDialog!=null){
                prDialog.dismiss();
            }
            finalMySwipeRefreshLayout.setRefreshing(false);
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            try {
                webView.stopLoading();
            } catch (Exception e) {
            }

            if (webView.canGoBack()) {
                webView.goBack();
            }

            //webView.loadUrl("about:blank");
            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
            alertDialog.setTitle("Oops!");
            alertDialog.setMessage("We are not able to connect.Please check your internet connection.");
            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Try Again", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                    startActivity(getIntent());
                }
            });

            alertDialog.show();
            super.onReceivedError(webView, error.getErrorCode(), error.getDescription().toString(), request.getUrl().toString());
        }

    }
}