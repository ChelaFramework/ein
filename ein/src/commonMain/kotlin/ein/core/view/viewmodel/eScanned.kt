package ein.core.view.viewmodel

import ein.core.core.eJsonObject

class eScanned<T> internal constructor(
    private val processor:eProcessor<T>,
    private val property:eProperty<T>
):MutableSet<eItem> by mutableSetOf(){
    internal val keyItem = mutableMapOf<String, eItem>()
    private var view:T? = null
    private val itemView = mutableMapOf<eItem, T>()
    internal fun clone() = eScanned(processor, property).let{
        it.addAll(this)
        it.keyItem += keyItem
        it
    }
    operator fun invoke(view:T? = null, record:eViewModel? = null, i:Int = 0, size:Int = 0, template:eTemplate? = null, ref:eJsonObject? = null){
        val isNew = view != null && view !== this.view
        if(isNew) this.view = view!!
        forEach {item->
            if(isNew) itemView[item] = processor.getItemView(view, item)
            item(record, i, size)?.let{
                val v = itemView[item]!!
                processor.beforeItemRender(v, record, i, size, template, ref)
                it.forEach {(k, value)-> property[k]?.invoke(v, value)}
            }
        }
    }
}