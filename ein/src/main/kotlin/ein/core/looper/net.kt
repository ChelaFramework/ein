package ein.core.looper

import ein.android.looper.MainLooper

actual fun net(isBack:Boolean) = if(isBack) MainLooper.netLooper else MainLooper.asyncAndroid