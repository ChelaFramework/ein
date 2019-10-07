package ein.core.value

class eString(override val v:String):eValue {
    override fun stringify() = "\"${v.replace("\"", "\\\"")}\""
}