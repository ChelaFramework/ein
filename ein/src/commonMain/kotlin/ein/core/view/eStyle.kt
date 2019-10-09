package ein.core.view

import ein.core.log.log
import ein.core.value.eJsonObject
import ein.core.resource.eLoader
import ein.core.value.eValue

/*{
    "style":{
        "key":{
            "width":"15px",
            "height":12
        }...N
    }

} */
object eStyle:eLoader{
    override fun load(res:eJsonObject) {
        (res["style"] as? eJsonObject)?.forEach{(k, v)->(v as? eJsonObject)?.let{set(k, it)}}
    }
    val styles = mutableMapOf<String, eJsonObject>()
    operator fun get(k:String) = styles[k]
    operator fun set(k:String, v:eJsonObject) = styles.put(k, v)
    operator fun minusAssign(k:String){remove(k)}
    fun remove(k:String) = styles.remove(k)
}