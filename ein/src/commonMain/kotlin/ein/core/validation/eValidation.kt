package ein.core.validation

import ein.core.value.eJsonObject
import ein.core.core.elazy
import ein.core.resource.eLoader

/*
{
    "validation":{
        "@default":"@{cdata.error}", //optinal, default error msg for all validations
        "rating":{ //key
            "type":"base", //optional, default "base"
            "ruleset":"trim|string|a@range[1,5]-or-b@first[a]|c@range[1,4]", //mandatory
            "msg":{ //optional
                "@default":"@{cdata.error/rating}", //optional
                "a":"범위가 다름",
                "b":"첫글자가 a가 아님",
                "c":"범위가 다름"
            },
            "arg":{ //optional
                "relatedField":"confirmPw"
            }
        },
        "passConfirm":{
            type:"pw",
            ruleset:"",
            msg:{
             "@default":"같지 않다"
            },
            arg:{
               key:"pass"
            }
        }
    }
}
 */
private typealias eValiF = (eRuleSet, eJsonObject?, eJsonObject?)->eValidation
open class eValidation(val ruleset:eRuleSet, val msg:eJsonObject?, val arg:eJsonObject?){
    companion object:eLoader{
        override fun load(res:eJsonObject){
            (res["validation"] as? eJsonObject)?.forEach{(k, v)->
                when(k){
                    "@default"->defaultMsg(v.v.toString())
                    else->(v as? eJsonObject)?.let{set(k, it)}
                }
            }
        }
        private var defaultMsg = "invalid"
        private val types = mutableMapOf<String, eValiF>(
            "base" to {r, m, a-> eValidation(r, m, a) }
        )
        private val map = mutableMapOf<String, eValidation>()
        fun setType(k:String, f:eValiF) = types.set(k, f)
        fun defaultMsg(v:String){defaultMsg = v}
        operator fun set(k:String, v:eJsonObject) = types[v["type"] ?: "base"]?.let {
            map[k] = it(
                eRuleSet(v["ruleset"]?.v?.toString() ?: throw Throwable("no ruleset")),
                v["msg"] as? eJsonObject,
                v["arg"] as? eJsonObject
            )
        } ?: throw Throwable("invalid type:${v["type"]}")
        operator fun get(k:String) = map[k]
        fun validate(v:Map<String, Any>):Pair<Boolean, String>{
            var result = true
            var msg = ""
            v.any{(k, v)->
                map[k]?.let {
                    val checked = it.check(v)
                    if(!checked.first){
                        result = false
                        msg = "${checked.second}"
                        false
                    }else true
                } ?: run {
                    result = false
                    msg = "invalid key:$k"
                    false
                }
            }
            return result to msg
        }
    }
    private val defaultM by elazy(true){msg?.get("@default")?.v ?: defaultMsg}
    open fun check(v:Any):Pair<Boolean, Any> = ruleset.check(v).run{
        if(first) this else false to (msg?.get(second)?.v ?: defaultM)
    }
}