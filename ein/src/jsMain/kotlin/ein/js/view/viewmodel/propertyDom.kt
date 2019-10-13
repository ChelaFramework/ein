package ein.js.view.viewmodel

import ein.core.core.uuid
import ein.core.value.eRemove
import ein.js.view.viewmodel.prop.*
import ein.core.view.viewmodel.eProperty
import ein.js.js.delete
import ein.js.js.isMobile
import ein.js.js.jsIn
import org.w3c.dom.HTMLElement
import org.w3c.dom.events.Event
import kotlin.browser.document
import kotlin.browser.window

object propertyDom:eProperty<HTMLElement>(){
    init{
        PropBase()
    }
    val rStyle = """-[a-z]""".toRegex()
    private val prefix = "webkit,Moz,chrome,ms".split(',')
    private val keys = mutableMapOf<String, String>()
    private val event = run{
        val win = window.asDynamic()
        win.__ein__ = js("{e:{}}")
        win.__ein__.e
    }
    private val evKey = if(isMobile) mutableMapOf(
        "down" to  "ontouchstart",
        "up" to "ontouchend",
        "move" to "ontouchmove"
    ) else mutableMapOf(
        "down" to "onmousedown",
        "up" to "onmouseup",
        "move" to "onmousemove"
    )
    private val body = document.body
    private val bodyStyle = body?.style
    private fun styleKey(k:String):String{
        val v = rStyle.replace(k){it.groupValues[0].substring(1).toUpperCase()}
        var r = ""
        bodyStyle?.let{
            val bs = it.asDynamic()
            if(bs[v] != null && bs[v] != undefined) r = v
            else{
                val vk = v[0].toUpperCase() + v.substring(1)
                prefix.any{
                    if(bs[it + vk] != null){
                        r = it + vk
                        true
                    }else false
                }
            }
        }
        keys[k] = r
        return r
    }
    private fun eventKey(k:String) = run {
        if(k !in evKey) evKey[k] = if("on$k" jsIn body) "on$k" else ""
        evKey[k]!!
    }
    override fun invoke(view:HTMLElement, k:String, v:Any) {
        when(k[0]) {
            'A'->attr(view, k.substring(1), v)
            else-> style(view, k, v) || event(view, k, v)
        }
    }
    fun style(view:HTMLElement, k:String, v:Any):Boolean{
        val key = keys[k] ?: styleKey(k)
        return if(key.isNotBlank()){
            view.style.asDynamic()[key] = if(v is eRemove) null else v
            true
        }else false
    }
    fun attr(view:HTMLElement, k:String, v:Any){
        if(v is eRemove) view.removeAttribute(k)
        else view.setAttribute(k, "$v")
    }
    fun event(view:HTMLElement, k:String, v:Any):Boolean{
        val key = eventKey(k)
        if(key.isBlank()) return false
        @Suppress("UNCHECKED_CAST")
        if(v is eRemove){
            view.getAttribute(k)?.let{
                if(it.isNotBlank()) delete(event[it.substring(it.indexOf('[') + 1, it.indexOf(']'))])
            }
            view.removeAttribute(k)
        }else (v as? (Event, HTMLElement)->Unit)?.let{
            val id = uuid()
            event[id] = it
            view.setAttribute(k, "__ch__.e['$id'](event, this)")
        }
        return true
    }
}