import ein.core.channel.eChannel
import ein.core.channel.eListener
import org.junit.Assert.*
import org.junit.Test
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class Channel {
    @Test
    fun channel(){
        val lock = CountDownLatch(1)
        var isEnd = false
        val listner = object:eListener(){
            override fun listen(channel:String, value:Any?) {
                when(channel){
                    "afterStart"->assertEquals("abc", value)
                    "pauseMemo"->{
                        assertEquals("def", value)
                        isEnd = true
                    }
                }
            }
        }
        listner.addChannels("afterStart", "pauseMemo")
        listner.start()
        eChannel.notify("afterStart", "abc")
        listner.pause()
        eChannel.notify("pauseMemo", "def")
        listner.resume()
        lock.await(1, TimeUnit.MILLISECONDS)
        assertEquals(true, isEnd)
    }
}