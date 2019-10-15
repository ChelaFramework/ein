package ein.js.view.viewmodel

import ein.core.log.log
import ein.core.value.eJsonObject
import ein.core.view.viewmodel.eItem
import ein.core.view.viewmodel.eProcessor
import ein.core.view.viewmodel.eTemplate
import ein.core.view.viewmodel.eViewModel
import ein.js.dom.getChildAt
import ein.js.dom.indexOfChild
import ein.js.dom.toList
import ein.js.js.obj
import org.w3c.dom.HTMLElement

object processorDom:eProcessor<HTMLElement>() {
    const val KEY = "data-ein"
    const val TMPL = "data-eint"
    override fun getStack(view:HTMLElement) = view.querySelectorAll("[$KEY]").toList().also{
        if(!view.getAttribute(KEY).isNullOrBlank()) it += view
    }
    override fun getItem(root:HTMLElement, view:HTMLElement) = mutableListOf<Int>().run{
        var target = view
        var limit = 30
        while(target !== root && limit-- > 0){
            (target.parentElement as? HTMLElement)?.let {
                add(it.indexOfChild(target))
                target = it
            } ?: break
        }
        val data = view.getAttribute(KEY) ?: ""
        view.removeAttribute(KEY)
        eItem(this, data)
    }
    override fun getItemView(view:HTMLElement, pos:List<Int>):HTMLElement {
        var target = view
        var i = pos.size
        while(i-- > 0) target = target.getChildAt(pos[i]) ?: throw Throwable("invalid idx:${pos[i]} in ${target.outerHTML}")
        return target
    }
    override fun template(view:HTMLElement) = view.querySelectorAll("[$TMPL]").toList().run{
        if(!view.getAttribute(TMPL).isNullOrBlank()) this += view
        forEach{it.parentElement?.removeChild(it)}
        //st.forEach{ChTemplate += it}
    }
    override fun beforeItemRender(root:HTMLElement?, view:HTMLElement, record:eViewModel?, i:Int, size:Int, template:eTemplate?, ref:eJsonObject?){
        val v = view.asDynamic()
        if(v.__einEl__ == undefined) v.__einEl__ = obj{
            data = obj{
                this.index = i
                this.length = size
                this.data = record
                this.tmpl = template
                this.ref = ref
                this.view = root
            }
        }
    }
}
