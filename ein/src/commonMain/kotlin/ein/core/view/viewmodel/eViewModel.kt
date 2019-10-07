package ein.core.view.viewmodel

import ein.core.value.eValue
import ein.core.looper.eLooperItem
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
            return v
        }
        operator fun setValue(ref:Any?, prop:KProperty<*>, v:T){
            target[prop.name] = v
            this.v = v
        }
    }
    internal val map = mutableMapOf<String, Any>()
    operator fun get(k:String) = map[k]
    operator fun set(k:String, v:Any) = map.put(k, v)
    protected fun <T:Any> v(v:T) = VDele(v, map)
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
    open fun resumeAnimation(it:eLooperItem){}
    open fun pauseAnimation(it:eLooperItem){}
}

