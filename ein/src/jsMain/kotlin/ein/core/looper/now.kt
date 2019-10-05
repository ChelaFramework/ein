package ein.core.looper

import kotlin.browser.window

actual fun now() = window.performance.now()