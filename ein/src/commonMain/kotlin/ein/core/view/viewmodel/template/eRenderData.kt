package ein.core.view.viewmodel.template

import ein.core.log.log
import ein.core.value.eJsonObject
import ein.core.view.viewmodel.eProcessor
import ein.core.view.viewmodel.eViewModel

class eRenderData<T>(private val tmpl: List<eTemplate<T>>, private val processor:eProcessor<T>) {
    val old = mutableListOf<String>()
    fun check(t: List<eTemplate<T>>):Boolean{
        tmpl.forEachIndexed {i, tmpl->if(t[i] !== tmpl) return false}
        return true
    }
    fun render(target:T, data:Array<out eViewModel>?, r:eJsonObject?){
        if(data == null) return
        val oSize = old.size
        val dSize = data.size
        val j = if(oSize > dSize) dSize else oSize
        var child:T? = processor.tmplFirstChild(target)
        var i = 0
        while(i < j){
            child?.let {
                val curr = data[i]
                val v = curr.stringify()
                val isSkip = v == old[i]
                if(!isSkip) old[i] = v
                tmpl.forEach{child = it.rerender(child, i, dSize, curr, isSkip, r)}
            } ?: break
            i++
        }
        while(i < dSize){
            val curr = data[i]
            old.add(curr.stringify())
            tmpl.forEach {it.render(target, i, dSize, curr, r)}
            i++
        }
        while(i < oSize){
            child?.let {v->
                old.removeAt(old.size - 1)
                tmpl.forEach {child = it.drain(v, i, dSize)}
            } ?: break
            i++
        }
    }
}