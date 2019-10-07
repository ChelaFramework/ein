import ein.core.regex.eRegValue
import ein.core.value.*
import org.junit.Assert.*
import org.junit.Test

class Value {
    @Test
    fun invokeValue() {
        eRegValue("true")
        assertEquals(10, eValue(10).v)
        assertEquals(10L, eValue(10L).v)
        assertEquals(10F, eValue(10F).v)
        assertTrue(eValue(10.0).v == 10.0)
        assertEquals(true, eValue(true).v)
        assertEquals("abc", eValue("abc").v)
        assertEquals("a", eValue("@{a}").v)
        assertEquals("a", eValue("\${a}").v)
        assertEquals(3L, (eValue("{a:3, b:5}") as? eJsonObject)?.get("a")?.v)
        assertTrue(eValue("@{a}") is eStore)
        assertTrue(eValue("\${a}") is eRecord)
        assertEquals("a", eValue(listOf("a")).v[0].v)
        assertEquals("b", eValue(setOf("a", "b")).v[1].v)
        assertEquals(3, eValue(mapOf("a" to 3)).v["a"]?.v)
    }
    @Test
    fun newValueType() {
        eRegValue("null")
        assertEquals(10, eInt(10).v)
        assertEquals(10L, eLong(10L).v)
        assertEquals(10F, eFloat(10F).v)
        assertTrue(eDouble(10.0).v == 10.0)
        assertEquals(true, eBoolean(true).v)
        assertEquals("abc", eString("abc").v)

    }
    @Test
    fun jsonParse(){
        val json1 = eValue("""{
            "d0":true,
            "a":{
                "b":5.5,
                "c":["abc",{
                    "d":true,
                    "e":"def"
                },2],
                "f":null
            },
            "g":false,
            "h":{"i":"ghi"},
            "j":[6, false]
        }""") as eJsonObject
        assertEquals(true, json1("d0").v)
        assertEquals(5.5, json1("a.b").v)
        assertEquals("abc", json1("a.c.0").v)
        assertEquals(true, json1("a.c.1.d").v)
        assertEquals("def", json1("a.c.1.e").v)
        assertEquals(2L, json1("a.c.2").v)
        assertTrue(json1("a.f") is eNull)
        assertEquals(false, json1("g").v)
        assertEquals("ghi", json1("h.i").v)
        assertEquals(6L, json1("j.0").v)
        assertEquals(false, json1("j.1").v)
        val json2 = eValue("""{
           a:"{}-[]-\"\"-",
           b:1
        }""") as eJsonObject
        assertEquals("""{}-[]-""-""", json2("a").v)
        assertEquals(1L, json2("b").v)
    }
}
