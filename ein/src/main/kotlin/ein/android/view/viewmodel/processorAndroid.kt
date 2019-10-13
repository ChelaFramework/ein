package ein.android.view.viewmodel

import android.view.View
import android.view.ViewGroup
import ein.core.log.log
import ein.core.value.eJsonObject
import ein.core.view.viewmodel.eItem
import ein.core.view.viewmodel.eProcessor
import ein.core.view.viewmodel.eTemplate
import ein.core.view.viewmodel.eViewModel

object processorAndroid:eProcessor<View>() {
    override fun getStack(view:View) = mutableListOf<View>().apply{
        val stack = mutableListOf(view)
        var limit = 200
        while(stack.isNotEmpty()&& limit-- > 0){
            val v = stack.removeAt(stack.size - 1)
            if(v.tag != null && v.tag is String) add(v)
            if(v is ViewGroup){
                var i = v.childCount
                while(i-- > 0) stack.add(v.getChildAt(i))
            }
        }
    }
    override fun getItem(root:View, view:View) = mutableListOf<Int>().run{
        var target = view
        var limit = 30
        while(target !== root && limit-- > 0){
            target.parent?.let {
                val p = it as ViewGroup
                add(p.indexOfChild(target))
                target = p
            }
        }
        eItem(this, "${view.tag}")
    }
    override fun getItemView(view:View, pos:List<Int>):View {
        var target = view
        var i = pos.size
        while(i-- > 0) target = (target as ViewGroup).getChildAt(pos[i])
        return target
    }
    override fun template(view:View){/*android template is always xml*/}
    override fun beforeItemRender(root:View?, view:View, record:eViewModel?, i:Int, size:Int, template:eTemplate?, ref:eJsonObject?){
        /*no action*/
    }
}
