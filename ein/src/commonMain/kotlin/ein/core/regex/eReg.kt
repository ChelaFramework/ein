package ein.core.regex

abstract class eReg(r:String, vararg arg:RegexOption){
    internal val re = if(arg.isEmpty()) r.toRegex() else r.toRegex(arg.toSet())
    companion object{
        val value = ein.core.regex.eRegValue
        val param = ein.core.regex.param
    }
}