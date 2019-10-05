package ein.core.sql

import ein.core.core.eJsonArray
import ein.core.core.eJsonObject
import ein.core.log.log
import ein.core.resource.eLoader
import ein.core.validation.eRuleSet

class eQuery(v:String) {
    companion object:eLoader{
        override fun load(res:eJsonObject) {
            (res["query"] as? eJsonObject)?.forEach{(k, v)->
                set(k){eQuery(when(v){
                    is eJsonArray->v.fold(""){acc, data->acc + " ${data.v}"}.substring(1)
                    else->"${v.v}"
                })}
            }
        }
        private val blocks = mutableMapOf<String, (String)->eQuery>()
        private val queries = mutableMapOf<String, eQuery>()
        operator fun get(k:String) = queries[k] ?: blocks[k]?.invoke(k)?.apply{queries[k] = this}
        operator fun set(k:String, block:(String)->eQuery){blocks[k] = block}
        operator fun minusAssign(k:String){
            blocks.remove(k)
            queries.remove(k)
        }
        fun remove(k:String) = minusAssign(k)
        private val rItem = """@(?:([^@:]+)(?::([^@:]+))?)@""".toRegex()
        private fun setItem(v:String, block:(k:String, v:String)->Unit):String = rItem.replace(v){
            val type = it.groupValues[2]
            block(it.groupValues[1], type)
            if(type.isBlank()) it.groupValues[0] else "?"
        }
    }
    private val items = mutableListOf<Map<String, eQueryItem>>()
    private val replacer = mutableListOf<Map<String, String>>()
    var msg = ""
    val query = run {
        v.split(";").map{
            var query = it.trim()
            query = if(query.toLowerCase().startsWith("select")) "r$query" else "w$query"
            val map = mutableMapOf<String, eQueryItem>()
            val rep = mutableMapOf<String, String>()
            items += map
            replacer += rep
            setItem(query){k, v->
                if(v.isBlank()) rep[k] = k
                else map[k] = eQueryItem(map.size, k, eRuleSet.getRuleset(v))
            }
        }
    }
    internal fun param(param:Array<out Pair<String, Any>>):List<Pair<String, Array<String>>>? = items.mapIndexed{i, item->_param(query[i], item, replacer[i], param) ?: return null}
    private fun _param(q:String, item:Map<String, eQueryItem>, rep:Map<String, String>, param:Array<out Pair<String, Any>>):Pair<String, Array<String>>?{
        val r = MutableList(item.size){""}
        var iCnt = 0
        var rCnt = 0
        var query = q
        param.forEach {(k, v)->
            rep[k]?.let {
                query = query.replace("@$k@", "$v")
                rCnt++
            }
            item[k]?.let {
                val (isOk, checked) = it.ruleSet.check(v)
                if(isOk){
                    r[it.i] = "$checked"
                    iCnt++
                }else{
                    msg = "invalid type:$k - ${it.ruleSet} - $checked"
                    return null
                }
            }
        }
        return if(iCnt != item.size || rCnt != rep.size){
            msg = "param not match:item($iCnt, ${item.size}) replace($rCnt, ${rep.size}) - $query"
            null
        }else query to r.toTypedArray()
    }
}
