package ein.jvm.net

import ein.core.value.eValue
import ein.core.core.elazy
import ein.core.net.eResponse
import okhttp3.Response

class eResponseOkHttp(private val res:Response?, error:String?):eResponse(error){
    private var isOpened = false
    override val state:Int by elazy(true) {res?.code() ?: 0}
    override fun header(k:String) = res?.header(k) ?: ""
    override fun text(block:(String)->Unit) {
        if(isOpened) throw Throwable("is opened")
        isOpened = true
        block(res?.body()?.use{it.string()} ?: throw Throwable("invalid text"))
    }
    override fun json(block:(eValue?)->Unit) {
        if(isOpened) throw  Throwable("is opened")
        isOpened = true
        block(res?.body()?.use{eValue.json(it.string())} ?: throw Throwable("invalid json"))
    }

    override fun bytes(block:(ByteArray?)->Unit) {
        if(isOpened) throw  Throwable("is opened")
        isOpened = true
        block(res?.body()?.use{it.bytes()} ?: throw Throwable("invalid bytes"))
    }
}