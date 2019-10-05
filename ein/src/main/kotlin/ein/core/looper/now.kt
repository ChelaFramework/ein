package ein.core.looper

import android.os.SystemClock

actual fun now() = SystemClock.uptimeMillis().toDouble()