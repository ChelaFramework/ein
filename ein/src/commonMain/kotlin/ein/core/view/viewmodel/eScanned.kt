package ein.core.view.viewmodel

import ein.core.log.log
import ein.core.value.eJsonObject

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
    operator fun invoke(view:T? = null, record:eViewModel? = null, i:Int = 0, size:Int = 0, template:eTemplate? = null, ref:eJsonObject? = null){
        val isNew = view != null && view !== this.view
        if(isNew) this.view = view!!
        items.forEach{
            it(memos[it] ?: mutableMapOf<String, Any>().apply{memos[it] = this}, record, i, size)?.run{
                if(isNew || it !in views) views[it] = processor.getItemView(view!!, it.pos)
                val v = views[it]!!
                processor.beforeItemRender(v, record, i, size, template, ref)
                forEach{(k, value)->property[k]?.invoke(v, value)}
            }
        }
    }
}