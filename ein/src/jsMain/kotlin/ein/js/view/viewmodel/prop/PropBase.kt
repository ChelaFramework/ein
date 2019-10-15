package ein.js.view.viewmodel.prop

import ein.core.core.eNN
import ein.core.core.eRunnable
import ein.core.looper.getLooper
import ein.core.value.eRemove
import ein.js.view.viewmodel.propertyDom
import org.w3c.dom.*
import kotlin.browser.window

fun PropBase() = propertyDom.let{
    it["className"] = fun(view:HTMLElement, v:Any){view.className = "$v"}
    it["className_"] = fun(view:HTMLElement, v:Any){view.className += " $v"}
    it["html"] = fun(view:HTMLElement, v:Any){view.innerHTML = "$v"}
    it["html_"] = fun(view:HTMLElement, v:Any){view.innerHTML += "$v"}
    it["_html"] = fun(view:HTMLElement, v:Any){view.innerHTML = "$v" + view.innerHTML}
    it["name"] = fun(view:HTMLElement, v:Any){
        when(view){
            is HTMLFormElement-> view.name = "$v"
            is HTMLInputElement-> view.name = "$v"
        }
    }
    it["runSubmit"] = fun(view:HTMLElement, v:Any){if(v == true)(view as? HTMLFormElement)?.submit()}
    it["runFocus"] = fun(view:HTMLElement, v:Any){if(v == true)view.focus()}
    it["runBlur"] = fun(view:HTMLElement, v:Any){if(v == true)view.blur()}
    it["disabled"] = fun(view:HTMLElement, v:Any){
        if(v !is Boolean) return
        when(view) {
            is HTMLInputElement->view.disabled = v
            is HTMLButtonElement->view.disabled = v
            is HTMLSelectElement->view.disabled = v
            is HTMLOptGroupElement->view.disabled = v
            is HTMLOptionElement->view.disabled = v
            is HTMLTextAreaElement->view.disabled = v
        }
    }
    it["checked"] = fun(view:HTMLElement, v:Any) = eNN(view as? HTMLInputElement, v as Boolean){el, b->el.checked = b}
    it["selected"] = fun(view:HTMLElement, v:Any) = eNN(view as? HTMLOptionElement, v as Boolean){el, b->el.selected = b}
    it["selectedIndex"] = fun(view:HTMLElement, v:Any) = eNN(view as? HTMLSelectElement, v as Int){el, i->el.selectedIndex = i}
    it["unselect"] = fun(view:HTMLElement, v:Any){
        if(v !is Boolean) return
        if(v){
            it.style(view, "user-select", "none")
            it.style(view, "touch-callout", "none")
            view.setAttribute("unselectable", "on")
            view.setAttribute("onselectstart", "return false")
        }else{
            it.style(view, "user-select", "null")
            it.style(view, "touch-callout", "null")
            view.removeAttribute("unselectable")
            view.removeAttribute("onselectstart")
        }
    }
    it["value"] = fun(view:HTMLElement, v:Any){
        if(v is eRemove) {
            view.removeAttribute("value")
            when(view) {
                is HTMLSelectElement->view.value = ""
                is HTMLInputElement->view.value = ""
            }
        }else{
            view.setAttribute("value", "$v")
            when(view) {
                is HTMLSelectElement->view.value = "$v"
                is HTMLInputElement->view.value = "$v"
            }
        }
    }
    it["lazySrc"] = fun(view:HTMLElement, v:Any){
        @Suppress("UNCHECKED_CAST")
        (v as? Pair<String, String>)?.let {(low, high)->
            if(window.innerHeight + 100 > view.getBoundingClientRect().top) view.setAttribute("src", high)
            else {
                if(low.isNotBlank()) view.setAttribute("src", low)
                getLooper().block {item->
                    if(window.innerHeight + 50 > view.getBoundingClientRect().top){
                        view.setAttribute("src", high)
                        item.stop()
                    }
                }
            }
        }
    }
    it["topViewPort"] = fun(view:HTMLElement, v:Any){
        if(v !is eRunnable) return
        if(window.innerHeight > view.getBoundingClientRect().top) v()
        else getLooper().block {item->
            if(window.innerHeight > view.getBoundingClientRect().top){
                v()
                item.stop()
            }
        }
    }
    it["rectHeight"] = fun(view:HTMLElement, v:Any){
        if(v == true) view.style.height = "${window.innerHeight - view.getBoundingClientRect().top}px"
    }
}