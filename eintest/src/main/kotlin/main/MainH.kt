package main

import android.view.View
import android.webkit.WebView
import base.Alert
import base.App
import base.JSobj
import base.R
import ein.android.view.router.eViewHolder
import ein.android.view.viewmodel.propertyAndroid

class MainH(routerKey:String, holderKey:String, data:Any?):eViewHolder(routerKey, holderKey, data){
    override val layout = R.layout.main
    private var isMain = false
    private lateinit var wv:WebView
    private lateinit var sp:View
    override val view:View
        get(){
            val v = super.view
            wv = v.findViewById<WebView>(R.id.browser)
            propertyAndroid.let{
                it["webviewSetting"]?.invoke(wv, "default")
                it["chromeClient"]?.invoke(wv, Alert)
                it["client"]?.invoke(wv, "default")
                it["loadUrl"]?.invoke(wv, "https://seller.bsidesoft.com/hika/")
                it["javascriptInterface"]?.invoke(wv, JSobj)
                App.seller?.let{s->
                    it["click"]?.invoke(s, View.OnClickListener {_->
                        it["loadUrl"]?.invoke(wv, "https://seller.bsidesoft.com/summer/")
                    })
                }
                App.test?.let{s->
                    it["click"]?.invoke(s, View.OnClickListener {_->
                        it["loadUrl"]?.invoke(wv, "https://seller.bsidesoft.com/hika/")
                    })
                }
            }
            return v
        }
    override fun action(k:String, v:Any?):Any? {
        when(k){
            "canGoBack"->return wv.canGoBack()
            "goBack"->wv.goBack()
        }
        return null
    }
}