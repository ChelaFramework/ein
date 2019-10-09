package ein.core.view.router

class eRouter<T:eBase<R, U>, R:eHolder<U>, U>(val key:String, val base:T){
    private var curr:R? = null
    private val holders = mutableMapOf<String, (routerKey:String, holderKey:String, data:Any?)->R>()
    operator fun set(k:String, block:(routerKey:String, holderKey:String, data:Any?)->R) = holders.put(k, block)
    operator fun minusAssign(k:String) = remove(k)
    fun remove(k:String){holders.remove(k)}
    operator fun invoke(k:String, data:Any? = null){
        holders[k]?.let{
            val h = it(key, k, data)
            base.push(h)
            h.prev = curr
            curr?.next = h
            curr = h
        }
    }
    fun push(k:String, data:Any? = null) = invoke(k, data)
    fun pop() = pop("", true, 1)
    fun pop(count:Int, isAni:Boolean) = pop("", isAni, count)
    fun pop(key:String, isAni:Boolean = false, count:Int = 100){
        var i = count
        while(i-- > 0) curr?.prev?.let {
            base.pop(isAni)
            it.next = null
            @Suppress("UNCHECKED_CAST")
            curr = it as R
            if(it.key == key) return
        } ?: break
    }
    fun action(k:String, v:Any? = null) = curr?.action(k, v)
    /*
    fun prev(count:Int, isAni:Boolean){
        var i = count
        var target:eHolder<U> = curr ?: return
        while(i-- > 0) target.prev?.let {
            eintest.test.base.prev(isAni)
            target = it
        }
    }
    fun next(count:Int, isAni:Boolean){
        var i = count
        var target:eHolder<U> = curr ?: return
        while(i-- > 0) target.prev?.let {
            eintest.test.base.next(isAni)
            target = it
        }
    }*/
}