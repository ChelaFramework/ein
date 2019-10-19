package ein.core.view.viewmodel

import ein.core.value.eJsonObject
import ein.core.view.viewmodel.template.eTemplate

abstract class eProcessor<T> {
    internal abstract fun getStack(view:T):List<T>
    internal abstract fun template(view:T)
    internal abstract fun getItemView(view:T, pos:List<Int>):T
    internal abstract fun beforeItemRender(root:T?, view:T, record:eViewModel?, i:Int, size:Int, template:eTemplate<T>?, ref:eJsonObject?)
    abstract fun getItem(root:T, view:T):eItem
    abstract fun tmplClone(target:T, item:T):T
    abstract fun tmplNext(v:T?):T?
    abstract fun tmplRemove(v:T)
    abstract fun tmplJson(item:T):eJsonObject
    abstract fun tmplRender(target:T, templates:List<eTemplate<T>>, data:Array<eViewModel>?, ref:eJsonObject?)
    abstract fun tmplFirseChild(target:T):T
}
