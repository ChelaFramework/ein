package ein.core.channel

object eChannel{
    private val channels = mutableMapOf<String, MutableSet<eListener>>()
    internal operator fun set(channel:String, listener:eListener){
        channels[channel] ?: mutableSetOf<eListener>().apply{channels[channel] = this} += listener
    }
    internal fun remove(channel:String){
        channels -= channel
    }
    internal fun remove(channel:String, listener:eListener){
        channels[channel]?.let{it -= listener}
    }
    fun notify(channel: String, value:Any? = null) = channels[channel]?.let{c->
        val e = eEvent(channel, value)
        c.all{
            if(e.isStop) false
            else{
                it(e)
                if(e.isRemove){
                    c-= it
                    e.isRemove = false
                }
                true
            }
        }
    }
}


