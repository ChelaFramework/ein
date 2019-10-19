package ein.js.view.viewmodel

import ein.core.log.log
import ein.core.value.eJsonObject
import ein.core.value.eValue
import ein.core.view.viewmodel.eItem
import ein.core.view.viewmodel.eProcessor
import ein.core.view.viewmodel.eViewModel
import ein.core.view.viewmodel.template.eRenderData
import ein.core.view.viewmodel.template.eTemplate
import ein.js.dom.getChildAt
import ein.js.dom.indexOfChild
import ein.js.dom.toList
import ein.js.js.obj
import org.w3c.dom.HTMLElement

object processorDom:eProcessor<HTMLElement>() {
    override fun tmplJson(item:HTMLElement) = item.getAttribute("data-eint")?.let{
        item.removeAttribute("data-eint")
        eValue.json("{$it}") as eJsonObject
    } ?: eJsonObject()
    override fun tmplClone(target:HTMLElement, item:HTMLElement) = target.appendChild(item.cloneNode(true)) as HTMLElement
    override fun tmplNext(v:HTMLElement?) = v?.nextElementSibling as? HTMLElement
    override fun tmplRemove(v:HTMLElement){v.parentNode?.removeChild(v)}
    override fun tmplRender(target:HTMLElement, templates:List<eTemplate<HTMLElement>>, data:Array<eViewModel>?, ref:eJsonObject?) {
        val d = target.asDynamic()
        @Suppress("UnsafeCastFromDynamic")
        var prev = d.__einRd__ as? eRenderData<HTMLElement>
        if(prev == null || !prev.check(templates)){
            prev = eRenderData(templates, this)
            d.__chRd__ = prev
            target.innerHTML = ""
        }
        prev.render(target, data, ref)
    }
    override fun tmplFirseChild(target:HTMLElement) = target.firstElementChild as HTMLElement
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
        forEach{eTemplate(it, processorDom, propertyDom)}
    }
    override fun beforeItemRender(root:HTMLElement?, view:HTMLElement, record:eViewModel?, i:Int, size:Int, template:eTemplate<HTMLElement>?, ref:eJsonObject?){
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
