package ein.core.resource

import ein.core.view.eStyle
import ein.core.cdata.eCdata
import ein.core.value.eJsonObject
import ein.core.net.eApi
import ein.core.sql.eQuery
import ein.core.validation.eRuleSet
import ein.core.validation.eValidation
import ein.core.value.eValue

interface eLoader{
    companion object{
        private val factories = mutableSetOf<eLoader>()
        private var isInited = false
        operator fun plusAssign(v:eLoader){factories += v}
        fun load(res:eJsonObject) {
            if(!isInited){
                isInited = true
                factories += listOf(eRuleSet, eValidation, eApi, eQuery, eStyle, eCdata)
                eValue["style"] = eStyle.styles
                eValue["cdata"] = eCdata.root
            }
            factories.forEach {it.load(res)}
        }
    }
    fun load(res:eJsonObject)
}