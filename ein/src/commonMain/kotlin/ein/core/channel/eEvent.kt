package ein.core.channel

class eEvent(val channel: String, val value:Any?){
    internal var isStop = false
    internal var isRemove = false
    fun stop(){isStop = true}
    fun remove(){isRemove = true}
}