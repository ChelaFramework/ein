package ein.core.looper.async

import ein.core.core.eLock
import ein.core.looper.eLooper
import ein.core.looper.eScheduler

class eAsync(term:Double, looper:eLooper, lock:eLock = eLock.EMPTY):eScheduler<eAwait, eAsyncSerial>(term, looper, lock){
    override fun serial() = eAsyncSerial(this)
    override fun task() = eAwait()
}

