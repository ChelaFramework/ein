package ein.android.app

import android.app.Application
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.provider.Settings
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.content.res.AppCompatResources
import ein.android.sql.eSqliteDB
import ein.core.core.elazy
import ein.core.regex.eRegValue

object eApp{
    internal lateinit var app:Application
    val clip: ClipboardManager by elazy(true){app.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager}
    val res by elazy(true){app.resources}
    val asset by elazy(true){app.assets}
    val cm by elazy(true){app.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager}
    val imm by elazy(true){app.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager}
    val packName by elazy(true){app.packageName}
    val dm by elazy(true){res.displayMetrics}
    val fileDir by elazy(true){app.filesDir}
    val cacheDir by elazy(true){app.cacheDir}
    val locale by elazy(true){
        @Suppress("DEPRECATION")
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) res.configuration.locales[0]
        else res.configuration.locale
    }
    val language by elazy(true){locale.language}
    val appVersion by elazy(true){app.packageManager.getPackageInfo(app.packageName, 0).versionName}
    val deviceId by elazy(true){Settings.Secure.ANDROID_ID}
    val deviceModel by elazy(true){Build.MODEL}
    val deviceVersion by elazy(true){Build.VERSION.RELEASE}
    var isDebug = false
    private var isInited = false
    operator fun invoke(a:Application, isDebug:Boolean = false){
        //if(isInited) return
        isInited = true
        this.isDebug = isDebug
        app = a
        eSqliteDB.init()
        eRegValue.apply {
            dp = eWindow.DptoPx
            sp = eWindow.SptoPx
            width = eWindow.width.toDouble()
            height = eWindow.height.toDouble()
        }
        if(!isDebug) Thread.currentThread().setUncaughtExceptionHandler{_, _->}
    }
    fun resS2I(type:String, name:String):Int = res.getIdentifier(name, type, packName)
    fun resDrawable(v: String):Int = resS2I("drawable", v)
    fun resId(v: String):Int = resS2I("id", v)
    fun resLayout(v: String):Int = resS2I("layout", v)
    fun resFont(v: String):Int = resS2I("font", v)
    fun resName(id:Int):String = res.getResourceEntryName(id)
    fun drawable(v:String):Drawable? = drawable(resS2I("drawable", v))
    fun drawable(v:Int):Drawable? = AppCompatResources.getDrawable(app, v)
    fun drawable(v:Any?) = when(v){
        is Drawable-> v
        is String ->drawable(v)
        is Int ->drawable(v)
        else->null
    }
    fun bitmap2Drawable(v:Bitmap): BitmapDrawable = BitmapDrawable(res, v)
    fun string(v:String):String = string(resS2I("string", v))
    fun string(v:Int):String = res.getString(v)

    fun isConnected() = connectedType() != "none"
    fun connectedType():String{
        if(Build.VERSION.SDK_INT < 23){
            @Suppress("DEPRECATION")
            cm.activeNetworkInfo?.let{
                return when(it.type){
                    ConnectivityManager.TYPE_WIFI->"wifi"
                    ConnectivityManager.TYPE_MOBILE->"mobile"
                    else ->"none"
                }
            }
        }else{
            cm.activeNetwork?.let {
                val nc = cm.getNetworkCapabilities(it)
                return when{
                    nc.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> "wifi"
                    nc.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> "mobile"
                    else -> "none"
                }
            }
        }
        return "none"
    }
}