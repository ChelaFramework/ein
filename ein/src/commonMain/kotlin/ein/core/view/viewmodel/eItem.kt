package ein.core.view.viewmodel

import ein.core.core.*
/*
"@{store.a}" //style:store.a
"${}" //style:record self

"${:record}" //record self

//special key
"a:${:index}" //index
"b:${:size}" //record size

//no memo @
"@a:3" //every update
 */
class eItem internal constructor(internal val pos:MutableList<Int>, data:String){
    internal var key = ""
    private val map = (ePrimitive.json(when(data[0]){
            '@', '$'->"{style:$data}"
            else->"{$data}"
        }) as? eJsonObject) ?: throw Throwable("invalid data:$data")
    private val memo = mutableMapOf<String, Any>()
    operator fun invoke(record:eViewModel?, i:Int, size:Int):Map<String, Any>?{
        var r:MutableMap<String, Any>? = null
        map.forEach {(k, v)->
            val value = ePrimitive[v, record, i, size]
            if(k[0] == '@') (r ?: mutableMapOf<String, Any>().apply{r = this})[k.substring(1)] = value
            else if(memo[k] != value){
                (r ?: mutableMapOf<String, Any>().apply{r = this})[k] = value
                memo[k] = value
            }
        }
        return r
    }
}