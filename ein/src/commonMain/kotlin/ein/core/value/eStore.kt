package ein.core.value

import ein.core.log.log
import ein.core.resource.eLoader

class eStore(override val v:String):eValue {
    override fun stringify() = "s@$v@"
    companion object:eLoader{
        override fun load(res:eJsonObject){
            (res["store"] as? eJsonObject)?.forEach{(k, v)->
                eValue[k] = v
            }
        }
    }
}