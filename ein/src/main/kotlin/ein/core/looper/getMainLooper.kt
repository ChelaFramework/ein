package ein.core.looper

import ein.android.looper.mainLooperAndroid

actual fun getLooper() = mainLooperAndroid.apply {
    resume()
}