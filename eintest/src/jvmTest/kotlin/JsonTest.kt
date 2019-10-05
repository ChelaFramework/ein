import ein.core.core.eJsonObject
import ein.core.core.ePrimitive
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class JsonTest {
    /*
    @Test
    fun jsonTest(){
        val v = ePrimitive.json("""{
                a:3, b:"abc", c:{a:1, b:3}, d:[1,2,3]
            }""")
        assertTrue(v is eJsonObject, "is json")
        assertEquals(3L, v("a").v)
        assertEquals("abc", v("b").v)
        assertEquals(1L, v("c.a").v)
        assertEquals(3L, v("c.b").v)
        assertEquals(1L, v("d.0").v)
        assertEquals(2L, v("d.1").v)
        assertEquals(3L, v("d.2").v)
    }*/
}