package ein.core.view.viewmodel

import ein.core.core.eJsonObject

abstract class eProcessor<T> {
    internal abstract fun getStack(view:T):List<T>
    internal abstract fun getParent(target:T):T
    internal abstract fun getIndex(parent:T, target:T):Int
    internal abstract fun getData(v:T):String
    internal abstract fun template(view:T)
    internal abstract fun getItemView(view:T?, pos:eItem):T
    internal abstract fun beforeItemRender(view:T, record:eViewModel?, i:Int, size:Int, template:eTemplate?, ref:eJsonObject?)
}
