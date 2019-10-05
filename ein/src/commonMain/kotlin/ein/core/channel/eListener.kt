package ein.core.channel

abstract class eListener{
    companion object{
        internal val EMPTY = object :eListener(){
            override fun listen(channel:String, value:Any?){}
        }
    }
    private val channels = mutableSetOf<String>()
    private val isUpdated = mutableMapOf<String, Any?>()
    private var isActivated = false
    internal operator fun invoke(e:eEvent){
        if(!isActivated) isUpdated[e.channel] = e.value
        else listen(e.channel, e.value)
    }
    protected abstract fun listen(channel:String, value:Any?)
    fun addChannels(vararg channel:String) = channels.addAll(channel)
    fun removeChannels(vararg channel:String) = channels.removeAll(channel)
    fun start(){
        isActivated = true
        channels.forEach {eChannel[it] = this}
    }
    fun end(){
        isActivated = false
        channels.forEach {eChannel.remove(it, this)}
    }
    fun resume(readMemo:Boolean = true){
        isActivated = true
        if(readMemo) isUpdated.forEach{(k, v)->listen(k, v)}
        isUpdated.clear()
    }
    fun pause(){
        isActivated = false
    }
}