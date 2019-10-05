package ein.core.looper

import ein.android.looper.netLooper

actual fun getNetLooper() = netLooper.apply {
    resume()
}

