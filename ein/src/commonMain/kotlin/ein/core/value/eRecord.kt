package ein.core.value

class eRecord(override val v:String):eValue {
    override fun stringify() = "r@$v@"
}