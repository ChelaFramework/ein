package ein.core.resource

import chela.kotlinJS.view.eStyle
import ein.core.cdata.eCdata
import ein.core.core.eJsonObject
import ein.core.net.eApi
import ein.core.sql.eDB
import ein.core.sql.eQuery
import ein.core.validation.eRuleSet
import ein.core.validation.eValidation

interface eLoader{
    companion object{
        private val factories = mutableSetOf(eCdata, eRuleSet, eValidation, eApi, eStyle, eQuery)
        operator fun plusAssign(v:eLoader){factories += v}
        fun load(res:eJsonObject) = factories.forEach {it.load(res)}
    }
    fun load(res:eJsonObject)
}