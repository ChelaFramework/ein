package ein.core.looper

import ein.core.core.eLock
import ein.core.log.log

typealias ItemBlock = (eLooperItem)->Unit

class eLooper(var scheduler:eScheduler, private val lock:eLock = eLock.EMPTY){
    var fps = 0.0
        private set(v){field = v}
    private val sequence = eLooperSequence(this)
    private var previus = 0.0
    private var pauseStart = 1.0
    private var pausedTime = 0.0
    private val items = mutableListOf<eLooperItem>()
    private val remove = mutableListOf<eLooperItem>()
    private val add = mutableListOf<eLooperItem>()
    private val itemPool = mutableListOf<eLooperItem>()
    private fun itemStart(item:eLooperItem){
        item.start = now().plus(item.delay)
        item.end = if(item.term == -1.0) -1.0 else item.start + item.term
    }
    private fun addSeq(item:eLooperItem):eLooperSequence{
        itemStart(item)
        lock.write{items += item}
        sequence.item = item
        return sequence
    }
    fun item(block: eLooperItemDSL.()->Unit) = addSeq(getItem(eLooperItemDSL().apply{block()}))
    fun run(block:()->Unit) = addSeq(getItem{
        it.stop()
        block()
    })
    fun run(ms:Int, run:() -> Unit) = addSeq(getItem(eLooperItemDSL().apply {
        delay = ms
        block = {
            it.stop()
            run()
        }
    }))
    fun block(block:ItemBlock) = addSeq(getItem(block))
    fun block(items:List<ItemBlock>):eLooperSequence {
        block(items.first())
        items.drop(1).forEach{sequence block it}
        return sequence
    }
    private fun pop():eLooperItem?{
        val size = itemPool.size
        return if(size > 0) itemPool.removeAt(size - 1) else null
    }
    internal fun getItem(b:ItemBlock):eLooperItem = (pop() ?: eLooperItem()).apply{
        term = 0.0
        delay = 0.0
        loop = 1
        block = b
        isInfinity = true
        next = null
    }
    internal fun getItem(i:eLooperItemDSL):eLooperItem = (pop() ?: eLooperItem()).apply{
        term = i.time.toDouble()
        delay = i.delay.toDouble()
        loop = i.loop
        block = i.block
        ended = i.ended
        isInfinity = i.isInfinity
        next = null
    }
    internal fun loop(){
        if(pauseStart != 0.0 || items.isEmpty()) return
        val c = now()
        val gap = c - previus
        previus = c
        if(gap > 0.0) fps = 1000.0 / gap
        remove.clear()
        add.clear()
        var i = 0
        while(i < items.size){
            val item = items[i++]
            if(item.isPaused || item.start > c) continue
            item.isTurn = false
            var isEnd = false
            item.rate = if(item.end <= c){
                if(!item.isInfinity && --item.loop < 1) {
                    isEnd = true
                    1.0
                }else{
                    item.isTurn = !item.isTurn
                    itemStart(item)
                    0.0
                }
            }else if(item.term == 0.0) 0.0
            else (c - item.start) / item.term
            item.current = c
            item.isStop = false
            item.block(item)
            if(item.isStop || isEnd){
                item.ended(item)
                item.next?.let {
                    itemStart(it)
                    add += it
                }
                remove += item
            }
        }
        if(remove.isNotEmpty() || add.isNotEmpty()) lock.write{
            if(remove.isNotEmpty()) {
                items -= remove
                itemPool += remove
            }
            if(add.isNotEmpty())items += add
        }
    }
    fun pause(){
        if(pauseStart != 0.0) pauseStart = now()
    }
    fun resume(){
        if(pauseStart != 0.0){
            pausedTime += now() - pauseStart
            pauseStart = 0.0
            scheduler.looper = this
            scheduler.start()
        }
    }
    fun stopScheduler(){
        pause()
        scheduler.stop()
    }
}