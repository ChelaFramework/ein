package ein.js.dom

import org.w3c.dom.HTMLElement
import org.w3c.dom.NodeList
import org.w3c.dom.get

interface DomUtil{
    companion object{
    }
}
inline fun NodeList.toList() = mutableListOf<HTMLElement>().also{
    var i = 0
    while(i < length) it += this[i++] as HTMLElement
}
inline fun HTMLElement.indexOfChild(v:HTMLElement):Int{
    var idx = 0
    var child = firstElementChild
    while(idx < 200 && child != null && child !== v){
        idx++
        child = child.nextElementSibling
    }
    return idx
}
inline fun HTMLElement.getChildAt(i:Int):HTMLElement?{
    var child = firstElementChild
    var idx = 0
    while(child != null && idx++ != i) child = child.nextElementSibling
    return child as? HTMLElement
}