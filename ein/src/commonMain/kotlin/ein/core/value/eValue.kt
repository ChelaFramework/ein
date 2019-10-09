package ein.core.value

import ein.core.cdata.eCdata
import ein.core.core.elazy
import ein.core.log.log
import ein.core.regex.regRecord
import ein.core.regex.regStore
import ein.core.view.viewmodel.eViewModel

interface eValue {
    companion object {
        val EMPTY by elazy(true) {eValue("")}
        val srReg = """^\s*$regStore|$regRecord\s*$""".toRegex()
        private val stores = mutableMapOf<String, Any>()

        fun json(v:String) = parseJSON(v)
        operator fun invoke(v:eValue) = v
        operator fun invoke(v:Int) = eInt(v)
        operator fun invoke(v:Long) = eLong(v)
        operator fun invoke(v:Float) = eFloat(v)
        operator fun invoke(v:Double) = eDouble(v)
        operator fun invoke(v:Boolean) = eBoolean(v)
        operator fun invoke(v:String) = if(v.isNotBlank() && v[0] == '{') json(v)
            else srReg.find(v)?.groupValues?.run{
                if(this[1].isNotBlank()) eStore(this[1]) else eRecord(this[2])
            } ?: eString(v)
        operator fun invoke(v:Map<String, *>):eJsonObject = eJsonObject().apply{
            v.forEach{(k, v)->this[k] = convert(v)}
        }
        operator fun invoke(v:List<Any?>):eJsonArray = eJsonArray().apply{this += v.map{convert(it)}}
        operator fun invoke(v:Set<Any?>):eJsonArray = eJsonArray().apply{this += v.map{convert(it)}}
        operator fun invoke(v:Any) = eAny(v)
        fun rs2Json(rs:Array<Array<Any?>>) = "[" + rs.fold(""){acc, record->
            acc + "," + invoke(record.toList()).stringify()
        }.substring(1) + "]"

        private fun convert(v:Any?) = when(v){
            is String->eString(v)
            is Int->eInt(v)
            is Long->eLong(v)
            is Double->eDouble(v)
            is Float->eFloat(v)
            is Boolean->eBoolean(v)
            is Set<*>->invoke(v)
            is List<*>->invoke(v)
            is Map<*, *>->invoke(v)
            else->eNull()
        }
        operator fun <T:Any>set(k:String, v:T) = set(k.split('.'), v)
        operator fun <T:Any>set(k:eString, v:T) = set(k.v.split('.'), v)
        operator fun <T:Any>set(k:eStore, v:T) = set(k.v.split('.'), v)
        @Suppress("UNCHECKED_CAST")
        operator fun <T:Any>set(k:List<String>, v:T){
            if(k.size == 1){
                stores[k[0]] = v
                return
            }
            val key = k.last()
            when(val target = get(eStore(k.dropLast(1).joinToString(".")))){
                is eViewModel->target[key] = v
                is eJsonObject->target[key] = eValue(v)
                is eJsonArray->target[key.toInt()] = eValue(v)
                is MutableMap<*, *>->(target as? MutableMap<String, T>)?.put(key, v)
                is MutableList<*>->(target as? MutableList<T>)?.add(key.toInt(), v)
                else->throw Throwable("invalid key:$k")
            }
        }
        operator fun get(k:Any, record:Any? = null, idx:Int = 0, size:Int = 0):Any{
            var i = 10
            var r:Any = k
            do {
                val (search, store) = when(r) {
                    is eStore->r.v to stores
                    is eRecord->r.v to (record ?: throw Throwable("no record for $k"))
                    is eString->srReg.find(r.v)?.groupValues?.let{
                        if(it[1].isNotBlank()) it[1] to stores else it[2] to (record ?: throw Throwable("no record for $k"))
                    } ?: return r.v
                    is eValue->return r.v
                    is String->srReg.find(r)?.groupValues?.let{
                        if(it[1].isNotBlank()) it[1] to stores else it[2] to (record ?: throw Throwable("no record for $k"))
                    } ?: return r
                    else->return r
                }
                r = search.split(".").fold(store){acc, v->
                    when(v){
                        ":index"->idx
                        ":size"->size
                        "", ":record"->record!!
                        else->when(acc) {
                            is eViewModel->acc[v]
                            is eCdata->eCdata[v]
                            is eJsonObject->acc[v]
                            is eJsonArray->acc[v.toInt()]
                            is Map<*, *>->acc[v]
                            is List<*>->acc[v.toInt()]
                            else->null
                        } ?: throw Throwable("invalid key:$v of $r")
                    }
                }
            }while(i-- > 0)
            throw Throwable("invalid k:$k")
        }

    }
    val v:Any
    fun stringify() = "$v"
}

fun <T, R> Map<T,R>.stringify() = eValue(this).stringify()
fun <T> List<T>.stringify() = eValue(this).stringify()
fun <T> Set<T>.stringify() = eValue(this).stringify()
