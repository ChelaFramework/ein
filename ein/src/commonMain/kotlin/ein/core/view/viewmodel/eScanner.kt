package ein.core.view.viewmodel

object eScanner{
    private val scanned = mutableMapOf<Any, eScanned<*>>()
    operator fun get(k:Any) = scanned[k]
    @Suppress("UNCHECKED_CAST")
    fun <T>scan(view:T, processor:eProcessor<T>, property:eProperty<T>, id:Any? = null) = scanned[id]?.let{it.clone() as eScanned<T>} ?:
        eScanned(processor, property).apply{
            processor.template(view)
            processor.getStack(view).forEach {v->
                val pos = mutableListOf<Int>()
                var target = v
                var limit = 30
                while(target !== view && limit-- > 0){
                    processor.getParent(target)?.let{
                        pos += processor.getIndex(it, target)
                        target = it
                    }
                }
                val item = eItem(pos, processor.getData(v))
                this += item
                if(item.key.isNotBlank()) this.keyItem[item.key] = item
            }
            id?.let{scanned[id] = this}
        }
}