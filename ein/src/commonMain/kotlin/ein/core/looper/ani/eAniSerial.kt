package ein.core.looper.ani

import ein.core.log.log
import ein.core.looper.eSerial

class eAniSerial(private val ani:eAni):eSerial<eAniTask>() {
    override fun addScheduler(task:eAniTask){ani += task}
    fun ani(init:eAniTask.()->Unit = {}, block:(eAniTask) -> Unit) = last(ani.task({
        time = 0.0
        turn = 0
        loop = 1
        isInfinity = false
        init()
    }, block))
    fun once(delay:Int = 0, block:() -> Unit) = last(ani.task({
        time = 0.0
        turn = 0
        loop = 1
        this.delay = delay
        isInfinity = false
    }){
        block()
        it.stop()
    })
    fun infinity(term:Int, block:() -> Unit) = last(ani.task({
        time = 0.0
        turn = 0
        loop = 1
        delay = term
        isInfinity = true
    }){
        block()
        it.stop()
    })
}