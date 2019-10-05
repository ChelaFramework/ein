import ein.js.looper.mainLooper
import org.w3c.dom.HTMLDivElement
import kotlin.browser.document

fun main(){
    val el = document.createElement("div") as HTMLDivElement
    el.style.cssText = "left:0;top:0;width:100px;height:100px;position:absolute;background:red"
    document.body?.append(el)
    val fps = document.createElement("div") as HTMLDivElement
    fps.style.cssText = "right:0;top:0;width:150px;height:20px;position:absolute;background:#ff0;font-size:11px"
    document.body?.append(el)
    document.body?.append(fps)
    mainLooper.resume()
    mainLooper.item{
        delay = 200
        time = 1000
        block = {
            val v = it.backOut(0.0, 200.0)
            el.style.transform = "translateX(${v}px) translateY(${v}px)"
            fps.innerHTML = "fps:${mainLooper.fps}"
        }
    }.item {
        time = 1000
        block = {
            val v = it.backOut(200.0, 0.0)
            el.style.transform = "translateX(${v}px) translateY(${v}px)"
            fps.innerHTML = "fps:${mainLooper.fps}"
        }
    }
}