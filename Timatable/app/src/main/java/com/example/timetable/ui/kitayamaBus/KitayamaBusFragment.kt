package com.example.timetable.ui.kitayamaBus

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

class KitayamaBusFragment : Fragment() {

  private lateinit var kitayamaBusViewModel: KitayamaBusViewModel

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    kitayamaBusViewModel =
    ViewModelProviders.of(this).get(KitayamaBusViewModel::class.java)
    val root = inflater.inflate(R.layout.fragment_kitayama_bus, container, false)

    val webView = root.findViewById<WebView>(R.id.kitayamaWebView)
    webView.webViewClient = WebViewClient();
    webView.settings.javaScriptEnabled = true

    val progressDialog = ProgressDialog(root.context);
    progressDialog.setMessage("Loading...");
    progressDialog.show();

    webView.loadUrl("https://busnavi.keihanbus.jp/pc/diagrampoledtl?mode=1&fr=%E5%8C%97%E5%B1%B1%E4%B8%AD%E5%A4%AE&frsk=B&tosk=&dt=202010162158&dd=2&dgmpl=%E5%8C%97%E5%B1%B1%E4%B8%AD%E5%A4%AE%E3%80%94%E4%BA%AC%E9%98%AA%E3%83%90%E3%82%B9%E3%80%95:2::41019,41029,40619,40629,40719,40729&p=0%2C8%2C10%2C12")

    webView.webViewClient = object : WebViewClient() {
      override fun onPageFinished(view: WebView, url: String) {
        progressDialog.dismiss()
      }
    }

    return root
  }
}