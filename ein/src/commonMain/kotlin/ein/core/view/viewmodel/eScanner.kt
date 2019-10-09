package ein.core.view.viewmodel

object eScanner{
    private val ids = mutableMapOf<Any, eScanned<*>>()
    operator fun get(k:Any) = ids[k]
    @Suppress("UNCHECKED_CAST")
    fun <T>scan(view:T, processor:eProcessor<T>, property:eProperty<T>, id:Any? = null) = ids[id]?.run{clone() as eScanned<T>} ?:
        mutableSetOf<eItem>().run{
            val scanned = eScanned(processor, property, this)
            processor.template(view)
            processor.getStack(view).forEach {v->
                val item = processor.getItem(view, v)
                this += item
                if(item.key.isNotBlank()) scanned.keyItem[item.key] = item
            }
            id?.let{ids[it] = scanned}
            scanned
        }
}