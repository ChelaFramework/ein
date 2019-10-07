package ein.android.view.viewmodel

import android.view.View
import ein.core.value.eJsonObject
import ein.core.view.viewmodel.eItem
import ein.core.view.viewmodel.eProcessor
import ein.core.view.viewmodel.eTemplate
import ein.core.view.viewmodel.eViewModel

object processorAndroid:eProcessor<View>() {
    override fun getItemView(view:View?, pos:eItem):View {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getStack(view:View):List<View> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getParent(target:View):View {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getIndex(parent:View, target:View):Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getData(v:View):String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun template(view:View) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun beforeItemRender(view:View, record:eViewModel?, i:Int, size:Int, template:eTemplate?, ref:eJsonObject?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
