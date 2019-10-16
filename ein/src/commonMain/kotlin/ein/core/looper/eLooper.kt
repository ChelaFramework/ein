package ein.core.looper

abstract class eLooper{
    protected var isStarted = false
    private var previus = 0.0
    var fps = 0.0
        private set(v){field = v}
    private val schedulers = mutableListOf<eScheduler<*, *>>()
    protected fun loop(){
        if(!isStarted) return
        val c = now()
        val gap = c - previus
        previus = c
        if(gap > 0.0) fps = 1000.0 / gap
        var i = schedulers.size
        while(i-- > 0){
            val s = schedulers[i]
            if(c - s.prev > s.term) {
                s._loop(c)
                s.prev = now()
            }
        }
    }
    internal operator fun plusAssign(scheduler:eScheduler<*, *>){
        schedulers += scheduler
        if(isStarted) return
        isStarted = true
        start()
    }
    internal operator fun minusAssign(scheduler:eScheduler<*, *>){
        schedulers -= scheduler
        if(schedulers.isEmpty()) stop()
    }
    fun end(){
        if(!isStarted) return
        isStarted = false
        stop()
    }
    protected open fun start(){}
    protected open fun stop(){}
}