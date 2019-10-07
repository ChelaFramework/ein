package ein.core.core

import ein.core.regex.eRegValue
import ein.core.value.eJsonArray
import ein.core.value.eJsonObject
import ein.core.value.eValue

private val atom = """\s*(\{[^\{\}\[\]]*\}|\[[^\{\}\[\]]*\])\s*""".toRegex(RegexOption.MULTILINE)
private val atomKey = """<@#([0-9]+)!\$>""".toRegex(RegexOption.MULTILINE)
private val jsonKey = """^\s*(?:"([^":]*)"|([^:,\s"`]+)|`([^`:]*)`)\s*:""".toRegex(RegexOption.MULTILINE)
private val sstr = """(\s|:|,|^)(\"(?:\\"|[^"])*[\{\}\[\]]+(?:\\"|[^"])*[^\\]?\")""".toRegex()
private val sKey = """<!#([0-9]+)#\$>""".toRegex()
internal fun parseJSON(txt:String, limit:Int = 10000):eValue {
    if(txt.isBlank()) return eValue.EMPTY
    if(eRegValue.re.find(txt) != null) return eRegValue(txt) ?: eValue.EMPTY
    var idx = 0
    val ss = mutableMapOf<String, String>()
    var v = sstr.replace(txt){
        val k = "<!#${idx++}#$>"
        ss[k] = it.groupValues[2]
        it.groupValues[1] + k
    }
    val kv = mutableMapOf<String, String>()
    idx = 0
    while(atom.find(v) != null) v = atom.replace(v) {
        val k = "<@#${idx++}!$>"
        kv[k] = it.groupValues[1]
        k
    }
    val map = mutableMapOf<String, eValue>()
    var last:eValue = eValue.EMPTY
    kv.forEach {(k, v)->
        var body = sKey.replace(v.substring(1, v.length - 1)){
            ss[it.groupValues[0]] ?: ""
        }
        last = if(v[0] == '{') {
            val obj = eJsonObject()
            var i = limit
            do{
                val findKey = jsonKey.find(body)?.groupValues ?: break
                val objK = findKey[1] + findKey[2] + findKey[3]
                body = jsonKey.replaceFirst(body, "")
                if(eRegValue.re.find(body) != null){
                    obj[objK] = eRegValue(body) ?: eValue.EMPTY
                    body = eRegValue.re.replaceFirst(body, "")
                }else if(atomKey.find(body) != null){
                    obj[objK] = map[atomKey.find(body)?.groupValues?.get(0) ?: ""] ?: eValue.EMPTY
                    body = atomKey.replaceFirst(body, "")
                }else break
                if(body.isNotBlank() && body[0] == ',') body = body.substring(1) else break
            }while(i-- > 0)
            obj
        }else{
            val arr = eJsonArray()
            var i = limit
            do{
                if(eRegValue.re.find(body) != null) {
                    arr += eRegValue(body) ?: eValue.EMPTY
                    body = eRegValue.re.replaceFirst(body, "")
                }else if(atomKey.find(body) != null){
                    arr += map[atomKey.find(body)?.groupValues?.get(0) ?: ""] ?: eValue.EMPTY
                    body = atomKey.replaceFirst(body, "")
                }else break
                if(body.isNotBlank() && body[0] == ',') body = body.substring(1) else break
            }while(i-- > 0)
            arr
        }
        map[k] = last
    }
    return last
}
