package ein.core.value

class eJsonArray:MutableList<eValue> by mutableListOf(), eValue {
    override val v:MutableList<eValue> = this
    override fun stringify():String{
        val v= fold(""){r, v->r + ",${v.stringify()}"}
        return "[${if(v.isNotBlank()) v.substring(1) else ""}]"
    }
}