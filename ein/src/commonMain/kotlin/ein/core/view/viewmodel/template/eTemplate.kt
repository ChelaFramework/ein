package ein.core.view.viewmodel.template

import ein.core.value.eJsonObject
import ein.core.view.viewmodel.*

class eTemplate<T>(
        val key:String = "",
        val item:T,
        val processor:eProcessor<T>,
        val scanned:eScanned<T>,
        val nth:Nth
){
    internal fun rerender(target:T?, i: Int, dSize: Int, curr:eViewModel, isSkip:Boolean,  ref:eJsonObject?) = if(nth(i, dSize)){
        if(!isSkip) scanned(target, curr, i, dSize, this, ref)
        processor.tmplNext(target)
    }else target
    internal fun render(target:T, i: Int, dSize: Int, curr:eViewModel, ref:eJsonObject?){
        if(nth(i, dSize)) scanned(processor.tmplClone(target, item), curr, i, dSize, this, ref)
    }
    internal fun drain(target:T, i: Int, dSize: Int) = processor.tmplNext(target).apply{
        if(nth(i, dSize)) processor.tmplRemove(target)
    }
    companion object{
        private val tmpls = mutableMapOf<String, eTemplate<*>>()
        operator fun <T>invoke(item:T, processor:eProcessor<T>, property:eProperty<T>){
            val json = processor.tmplJson(item)
            val key = "${json["key"]?.v ?: throw Throwable("no key")}"
            val nth = Nth["${json["nth"]?.v ?: "all"}"] ?: throw Throwable("no nth")
            tmpls[key] = eTemplate(key, item, processor, eScanner.scan(item, processor, property), nth)
        }
        @Suppress("UNCHECKED_CAST")
        fun <T>render(target:T, data:Array<eViewModel>?, tmpl:Array<out String>, ref:eJsonObject?){
            if(tmpl.isEmpty()) throw Throwable("no template")
            val templates = tmpl.map{(tmpls[it] as? eTemplate<T>) ?: throw Throwable("invalid tmpl $it")}
            val processor = templates[0].processor
            processor.tmplRender(target, templates, data, ref)
        }
    }
}