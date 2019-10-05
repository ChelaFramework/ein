package ein.core.net

import ein.core.core.eEncodeUrl
import ein.core.core.eJsonObject

class eRequest(internal var url:String, me:String = "GET"){
    internal class File(val filename:String, val mine:String, val file:ByteArray)
    val extra = mutableMapOf<String, Any>()
    internal val method = me.toUpperCase()
    internal var header:MutableMap<String, String>? = null
    internal var form:MutableMap<String, String>? = null
    internal var json:String? = null
    internal var text:String? = null
    internal var blob:ByteArray? = null
    private var retry = 1
    internal var readTimeout = 5000
    internal var connectTimeout = 3000
    internal var files:MutableMap<String, File>? = null
    private var isUsed = false
    fun header(key:String, value:String){
        if(header == null) header = mutableMapOf()
        header?.put(key, value)
    }
    fun header(v:eJsonObject){
        if(header == null) header = mutableMapOf()
        v.forEach{(k, v)->header?.put(k, v.toString())}
    }
    fun form(key:String, value:String){
        if(form == null) form = mutableMapOf()
        form?.put(key, value)
    }
    fun form(v:eJsonObject){
        if(form == null) form = mutableMapOf()
        v.forEach{(k, v)->form?.put(k, v.toString())}
    }
    fun json(v:String){json = v}
    fun json(v:eJsonObject){json = v.stringify()}
    fun body(v:String){text = v}
    fun body(v:eJsonObject){text = v.stringify()}
    fun bytes(v:ByteArray){blob = v}
    fun retry(v:Int){retry = v}
    fun timeout(read:Int, connect:Int = 3000){
        readTimeout = read
        connectTimeout = connect
    }
    fun file(key:String, filename:String, mine:String, file:ByteArray){
        if(files == null) files = mutableMapOf()
        files?.put(key, File(filename, mine, file))
    }
    fun send(block:(eResponse)->Unit){
        if(isUsed) throw Throwable("used request")
        isUsed = true
        val i = url.lastIndexOf('#')
        if(i != -1) url = url.substring(0, i)
        if(method == "GET") form?.let {
            url += (if(url.lastIndexOf('?') == -1) "?" else "&") +
                    it.map {(k, v)-> "${eEncodeUrl(k)}=${eEncodeUrl(v)}"}.joinToString("&")
        }
        send(this, block)
    }
}
