package ein.core.value

inline class eString(override val v:String):eValue {
    override fun stringify() = "\"${v.replace("\"", "\\\"")}\""
}