package base

import android.annotation.SuppressLint
import android.view.View
import android.webkit.JsResult
import android.widget.TextView
import ein.android.view.viewmodel.prop.ChromeAlert
import ein.android.view.viewmodel.propertyAndroid
import ein.core.looper.getLooper

@SuppressLint("StaticFieldLeak")
object Alert:ChromeAlert {
    lateinit var alert:View
    lateinit var title:TextView
    lateinit var msg:TextView
    private var alertResult:JsResult? = null
    operator fun invoke(v:View){
        alert = v
        title = v.findViewById<TextView>(R.id.alerttitle)
        msg = v.findViewById<TextView>(R.id.alertmsg)
        propertyAndroid.let {
            it["visibility"]?.invoke(v, false)
            it["click"]?.invoke(v.findViewById<TextView>(R.id.alert), View.OnClickListener {
                v.visibility = View.GONE
                alertResult?.confirm()
            })
            it["background"]?.invoke(v.findViewById<TextView>(R.id.alertline), "border")
        }
    }
    override fun alert(v:String, r:JsResult?) {
        getLooper().run {
            if("::" in v){
                val s = v.split("::")
                title.text = s[0]
                msg.text = s[1]
            }else{
                title.text = "Alert"
                msg.text = v
            }
            alert.visibility = View.VISIBLE
            alertResult = r
        }
    }
}