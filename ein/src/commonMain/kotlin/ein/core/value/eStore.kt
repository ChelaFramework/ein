package ein.core.value

inline class eStore(override val v:String):eValue {
    override fun stringify() = "@{$v}"
}