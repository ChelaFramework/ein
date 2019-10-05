package ein.core.view.router

import ein.core.channel.eListener
import ein.core.log.log
import ein.core.looper.ItemBlock
import ein.core.looper.getLooper
import ein.core.view.viewmodel.eScanned

abstract class eHolder<T>(
    internal val routerKey:String,
    internal val key:String,
    protected val data:Any?,
    vararg channels:Pair<String, (channel:String, value:Any?)->Unit>
){
    companion object{
        private const val LIMIT = 100
    }
    abstract val view:T
    internal var next:eHolder<T>? = null
    internal var prev:eHolder<T>? = null

    protected var scanned:eScanned<T>? = null

    private var pushAni = eAni.LEFT
    private var popAni = eAni.RIGHT
    private var pushTime = 300
    private var popTime = pushTime
    private var isAlive = false
    protected val listener = eHolderListener(*channels)

    protected fun render(){
        scanned?.invoke(view)
    }
    protected fun pushSet(time:Int, ani: eAni){
        pushTime = time
        pushAni = ani
    }
    protected fun popSet(time:Int, ani: eAni){
        popTime = time
        popAni = ani
    }
    internal fun _push(isAni:Boolean, end:()->Unit){
        isAlive = true
        listener.start()
        push(isAni, end, pushAni, pushTime)
    }
    internal fun _toBack(isAni:Boolean, isCurr:Boolean, isDisplay:Boolean){
        if(isCurr) {
            isAlive = false
            listener.pause()
        }
        toBack(isAni, isCurr, isDisplay, pushAni, pushTime)
    }
    internal fun _toFront(isAni:Boolean, isCurr:Boolean, isDisplay:Boolean){
        if(isCurr) {
            isAlive = true
            listener.resume()
        }
        toFront(isAni, isCurr, isDisplay, popAni, popTime)
    }
    internal fun _pop(isAni:Boolean, end:()->Unit){
        isAlive = false
        listener.end()
        pop(isAni, end, popAni, popTime)
    }
    protected open fun push(isAni:Boolean, end:()->Unit, pushAni:eAni, pushTime:Int){
        getLooper().run(end)
    }
    protected open fun toBack(isAni:Boolean, isCurr:Boolean, isDisplay:Boolean, pushAni:eAni, pushTime:Int){}
    protected open fun toFront(isAni:Boolean, isCurr:Boolean, isDisplay:Boolean, popAni:eAni, popTime:Int){}
    protected open fun pop(isAni:Boolean, end:()->Unit, popAni:eAni, popTime:Int){
        getLooper().run(end)
    }
    internal open fun pushed(){}
    internal open fun poped(){}
    internal open fun restored(isCurr:Boolean){}
    internal fun next(limit:Int = LIMIT, block:(eHolder<T>)->Unit){
        if(limit > 0) next?.let{
            block(it)
            it.next(limit - 1, block)
        }
    }
    internal fun prev(limit:Int = LIMIT, block:(eHolder<T>)->Unit){
        if(limit > 0) prev?.let{
            block(it)
            it.prev(limit - 1, block)
        }
    }
    abstract fun action(k:String, v:Any?):Any?
}