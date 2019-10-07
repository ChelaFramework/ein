package ein.core.value

inline class eRecord(override val v:String):eValue {
    override fun stringify() = "\${$v}"
}