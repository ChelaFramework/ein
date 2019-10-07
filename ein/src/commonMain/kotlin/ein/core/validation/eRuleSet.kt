package ein.core.validation

import ein.core.value.eJsonObject
import ein.core.core.elazy
import ein.core.regex.eReg
import ein.core.resource.eLoader

/*
{
    "ruleset":{
        "https":"trim|startwith[https]|url",
        "title":"trim|range[4,255]"
    }
}
 */
private typealias F = (v:Any)->Any
private typealias ruleF = (arg:List<String>)->F
private object Reg{
    val ip = """^(?:(?:[0-9]|(?:1\d{1,2})|(?:2[0-4]\d)|(?:25[0-5]))[.]){3}(?:[0-9]|[1-9][0-9]{1,2}|2[0-4]\d|25[0-5])$""".toRegex()
    val url = """^https?://[a-zA-Z0-9.-]+(?:[.]+[A-Za-z]{2,4})+(?:[:]\d{2,4})?""".toRegex()
    val email = """^[0-9a-zA-Z-_.]+@[0-9a-zA-Z-]+(?:[.]+[A-Za-z]{2,4})+$""".toRegex()
    val korean = """^[ㄱ-힣]+$""".toRegex()
    val japanese = """^[ぁ-んァ-ヶー一-龠！-ﾟ・～「」“”‘’｛｝〜−]+$""".toRegex()
    val lower = """^[a-z]+$""".toRegex()
    val upper = """^[A-Z]+$""".toRegex()
    val num = """^(?:-?(?:0|[1-9]\d*)(?:\.\d+)(?:[eE][-+]?\d+)?)|(?:-?(?:0|[1-9]\d*))$""".toRegex()
    val intnum = """^(?:-?(?:0|[1-9]\d*))$""".toRegex()
    val doublenum = """^(?:-?(?:0|[1-9]\d*)(?:\.\d+)(?:[eE][-+]?\d+)?)$""".toRegex()
    val lowernum = """^[a-z0-9]+$""".toRegex()
    val uppernum = """^[A-Z0-9]+$""".toRegex()
    val alphanum = """^[a-zA-Z0-9]+$""".toRegex()
    val firstlower = """^[a-z]""".toRegex()
    val firstUpper = """^[A-Z]""".toRegex()
    val noblank = """\s""".toRegex()
}
class eRuleSet internal constructor(rule:String){
    companion object:eLoader{
        override fun load(res:eJsonObject) {
            (res["ruleset"] as? eJsonObject)?.forEach{(k, v)->setRuleSet(k, "${v.v}")}
        }
        val FALSE = object {}
        private val reg by elazy(true){Reg}
        private val rules by elazy(true) {
            mutableMapOf<String, ruleF>(
                "int" to {_-> {v-> if(v is Int) v else FALSE}},
                "long" to {_-> {v-> if(v is Long) v else FALSE}},
                "float" to {_-> {v-> if(v is Float) v else FALSE}},
                "double" to {_-> {v-> if(v is Double) v else FALSE}},
                "string" to {_-> {v-> if(v is String) v else FALSE}},
                "char" to {_-> {v-> if(v is Char) v else FALSE}},
                "ip" to {_-> {v-> if(v is String && reg.ip.find(v) != null) v else FALSE}},
                "url" to {_-> {v-> if(v is String && reg.url.find(v) != null) v else FALSE}},
                "email" to {_-> {v-> if(v is String && reg.email.find(v) != null) v else FALSE}},
                "korean" to {_-> {v-> if(v is String && reg.korean.find(v) != null) v else FALSE}},
                "japanese" to {_-> {v-> if(v is String && reg.japanese.find(v) != null) v else FALSE}},
                "lower" to {_-> {v-> if(v is String && reg.lower.find(v) != null) v else FALSE}},
                "upper" to {_-> {v-> if(v is String && reg.upper.find(v) != null) v else FALSE}},
                "num" to {_-> {v-> if(v is String && reg.num.find(v) != null) v else FALSE}},
                "intnum" to {_-> {v-> if(v is String && reg.intnum.find(v) != null) v else FALSE}},
                "doublenum" to {_-> {v-> if(v is String && reg.doublenum.find(v) != null) v else FALSE}},
                "lowernum" to {_-> {v-> if(v is String && reg.lowernum.find(v) != null) v else FALSE}},
                "uppernum" to {_-> {v-> if(v is String && reg.uppernum.find(v) != null) v else FALSE}},
                "alphanum" to {_-> {v-> if(v is String && reg.alphanum.find(v) != null) v else FALSE}},
                "startwith" to {arg-> {v-> if(v is String && v.startsWith(arg[0])) v else FALSE}},
                "endwith" to {arg-> {v-> if(v is String && v.endsWith(arg[0])) v else FALSE}},
                "firstlower" to {_-> {v-> if(v is String && reg.firstlower.find(v) != null) v else FALSE}},
                "firstUpper" to {_-> {v-> if(v is String && reg.firstUpper.find(v) != null) v else FALSE}},
                "noblank" to {_-> {v-> if(v is String && reg.noblank.find(v) == null) v else FALSE}},
                "norule" to {_-> {v-> v}},
                "minlength" to {arg-> {v-> if(v is String && arg.size == 1 && v.length >= arg[0].toInt()) v else FALSE}},
                "maxlength" to {arg-> {v-> if(v is String && arg.size == 1 && v.length <= arg[0].toInt()) v else FALSE}},
                "lessthan" to {arg-> {v-> if(v is Number && arg.size == 1 && v.toDouble() < arg[0].toDouble()) v else FALSE}},
                "greaterthan" to {arg-> {v-> if(v is Number && arg.size == 1 && v.toDouble() > arg[0].toDouble()) v else FALSE}},
                "range" to {arg->
                    {v->
                        if(v is Number && arg.size == 2 && arg[0].toDouble() <= v.toDouble() && v.toDouble() <= arg[1].toDouble()) v
                        else if(v is String && arg.size == 2 && arg[0].toInt() <= v.length && v.length <= arg[1].toInt()) v
                        else FALSE
                    }
                },
                "equal" to {arg->
                    {v->
                        when {
                            arg.size != 1->this
                            v is Number->if(v.toDouble() == arg[0].toDouble()) v else FALSE
                            v is String->if(v == arg[0]) v else FALSE
                            v is Boolean->if(v == arg[0].toBoolean()) v else FALSE
                            else->this
                        }
                    }
                },
                "in" to {arg->
                    {v->
                        when(v) {
                            is String->if(arg.contains(v)) v else FALSE
                            is Int->if(arg.map {it.toInt()}.contains(v)) v else FALSE
                            is Long->if(arg.map {it.toLong()}.contains(v)) v else FALSE
                            is Float->if(arg.map {it.toFloat()}.contains(v)) v else FALSE
                            is Double->if(arg.map {it.toDouble()}.contains(v)) v else FALSE
                            else->this
                        }
                    }
                },
                "notblank" to {_-> {v-> if(v is String && v.isNotBlank()) v else FALSE}}
            )
        }
        private val rulesets = mutableMapOf<String, eRuleSet>()
        private val string get() = rulesets["string"]!!
        fun getRule(k:String) = rules[k] ?: throw Exception("invalid rule:$k")
        fun setRule(k:String, rule:ruleF) = rules.set(k, rule)
        fun removeRule(k:String) = rules.remove(k)
        fun getRuleset(k:String) = rulesets[k] ?: setRuleSet(k, k)
        fun setRuleSet(k:String, rule:String) = eRuleSet(rule).apply{rulesets.set(k, this)}
        fun removeRuleSet(k:String) = rulesets.remove(k)
    }
    var msgKey = mutableMapOf<F, String>()
    private val rule = rule.split("-or-").map{
        it.split("|").filter{it.isNotBlank()}.map{v->
            val (k, arg) = eReg.param.parse(v)
            if(k.contains('@')){
                val ks = k.split('@')
                val r = getRule(ks[1])(arg)
                msgKey[r] = ks[0]
                r
            }else getRule(k)(arg)
        }
    }
    fun check(v: Any):Pair<Boolean, Any>{
        var r = v
        var key = "@default"
        return if(rule.any{
            r = v
            it.all{
                msgKey[it]?.let{key = it}
                r = it(r)
                r !== FALSE
            }
        }) true to r else false to key
    }
}