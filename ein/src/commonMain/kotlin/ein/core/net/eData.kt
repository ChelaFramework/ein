package ein.core.net

import ein.core.resource.eLoader
import ein.core.sql.eDB
import ein.core.value.eInvalid
import ein.core.value.eJsonObject

abstract class eData(val key:Any, db:String){
    companion object{
        private val cache = mutableMapOf<Any, Any>()
    }
    private var flag:Any = eInvalid
    protected val db by lazy{eDB[db] ?: throw Throwable("no db:$db")}
    protected abstract fun net(block:(Any)->Unit)
    protected abstract fun data(v:Any)
    protected open fun get() = flag
    protected open fun set(v:Any):Boolean{
        flag = v
        return true
    }
    protected open fun isValidCache(v:Any?) = v != null && flag !== eInvalid
    protected open fun renew(v:Any){if(v is eJsonObject) eLoader.load(v)}
    protected open fun error(){}
    open fun isChanged(){flag = eInvalid}
    open fun reload() = net{if(!set(it) || !isValid()) error()}
    open operator fun invoke(){
        if(key in cache && isValidCache(cache[key])) data(cache[key]!!)
        else{
            cache -= key
            if(!isValid()) net{if(!set(it) || !isValid()) error()}
        }
    }
    private fun isValid(v:Any = get()) = if(v !== eInvalid){
        renew(v)
        cache[key] = v
        data(v)
        true
    }else false
}