package ein.core.view.viewmodel

import ein.core.log.log
import ein.core.value.eJsonObject
import ein.core.value.stringify
import ein.core.view.viewmodel.template.eTemplate

class eScanned<T> internal constructor(
    private val processor:eProcessor<T>,
    private val property:eProperty<T>,
    val items:Set<eItem>
){
    internal val keyItem = mutableMapOf<String, eItem>()
    private val views = mutableMapOf<eItem, T>()
    private val memos = mutableMapOf<eItem, MutableMap<String, Any>>()
    private var view:T? = null
    internal fun clone() = eScanned(processor, property, items).apply{
        keyItem += keyItem
    }
    operator fun get(k:String) = keyItem[k]
    operator fun invoke(view:T? = null, record:eViewModel? = null, i:Int = 0, size:Int = 0, template:eTemplate<T>? = null, ref:eJsonObject? = null){
        val isNew = view != null && view !== this.view
        if(isNew) this.view = view!!
        items.forEach{item->
            item(memos[item] ?: mutableMapOf<String, Any>().apply{memos[item] = this}, record, i, size)?.let{
                if(isNew || item !in views) views[item] = processor.getItemView(view!!, item.pos)
                val v = views[item]!!
                processor.beforeItemRender(this.view, v, record, i, size, template, ref)
                it.forEach{(k, value)->property[k]?.invoke(v, value) ?: property(v, k, value)}
            }
        }
    }
}