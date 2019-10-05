package ein.core.looper

class eLooperSequence internal constructor(private val looper:eLooper){
    internal lateinit var item:eLooperItem
    infix fun item(block: eLooperItemDSL.()->Unit):eLooperSequence {
        val i = looper.getItem(eLooperItemDSL().apply{block()})
        item.next = i
        item = i
        return this
    }
    infix fun block(block:ItemBlock):eLooperSequence {
        val i = looper.getItem(block)
        item.next = i
        item = i
        return this
    }
    infix fun run(block:()->Unit):eLooperSequence {
        val i = looper.getItem{
            it.stop()
            block()
        }
        item.next = i
        item = i
        return this
    }
    infix fun run(run:Pair<Int, ()->Unit>):eLooperSequence {
        val i = looper.getItem(eLooperItemDSL().apply {
            delay = run.first
            block = {
                it.stop()
                run.second()
            }
        })
        item.next = i
        item = i
        return this
    }
}