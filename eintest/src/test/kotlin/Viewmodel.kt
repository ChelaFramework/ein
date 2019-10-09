import ein.core.regex.eRegValue
import ein.core.value.*
import ein.core.view.viewmodel.eViewModel
import org.junit.Assert.*
import org.junit.Test

class Viewmodel {
    @Test
    fun viewmodel() {
        val vm = object:eViewModel(){
            val a by v("abc")
        }
        assertEquals("abc", vm.a)
        assertEquals("abc", vm["a"])
    }
}
