package com.example.anuj_ilm.connect_2;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class AttachmentWebViewFragment extends Fragment {

    private WebView attachmentWebView ;

    public AttachmentWebViewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        return inflater.inflate(R.layout.fragment_attachment_web_view, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        Bundle args = getArguments() ;
        attachmentWebView = (WebView) getView().findViewById(R.id.attachmentWebView);
        attachmentWebView.getSettings().setJavaScriptEnabled(true);
        attachmentWebView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });
        attachmentWebView.loadUrl(args.getString("url"));
        Toast.makeText(getContext(), "Loading...", Toast.LENGTH_SHORT).show();

    }
}
