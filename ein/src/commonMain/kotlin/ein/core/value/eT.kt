package ein.core.value

class eT<T:Any>(val value:T):eValue {
    override val v = value
    override fun stringify() = "$v"
}