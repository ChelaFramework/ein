package ein.core.core

interface eRunnable{
    companion object:eRunnable{
        override fun invoke() {}
    }
    operator fun invoke()
}