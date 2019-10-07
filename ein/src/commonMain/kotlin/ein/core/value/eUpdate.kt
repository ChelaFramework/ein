package ein.core.value

inline class eUpdate(override val v:Any):eValue {
    override fun stringify() = if(v is String) "\"${v.replace("\"", "\\\"")}\"" else "$v"
}