package ein.core.core

interface eLock{
    companion object{
        val EMPTY = object :eLock{
            override fun read(block:()->Unit) = block()
            override fun write(block:()->Unit)= block()
        }
    }
    fun read(block:()->Unit)
    fun write(block:()->Unit)
}