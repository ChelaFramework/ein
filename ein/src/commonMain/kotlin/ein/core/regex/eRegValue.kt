package ein.core.regex

import ein.core.core.ePrimitive
import ein.core.core.eRecord
import ein.core.core.eStore
import ein.core.log.log

object eRegValue: eReg("""^\s*""" +
        //1,2-string
        """(?:"((?:[^\\"]+|\\["\\bfnrt]|\\u[0-9a-fA-invoke]{4})*)"|`((?:[^`]+|\\[`\\bfnrt]|\\u[0-9a-fA-invoke]{4})*)`|""" +
        //3-long
        """(-?(?:0|[1-9]\d*)(?:dp|sp|%w|%h)?)|""" +
        //4-double
        """(-?(?:0|[1-9]\d*)(?:\.\d+)(?:[eE][-+]?\d+)?(?:dp|%w|%h)?)|"""+
        //5-bool
        """(true|false)|""" +
        //6-store, 7-record
        """(?:\@\{([^}]+)\})|(?:\$\{([^}]*)\}))\s*"""
){
    var dp = 1.0
    var sp = 1.0
    var width = 1.0
    var height = 1.0
    operator fun invoke(v:String) = re.find(v)?.let{
        val g = it.groups
        g[1]?.let{ePrimitive(it.value)} ?:
        g[2]?.let{ePrimitive(it.value)} ?:
        g[3]?.let{ePrimitive(group3(it))} ?:
        g[4]?.let{ePrimitive(group4(it))} ?:
        g[5]?.let{ePrimitive(it.value.toBoolean())} ?:
        g[6]?.let{eStore(it.value)} ?:
        g[7]?.let{eRecord(it.value)}
    }
    fun group3(it:MatchGroup):Long{
        val v = it.value
        return when {
            v.endsWith("dp")->(v.substring(0, v.length - 2).toDouble()*dp).toLong()
            v.endsWith("sp")->(v.substring(0, v.length - 2).toDouble()*sp).toLong()
            v.endsWith("%w")->(v.substring(0, v.length - 2).toDouble()*width).toLong()
            v.endsWith("%h")->(v.substring(0, v.length - 2).toDouble()*height).toLong()
            else->v.toLong()
        }
    }
    fun group4(it:MatchGroup):Double{
        val v = it.value
        return when {
            v.endsWith("dp") -> v.substring(0, v.length - 2).toDouble()*dp
            v.endsWith("sp") -> v.substring(0, v.length - 2).toDouble()*sp
            v.endsWith("%w") -> v.substring(0, v.length - 2).toDouble()* width
            v.endsWith("%h") -> v.substring(0, v.length - 2).toDouble()* height
            else -> v.toDouble()
        }
    }
    fun num(it:String):Number?{
        return re.find(it)?.let{
            it.groups[3]?.let{group3(it)} ?: it.groups[4]?.let{group4(it)}
        } as Number?
    }
}