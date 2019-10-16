package ein.core.looper

import ein.jvm.looper.MainLooper

actual fun net(isBack:Boolean) = MainLooper.asyncJVM