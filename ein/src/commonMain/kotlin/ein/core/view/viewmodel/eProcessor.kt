package ein.core.view.viewmodel

import ein.core.value.eJsonObject

abstract class eProcessor<T> {
    internal abstract fun getStack(view:T):List<T>
    internal abstract fun template(view:T)
    internal abstract fun getItemView(view:T, pos:List<Int>):T
    internal abstract fun beforeItemRender(view:T, record:eViewModel?, i:Int, size:Int, template:eTemplate?, ref:eJsonObject?)
    abstract fun getItem(root:T, view:T):eItem
}
