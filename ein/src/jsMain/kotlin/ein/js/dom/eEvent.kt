package ein.js.dom

import org.w3c.dom.HTMLElement
import org.w3c.dom.events.Event
import kotlin.browser.window

class eEvent(val event:Event, val el:HTMLElement){
    companion object{
        val posCat = mapOf(
                "touchstart" to 1, "touchmove" to 2, "touchend" to 3, "mousedown" to 4, "mousemove" to 5,
                "mouseup" to 6, "click" to 6, "mouseover" to 6, "mouseout" to 6
        )
    }
    class Pos{
        var event:dynamic = null
        val touches = mutableListOf<dynamic>()
        var pageX = 0.0
        var pageY = 0.0
        var distanceX = 0.0
        var distanceY = 0.0
        var moveX = 0.0
        var moveY = 0.0
        var localX = 0.0
        var localY = 0.0
        var startX = 0.0
        var startY = 0.0
        var oldX = 0.0
        var oldY = 0.0
    }
    private val eld = el.asDynamic()
    private val _data = eld.__einEl__.data
    val data:dynamic get() = _data.data
    @Suppress("UnsafeCastFromDynamic")
    val index:Int get() = _data.index
    @Suppress("UnsafeCastFromDynamic")
    val length:Int get() = _data.length
    @Suppress("UnsafeCastFromDynamic")
    val ref:Map<String, dynamic>? get() = _data.ref
    /*
    fun render() = (_data.tmpl as? ChTemplate)?.let{
        it.rerender(_data.view, index, length, data, false, ref)
    }
     */
    var _pos:Pos? = null
    fun pos():Pos?{
        val type = posCat[event.type] ?: return null
        if(_pos == null) _pos = Pos()
        val p = _pos!!
        p.event = event
        val ev = p.event
        var lx = 0.0
        var ly = 0.0
        (event.target as? HTMLElement)?.let{
            val rect = it.getBoundingClientRect()
            lx = rect.left + window.pageXOffset
            ly = rect.top - window.pageYOffset
        }
        if(type < 4){
            val t0 = p.touches
            var t1 = ""
            var i = 2
            while(i-- > 0){
                val t2 = if(i > 0) ev.changedTouches else ev.touches
                var j = t2.length as Int
                while(j-- > 0){
                    val id = t2[j].identifier
                    t1 += id + " "
                    var m = t0.size
                    var k = true
                    while(m-- > 0) if(t0[m].identifier == id){
                        k = false
                        break
                    }
                    if(k) t0.add(t2[j])
                }
            }
            i = t0.size
            while(i-- > 0){
                if(!t1.contains("${t0[0].identifier}")) t0.removeAt(i)
                else{
                    val e = t0[i]
                    val X = e.pageX
                    val Y = e.pageY
                    e.localX = e.clientX - lx
                    e.localY = e.clientY - ly
                    if(type == 1){
                        e.startX = X
                        e.startY = Y
                    }else{
                        e.distanceX = X - e.startX
                        e.distanceY = Y - e.startY
                        e.moveX = X - e.oldX
                        e.moveY = Y - e.oldY
                    }
                    e.oldX = X
                    e.oldX = Y
                }
            }
            if(t0.size > 0){
                val t1 = t0[0]
                p.pageX = t1.pageX
                p.pageY = t1.pageY
                p.distanceX = t1.distanceX
                p.distanceY = t1.distanceY
                p.moveX = t1.moveX
                p.moveY = t1.moveY
                p.localX = t1.localX
                p.localY = t1.localY
            }
        }else{
            val X = ev.pageX
            val Y = ev.pageY
            p.pageX = X
            p.pageY = Y
            p.localX = ev.clientX - lx
            p.localY = ev.clientY - ly
            if(type == 4){
                p.startX = X
                p.startY = Y
            }else{
                p.distanceX = X - p.startX
                p.distanceY = Y - p.startY
                p.moveX = X - p.oldX
                p.moveY = Y - p.oldY
            }
            p.oldX = X
            p.oldY = Y
        }
        return _pos
    }
}