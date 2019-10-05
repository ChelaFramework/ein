package ein.core.core

inline class eStore(override val v:String):ePrimitive {
    override fun stringify() = "@{$v}"
}