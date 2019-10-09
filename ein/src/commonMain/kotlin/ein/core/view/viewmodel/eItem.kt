package ein.core.view.viewmodel

import ein.core.log.log
import ein.core.regex.eRegValue
import ein.core.value.*

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
class eItem internal constructor(internal val pos:List<Int>, data:String){
    internal var key = ""
    val map = (eValue.json(eValue.srReg.matchEntire(data)?.let{"{style:$data}"} ?: "{$data}") as? eJsonObject) ?: throw Throwable("invalid data:$data")
    private fun process(k:String, v:Any, r:MutableMap<String, Any>, memo:MutableMap<String, Any>, record:eViewModel?, i:Int, size:Int){
        val value = eValue[v, record, i, size].let{if(it !is String) it else eRegValue.num(it) ?: it}
        if(k[0] == '@') r[k.substring(1)] = value
        else if(memo[k] != value){
            memo[k] = value
            r[k] = value
        }
    }
    operator fun invoke(memo:MutableMap<String, Any>, record:eViewModel?, i:Int, size:Int) = mutableMapOf<String, Any>().run{
        map.forEach {(k, v)->
            if(k != "style") process(k, v, this, memo, record, i, size)
            else{
                val value = when(v){
                    is eStore, is eRecord->eValue[v, record, i, size]
                    else->null
                }
                (when(value){
                    is eViewModel->value.map
                    is eJsonObject->value
                    else->throw Throwable("invalid style:${v.stringify()}")
                }).forEach {(k, v)->
                    process(k, v, this, memo, record, i, size)
                }
            }
        }
        if(this.isEmpty()) null else this
    }
}