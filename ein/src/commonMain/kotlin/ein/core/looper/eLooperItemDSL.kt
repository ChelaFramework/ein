package ein.core.looper

class eLooperItemDSL{
    companion object{val empty:ItemBlock = {}}
    var time:Int = -1
    var delay:Int = 0
    var loop:Int = 1
    var block:ItemBlock = empty
    var ended:ItemBlock = empty
    var isInfinity:Boolean = false
}