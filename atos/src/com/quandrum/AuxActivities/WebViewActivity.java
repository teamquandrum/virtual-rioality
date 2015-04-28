package com.quandrum.AuxActivities;

import com.quandrum.Json.FilePathSetter;
import com.quandrum.atos.R;
import com.quandrum.atos.R.id;
import com.quandrum.atos.R.layout;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

/**
 * Accepts 'url' as extra from intent, which is the website to display
 * @author Abraham
 *
 */
public class WebViewActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web_view);

		FilePathSetter.setFilePaths(this);
		
		String url = getIntent().getStringExtra("url");
		WebView wv = (WebView)findViewById(R.id.webview);
		wv.loadUrl(url);
	}
}
