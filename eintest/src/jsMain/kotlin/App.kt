import ein.core.view.router.eRouter
import ein.js.view.router.eDomBase
import ein.js.view.router.eDomHolder
import home.HomeH
import org.w3c.dom.HTMLElement
import kotlin.browser.document

object App{
    lateinit var router:eRouter<eDomBase, eDomHolder, HTMLElement>
    operator fun invoke(){
        (document.querySelector("#main") as? HTMLElement)?.let {
            eRouter("", eDomBase(it)).run {
                router = this
                this["home"] = {rk, hk, d-> HomeH(rk, hk, d)}
            }
            router.push("home")
        }
    }
}