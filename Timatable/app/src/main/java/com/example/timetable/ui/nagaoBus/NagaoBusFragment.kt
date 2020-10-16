package com.example.timetable.ui.nagaoBus

import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.timetable.R

class NagaoBusFragment : Fragment() {

  private lateinit var nagaoBusViewModel: NagaoBusViewModel

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    nagaoBusViewModel =
    ViewModelProviders.of(this).get(NagaoBusViewModel::class.java)
    val root = inflater.inflate(R.layout.fragment_nagao_bus, container, false)

    val webView = root.findViewById<WebView>(R.id.nagaoWebView)
    webView.webViewClient = WebViewClient();
    webView.settings.javaScriptEnabled = true

    val progressDialog = ProgressDialog(root.context);
    progressDialog.setMessage("Loading...");
    progressDialog.show();

    webView.loadUrl("https://busnavi.keihanbus.jp/pc/diagrampoledtl?mode=1&fr=%E9%95%B7%E5%B0%BE%E9%A7%85&frsk=B&tosk=&dt=202010162211&dgmpl=%E9%95%B7%E5%B0%BE%E9%A7%85%E3%80%94%E4%BA%AC%E9%98%AA%E3%83%90%E3%82%B9%E3%80%95%3A1%3A3&p=0%2C8%2C10&qry=")

    webView.webViewClient = object : WebViewClient() {
      override fun onPageFinished(view: WebView, url: String) {
        progressDialog.dismiss()
      }
    }
    return root
  }
}