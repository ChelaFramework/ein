package ein.core.core

import ein.core.view.viewmodel.eViewModel

interface ePrimitive {
    companion object {
        val EMPTY by elazy(true) {ePrimitive("")}
        private val srReg = """^\s*(?:\@\{([^}]+)\})|(?:\$\{([^}]*)\})\s*$""".toRegex()
        private val stores = mutableMapOf<String, Any>()

        fun json(v:String) = parseJSON(v)
        operator fun invoke(v:ePrimitive) = v
        operator fun invoke(v:Int) = eInt(v)
        operator fun invoke(v:Long) = eLong(v)
        operator fun invoke(v:Double) = eDouble(v)
        operator fun invoke(v:Boolean) = eBoolean(v)
        operator fun invoke(v:String) = srReg.find(v)?.groupValues?.run{
            if(this[1].isNotBlank()) eStore(this[1]) else eRecord(this[2])
        } ?: eString(v)
        operator fun invoke(v:Map<*, *>):eJsonObject = eJsonObject().apply{
            v.forEach{(k, v)->this["$k"] = convert(v)}
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
            val key = k.last()
            when(val target = get("@{${k.dropLast(1)}}")){
                is MutableMap<*, *>->(target as? MutableMap<String, T>)?.put(key, v)
                is MutableList<*>->(target as? MutableList<T>)?.add(key.toInt(), v)
                is eJsonObject->target[key] = ePrimitive(v)
                is eJsonArray->target[key.toInt()] = ePrimitive(v)
                else->throw Throwable("invalid key:$k")
            }
        }
        operator fun get(k:Any, record:Any? = null, idx:Int = 0, size:Int = 0):Any{
            var key = when(k){
                is String->k
                is eString->k.v
                is eStore->k.stringify()
                is eRecord->k.stringify()
                is ePrimitive->return k.v
                else->return k
            }
            var i = 100
            do{
                key = srReg.find(key)?.groupValues?.let{
                    val isStore = it[1].isNotBlank()
                    (if(isStore) it[1] else it[2]).split(".").fold(
                        if(isStore) stores else record ?: throw Throwable("no record")
                    ){target, k->
                        when(k){
                            ":index"->idx
                            ":size"->size
                            "",":record"->record!!
                            else->when(target){
                                is Map<*, *>->target[k]
                                is List<*>->target[k.toInt()]
                                is eJsonObject->target[k]
                                is eJsonArray->target[k.toInt()]
                                is eViewModel->target[k]
                                else->null
                            } ?: throw Throwable("invalid key:$k of $key")
                        }
                    }.let{
                        when(it){
                            is eString->it.v
                            is eStore->it.stringify()
                            is eRecord->it.stringify()
                            is ePrimitive->return it.v
                            else->return it
                        }
                    }
                } ?: return key
            }while(i-- > 0)
            return ""
        }

    }
    val v:Any
    fun stringify():String
}
inline class eUpdate(override val v:Any):ePrimitive{
    override fun stringify() = if(v is String) "\"${v.replace("\"", "\\\"")}\"" else "$v"
}
class eOnce(override val v:Any):ePrimitive{
    var isRun = false
    override fun stringify() = if(v is String) "\"${v.replace("\"", "\\\"")}\"" else "$v"
}
inline class eString(override val v:String):ePrimitive{
    override fun stringify() = "\"${v.replace("\"", "\\\"")}\""
}
inline class eInt(override val v:Int):ePrimitive{
    override fun stringify() = "$v"
}
inline class eLong(override val v:Long):ePrimitive{
    override fun stringify() = "$v"
}
inline class eDouble(override val v:Double):ePrimitive{
    override fun stringify() = "$v"
}
inline class eFloat(override val v:Float):ePrimitive{
    override fun stringify() = "$v"
}
inline class eBoolean(override val v:Boolean):ePrimitive{
    override fun stringify() = "$v"
}
inline class eAny(override val v:Any):ePrimitive{
    override fun stringify() = "$v"
}
inline class eBytes(override val v:ByteArray):ePrimitive{
    override fun stringify() = "$v"
}
inline class eNull(override val v:Boolean = true):ePrimitive{
    override fun stringify() = "null"
}
class eT<T:Any>(val value:T):ePrimitive{
    override val v = value
    override fun stringify() = "$v"
}
fun <T, R> Map<T,R>.stringify() = ePrimitive(this).stringify()
fun <T> List<T>.stringify() = ePrimitive(this).stringify()
fun <T> Set<T>.stringify() = ePrimitive(this).stringify()
infix fun <A, B, C> Pair<A, B>.t3(that:C) = Triple(this.first, this.second, that)
infix fun <A, B, C> Pair<A, B>.t2(that:C) = Triple(this.first, that, this.second)
infix fun <A, B, C> Pair<A, B>.t1(that:C) = Triple(that, this.first, this.second)