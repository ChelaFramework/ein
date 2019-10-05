package ein.android.app

import android.graphics.Typeface
import ein.core.net.eRequest
import java.io.File
import java.io.FileOutputStream

typealias fontL = (Typeface)->Unit
object eFont{
    private val fonts = mutableMapOf<String, Typeface>()
    private val fontListener = mutableMapOf<String, MutableList<fontL>>()
    operator fun get(k:String, block:fontL):Boolean = fonts[k]?.let{
        if(it === Typeface.DEFAULT){
            if(fontListener[k] == null) fontListener[k] = mutableListOf()
            fontListener[k]?.let{it += block}
        }else block(it)
        true
    } ?: false
    operator fun minusAssign(k:String){remove(k)}
    fun remove(k:String) = fonts.remove(k)
    operator fun set(k:String, file: File) = fonts.put(k, Typeface.createFromFile(file))
    operator fun set(k:String, path:String){
        if(path.startsWith("http")){
            val f = File(eApp.fileDir, "ch_font_$k")
            if(f.exists() && f.length() > 0L) set(k, f)
            else{
                fonts[k] = Typeface.DEFAULT
                eRequest(path).send{res->res.bytes{data->
                        if(f.createNewFile()) FileOutputStream(f).use{it.write(data)}
                        set(k, f)
                        fonts[k]?.let{font->
                            fontListener[k]?.let{
                            it.forEach{it(font)}}
                            fontListener.remove(k)
                        }
                    }
                }
            }
        }else fonts[k] = Typeface.createFromAsset(eApp.asset, path)
    }
}