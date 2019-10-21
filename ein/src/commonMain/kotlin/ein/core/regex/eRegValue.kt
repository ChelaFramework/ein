package ein.core.regex

import ein.core.value.*

val regString = """"((?:\\"|[^"])*)"|`([^`]*)`"""
val regStore = "(?:s@([^@]+)@)"
val regRecord = "(?:r@([^@]*)@)"
val regDouble = """(-?(?:0|[1-9]\d*)(?:\.\d+)(?:[eE][-+]?\d+)?(?:dp|%w|%h)?)"""
val regLong = """(-?(?:0|[1-9]\d*)(?:dp|sp|%w|%h)?)"""
val regBool = "(true|false)"
val regNull = "(null)"
object eRegValue: eReg("""^\s*(?:$regString|$regStore|$regRecord|$regDouble|$regLong|$regBool|$regNull)\s*"""){
    var dp = 1.0
    var sp = 1.0
    var width = 1.0
    var height = 1.0
    private val rNum = """^\s*(?:$regDouble|$regLong)\s*$""".toRegex()
    operator fun invoke(v:String) = re.find(v)?.groups?.let{
        it[1]?.run{eString(value.replace("\\\"", "\""))} ?:
        it[2]?.run{eString(value)} ?:
        it[3]?.run{eStore(value)} ?:
        it[4]?.run{eRecord(value)} ?:
        it[5]?.run{eDouble(dbl(this))} ?:
        it[6]?.run{eLong(lng(this))} ?:
        it[7]?.run{eBoolean(value.toBoolean())} ?:
        it[8]?.run{eNull()}
    }
    fun dbl(it:MatchGroup) = it.value.run{
        when {
            endsWith("dp") -> substring(0, length - 2).toDouble()*dp
            endsWith("sp") -> substring(0, length - 2).toDouble()*sp
            endsWith("%w") -> substring(0, length - 2).toDouble()* width
            endsWith("%h") -> substring(0, length - 2).toDouble()* height
            else -> toDouble()
        }
    }
    fun lng(it:MatchGroup) = dbl(it).toLong()
    fun num(it:String):Number?{
        return rNum.matchEntire(it)?.let{
            it.groups[1]?.let{dbl(it)} ?: it.groups[2]?.let{lng(it)}
        } as Number?
    }
}