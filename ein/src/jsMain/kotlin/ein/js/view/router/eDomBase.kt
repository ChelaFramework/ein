package ein.js.view.router

import ein.core.view.router.eBase
import ein.js.dom.getChildAt
import org.w3c.dom.HTMLElement

class eDomBase(val el:HTMLElement, historyLevel:Int = 1):eBase<eDomHolder, HTMLElement>(historyLevel){
    private fun restore() = curr?.let {
        var i = historyLevel
        it.prev{h->if(i-- > 0) add(h as eDomHolder, false, 0)}
        add(it, true)
        i = historyLevel
        it.next{h->if(i-- > 0) add(h as eDomHolder, false)}
    }
    override fun add(holder:eDomHolder, isCurr:Boolean, idx:Int){
        holder.restored(isCurr)
        if(idx == -1) el.appendChild(holder.view)
        else el.getChildAt(idx)?.let{el.insertBefore(holder.view, it)}
        holder.render()
    }
    override fun remove(holder:eDomHolder) {
        el.removeChild(holder.view)
    }
}