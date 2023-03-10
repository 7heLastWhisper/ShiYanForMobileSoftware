package com.jnu.booklistapplication;

import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class WebViewFragment extends Fragment {

    public WebViewFragment() {
        // Required empty public constructor
    }

    public static WebViewFragment newInstance(String param1, String param2) {

        WebViewFragment fragment = new WebViewFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_web_view, container, false);
        WebView webView = rootView.findViewById(R.id.wv_web_content);
        webView.loadUrl("https://news.sina.cn/");
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        return rootView;
    }
}