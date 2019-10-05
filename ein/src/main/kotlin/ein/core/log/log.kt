package ein.core.log

import android.util.Log

actual fun log(v:String) {
    Log.w("ein", v)
}