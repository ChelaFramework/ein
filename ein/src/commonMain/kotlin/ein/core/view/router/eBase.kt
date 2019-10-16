package ein.core.view.router

@Suppress("UNCHECKED_CAST")
abstract class eBase<T:eHolder<R>, R>(protected val historyLevel:Int){
    protected var curr:T? = null
    internal fun push(holder:T, isAni:Boolean = true){
        val c = curr
        curr = holder
        add(holder, true)
        holder._push(isAni){holder.pushed()}
        c?.next{h->h._pop(false){
            h.poped()
            remove(h as T)
        }}
        var count = 0
        c?.prev{
            val isDisplay = count++ < historyLevel
            it._toBack(isAni, false, isDisplay)
            if(isDisplay) add(it as T, false,0) else remove(it as T)
        }
    }
    internal fun pop(isAni:Boolean = true){
        curr?.let {c->
            curr = curr?.prev as T?
            curr?.next = null
            c._pop(isAni){
                c.poped()
                remove(c)
            }
            c.next{h->h._pop(false){
                h.poped()
                remove(c)
            }}
            var count = 0
            c.prev{
                val isDisplay = count++ < historyLevel + 1
                val isCurr = it === curr
                it._toFront(isAni, isCurr, isDisplay)
                if(isDisplay) add(it as T, isCurr,0) else remove(it as T)
            }
        }
    }/*
    internal fun prev(isAni:Boolean) = curr?.prev?.let{p->
        curr = p as T?
        p._toFront(isAni, true, true)
        var count = 0
        p.next{h->h._toFront(isAni, false, count++ < historyLevel)}
        count = 0
        p.prev{h->h._toFront(isAni, false, count++ < historyLevel)}
    }
    internal fun next(isAni:Boolean) = curr?.next?.let{n->
        curr = n as T?
        n._toBack(isAni, true, true)
        n.next{h->h._toBack(isAni, false, false)}
        n.prev{h->h._toBack(isAni, false, false)}
    }
    */
    protected abstract fun add(holder:T, isCurr:Boolean, idx:Int = -1)
    protected abstract fun remove(holder:T)
}