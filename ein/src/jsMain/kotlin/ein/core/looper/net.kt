package ein.core.looper

import ein.js.looper.MainLooper

actual fun net(isBack:Boolean) = MainLooper.asyncJS
