package com.example.webviewexample

import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import java.net.InetAddress
import java.net.UnknownHostException

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val webView: WebView = findViewById(R.id.webView)
        val search: SearchView = findViewById(R.id.searchView)
        val btnBack: ImageView = findViewById(R.id.img_arrow_back)
        val btnNext: ImageView = findViewById(R.id.img_arrow_next)

        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                Thread{
                    val url = try {
                        InetAddress.getByName(query)
                        "https://$query"
                    }catch (e : UnknownHostException){
                        "https://www.google.com/search?query=$query"
                    }
                 runOnUiThread { webView.loadUrl(url) }
                }.start()

                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }

        })

        /**If java script is active the images do not load
         * */
        //webView.settings.javaScriptEnabled = true

        webView.loadUrl("https://br.louisvuitton.com/")

        //webView.webViewClient = WebViewClient()
        webView.webViewClient = object: WebViewClient(){
            override fun onPageFinished(view: WebView?, url: String?) {
                btnBack.isEnabled = webView.canGoBack()
                btnNext.isEnabled = webView.canGoForward()

                //This can be removed
                search.setQuery(url, false)

                btnBack.imageTintList = if (btnBack.isEnabled)
                    ColorStateList.valueOf(ContextCompat.getColor(applicationContext, R.color.teal_200))
                else
                    ColorStateList.valueOf(Color.GRAY)

                btnNext.imageTintList = if (btnNext.isEnabled)
                    ColorStateList.valueOf(ContextCompat.getColor(applicationContext, R.color.teal_200))
                else
                    ColorStateList.valueOf(Color.GRAY)

                //This method can be deleted
                super.onPageFinished(view, url)
            }
        }

        btnBack.setOnClickListener {
            webView.goBack()
        }

        btnNext.setOnClickListener {
            webView.goForward()
        }
    }

}