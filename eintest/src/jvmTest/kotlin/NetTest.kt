import ein.core.net.eRequest
import kotlin.test.Test
import kotlin.test.assertEquals

class NetTest {
    @Test
    fun getTest(){
        eRequest("http://www.bsidesoft.com/hika/ein/a.txt").send{
            it.text{
                assertEquals("helloA", it)
            }
        }
    }
}