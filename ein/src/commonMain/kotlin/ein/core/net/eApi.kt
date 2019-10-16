package ein.core.net

import ein.core.core.*
import ein.core.log.log
import ein.core.looper.*
import ein.core.looper.async.eAsyncSerial
import ein.core.regex.eReg
import ein.core.resource.eLoader
import ein.core.validation.eRuleSet
import ein.core.value.eJsonArray
import ein.core.value.eJsonObject
import ein.core.value.eString
import ein.core.value.stringify

/*
{
    "api": {
      "@prefix":"http://www.bsidesoft.com:9980/",
      "@suffix":"",
      "getList": {
        "url": "seller2/api/gateway",
        "method": "POST",
        "requestTask": "header[client]|akey|sheader|gateway|aes",
        "responseTask": "pkey|aes|json|error|data",
        "request": ["id", "pw"],
           or
        "request": "id,pw,client",
           or
        "request":{
          "id":{
            "name":"userid", //optional, ?: key = "id",
            "ruleset":"trim|range[2,3]" or "rulsetname", //optional
            "task":"a|b|c" //optional
          },...N
        }-*
      }
    }
}

eApi["gateway"]("id" to "aaa", "pw" to "xxx", "client" to "iphone")
 */
private typealias requestTaskF = (request: eRequest, item:Map<String, Any>, arg:List<String>)->Boolean
private typealias responseTaskF = (response:eResponse, arg:List<String>, next:(Boolean)->Unit)->Unit
class eApi private constructor(
    val key:String,
    private val url:String,
    private val method:String,
    private val requestTask:List<String>?,
    private val responseTask:List<String>?,
    private val requestItem:MutableMap<String, RequestItem>?
){
    private class RequestItem(val name: String?, val ruleset: String? = null, val task: List<String>? = null)
    companion object:eLoader {
        override fun load(res:eJsonObject) {
            (res["api"] as? eJsonObject)?.let{
                val prefix = "${it["@prefix"]?.v ?: ""}"
                val suffix = "${it["@suffix"]?.v ?: ""}"
                it.forEach {(k, v)->
                    (v as? eJsonObject)?.let{
                        set(k, eApi(
                            k,
                        "$prefix${it["url"]?.v ?: ""}$suffix",
                    it["method"]?.let{"${it.v}".toUpperCase()} ?: "POST",
                            it["requestTask"]?.let{"${it.v}".split("|").map{it.trim()}},
                            it["responseTask"]?.let{"${it.v}".split("|").map{it.trim()}},
                            it["request"]?.let{
                                val r = mutableMapOf<String, RequestItem>()
                                when(it){
                                    is eString->{
                                        it.v.split(",").forEach {
                                            val name = it.trim()
                                            r[name] = RequestItem(name)
                                        }
                                    }
                                    is eJsonArray->{
                                        it.forEach {
                                            val name = "${it.v}".trim()
                                            r[name] = RequestItem(name)
                                        }
                                    }
                                    is eJsonObject->{
                                        it.forEach {(ik, iv)->
                                            (iv as? eJsonObject)?.let {
                                                val name = "${it["name"]?.v ?: ik}"
                                                val ruleset = (it["ruleset"] as? eString)?.v
                                                val task = (it["task"] as? eString)?.v?.split("|")
                                                r[ik] = RequestItem(name, ruleset, task)
                                            }
                                        }
                                    }
                                }
                                r
                            }
                        ))
                    }
                }
            }
        }
        const val EXTRA_JSON = "EXTRA_JSON"
        const val EXTRA_BODY = "EXTRA_BODY"
        const val EXTRA_REQUEST_BODY = "EXTRA_REQUEST_BODY"
        private val apis = mutableMapOf<String, eApi>()
        private val requestItemTasks = mutableMapOf<String, (Any) -> Any?>()
        private val requestTasks by elazy(true){
            mutableMapOf<String, requestTaskF>(
                "header" to {request, item, arg->
                    var cnt = 0
                    arg.forEach {k->
                        item[k]?.let {
                            request.header(k, "$it")
                            cnt++
                        }
                    }
                    cnt == arg.size
                },
                "json" to {request, item, _->
                    request.extra[EXTRA_JSON] = item.stringify()
                    true
                },
                "jsonbody" to {request, item, arg->
                    if(request.extra[EXTRA_REQUEST_BODY] != null) false
                    else {
                        request.extra[EXTRA_REQUEST_BODY] = "json"
                        if(arg.size == 1) {
                            item[arg[0]]?.let {
                                when(it) {
                                    is eJsonObject-> {
                                        request.json(it)
                                        true
                                    }
                                    is String-> {
                                        request.json(it)
                                        true
                                    }
                                    else->false
                                }
                            } ?: false
                        } else request.extra[EXTRA_JSON]?.let {
                            when(it) {
                                is eJsonObject-> {
                                    request.json(it)
                                    true
                                }
                                is String-> {
                                    request.json(it)
                                    true
                                }
                                else->false
                            }
                        } ?: run {
                            request.json(item.stringify())
                            true
                        }
                    }
                },
                "body" to {request, item, arg->
                    if(request.extra[EXTRA_REQUEST_BODY] != null) false
                    else {
                        request.extra[EXTRA_REQUEST_BODY] = "body"
                        if(arg.size == 1) {
                            item[arg[0]]?.let {
                                request.body("$it")
                                true
                            } ?: false
                        } else request.extra[EXTRA_BODY]?.let {
                            request.body("$it")
                            true
                        } ?: false
                    }
                }
            )
        }
        private val responseTasks by elazy(true) {
            mutableMapOf<String, responseTaskF>(
                "json" to {res, _, next->res.json{
                    next(it?.let{
                        res.extra[EXTRA_JSON] = it
                        res.result = it
                        true
                    } ?: false)
                }},
                "body" to {res, _, next->res.text{
                    res.result = it
                    next(true)
                }},
                "blob" to {res, _, next->res.bytes{
                    next(it?.let{
                        res.result = it
                        true
                    } ?: false)
                }}
            )
        }
        fun setRequestTask(k:String, block:requestTaskF) = requestTasks.set(k, block)
        fun setRequestItemTask(k:String, block:(Any)->Any?) = requestItemTasks.set(k, block)
        fun setResponseTask(k:String, block:responseTaskF)  = responseTasks.set(k, block)
        operator fun get(k:String) = apis[k]
        operator fun set(k:String, api:eApi) = apis.set(k, api)
        operator fun minusAssign(k:String){remove(k)}
        fun remove(k:String) = apis.remove(k)
        private val net by elazy(true){net(true)}
        private val main by elazy(true){net(false)}
    }
    operator fun invoke(vararg arg:Pair<String, Any>, block:(isOk:Boolean, eResponse, err:String)->Unit):String?{
        val reqItem = mutableMapOf<String, Any>()
        requestItem?.let{
            if(arg.size != it.size) return "invalid arg count:arg ${arg.size}, api ${it.size}"
            arg.forEach{(k, v)->
                val req = it[k] ?: return "invalid request param:$k"
                var r = v
                req.ruleset?.let {key->
                    r = eRuleSet.getRuleset(key).check(r)
                    if(r == eRuleSet.FALSE) return "ruleset($key) check failed $k, $v"
                }
                req.task?.forEach{Key->
                    val task = requestItemTasks[Key] ?: return "invalid request item task:$key for $k"
                    r = task(r) ?: return "request item task stop:$key for $k"
                }
                reqItem += (req.name ?: k) to r
            }
        }
        val request = eRequest(url, method)
        var msg = ""
        if(false == requestTask?.all{
            val (k, a) = eReg.param.parse(it)
            requestTasks[k]?.let{
                if(!it(request, reqItem, a)){
                    msg = "request task stop:$k for $key"
                    false
                }else true
            } ?: run{
                msg = "invalid request task:$k for $key"
                false
            }
        }) return msg
        request.send {res->
            val end:eAsyncSerial.()->Unit = {
                run{block(msg.isBlank(), res, msg)}
            }
            responseTask?.let {tasks->
                if(tasks.isEmpty()) main(end)
                else net{
                    tasks.forEach {task->
                        wait {
                            if(msg.isNotBlank()) go()
                            else {
                                val (k, a) = eReg.param.parse(task)
                                responseTasks[k]?.let {
                                    it(res, a) {isOk->
                                        if(!isOk) msg = "error response task:$k, $a for $key"
                                        go()
                                    }
                                } ?: run {
                                    msg = "invalid response task:$k, $a for $key"
                                    go()
                                }
                            }
                        }
                    }
                    run{main(end)}
                }
            } ?: main(end)
        }
        return null
    }
}