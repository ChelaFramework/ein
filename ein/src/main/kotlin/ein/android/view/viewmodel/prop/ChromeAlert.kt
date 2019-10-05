package ein.android.view.viewmodel.prop

import android.webkit.JsResult

interface ChromeAlert{
    fun alert(v:String, r:JsResult?)
}