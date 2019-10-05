package ein.core.core

class eJsonObject:MutableMap<String, ePrimitive> by mutableMapOf(), ePrimitive {
    override val v:MutableMap<String, ePrimitive> = this
    operator fun invoke(k:eString) = invoke(k.v.split("."))
    operator fun invoke(key:String) = invoke(key.split("."))
    operator fun invoke(key:List<String>)= key.fold(this as ePrimitive){target, k->
        when(target){
            is eJsonObject->target[k]
            is eJsonArray->target[k.toInt()]
            else->null
        } ?: throw Throwable("invalid key:$k")
    }
    override fun stringify():String{
        var r = ""
        forEach{(k, v)->r += ""","$k":${v.stringify()}"""}
        return "{${if(r.isNotBlank()) r.substring(1) else ""}}"
    }
    fun i(k:String, v:Int){this[k] = eInt(v)}
    fun f(k:String, v:Float){this[k] = eFloat(v)}
    fun d(k:String, v:Double){this[k] = eDouble(v)}
    fun l(k:String, v:Long){this[k] = eLong(v)}
    fun b(k:String, v:Boolean){this[k] = eBoolean(v)}
    fun s(k:String, v:String){this[k] = eString(v)}
    fun o(k:String, v:eJsonObject){this[k] = v}
    fun a(k:String, v:eJsonArray){this[k] = v}

    fun i(k:String):Int = getValue(k)
    fun f(k:String):Float = getValue(k)
    fun d(k:String):Double = getValue(k)
    fun l(k:String):Long = getValue(k)
    fun b(k:String):Boolean = getValue(k)
    fun s(k:String):String = getValue(k)
    fun o(k:String):eJsonObject = getValue(k)
    fun a(k:String):eJsonArray = getValue(k)
    @Suppress("UNCHECKED_CAST")
    fun <T>getValue(k:String) = invoke(k).v as T
    fun toTypedArray() = map {(k, v)->k to v.v}.toTypedArray()
}
