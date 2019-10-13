package ein.js.view.router

import ein.core.core.elazy
import ein.core.view.router.eHolder
import org.w3c.dom.HTMLElement
import kotlin.browser.document

abstract class eDomHolder(
    routerKey:String, key:String, data:Any? = null,
    vararg channels:Pair<String, (channel:String, value:Any?)->Unit>
):eHolder<HTMLElement>(routerKey, key, data, *channels){
    companion object{
        private val div = document.createElement("div") as HTMLElement
    }
    private var html = ""
    override val view by elazy(true) {
        div.innerHTML = html
        (div.firstElementChild as? HTMLElement) ?: throw Throwable("invalid html:$html")
    }
    abstract fun render()
}