package ein.core.looper

import ein.jvm.looper.mainLooper

actual fun getLooper() = mainLooper.apply {
    resume()
}