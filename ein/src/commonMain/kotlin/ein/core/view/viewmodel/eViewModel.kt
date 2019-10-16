package ein.core.view.viewmodel

import ein.core.value.eValue
import kotlin.reflect.KProperty

/*
class test:eViewModel(){
    var a by v("test")
}
 */
abstract class eViewModel(isStored:Boolean = false){
    @Suppress("UNCHECKED_CAST")
    protected class VDele<T:Any>(private var v:T, private val target:MutableMap<String, Any>){
        operator fun getValue(ref:Any?, prop:KProperty<*>):T{
            if(prop.name !in target) target[prop.name] = v
            return target[prop.name] as T
        }
        operator fun setValue(ref:Any?, prop:KProperty<*>, v:T){
            target[prop.name] = v
            this.v = v
        }
    }
    val map = mutableMapOf<String, Any>()
    operator fun get(k:String) = map[k]
    operator fun set(k:String, v:Any) = map.put(k, v)
    protected fun <T:Any> v(v:T, k:String? = null) = VDele(v, map).apply{
        k?.let{map[it] = v}
    }
    protected val s get() = VDele("", map)
    protected val i get() = VDele(0, map)
    protected val l get() = VDele(0L, map)
    protected val f get() = VDele(0F, map)
    protected val d get() = VDele(0.0, map)
    protected val b get() = VDele(false, map)
    init{
        if(isStored){
            this::class.simpleName?.let{
                eValue[it] = map
            }
        }
    }
    open fun start(){}
    open fun end(){}
    open fun paused(){}
}

