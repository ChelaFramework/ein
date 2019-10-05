package ein.js.net

import ein.core.core.ePrimitive
import ein.core.core.elazy
import ein.core.net.eResponse
import org.khronos.webgl.ArrayBuffer
import org.khronos.webgl.Int8Array
import org.khronos.webgl.get
import org.w3c.fetch.Response
import org.w3c.files.FileReader

class eResponseFetch(private val res:Response?, error:String?):eResponse(error){
    private var isOpened = false
    override val state by elazy(true) {res?.status?.toInt() ?: 0}
    override fun header(k:String) = res?.headers?.get(k) ?: ""
    override fun text(block:(String)->Unit) {
        if(isOpened) throw Throwable("is opened")
        isOpened = true
        res?.text()?.then(block)
    }
    override fun json(block:(ePrimitive?)->Unit) {
        if(isOpened) throw  Throwable("is opened")
        isOpened = true
        res?.text()?.then{block(ePrimitive.json(it))}
    }
    override fun bytes(block:(ByteArray?)->Unit) {
        if(isOpened) throw  Throwable("is opened")
        isOpened = true
        res?.blob()?.then{
            var buf:ArrayBuffer? = null
            val fileReader = FileReader()
            fileReader.onload = {
                val r = Int8Array((fileReader.result as ArrayBuffer))
                var i = 0
                val j = r.byteLength
                val bytes = ByteArray(j)
                while(i < j){
                    bytes.set(i, r.get(i))
                    i++
                }
                block(bytes)
                1
            };
            fileReader.readAsArrayBuffer(it)
        }
    }
}