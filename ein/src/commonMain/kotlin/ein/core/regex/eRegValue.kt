package ein.core.regex

import ein.core.value.*

val regBool = """(true|false)"""
val regNull = """(null)"""
val regStore = """(?:\@\{([^}]+)\})"""
val regRecord = """(?:\$\{([^}]*)\})"""
val regDouble = """(-?(?:0|[1-9]\d*)(?:\.\d+)(?:[eE][-+]?\d+)?(?:dp|%w|%h)?)"""
val regLong = """(-?(?:0|[1-9]\d*)(?:dp|sp|%w|%h)?)"""
val regString = """\"((?:\\"|[^"])*)\"|`([^`]*)`"""
                                //1-bool,2-null,   3-store, 4-record,  5-double,  6-long,  7,8-string
object eRegValue: eReg("""^\s*(?:$regBool|$regNull|$regStore|$regRecord|$regDouble|$regLong|$regString)\s*"""){
    var dp = 1.0
    var sp = 1.0
    var width = 1.0
    var height = 1.0
    operator fun invoke(v:String) = re.find(v)?.let{
        val g = it.groups
        g[1]?.let{eBoolean(it.value.toBoolean())} ?:
        g[2]?.let{eNull()} ?:
        g[3]?.let{eStore(it.value)} ?:
        g[4]?.let{eRecord(it.value)} ?:
        g[5]?.let{eDouble(dbl(it))} ?:
        g[6]?.let{eLong(lng(it))} ?:
        g[7]?.let{eString(it.value.replace("\\\"", "\""))} ?:
        g[8]?.let{eString(it.value)}

    }
    fun dbl(it:MatchGroup):Double{
        val v = it.value
        return when {
            v.endsWith("dp") -> v.substring(0, v.length - 2).toDouble()*dp
            v.endsWith("sp") -> v.substring(0, v.length - 2).toDouble()*sp
            v.endsWith("%w") -> v.substring(0, v.length - 2).toDouble()* width
            v.endsWith("%h") -> v.substring(0, v.length - 2).toDouble()* height
            else -> v.toDouble()
        }
    }
    fun lng(it:MatchGroup):Long{
        val v = it.value
        return when {
            v.endsWith("dp")->(v.substring(0, v.length - 2).toDouble()*dp).toLong()
            v.endsWith("sp")->(v.substring(0, v.length - 2).toDouble()*sp).toLong()
            v.endsWith("%w")->(v.substring(0, v.length - 2).toDouble()*width).toLong()
            v.endsWith("%h")->(v.substring(0, v.length - 2).toDouble()*height).toLong()
            else->v.toLong()
        }
    }
    fun num(it:String):Number?{
        return re.find(it)?.let{
            it.groups[3]?.let{dbl(it)} ?: it.groups[4]?.let{lng(it)}
        } as Number?
    }
}