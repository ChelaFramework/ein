package ein.android.app

import ein.core.looper.getLooper
import java.nio.charset.Charset

object eAsset{
    private val asset = mutableMapOf<String, ByteArray>()
    fun bytes(path:String) = asset[path] ?: try {
        eApp.asset.open(path).use {
            val size = it.available()
            val buf = ByteArray(size)
            it.read(buf)
            asset[path] = buf
            buf
        }
    }catch(e:Throwable){null}
    fun bytes(path: String, block:(ByteArray)->Unit) = bytes(path)?.let{getLooper().run{block(it)}}
    fun string(path: String) = bytes(path)?.toString(Charset.defaultCharset())
    fun string(path: String, block:(String)->Unit) = string(path)?.let{getLooper().run{block(it)}}
}