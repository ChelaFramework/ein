package ein.core.view.viewmodel.template

import ein.core.value.eJsonObject
import ein.core.value.eValue
import ein.core.view.viewmodel.eViewModel

class eTemplateData(var data:Array<out eViewModel>? = null, var templates:Array<out String> = EMPTY_TMPL){
    companion object{
        val EMPTY_TMPL = arrayOf<String>()
        val EMPTY = eTemplateData()
    }
    var ref:eJsonObject? = null
    fun ref(vararg arg:Pair<String, Any>):eTemplateData{
        ref = eValue(arg.toMap())
        return this
    }
    fun data(vararg v:eViewModel){
        data = v
    }
    fun templates(vararg v:String){
        templates = v
    }
}