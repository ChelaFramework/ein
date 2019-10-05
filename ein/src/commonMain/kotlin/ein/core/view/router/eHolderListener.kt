package ein.core.view.router

import ein.core.channel.eListener

private typealias listener = (channel:String, value:Any?)->Unit
class eHolderListener(vararg channels:Pair<String, listener>):eListener(){
    private val dispatcher:MutableMap<String, listener> =
        if(channels.isEmpty()) mutableMapOf() else channels.toMap() as MutableMap<String, listener>
    operator fun set(k:String, v:listener){
        dispatcher[k] = v
        addChannels(k)
    }
    override fun listen(channel:String, value:Any?){
        dispatcher[channel]?.let{it(channel, value)}
        dispatcher["*"]?.let{it(channel, value)}
    }
}