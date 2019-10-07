package ein.core.value

inline class eNull(override val v:Boolean = true):eValue {
    override fun stringify() = "null"
}