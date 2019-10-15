package ein.core.looper

abstract class eTask{
    companion object:eTask()
    internal var delay:Int = 0
    internal var next:eTask? = null
    internal var start = 0.0
    internal var isStop = false
    fun stop(){
        isStop = true
    }
}