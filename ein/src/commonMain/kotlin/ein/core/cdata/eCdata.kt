package ein.core.cdata

import ein.core.value.eJsonObject
import ein.core.value.eString
import ein.core.resource.eLoader

/*
{
    "cdata":{
        "@cat1":"en", //optional, current category value
        "@@cat2":"man", //optiona;, default category value
        "title@cat1":{ //key@category
            "en":"hello", //category value:value
            "ko@cat2":{ //category value@sub category
                "man":"man",
                "woman":"woman"
            }
        }
    }
}
 */
class eCdata(val cat:String):MutableMap<String, Any?> by mutableMapOf(){
    var default:String? = null
    companion object:eLoader{
        private val root = eCdata("_ROOT_")
        private val cat = mutableMapOf<String, String>()
        private val cats:String get() = if(cat.isEmpty()) "" else{
            var r = ""
            cat.forEach{(k, v)->r += "&$k=$v"}
            r.substring(1)
        }
        private val catDefault = mutableMapOf<String, String>()
        private val cache = mutableMapOf<String, Any>()
        private val requestKey = mutableMapOf<String, MutableSet<String>>()
        var requestApi:(String)->Unit = {}
        val requestState get() = requestKey.map{(k, v)->"$k=[${v.joinToString("&")}]"}.joinToString("&")
        fun setData(k:String, v:eJsonObject, parent:eCdata = root){
            val i = k.indexOf('@')
            if(i == -1) throw Throwable("no @ in key : $k")
            val key = k.substring(0, i)
            val data = (parent[key] as? eCdata) ?: run {
                val r = eCdata(k.substring(i + 1))
                parent[key] = r
                r
            }
            v.forEach {(k, v)->
                if(k == "@default" && v is eString) data.default = v.v
                else if(k.contains('@')){
                    if(v is eJsonObject) setData(k, v, data)
                    else throw Throwable("invalid v:$k, $v")
                }else data[k] = v
            }
        }
        fun removeData(k:String) = root.remove(k)
        fun setCat(k:String, v:String) = cat.set(k, v)
        fun getCat(k:String) = cat[k]
        fun clearCache(){
            cache.clear()
        }
        fun clearCache(k:String){
            cache.remove(k)
        }
        override fun load(res:eJsonObject){
            (res["cdata"] as? eJsonObject)?.forEach{(k, v)->
                when(v){
                    is eString->if(k.startsWith("@@")) catDefault[k.substring(2)] = v.v
                    else if(k[0] == '@') cat[k.substring(1)] = v.v
                    is eJsonObject->setData(k, v)
                }
            }
        }
        fun requestData(block:(String)->Unit = requestApi){
            val data = requestState
            requestKey.clear()
            block(data)
        }
        @Suppress("UNCHECKED_CAST")
        operator fun <T> get(k:String):T?{
            val key = "$k:$cats"
            (cache[key] as? T)?.let{return it}
            var target:Any = if(k in root) root[k]!! else {
                if(requestKey[k] == null) requestKey[k] = mutableSetOf()
                requestKey[k]?.add("*")
                return null
            }
            var i = 50
            var r = ""
            while(i-- > 0) (target as? eCdata)?.let{
                val c = it.cat
                val curr = cat[c]
                r += ",$c=$curr"
                val v = it[curr] ?: it[it.default] ?: it[catDefault[c]]
                if(v != null){
                    if(v is eCdata) target = v
                    else{
                        if(k[0] != '@') cache[key] = v
                        return v as? T
                    }
                }else{
                    if(requestKey[k] == null) requestKey[k] = mutableSetOf()
                    requestKey[k]?.add(r.substring(1))
                    return null
                }
            }
            return null
        }
    }
}