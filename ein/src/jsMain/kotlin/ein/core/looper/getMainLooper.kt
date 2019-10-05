package ein.core.looper

import ein.js.looper.mainLooper

actual fun getLooper() = mainLooper.apply {
    resume()
}