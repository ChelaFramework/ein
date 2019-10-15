package ein.core.looper

import ein.core.core.eLock

class eAsyncSerial(private val async:eAsync):eSerial<eAwait>(){
    private var isNext = false
    private val checkNext = {it:eAwait->Unit
        if(isNext) it.stop()
    }
    override fun addScheduler(task:eAwait) {
        async += task
    }
    fun wait(ms:Int = 0, block:eAsyncSerial.()->Unit) {
        last(
            async.task(ms){
                it.stop()
                isNext = false
                this.block()
            },
            async.task(checkNext)
        )
    }
    fun go(){isNext = true}
}
class eAwait:eTask()
class eAsync(term:Double, worker:eWorker, lock:eLock = eLock.EMPTY):eScheduler<eAwait, eAsyncSerial>(term, worker, lock){
    override fun serial() = eAsyncSerial(this)
    override fun task() = eAwait()
}

