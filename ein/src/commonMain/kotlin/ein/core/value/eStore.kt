package ein.core.value

class eStore(override val v:String):eValue {
    override fun stringify() = "s@$v@"
}