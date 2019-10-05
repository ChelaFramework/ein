package ein.core.core

inline class eRecord(override val v:String):ePrimitive{
    override fun stringify() = "\${$v}"
}