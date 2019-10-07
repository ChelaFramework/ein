package chela.kotlinJS.view

import ein.core.value.eJsonObject
import ein.core.resource.eLoader

/*{
    "style":{
        "key":{
            "width":"15px",
            "height":12 // toString
        }...N
    }

} */
class eStyle private constructor(base:Map<String, String> = mapOf()):Map<String, String> by base{
    companion object:eLoader{
        override fun load(res:eJsonObject) {
            (res["style"] as? eJsonObject)?.forEach{(k, v)->
                (v as? eJsonObject)?.let{set(k, it.mapValues{raw->"$raw"})}
            }
        }
        private val styles = mutableMapOf<String, eStyle>()
        operator fun get(k:String) = styles[k]
        operator fun set(k:String, v:eJsonObject) = styles.put(k, eStyle(v.mapValues{raw->"$raw"}))
        operator fun set(k:String, v:Map<String, String>) = styles.put(k, eStyle(v))
        operator fun minusAssign(k:String){remove(k)}
        fun remove(k:String) = styles.remove(k)
    }
}
