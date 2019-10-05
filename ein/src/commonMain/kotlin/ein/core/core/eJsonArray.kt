package ein.core.core

class eJsonArray:MutableList<ePrimitive> by mutableListOf(), ePrimitive {
    override val v:MutableList<ePrimitive> = this
    override fun stringify():String{
        val v= fold(""){r, v->r + ",${v.stringify()}"}
        return "[${if(v.isNotBlank()) v.substring(1) else ""}]"
    }
}