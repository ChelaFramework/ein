package ein.core.value

class eOnce(override val v:Any):eValue {
    var isRun = false
    override fun stringify() = if(v is String) "\"${v.replace("\"", "\\\"")}\"" else "$v"
}