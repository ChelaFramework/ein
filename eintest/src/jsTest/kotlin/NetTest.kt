import ein.core.net.eRequest
import kotlin.js.Promise
import kotlin.test.Test
import kotlin.test.assertEquals

class NetTest {
    @Test
    fun getTest() = Promise<String?>{res,_->
        eRequest("https://www.bsidesoft.com/hika/ein/a.txt").send{
            it.text{res(it)}
        }
    }.then {
        assertEquals("helloA", it)
    }
}